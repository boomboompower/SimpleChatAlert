/** ==Plugin Information==
 * @Name        SimpleChatAlert
 * @Desription  A simple alert plugin for your server!
 * @Website     http://boomboompower.weebly.com/
 * @Version     9.1
 * @Author      boomboompower 
**/

package me.boomboompower;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Set;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Server;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.craftbukkit.v1_9_R1.util.CraftChatMessage;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.base.Charsets;

import me.boomboompower.SCA.Utils.AlertUtils;
import me.boomboompower.SCA.Utils.MetricsLite;
import me.boomboompower.SCA.Utils.UpdateChecker;

import net.minecraft.server.v1_9_R1.BossBattle;
import net.minecraft.server.v1_9_R1.BossBattleServer;
import net.minecraft.server.v1_9_R1.PlayerConnection;

@SuppressWarnings({"deprecation", "unused"})
public class SimpleChatAlert extends JavaPlugin {
	private static UpdateChecker updateChecker;
    private PlayerConnection playerConnection;
    private FileConfiguration newConfig = null;
    private File configFile = null;
    private Server server;
    private String christmas = "&aC&ch&ar&ci&as&ct&am&ca&as&r";
	
    public void onEnable() {
    	saveDefaultConfig();
    	saveFile("README.yml");
    	saveA("BossBar.yml");
    	saveA("ActionBar.yml");
    	saveA("Title.yml");
    	
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
        /* MetricsLite */
        if (getConfig().getBoolean("MetricsLite")) {
    		try {
    	        MetricsLite metrics = new MetricsLite(this);
    	        metrics.start();
    	    } catch (IOException e) {
    	    	broadcast("&7[&cSimpleChatAlert&7] &c&lFailed to submit statistics to mcstats.org:");
    	        e.printStackTrace();
    	    }
        }
    }
    final BossBar bar = server.createBossBar("", BarColor.WHITE, BarStyle.SEGMENTED_10, BarFlag.CREATE_FOG);
  
