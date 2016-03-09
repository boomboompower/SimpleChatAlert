/** ==Plugin Information==
 * @Name        SimpleChatAlert
 * @Desription  A simple alert plugin for your server!
 * @Website     http://boomboompower.weebly.com/
 * @Version     9.0
 * @Author      boomboompower 
**/

package me.boomboompower;

import java.io.File;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.boss.BarColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.craftbukkit.v1_9_R1.CraftServer;
import org.bukkit.craftbukkit.v1_9_R1.boss.CraftBossBar;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import me.boomboompower.Utils.AlertUtils;
import net.minecraft.server.v1_9_R1.BossBattleServer;
import net.minecraft.server.v1_9_R1.ChatMessage;
import net.minecraft.server.v1_9_R1.EntityPlayer;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle.EnumTitleAction;
import net.minecraft.server.v1_9_R1.PlayerConnection;

@SuppressWarnings({"deprecation", "unused"})
public class SimpleChatAlert extends JavaPlugin implements Listener {
	private static UpdateChecker updateChecker;
	private static SimpleChatAlert sca;
    private PlayerConnection playerConnection;
    private BarFlag flags = BarFlag.PLAY_BOSS_MUSIC;
    private BarColor color = BarColor.PURPLE;
    private BarStyle style = BarStyle.SOLID;
    private BossBar bar = Bukkit.createBossBar("", color, style, flags);
    private Integer counter = 0;
    private String christmas = "&aC&ch&ar&ci&as&ct&am&ca&as&r";
	
    public void onEnable() {
    	saveDefaultConfig();
    	saveFile("README.yml");
        
        if (!Bukkit.getServer().getClass().getPackage().getName().contains("v1_9")) {
        	status("&cDisabled");
        	broadcast("&ePlease use Spigot/Craftbukkit v1.9.X to use this version!");
        	broadcast("&cUsing a 1.9.X version? Contact boomboompower!");
        	this.setEnabled(false);
        	return;
        }
        
        status("&aEnabled");
        
        if (getConfig().getBoolean("UpdateChecker")) {
        	updateChecker = new UpdateChecker(getDescription().getVersion());
        	if ((!updateChecker.updateNeeded()) || ((updateChecker.getLastestVersion() == null) && (updateChecker.getLink() == null))) {
        		broadcast("&9========================= &bSCA &9==========================");
                broadcast("");
                broadcast("&cAn error occured while testing for the plugin update!");
                broadcast("&cCheck your internet connection! Ignore the error above.");
                broadcast("");
                broadcast("&9========================= &bSCA &9==========================");
        	}
        	else if (updateChecker.getLastestVersion().equals(getDescription().getVersion())) {
        		broadcast("&9========================= &bSCA &9==========================");
        		broadcast("");
        		broadcast("&aYou have the latest version for SimpleChatAlert!");
        		broadcast("");
        		broadcast("&9========================= &bSCA &9==========================");
        	} else {
        		broadcast("&9========================= &bSCA &9==========================");
        		broadcast("");
        		broadcast("&aA new version of SimpleChatAlert is available: v" + updateChecker.getLastestVersion());
        		broadcast("&aYou can get it from the following link: ");
        		broadcast("&a" + updateChecker.getLink());
        		broadcast("");
        		broadcast("&9========================= &bSCA &9==========================");
        	}
        }
    }
  
    public void onDisable() {
    	status("&cDisabled");
    	
    	for (Player all : Bukkit.getOnlinePlayers()) {
    		bar.removePlayer(all);
    		bar.hide();
    		all.resetTitle();
    		
    		if (getConfig().getBoolean("Glow")) {
    			all.setGlowing(false);
    		}
    	}
    }
  
