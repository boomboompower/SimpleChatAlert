package me.boomboompower;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class UpdateChecker
{
  private URL filesFeed;
  private String version;
  private String lastversion;
  private String link;
  
  public UpdateChecker(String version)
  {
    this.version = version;
    try
    {
      this.filesFeed = new URL("http://dev.bukkit.org/bukkit-plugins/Simple-Chat-Alert/files.rss");
    }
    catch (MalformedURLException e)
    {
      e.printStackTrace();
    }
  }
  
  public boolean updateNeeded()
  {
    try
    {
      InputStream input = this.filesFeed.openConnection().getInputStream();
      Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(input);
      
      Node latestFile = document.getElementsByTagName("item").item(0);
      NodeList children = latestFile.getChildNodes();
      
      this.lastversion = children.item(1).getTextContent().replaceAll("[^0-9.]", "");
      this.link = children.item(3).getTextContent();
      
      return !this.version.equals(this.lastversion);
    }
    catch (Exception e)
    {
      e.printStackTrace();
    }
    return false;
  }
  
  public String getCurrentVersion()
  {
    return this.version;
  }
  
  public String getLastestVersion()
  {
    return this.lastversion;
  }
  
  public String getLink()
  {
    return this.link;
  }
}
