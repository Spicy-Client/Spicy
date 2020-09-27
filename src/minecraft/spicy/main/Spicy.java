package spicy.main;


import com.darkmagician6.eventapi.EventManager;
import net.famzangl.minecraft.minebot.PlayerUpdateHandler;
import net.famzangl.minecraft.minebot.ai.AIController;
import net.famzangl.minecraft.minebot.ai.path.world.BlockBoundsCache;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configuration;
import org.lwjgl.opengl.Display;
import spicy.alt.AltManager;
import spicy.command.CommandManager;
import spicy.discord.DiscordAPI;
import spicy.font.FontManager;
import spicy.friend.FriendManager;
import spicy.gui.splash.SplashProgress;
import spicy.module.ModuleManager;
import spicy.music.MusicManager;
import spicy.security.HWID;
import spicy.security.Version;
import spicy.settings.SettingsManager;
import spicy.user.User;

import java.net.URISyntaxException;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public enum  Spicy {

    /**
     * The instance of spicy client.
     */
    INSTANCE;

    /**
     * The version of spicy.
     */
    public final String VERSION = "1.5";

    /**
     * The client's module manager.
     */
    public ModuleManager moduleManager;

    /**
     * The client's settings manager.
     */
    public SettingsManager settingsManager;

    /**
     * The client's font manager.
     */
    public FontManager fontManager;

    /**
     * The client'a alt manager.
     */
    public AltManager altManager;

    /**
     * The Spicy user.
     */
    public User user;

    /**
     * The client's command manager.
     */
    public CommandManager commandManager;

    /**
     * The client's music manager.
     */
    public MusicManager musicManager;

    /**
     * Starts the client
     * @throws InterruptedException
     */
    public void startClient() throws InterruptedException {

        /**Changes display */
        Display.setTitle("Spicy " + VERSION + " (Latest)");

        /**Sets the setting manager */
        this.settingsManager = new SettingsManager();
        SplashProgress.setProgress(9, "Settings Manager loaded.");
        Thread.sleep(500L);

        /**Sets the font manager.*/
        fontManager = new FontManager();
        SplashProgress.setProgress(12, "Font Manager loaded.");
        Thread.sleep(500L);

        /**Sets the module manager.*/
        this.moduleManager = new ModuleManager();
        Spicy.INSTANCE.settingsManager.load();
        SplashProgress.setProgress(10, "Module Manager loaded.");
        Thread.sleep(500L);

        /**Sets the command manager.*/
        this.commandManager = new CommandManager();
        SplashProgress.setProgress(11, "Command Manager loaded.");
        Thread.sleep(500L);

        /**Sets the alt manager.*/
        this.altManager = new AltManager();

        /**Sets the music manager.*/
        musicManager = new MusicManager();

        /**Sets the user.*/
        user = new User();
        SplashProgress.setProgress(13, "User Manager loaded.");

        /**Starts the friend manager.*/
        FriendManager.start();

        /**Checks the hwids from web site.*/
        HWID.CheckHWIDfromWebsite();
        Version.CheckVersionFromWebsite();
        String doLogging = System.getProperty("MINEBOT_LOG", "0");
        if (doLogging.equals("1")) {
            LoggerContext context = (LoggerContext) LogManager.getContext(false);
            Configuration config = context.getConfiguration();
            try {
                context.setConfigLocation(Object.class.getResource("log4j.xml").toURI());
            } catch (URISyntaxException e) {
                e.printStackTrace();
            }
        }
        BlockBoundsCache.initialize();
        EventManager.register(new PlayerUpdateHandler());
        final AIController controller = new AIController();
        controller.initialize();
        DiscordAPI.getInstance().getDiscordRP().update("Playing Minecraft 1.8.9", "");
    }
}