    public boolean onCommand(CommandSender sender, Command commmnd, String cmd, String[] args) {
    	if (cmd.equalsIgnoreCase("sca")) {
    		if (sender instanceof Player && !sender.hasPermission("sca.admin")) {
    			sendMessage(sender, getConfig().getString("NoPermission"));
    		} if (args.length == 0) {
    			sendMessage(sender, "&cPlease do &4&o/sca help&c for a list of commands!");
    		} else try {
    			if (args[0].equalsIgnoreCase("help")) {
    				sendMessage(sender, "&e-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    				sendMessage(sender, "");
    				sendMessage(sender, "&bReload &7- &aReloads the plugin");
    				sendMessage(sender, "&bStop &7- &aStops the plugin");
    				sendMessage(sender, "&bStopGlow &7- &aStops sender glowing");
    				sendMessage(sender, "&bRemoveBoss &7- &aRemoves the bossbar");
    				sendMessage(sender, "&bServerInfo &7- &aDisplay server info");
    				sendMessage(sender, "");
    				sendMessage(sender, "&e-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    			} else if (args[0].equalsIgnoreCase("stop")) {
    				this.setEnabled(false);
    			} else if (args[0].equalsIgnoreCase("stopglow")) {
    				if (sender instanceof Player) {
    					((Player) sender).setGlowing(false);
    				} else {
    					sendMessage(sender, "&cOnly a player may use this command!");
    				}
    			} else if (args[0].equalsIgnoreCase("serverinfo")) {
    				sendMessage(sender, "&aRunning: &2" + Bukkit.getServer().getBukkitVersion());
    				sendMessage(sender, "&aCraftbukkit Version: &2" + getServer().getVersion());
    				sendMessage(sender, "&aYour NMS: &2" + getServer().getClass().getPackage().getName());
    				sendMessage(sender, "&aPlugin Version: &2SimpleChatAlert v" + getDescription().getVersion());
    			} else if (args[0].equalsIgnoreCase("removeboss")) {
    				for (Player all : Bukkit.getOnlinePlayers()) {
    					bar.removePlayer(all);
    					bar.hide();
    				}
    			} else if (args[0].equalsIgnoreCase("reload")) {
    				reloadConfig();
    				sendMessage(sender, "&7[&c&lSimpleChatAlert&7] &aConfig reloaded!");
    				if (sender instanceof Player) {
    					Player p = (Player) sender;
    					broadcast("&7[&c&lSimpleChatAlert&7] &2" + p.getName() + "&a has reloaded the config!");
    				}
    			} else {
    				sendMessage(sender, "&cUnknown command, do &4&o/sca help&c for help!");
    			}
    		} catch (ArrayIndexOutOfBoundsException e) {
    			sendMessage(sender, "&cUnknown command, do &4&o/sca help&c for help!");
    		}
    	} else if (cmd.equalsIgnoreCase("alert")) {
		    if (!(sender instanceof Player)) {
			    if (args.length == 0) {
				    sendMessage(sender, getConfig().getString("NoMessage"));
			    } else {
				    if (getConfig().getBoolean("ChatAlert")) {
				    	AlertUtils.chatAlert(getConfig().getString("Message") + AlertUtils.getMessage(args, sender.getName()), sender);
				    } if (getConfig().getBoolean("BossBar")) {
					    for (Player all : Bukkit.getOnlinePlayers()) {
					    	bossAlert(getConfig().getString("Message") + AlertUtils.getMessage(args, sender.getName()), sender);
					    }
				    } if (getConfig().getBoolean("ActionBar")) {
					    for (Player all : Bukkit.getOnlinePlayers()) {
					    	all.getLastDamageCause().getEntity().sendMessage("");
						    //ActionBarAPI.sendActionBar(all, ChatColor.translateAlternateColorCodes('&', getConfig().getString("Message") + message(args)).replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));
					    }
				    } if (getConfig().getBoolean("Title")) {
				    	for (Player all : Bukkit.getOnlinePlayers()) {
				    		AlertUtils.titleAlert(sender, 10, 50, 10, getConfig().getString("TitlePrefix"), getConfig().getString("TitleColor") + AlertUtils.getMessage(args, sender.getName()));
				    	}
				    }
			    }
		    } else {
			    Player p = (Player) sender;
			    if (!p.hasPermission("sca.alert")) {
				    p.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NoPermission")));
			    } else if (args.length == 0) {
				    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', getConfig().getString("NoMessage")));
			    } else if (args.length > 0) {
				    if (getConfig().getBoolean("ChatAlert")) {
				    	AlertUtils.chatAlert(getConfig().getString("Message") + AlertUtils.getMessage(args, sender.getName()), sender);
				    } if (getConfig().getBoolean("BossBar")) {
				    	for (Player all : Bukkit.getOnlinePlayers()) {
				    		bossAlert(getConfig().getString("Message") + AlertUtils.getMessage(args, sender.getName()), sender);
				    	}
				    } if (getConfig().getBoolean("ActionBar")) {
				    	for (Player all : Bukkit.getOnlinePlayers()) {
				    		AlertUtils.actionBarAlert(all, getConfig().getString("Message") + AlertUtils.getMessage(args, sender.getName()));
				    	}
				    } if (getConfig().getBoolean("Title")) {
				    	AlertUtils.titleAlert(sender, 10, 50, 10, getConfig().getString("TitlePrefix"), getConfig().getString("TitleColor") + AlertUtils.getMessage(args, sender.getName()));
				    } if (getConfig().getBoolean("Glow")) {
				    	p.setGlowing(true);
				    	stopGlowing(p);
				    }
			    }
		    }
	    }
	    return true;
    }
  
    /* Send message supports color */
    private void status(String status) {
        broadcast("&e-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        broadcast("");
        broadcast("&bThe &rSimpleChatAlert &bPlugin has been " + status + "&b!");
        broadcast("&bReport any bugs/errors! - &fSCA Dev Team");
        broadcast("");
        broadcast("&e-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        broadcast("&aYou Are Using Build Version: &3" + getDescription().getVersion());
    }
    
    private static void broadcast(String message) {
    	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
    
    private void bossAlert(String message, CommandSender sender) {
    	this.flags = BarFlag.PLAY_BOSS_MUSIC;
    	this.color = BarColor.PURPLE;
    	this.style = BarStyle.SOLID;
    	
    	message = ChatColor.translateAlternateColorCodes('&', message.replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));
    	
    	for (Player all : Bukkit.getOnlinePlayers()) {
    		bar.setTitle(message);
    		bar.removeFlag(BarFlag.PLAY_BOSS_MUSIC);
    		bar.setProgress(100);
    		bar.setColor(color);
    		bar.addPlayer(all);
    		
    		counter(1);
    	}
    }
    
    private void counter(Integer time) {
    	new BukkitRunnable() {
			public void run() {
				if (counter < 6) {
					counter++;
					bar.setProgress(bar.getProgress() - 20);
					bar.show();
					counter(time);
				} else {
					for (Player all : Bukkit.getOnlinePlayers()) {
						bar.removePlayer(all);
						counter = 0;
						break;
					}
				}
			}
		}.runTaskLater(this, time * 20);
    }
    
    private void stopGlowing(Player p) {
    	new BukkitRunnable() {
			public void run() {
				p.setGlowing(false);
			}
    	}.runTaskLater(this, getConfig().getInt("GlowTime") * 20);
    }
    
    private void sendMessage(CommandSender sender, String message) {
    	if (sender instanceof Player) {
    		sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    	} else if (sender instanceof ConsoleCommandSender) {
    		broadcast(message);
    	}
    }
    
    public void saveFile(String file) {
		File customConfigFile = null;
		if (customConfigFile == null) customConfigFile = new File(getDataFolder(), file);
	    if (!customConfigFile.exists()) this.saveResource(file, false);
	}
    
    public static SimpleChatAlert getSCA() {
		return sca;
	}
}