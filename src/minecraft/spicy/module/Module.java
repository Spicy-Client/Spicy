package spicy.module;

import com.darkmagician6.eventapi.EventManager;
import net.minecraft.client.Minecraft;
import spicy.notification.NotificationPublisher;
import spicy.notification.NotificationType;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class Module {

    /**
     * The name of module's.
     */
    public String moduleName;

    /**
     * The module's suffix.
     */
    public String moduleSuffix;

    /**
     * The module's description.
     */
    public String moduleDesc;

    /**
     * The keybind of module's.
     */
    public int moduleKey;

    /**
     * The category of module's.
     */
    public Category moduleCategory;

    /**
     * The module state.
     */
    public boolean moduleState;

    /**
     * If the module is visible in the arraylist.
     */
    public boolean moduleVisible;

    public static Minecraft mc = Minecraft.getMinecraft();


    public Module(String moduleName, int moduleKey, Category moduleCategory) {
        this.moduleName = moduleName;
        this.moduleKey = moduleKey;
        this.moduleCategory = moduleCategory;
        this.moduleSuffix = "";
        this.moduleVisible = true;
        setup();
    }

    public Module(String moduleName, int moduleKey, Category moduleCategory, boolean moduleVisible) {
        this.moduleName = moduleName;
        this.moduleKey = moduleKey;
        this.moduleCategory = moduleCategory;
        this.moduleSuffix = "";
        this.moduleVisible = moduleVisible;
        setup();
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String newModuleName) {
        this.moduleName = newModuleName;
    }

    public String getModuleSuffix() {
        return moduleSuffix;
    }

    public void setModuleSuffix(String newModuleSuffix) {
        this.moduleSuffix = "§7 " + newModuleSuffix;
    }

    public int getModuleKey() {
        return moduleKey;
    }

    public void setModuleKey(int newModuleKey) {
        this.moduleKey = newModuleKey;
    }

    public Category getModuleCategory() {
        return moduleCategory;
    }

    public void setModuleCategory(Category moduleCategory) {
        this.moduleCategory = moduleCategory;
    }

    public boolean isModuleState() {
        return moduleState;
    }

    public void setModuleState(boolean moduleState) {
        this.onToggled();
        if (moduleState) {
            this.onEnabled();
            this.moduleState = true;
        } else {
            this.onDisabled();
            this.moduleState = false;
        }
    }

    public boolean isModuleVisible() {
        return moduleVisible;
    }

    public void setModuleVisible(boolean moduleVisible) {
        this.moduleVisible = moduleVisible;
    }

    public String getModuleDesc() {
        return moduleDesc;
    }

    public void toggle() {
        this.setModuleState(!this.isModuleState());
    }

    public void onEnabled() {
        EventManager.register(this);
        if(mc.thePlayer != null) {
            NotificationPublisher.queue(getModuleName(), getModuleDesc(), NotificationType.ENABLE);
            Minecraft.getMinecraft().thePlayer.playSound("note.harp", 1f, 1f);
        }
    }

    public void onDisabled() {
        EventManager.unregister(this);
        if(mc.thePlayer != null) {
            NotificationPublisher.queue(getModuleName(), getModuleDesc(), NotificationType.DISABLE);
            Minecraft.getMinecraft().thePlayer.playSound("note.hat", 1f, 1f);
        }
    }

    public void onToggled() {}

    public void setup() {}

    public boolean onSendChatMessage(final String s) {
        return true;
    }
}
