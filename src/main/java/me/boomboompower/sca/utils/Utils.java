package me.boomboompower.sca.utils;

/*
* Made for SimpleChatAlert
* by boomboompower 18/05/2016
*/

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

/**
 * Class used to trigger events in the plugin using easy-to-use OOP methods.
 *
 * <p>Can be used without having to initialize it in a variable (trying to do this will fail)</p>
 *
 * @author boomboompower
 * @version 1.0
 */
public class Utils {

    /**
     * Deny people using this to initializes a variable.
     */
    private Utils() {}

    /**
     * A way to quickly log things to console or give error messages if needed.
     *
     * @param message Message to be sent to console
     */
    public static void sendToConsole(String message) {
        Bukkit.getConsoleSender().sendMessage(translate(message));
    }

    /**
     * A simpler way of sending a colored message to a player.
     *
     * @param player The player the message will be sent to
     * @param message Message to be sent to the player
     */
    public static void sendToPlayer(Player player, String message) {
        player.sendMessage(translate(message));
    }

    /**
     * Allows you to send a message to something without having to check the instance yourself
     *
     * @param sender The sender to send the message to (Only supports Player and ConsoleCommandSender)
     *               if its none of those a runtime exception will be thrown.
     * @param message The message that will be sent to the Console/Player (supports colors)
     * @throws RuntimeException if the CommandSender is not a Player or ConsoleCommandSender or if they are null
     */
    public static void duelSend(CommandSender sender, String message) {
        if (sender != null && message != null) {
            if (sender instanceof Player) {
                sendToPlayer((Player) sender, message);
            } else if (sender instanceof ConsoleCommandSender) {
                sendToConsole(message);
            } else {
                throw new RuntimeException("duelSend only works for either console or player!");
            }
        } else {
            throw new RuntimeException("The CommandSender and message cannot be null!");
        }
    }

    /**
     * Sends an actionbar to all online players. (Color supported)
     *
     * @param message The actionbar message to be sent to all online players
     */
    public static void sendActionBar(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ReflectionUtils.sendPacketPlayOutChat(player, (byte) 2, translate(message));
        }
    }

    /**
     * Sends a PlayerList header/footer to all online players. (Color supported)
     *
     * @param header The PlayerList header message to be sent.
     * @param footer The PlayerList footer message to be sent.
     */
    public static void sendHeaderFooter(String header, String footer) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ReflectionUtils.sendHeaderAndFooter(player, translate(header), translate(footer));
        }
    }

    /**
     * Sends a title to all online players. (With fadeIn, stay, fadeOut)
     *
     * @param title The title to be sent.
     * @param subtitle The subtitle to be sent.
     * @param fadeIn The fadeIn time.
     * @param stay The stay time.
     * @param fadeOut The fadeOut time.
     */
    public static void sendTitle(String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        for (Player all : Bukkit.getOnlinePlayers()) {
            ReflectionUtils.sendTitle(all, title, subtitle, fadeIn, stay, fadeOut);
        }
    }

    /**
     *
     * @param message
     */
    public static void sendChatAlert(String message) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            ReflectionUtils.sendPacketPlayOutChat(player, (byte) 0, translate(message));
        }
    }

    /**
     * Allows you to translate any string to a color message using the '&' char.
     *
     * @param message The string to be translated to a color message.
     * @return The message after is has been translated.
     */
    public static String translate(String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }

    /**
     * Check if a player has a certain permission
     *
     * @param player The player to check the permission for
     * @param permission The permission to check the player for
     * @return boolean for if they have the permission
     */
    public static boolean permissionCheck(Player player, String permission) {
        return player.hasPermission(permission.toLowerCase());
    }

    /**
     * Tests for if the specified object is null. 
     * <p>If it is a IllegalArgumentException will be thrown with the specified message.</p>
     *
     * @param object Object that will be checked for if its null.
     * @param message The message in the exception if the object is null.
     * @throws IllegalArgumentException if the specified object is null.
     */
    private static void notNull(Object object, String message) {
        if (object == null) {
            throw new IllegalArgumentException(message);
        }
    }
}
