package spicy.module.modules.movement;

import com.darkmagician6.eventapi.SubscribeEvent;
import spicy.events.game.GameTickEvent;
import spicy.events.player.PlayerSprintingEvent;
import spicy.main.Spicy;
import spicy.module.Category;
import spicy.module.Module;
import spicy.module.ModuleManager;
import spicy.settings.Setting;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class Sprint extends Module {

    public Sprint() {
        super("Sprint", 0, Category.MOVEMENT, true);
    }


    @Override
    public void setup() {
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("multi-directional", this, true));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("auto-sprint", this, true));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("client-side", this, true));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("legit", this, false));
        super.setup();
    }

    @SubscribeEvent
    public void onTick(GameTickEvent event) {
        if (mc.thePlayer != null && this.canSprint()) {
            mc.thePlayer.setSprinting(true);
        }
    }

    @SubscribeEvent
    private void onSprint(final PlayerSprintingEvent event) {
        if (this.canSprint()) {
            event.setSprinting(true);
        }
    }


    @Override
    public void onEnabled() {
        System.out.println("Sprint enabled.");

        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        System.out.println("Sprint disabled.");
        super.onDisabled();
    }

    private boolean canSprint() {
        return Spicy.INSTANCE.settingsManager.getSettingByName("auto-sprint").getValBoolean() && (!mc.thePlayer.isCollidedHorizontally && !mc.thePlayer.isSneaking() && (!Spicy.INSTANCE.settingsManager.getSettingByName("legit").getValBoolean() || ModuleManager.getModule(NoSlowdown.class).isModuleState() || (Spicy.INSTANCE.settingsManager.getSettingByName("legit").getValBoolean() && mc.thePlayer.getFoodStats().getFoodLevel() > 5 && !mc.thePlayer.isUsingItem()))) && (Spicy.INSTANCE.settingsManager.getSettingByName("multi-directional").getValBoolean() ? (mc.thePlayer.movementInput.moveForward != 0.0f || mc.thePlayer.movementInput.moveStrafe != 0.0f) : (mc.thePlayer.movementInput.moveForward > 0.0f));
    }

    @Override
    public String getModuleDesc() {
        return "Auto sprinting";
    }
}
