package me.boomboompower.sca;

/*
* Made for SimpleChatAlert
* by boomboompower 18/05/2016
*/

import me.boomboompower.sca.commands.AlertCommand;
import me.boomboompower.sca.utils.ConfigRegister;
import me.boomboompower.sca.utils.Metrics;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public class SimpleChatAlert extends JavaPlugin {

    private ConfigRegister cr = new ConfigRegister(this);

    @Override
    public void onEnable() {
        saveDefaultConfig();

        new AlertCommand(this);

        status("&aenabled");
        metics();
    }

    @Override
    public void onDisable() {
        status("&cdisabled");
    }

    private void broadcast(String message) {
        Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }

    private void status(String status) {
        broadcast("&e-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        broadcast("");
        broadcast("&cSimpleChatAlert&4 has been " + status + "&4!");
        broadcast("&4Report any bugs/errors! &7-&f boomboompower");
        broadcast("");
        broadcast("&e-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-");
        broadcast("&fThis plugin is being used by: &7" + System.getProperty("user.name"));
    }

    private void metics() {
        if (cr.isMetricsEnabled()) {
            try {
                Metrics m = new Metrics(this);
                m.start();
            } catch (IOException ex) {
                broadcast("&7[&cSimpleChatAlert&7] &c&lFailed to submit statistics to mcstats.org:");
                ex.printStackTrace();
            }
        }
    }
}
