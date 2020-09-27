package spicy.utils;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class ThreadDownloadPlayerSkin extends Thread
{
    private static ThreadDownloadPlayerSkin instance;
    private static final String SKIN_LOCATION = "http://skins.minecraft.net/MinecraftSkins/%s.png";
    private FlexibleArray<String> queuedUsernames;
    private Map<String, BufferedImage> skinImages;
    public final Object obj;
    
    private ThreadDownloadPlayerSkin() {
        this.skinImages = new HashMap<String, BufferedImage>();
        this.obj = new Object();
        this.queuedUsernames = new FlexibleArray<String>();
    }
    
    public static ThreadDownloadPlayerSkin getInstance() {
        if (ThreadDownloadPlayerSkin.instance == null) {
            (ThreadDownloadPlayerSkin.instance = new ThreadDownloadPlayerSkin()).setName("Energetic Player Skin Downloader");
            ThreadDownloadPlayerSkin.instance.setPriority(3);
            ThreadDownloadPlayerSkin.instance.start();
        }
        return ThreadDownloadPlayerSkin.instance;
    }
    
    public FlexibleArray<String> getQueuedUsernames() {
        return this.queuedUsernames;
    }
    
    public Map<String, BufferedImage> getSkinImages() {
        return this.skinImages;
    }
    
    @Override
    public void run() {
        while (true) {
            for (final String username : this.queuedUsernames) {
                try {
                    final BufferedImage skinImage = ImageIO.read(new URL(String.format("http://skins.minecraft.net/MinecraftSkins/%s.png", username)));
                    this.skinImages.put(username, skinImage);
                }
                catch (Exception ex) {}
                synchronized (this.obj) {
                    this.queuedUsernames.remove(username);
                }
            }
        }
    }
}
