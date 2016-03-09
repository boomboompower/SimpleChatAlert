package me.boomboompower.Utils;

import java.lang.reflect.Field;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_9_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.ChatColor;

import me.boomboompower.SimpleChatAlert;

import net.minecraft.server.v1_9_R1.IChatBaseComponent;
import net.minecraft.server.v1_9_R1.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_9_R1.PacketPlayOutChat;
import net.minecraft.server.v1_9_R1.PacketPlayOutPlayerListHeaderFooter;
import net.minecraft.server.v1_9_R1.PacketPlayOutTitle;
import net.minecraft.server.v1_9_R1.PlayerConnection;

public class AlertUtils extends SimpleChatAlert {
	
	private static String christmas = "&aC&ch&ar&ci&as&ct&am&ca&as&r";
	private static AlertUtils utils;

	public static void titleAlert(CommandSender sender, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
		
		title = ChatColor.translateAlternateColorCodes('&', title.replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));
		subtitle = ChatColor.translateAlternateColorCodes('&', subtitle.replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer) all).getHandle().playerConnection;

	        PacketPlayOutTitle packetPlayOutTimes = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TIMES, null, fadeIn, stay, fadeOut);
	        connection.sendPacket(packetPlayOutTimes);

	        IChatBaseComponent iSubtitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + subtitle + "\"}");
	        PacketPlayOutTitle packetPlayOutSubTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.SUBTITLE, iSubtitle);
	        connection.sendPacket(packetPlayOutSubTitle);
	       
	        IChatBaseComponent iTitle = IChatBaseComponent.ChatSerializer.a("{\"text\": \"" + title + "\"}");
	        PacketPlayOutTitle packetPlayOutTitle = new PacketPlayOutTitle(PacketPlayOutTitle.EnumTitleAction.TITLE, iTitle);
	        connection.sendPacket(packetPlayOutTitle);
		}
    }
	 
	public static void chatAlert(String message, CommandSender sender) {
		message = ChatColor.translateAlternateColorCodes('&', message.replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));	
		broadcast(message);	
		for (Player all : Bukkit.getOnlinePlayers()) {
			all.sendMessage(message);
		}   
	}
	
	public static void actionBarAlert(CommandSender sender, String message) {
		
		message = ChatColor.translateAlternateColorCodes('&', message.replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));
		IChatBaseComponent chatBase = ChatSerializer.a("{\"text\": \"" + message + "\"}");
		
		PacketPlayOutChat playOutChat = new PacketPlayOutChat(chatBase, (byte) 2);
		
		for (Player all : Bukkit.getOnlinePlayers()) {
			((CraftPlayer)all).getHandle().playerConnection.sendPacket(playOutChat);
		}
	}
	
	public static void playerListAlert(CommandSender sender, String headerMessage, String footerMessage) {
		headerMessage = ChatColor.translateAlternateColorCodes('&', headerMessage.replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));
		footerMessage = ChatColor.translateAlternateColorCodes('&', footerMessage.replace("{PLAYER}", sender.getName()).replace("Christmas", christmas));
		for (Player all : Bukkit.getOnlinePlayers()) {
			PlayerConnection connection = ((CraftPlayer)all).getHandle().playerConnection;
			IChatBaseComponent header = ChatSerializer.a("{'color':'" + "', 'text':'" + headerMessage + "'}");
			IChatBaseComponent footer = ChatSerializer.a("{'color':'" + "', 'text':'" + footerMessage + "'}");
			PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();
			try {
				Field headerField = packet.getClass().getDeclaredField("a");
				headerField.setAccessible(true);
				headerField.set(packet, header);
				headerField.setAccessible(!headerField.isAccessible());
				
				Field footerField = packet.getClass().getDeclaredField("b");
				footerField.setAccessible(true);
				footerField.set(packet, footer);
				footerField.setAccessible(!footerField.isAccessible());
				
			} catch (Exception e) {}
			connection.sendPacket(packet);
		}
	}
	
	public static String getMessage(String[] args, String replaceWith) {
    	StringBuilder builder = new StringBuilder();
    	for (int i = 0; i < args.length; i++) {
    		args[i].replace("{PLAYER}", replaceWith).replace("Christmas", christmas);
    		builder.append(args[i]);
    		builder.append(" ");
    	}
    	return builder.toString().trim();
    }
	
	private static void broadcast(String message) {
    	Bukkit.getConsoleSender().sendMessage(ChatColor.translateAlternateColorCodes('&', message));
    }
	
	public static AlertUtils getUtils() {
		return utils;
	}
}
