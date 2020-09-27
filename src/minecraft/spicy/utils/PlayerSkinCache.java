package spicy.utils;

import java.util.*;
import java.awt.image.*;

public class PlayerSkinCache
{
    private static final String RESOURCE_NAME = "Player Skin (%s): %s";
    private static final PlayerSkinCache INSTANCE;
    private Map<String, PlayerSkin> playerSkins;
    private ThreadDownloadPlayerSkin skinDownloader;
    
    public PlayerSkinCache() {
        this.playerSkins = new HashMap<String, PlayerSkin>();
        this.skinDownloader = ThreadDownloadPlayerSkin.getInstance();
    }
    
    public static PlayerSkinCache getInstance() {
        return PlayerSkinCache.INSTANCE;
    }
    
    public PlayerSkin getPlayerSkin(final String username) {
        PlayerSkin playerSkin = this.playerSkins.get(username);
        if (playerSkin == null) {
            playerSkin = new PlayerSkin();
            this.playerSkins.put(username, playerSkin);
            if (!this.skinDownloader.getQueuedUsernames().contains(username)) {
                synchronized (this.skinDownloader.obj) {
                    this.skinDownloader.getQueuedUsernames().add(username);
                }
            }
        }
        if (!playerSkin.isSkinLoaded()) {
            final BufferedImage skinImage = this.skinDownloader.getSkinImages().get(username);
            if (skinImage != null) {
                playerSkin.setPlayerSkin(skinImage, String.format("Player Skin (%s): %s", username, "%s"));
            }
        }
        return playerSkin;
    }
    
    static {
        INSTANCE = new PlayerSkinCache();
    }
}