    public void onDisable() {
    	status("&cDisabled");
    	
    	for (Player all : Bukkit.getOnlinePlayers()) {
    		bar.setVisible(false);
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
    			switch(args[0].toLowerCase()) {
    			case "help":
    				sendMessage(sender, "&e-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    				sendMessage(sender, "");
    				sendMessage(sender, "&bReload &7- &aReloads the plugin");
    				sendMessage(sender, "&bStop &7- &aStops the plugin");
    				sendMessage(sender, "&bStopGlow &7- &aStops sender glowing");
    				sendMessage(sender, "&bRemoveBoss &7- &aRemoves the bossbar");
    				sendMessage(sender, "&bServerInfo &7- &aDisplay server info");
    				sendMessage(sender, "");
    				sendMessage(sender, "&e-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
    				break;
    			case "stop":
    				this.setEnabled(false);
    				break;
    			case "serverinfo":
    				sendMessage(sender, "&7[&c&lSimpleChatAlert&7] &aRunning: &2" + Bukkit.getServer().getBukkitVersion());
    				sendMessage(sender, "&7[&c&lSimpleChatAlert&7] &aCraftbukkit Version: &2" + getServer().getVersion());
    				sendMessage(sender, "&7[&c&lSimpleChatAlert&7] &aYour NMS: &2" + getServer().getClass().getPackage().getName());
    				sendMessage(sender, "&7[&c&lSimpleChatAlert&7] &aPlugin Version: &2SimpleChatAlert v" + getDescription().getVersion());
    				break;
    			case "removeboss":
    				sendMessage(sender, "&7[&c&lSimpleChatAlert&7] &aRemoved bossbar!");
    				for (Player all : Bukkit.getOnlinePlayers()) {
    					bar.removePlayer(all);
    					bar.hide();
    				} break;
    			case "stopglow":
    				if (sender instanceof Player) {
    					((Player) sender).setGlowing(false);
    				} else {
    					sendMessage(sender, "&7[&c&lSimpleChatAlert&7] &fOnly a player may use this command!");
    				} break;
    			case "reload":
    				reloadConfig();
    				reloadConfig("ActionBar.yml");
    				reloadConfig("BossBar.yml");
    				reloadConfig("Title.yml");
    				sendMessage(sender, "&7[&c&lSimpleChatAlert&7] &aConfig reloaded!");
    				if (sender instanceof Player) {
    					Player p = (Player) sender;
    					broadcast("&7[&c&lSimpleChatAlert&7] &2" + p.getName() + "&a has reloaded the config!");
    				} break;
    			default:
    				sendMessage(sender, "&cUnknown command, do &4&o/sca help&c for help!");
    				break;
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
				    } if (getFile("AlertSettings/" + "BossBar.yml").getBoolean("BossBar")) {
					    for (Player all : Bukkit.getOnlinePlayers()) {
					    	bar.removeAll();
					    	bossAlert(getFile("BossBar.yml").getString("Prefix") + AlertUtils.getMessage(args, sender.getName()) + getFile("BossBar.yml").getString("Suffix"), sender, BarColor.PURPLE, BarStyle.SEGMENTED_20);
					    }
				    } if (getFile("AlertSettings/" + "ActionBar.yml").getBoolean("ActionBar")) {
					    for (Player all : Bukkit.getOnlinePlayers()) {
					    	AlertUtils.actionBarAlert(sender, getConfig().getString("Message") + AlertUtils.getMessage(args, sender.getName()));
					    }
				    } if (getFile("AlertSettings/" + "Title.yml").getBoolean("Title")) {
				    	for (Player all : Bukkit.getOnlinePlayers()) {
				    		AlertUtils.titleAlert(sender, 10, 50, 10, getConfig().getString("TitlePrefix"), getConfig().getString("TitleColor") + AlertUtils.getMessage(args, sender.getName()));
				    	}
				    } if (getConfig().getBoolean("PlayerList")) {
				    	AlertUtils.playerListAlert(sender, getConfig().getString("PlayerListHeader"), getConfig().getString("PlayerListFooter") + AlertUtils.getMessage(args, sender.getName()));
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
				    	AlertUtils.chatAlert(getConfig().getString("Message") + AlertUtils.getMessage(args, p.getName()), p);
				    } if (getFile("AlertSettings/" + "BossBar.yml").getBoolean("BossBar")) {
				    	for (Player all : Bukkit.getOnlinePlayers()) {
				    		bar.removeAll();
				    		bossAlert(getFile("BossBar.yml").getString("Prefix") + AlertUtils.getMessage(args, sender.getName()) + getFile("BossBar.yml").getString("Suffix"), sender, BarColor.PURPLE, BarStyle.SEGMENTED_20);
				    	}
				    } if (getFile("AlertSettings/" + "ActionBar.yml").getBoolean("ActionBar")) {
				    	for (Player all : Bukkit.getOnlinePlayers()) {
				    		AlertUtils.actionBarAlert(p, getConfig().getString("Message") + AlertUtils.getMessage(args, p.getName()));
				    	}
				    } if (getFile("AlertSettings/" + "Title.yml").getBoolean("Title")) {
				    	AlertUtils.titleAlert(p, 10, 50, 10, getConfig().getString("TitlePrefix"), getConfig().getString("TitleColor") + AlertUtils.getMessage(args, p.getName()));
				    } if (getConfig().getBoolean("PlayerList")) {
				    	AlertUtils.playerListAlert(p, getConfig().getString("PlayerListHeader"), getConfig().getString("PlayerListFooter") + AlertUtils.getMessage(args, p.getName()));
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
    
    private BossBattleServer handle;
    private void bossAlert(String message, CommandSender sender, BarColor color, BarStyle style) {
    	message = ChatColor.translateAlternateColorCodes('&', message.replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));
    	
    	bar.setStyle(style);
    	bar.setColor(color);
    	bar.setTitle(message);
    	
    	handle = new BossBattleServer(CraftChatMessage.fromString(message, true)[0], convertColor(color), convertStyle(style));
    	
    	for (Player all : Bukkit.getOnlinePlayers()) {
    		handle.setCreateFog(getFile("AlertSettings/" + "BossBar.yml").getBoolean("BossCreateFog"));
    		handle.setDarkenSky(getFile("AlertSettings/" + "BossBar.yml").getBoolean("BossDarkenSky"));
    		handle.setPlayMusic(getFile("AlertSettings/" + "BossBar.yml").getBoolean("BossPlayMusic"));
    		handle.setProgress(getFile("AlertSettings/" + "BossBar.yml").getInt("BossProgress"));
    		handle.setVisible(true);
    		
    		bar.setVisible(true);
    		
    		new BukkitRunnable() {
    			public void run() {
    				bar.setVisible(false);
    			}
    		}.runTaskLater(this, getFile("AlertSettings/" + "BossBar.yml").getInt("RemoveBossBar") * 20);
    	}
    }
    
    private BossBattle.BarStyle convertStyle(BarStyle style) {
    	switch (style) {
    	default:
    		return BossBattle.BarStyle.PROGRESS;
    	case SEGMENTED_12: 
    		return BossBattle.BarStyle.NOTCHED_6;
    	case SEGMENTED_20: 
    		return BossBattle.BarStyle.NOTCHED_10;
    	case SEGMENTED_6: 
    		return BossBattle.BarStyle.NOTCHED_12;
    	case SEGMENTED_10: 
    		return BossBattle.BarStyle.NOTCHED_20;
    	}
	}

	private BossBattle.BarColor convertColor(BarColor color) {
		BossBattle.BarColor nmsColor = BossBattle.BarColor.valueOf(color.name());
		return nmsColor == null ? BossBattle.BarColor.WHITE : nmsColor;
	}

	private void barRemove(Integer time) {
    	new BukkitRunnable() {
			public void run() {
				bar.setVisible(false);
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
    	} else {
    		broadcast(message);
    	}
    }
    
    private void saveFile(String file) {
		File customConfigFile = null;
		if (customConfigFile == null) customConfigFile = new File(getDataFolder(), file);
	    if (!customConfigFile.exists()) this.saveResource(file, false);
	}
    
    private void saveA(String file) {
    	File alertFile = null;
    	if (alertFile == null) alertFile = new File(getDataFolder(), "AlertSettings/" + file);
    	if (!alertFile.exists()) this.saveResource("AlertSettings/" + file, false);
    }
    
    private YamlConfiguration getFile(String file) {
		File getFile = new File(getDataFolder(), file);
		YamlConfiguration newConfig = YamlConfiguration.loadConfiguration(getFile);
		
		return newConfig;
	}
    
    private void reloadConfig(String file) {
    	
    	configFile = new File(getDataFolder(), "AlertSettings/" + file);
    	newConfig = YamlConfiguration.loadConfiguration(configFile);
    
    	InputStream defConfigStream = getResource("AlertSettings/" + file);
    	
    	if (defConfigStream == null) {
    		return;	
    	}     
    	newConfig.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, Charsets.UTF_8)));
    }
}