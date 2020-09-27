package spicy.render;

import com.darkmagician6.eventapi.EventManager;
import com.darkmagician6.eventapi.SubscribeEvent;
import com.youtube.search.Item;
import minimap.XaeroMinimap;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.multiplayer.ServerAddress;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import org.lwjgl.opengl.GL11;
import spicy.events.game.ServerJoinEvent;
import spicy.main.Spicy;
import spicy.main.Wrapper;
import spicy.module.Module;
import spicy.module.ModuleManager;
import spicy.music.MusicManager;
import spicy.notification.NotificationPublisher;
import spicy.notification.NotificationType;
import spicy.utils.RenderUtil;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class IngameUI extends GuiIngame {
    public XaeroMinimap xaero = new XaeroMinimap();
    public static int yCount;
    private List<Item> displayedTracks = new ArrayList<>();



    public IngameUI(Minecraft mcIn) {
        super(mcIn);
        try {
            xaero.load();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        displayedTracks = MusicManager.getInstance().getPlaylist();
        EventManager.register(this);
    }


    @Override
    public void renderGameOverlay(float partialTicks) {
        super.renderGameOverlay(partialTicks);
        ScaledResolution scaledResolution = new ScaledResolution(Minecraft.getMinecraft());
        drawArmor(scaledResolution);
        drawPotionStatus(scaledResolution);
        if (MusicManager.getInstance().getCurrentItem() != null) {
            Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow(MusicManager.getInstance().getCurrentItem().getSnippet().getTitle(), 2, Minecraft.getMinecraft().currentScreen instanceof GuiChat ? scaledResolution.getScaledHeight() - 9 - 15 : 1 + scaledResolution.getScaledHeight() - 9, -1);
        } else {
            Spicy.INSTANCE.fontManager.getFont("FONT 15").drawString("No song", 2,scaledResolution.getScaledHeight() - 9, -1);
        }
        if(Minecraft.getMinecraft().gameSettings.showDebugInfo) return;
            final int sw = scaledResolution.getScaledWidth();
            final int sh = scaledResolution.getScaledHeight();
        RenderUtil.drawRect(sw / 2 - Spicy.INSTANCE.fontManager.getFont("FONT 30").getWidth("Spicy") + 14, 2, sw / 2 + Spicy.INSTANCE.fontManager.getFont("FONT 30").getWidth("Spicy") - 14, 20, Integer.MIN_VALUE);
        Spicy.INSTANCE.fontManager.getFont("FONT 30").drawCenteredString("Spicy", sw / 2, 2, -1);
        yCount = 2;
            for (final Module mod : ModuleManager.getSortedModules()) {
                if (mod.isModuleState() && mod.isModuleVisible()) {
                    final int startX = (int) (scaledResolution.getScaledWidth() - Spicy.INSTANCE.fontManager.getFont("FONT 17").getWidth(mod.getModuleName() + mod.getModuleSuffix()) - 5);
                    drawRect(startX + 3, yCount - 1, scaledResolution.getScaledWidth() - 1, yCount + Minecraft.getMinecraft().fontRendererObj.FONT_HEIGHT, Integer.MIN_VALUE);
                    GlStateManager.color(1,1,1);
                    Spicy.INSTANCE.fontManager.getFont("FONT 17").drawStringWithShadow(mod.getModuleName() + mod.getModuleSuffix(), sw - (Spicy.INSTANCE.fontManager.getFont("FONT 17").getWidth(mod.getModuleName() + mod.getModuleSuffix()) + 1), yCount, -1);
                    yCount += 10;
                }
            }
        }

    @SubscribeEvent
    public void onServerJoin(ServerJoinEvent serverJoinEvent) {
        NotificationPublisher.queue("Server", ServerAddress.ipAddress, NotificationType.WARNING);
    }

    public static double round(double value, int places) {
        if (places < 0) {
            throw new IllegalArgumentException();
        }
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private void drawArmor(final ScaledResolution scaledResolution) {
        int var25 = 15;
        ItemStack[] armorInventory;
        for (int length = (armorInventory = Wrapper.mc().thePlayer.inventory.armorInventory).length, i = 0; i < length; ++i) {
            final ItemStack var26 = armorInventory[i];
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glPushMatrix();
            Wrapper.mc().getRenderItem().renderItemAndEffectIntoGUI(var26, scaledResolution.getScaledWidth() / 2 + 90, scaledResolution.getScaledHeight() - ((Wrapper.mc().thePlayer.isInsideOfMaterial(Material.water) && Wrapper.mc().thePlayer.getAbsorptionAmount() <= 0.0f) ? (var25 + 10) : var25));
            Wrapper.mc().getRenderItem().renderItemOverlays(Wrapper.mc().fontRendererObj, var26, scaledResolution.getScaledWidth() / 2 + 90, scaledResolution.getScaledHeight() - ((Wrapper.mc().thePlayer.isInsideOfMaterial(Material.water) && Wrapper.mc().thePlayer.getAbsorptionAmount() <= 0.0f) ? (var25 + 10) : var25));
            if (var26 != null) {
                Spicy.INSTANCE.fontManager.getFont("FONT 15").drawStringWithShadow(new StringBuilder(String.valueOf(var26.getMaxDamage() - var26.getItemDamage())).toString(), scaledResolution.getScaledWidth() / 2 + 107, scaledResolution.getScaledHeight() - ((Wrapper.mc().thePlayer.isInsideOfMaterial(Material.water) && Wrapper.mc().thePlayer.getAbsorptionAmount() <= 0.0f) ? (var25 + 6) : (var25 - 4)), -1);
            }
            GL11.glPopMatrix();
            RenderHelper.disableStandardItemLighting();
            var25 += 14;
        }
    }


    private static void drawPotionStatus(ScaledResolution sr) {
        if(!NotificationPublisher.NOTIFICATIONS.isEmpty()) {
            return;
        }
        List<PotionEffect> potions = new ArrayList<>();
        for (Object o : Wrapper.mc().thePlayer.getActivePotionEffects())
            potions.add((PotionEffect) o);
        potions.sort(Comparator.comparingDouble(effect -> -Spicy.INSTANCE.fontManager.getFont("FONT 15")
                .getWidth(I18n.format((Potion.potionTypes[effect.getPotionID()]).getName()))));

        float pY = -2;
        for (PotionEffect effect : potions) {
            Potion potion = Potion.potionTypes[effect.getPotionID()];
            String name = I18n.format(potion.getName());
            String PType = "";
            if (effect.getAmplifier() == 1) {
                name = name + " II";
            } else if (effect.getAmplifier() == 2) {
                name = name + " III";
            } else if (effect.getAmplifier() == 3) {
                name = name + " IV";
            }
            if ((effect.getDuration() < 600) && (effect.getDuration() > 300)) {
                PType = PType + "\2476 " + Potion.getDurationString(effect);
            } else if (effect.getDuration() < 300) {
                PType = PType + "\247c " + Potion.getDurationString(effect);
            } else if (effect.getDuration() > 600) {
                PType = PType + "\2477 " + Potion.getDurationString(effect);
            }
            Spicy.INSTANCE.fontManager.getFont("FONT 17").drawStringWithShadow(name,
                    sr.getScaledWidth() - Spicy.INSTANCE.fontManager.getFont("FONT 17").getWidth(name + PType),
                    sr.getScaledHeight() - 9 + pY, -1);
            GlStateManager.color(1,1,1);
            Spicy.INSTANCE.fontManager.getFont("FONT 16").drawStringWithShadow(PType,
                    sr.getScaledWidth() - Spicy.INSTANCE.fontManager.getFont("FONT 16").getWidth(PType), sr.getScaledHeight() - 9 + pY, -1);
            pY -= 9;
        }
    }

}
