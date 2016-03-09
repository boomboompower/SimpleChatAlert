
package me.boomboompower;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateChecker {

	private URL filesFeed;

	private String version, lastversion, link;

	public UpdateChecker(String version) {
		this.version = version;
		
		try {
			filesFeed = new URL("http://dev.bukkit.org/bukkit-plugins/Simple-Chat-Alert/files.rss");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	public boolean updateNeeded() {
		try {
			InputStream input = filesFeed.openConnection().getInputStream();
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);

			Node latestFile = document.getElementsByTagName("item").item(0);
			NodeList children = latestFile.getChildNodes();

			lastversion = children.item(1).getTextContent().replaceAll("[^0-9.]", "");
			link = children.item(3).getTextContent();

			return !version.equals(lastversion);
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public String getCurrentVersion() {
		return version;
	}
	
	public String getLastestVersion() {
		return lastversion;
	}
	
	public String getLink() {
		return link;
	}
}
