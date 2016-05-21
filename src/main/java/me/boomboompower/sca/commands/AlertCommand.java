package me.boomboompower.sca.commands;

/*
* Made for SimpleChatAlert
* by boomboompower 18/05/2016
*/

import me.boomboompower.sca.SimpleChatAlert;
import me.boomboompower.sca.utils.ConfigRegister;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static me.boomboompower.sca.utils.Utils.*;

public class AlertCommand implements CommandExecutor {

    private SimpleChatAlert simpleChatAlert;
    private ConfigRegister cr;
    private String command = "alert";

    public AlertCommand(SimpleChatAlert simpleChatAlert) {
        this.simpleChatAlert = simpleChatAlert;
        this.cr = new ConfigRegister(simpleChatAlert);

        simpleChatAlert.getCommand(command).setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (cmd.getName().equalsIgnoreCase(command)) {
            if (sender instanceof Player && !permissionCheck((Player) sender, "sca.alert")) {
                sendToPlayer((Player) sender, cr.getDenyMessage());
            } else {
                if (args.length < 1) {
                    duelSend(sender, cr.getNoMessageFormat());
                } else {
                    try {
                        sendToConsole(getFullFormat(sender, args));
                        if (cr.isChatEnabled()) sendChatAlert(getFullFormat(sender, args));
                        if (cr.isTitleEnabled()) sendTitle(getTopFormat(sender, args), getBottomFormat(sender, args), cr.getFadeIn(), cr.getStay(), cr.getFadeOut());
                        if (cr.isActionBarEnabled()) sendActionBar(getFullFormat(sender, args));
                        if (cr.isHeaderFooterEnabled()) {
                            sendHeaderFooter(getHeaderFormat(sender, args), getFooterFormat(sender, args));
                            stopHeaderFooter();
                        }
                    } catch (IllegalArgumentException ex) {
                        duelSend(sender, cr.getNoMessageFormat());
                    }
                }
            }
        }
        return true;
    }

    private String getFullFormat(CommandSender sender, String[] args) {
        return cr.getAlertFormat().replaceFirst("\\{MESSAGE}", getArguments(args)).replace("{PLAYER}", sender.getName());
    }

    private String getTopFormat(CommandSender sender, String[] args) {
        return cr.getTitleTopFormat().replaceFirst("\\{MESSAGE}", getArguments(args)).replace("{PLAYER}", sender.getName());
    }

    private String getBottomFormat(CommandSender sender, String[] args) {
        return cr.getTitleBottomFormat().replaceFirst("\\{MESSAGE}", getArguments(args)).replace("{PLAYER}", sender.getName());
    }

    private String getHeaderFormat(CommandSender sender, String[] args) {
        return cr.getHeaderFormat().replaceFirst("\\{MESSAGE}", getArguments(args)).replace("{PLAYER}", sender.getName());
    }

    private String getFooterFormat(CommandSender sender, String[] args) {
        return cr.getFooterFormat().replaceFirst("\\{MESSAGE}", getArguments(args)).replace("{PLAYER}", sender.getName());
    }

    private String getArguments(String[] args) {
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            builder.append(args[i]);
            builder.append(" ");
        }
        return builder.toString().trim();
    }

    private void stopHeaderFooter() {
        if (!cr.isHeaderFooterDisableable()) return;

        int time = cr.getHeaderFooterDisableTime();
        if (time < 1) time = 100;
        new BukkitRunnable() {
            @Override
            public void run() {
                sendHeaderFooter("", "");
            }
        }.runTaskLater(simpleChatAlert, time);
    }
}
