package spicy.module.modules.render;


import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.optifine.util.MathUtils;
import org.lwjgl.opengl.GL11;
import spicy.events.render.Render2DEvent;
import spicy.main.Spicy;
import spicy.module.Category;
import spicy.module.Module;
import spicy.module.ModuleManager;
import spicy.module.modules.combat.KillAura;
import spicy.settings.Setting;
import spicy.utils.AnimationUtils;
import spicy.utils.ColorUtils;
import spicy.utils.RenderUtil;
import spicy.utils.Stopwatch;

import java.awt.*;

public final class TargetHUD extends Module
{
    private static final Color COLOR;
    private final Stopwatch animationStopwatch;
    private EntityOtherPlayerMP target;
    private double healthBarWidth;
    private double hudHeight;
    public static final ResourceLocation rekt = new ResourceLocation("spicy/rekt.png");
    public static final ResourceLocation lose = new ResourceLocation("spicy/lose.png");
    public static final ResourceLocation equal = new ResourceLocation("spicy/equal.png");

    public TargetHUD() {
        super("TargetHUD", 0, Category.RENDER, true);
        this.animationStopwatch = new Stopwatch();
    }

    @Override
    public void setup() {
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("posX", this, 200, 1.0, 1000.0, true));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("posY", this, 200, 1.0, 1000., true));

        super.setup();
    }

    @SubscribeEvent
    public final void onRender(final Render2DEvent event) {
        final KillAura aura = (KillAura) ModuleManager.getModule(KillAura.class);
        ScaledResolution scaledResolution = new ScaledResolution(mc);
        final float scaledWidth = (float) ((float)scaledResolution.getScaledWidth() + Spicy.INSTANCE.settingsManager.getSettingByName("posX").getValDouble());
        final float scaledHeight = (float) ((float)scaledResolution.getScaledHeight() - Spicy.INSTANCE.settingsManager.getSettingByName("posY").getValDouble());
        final EntityPlayer entityPlayer = (EntityPlayer) aura.targets.get(aura.index);
        if (aura.targets.get(aura.index) != null && aura.isModuleState()) {
            if (aura.targets.get(aura.index) instanceof EntityOtherPlayerMP) {
                this.target = (EntityOtherPlayerMP)aura.targets.get(aura.index);
                final float width = 140.0f;
                final float height = 40.0f;
                final float xOffset = 40.0f;
                final float x = scaledWidth / 2.0f - 70.0f;
                final float y = scaledHeight / 2.0f + 80.0f;
                final double health = (double)this.target.getHealth();
                double hpPercentage = health / this.target.getMaxHealth();
                hpPercentage = MathHelper.clamp_double(hpPercentage, 0.0, 1.0);
                final double hpWidth = 92.0 * hpPercentage;
                final int healthColor = ColorUtils.getHealthColor(this.target.getHealth(), this.target.getMaxHealth()).getRGB();
                final String healthStr = String.valueOf((int)this.target.getHealth() / 2.0f);
                if (this.animationStopwatch.elapsed(15L)) {
                    this.healthBarWidth = AnimationUtils.animate(hpWidth, this.healthBarWidth, 0.3529999852180481);
                    this.hudHeight = AnimationUtils.animate(40.0, this.hudHeight, 0.10000000149011612);
                    this.animationStopwatch.reset();
                }
                GL11.glEnable(3089);
                RenderUtil.prepareScissorBox(x, y, x + 140.0f, (float)(y + this.hudHeight));
                Gui.drawRect(x, y, x + Spicy.INSTANCE.fontManager.getFont("FONT 20").getWidth(this.target.getName()) + 50, y + 40.0f, TargetHUD.COLOR.getRGB());
                Spicy.INSTANCE.fontManager.getFont("FONT 20").drawStringWithShadow(this.target.getName(), x + 30, y + 2.0f, -1);
                Spicy.INSTANCE.fontManager.getFont("FONT 20").drawStringWithShadow("HP: " + (int)this.target.getHealth() / 2, x + 30, y + 14, -1);
               if(mc.thePlayer.getHealth() / 2 == this.target.getHealth() / 2) {
                   Spicy.INSTANCE.fontManager.getFont("FONT 20").drawStringWithShadow("DF: " + (int)MathUtils.roundToPlace(mc.thePlayer.getHealth() / 2.0f - (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) / 2.0f, 2), x + 30, y + 25, -1);
               } else if(mc.thePlayer.getHealth() / 2 > this.target.getHealth() / 2) {
                   Spicy.INSTANCE.fontManager.getFont("FONT 20").drawStringWithShadow("DF: §e+" + (int)MathUtils.roundToPlace(mc.thePlayer.getHealth() / 2.0f - (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) / 2.0f, 2), x + 30, y + 25, -1);
               } else {
                   Spicy.INSTANCE.fontManager.getFont("FONT 20").drawStringWithShadow("DF: §c" + (int) MathUtils.roundToPlace(mc.thePlayer.getHealth() / 2.0f - (entityPlayer.getHealth() + entityPlayer.getAbsorptionAmount()) / 2.0f, 2), x + 30, y + 25, -1);
               }

                GuiInventory.drawEntityOnScreen((int)(x + 13.333333f), (int)(y + 40.0f), 20, this.target.rotationYaw, this.target.rotationPitch, this.target);
                GL11.glDisable(3089);
            }
        }
        else {
            this.healthBarWidth = 92.0;
            this.hudHeight = 0.0;
            this.target = null;
        }
    }
    
    static {
        COLOR = new Color(0, 0, 0, 180);
    }


    @Override
    public String getModuleDesc() {
        return "Shows the target infos";
    }
}
