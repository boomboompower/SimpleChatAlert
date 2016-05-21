package me.boomboompower.sca.utils;

/*
* Made for SimpleChatAlert
* by boomboompower 18/05/2016
*/

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;

public class ReflectionUtils {

    public static void sendPacketPlayOutChat(Player player, byte type, String message) {
        notNull(player);
        notNull(type);
        notNull(message);
        try {
            Object actionBarJSON = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + message + "\"}");
            Object packet = getNMSClass("PacketPlayOutChat").getConstructor(getNMSClass("IChatBaseComponent"), byte.class).newInstance(actionBarJSON, type);

            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception ex) {}
    }

    public static void sendHeaderAndFooter(Player player, String header, String footer) {
        notNull(player);
        notNull(header);
        notNull(footer);
        try {
            Object headerJson = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + header + "\"}");
            Object footerJson = getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + footer + "\"}");
            Object packet = getNMSClass("PacketPlayOutPlayerListHeaderFooter").getConstructor(getNMSClass("IChatBaseComponent")).newInstance(headerJson);

            Field footerField = packet.getClass().getDeclaredField("b");
            footerField.setAccessible(true);
            footerField.set(packet, footerJson);

            Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            playerConnection.getClass().getMethod("sendPacket", getNMSClass("Packet")).invoke(playerConnection, packet);
        } catch (Exception ex) {}
    }

    // TODO get the method below working or get the times working
    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
        notNull(player);
        notNull(title);
        notNull(subtitle);
        player.sendTitle(title, subtitle);
    }

//    public static void sendTitle(Player player, String title, String subtitle, int fadeIn, int stay, int fadeOut) {
//        notNull(player);
//        notNull(title);
//        notNull(subtitle);
//        notNull(fadeIn);
//        notNull(stay);
//        notNull(fadeOut);
//        try {
//            Object handle = player.getClass().getMethod("getHandle").invoke(player), connection = handle.getClass().getField("playerConnection").get(handle);
//
//            // NMS Classes
//            Class<?> playPacket = getNMSClass("PacketPlayOutTitle"), genericPacket = getNMSClass("Packet"), chatComponent = getNMSClass("IChatBaseComponent"), serializer = getNMSClass("IChatBaseComponent$ChatSerializer"), action = getNMSClass("PacketPlayOutTitle$EnumTitleAction");
//
//            // Set the times
//            Object timesPacket = playPacket.getConstructor(int.class, int.class, int.class).newInstance(fadeIn, stay, fadeOut);
//            connection.getClass().getMethod("sendPacket", genericPacket).invoke(connection, timesPacket);
//
//            // Play the title packet
//            if (title != null && !title.isEmpty()) {
//                Object titleComponent = serializer.getMethod("a", String.class).invoke(null, title.toString()), titlePacket = playPacket.getConstructor(action, chatComponent).newInstance(action.getField("TITLE").get(null), titleComponent);
//                connection.getClass().getMethod("sendPacket", genericPacket).invoke(connection, titlePacket);
//            }
//
//            // Play the subtitle packet
//            if (subtitle != null && !subtitle.isEmpty()) {
//                Object subtitleComponent = serializer.getMethod("a", String.class).invoke(null, subtitle.toString()), subtitlePacket = playPacket.getConstructor(action, chatComponent).newInstance(action.getField("SUBTITLE").get(null), subtitleComponent);
//                connection.getClass().getMethod("sendPacket", genericPacket).invoke(connection, subtitlePacket);
//            }
//        } catch (Exception e) {}
//    }

    public static Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + getVersion() + "." + name);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static Class<?> getCraftBukkitClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + getVersion() + "." + name);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }

    private static void notNull(Object object) {
        if (object == null) {
            throw new IllegalArgumentException(object + " cannot be null!");
        }
    }
}
