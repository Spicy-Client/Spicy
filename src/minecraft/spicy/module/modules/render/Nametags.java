package spicy.module.modules.render;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import spicy.events.render.NametagRenderEvent;
import spicy.events.render.Render2DEvent;
import spicy.events.render.Render3DEvent;
import spicy.friend.FriendManager;
import spicy.main.Spicy;
import spicy.module.Category;
import spicy.module.Module;
import spicy.utils.RenderUtil;

import java.awt.*;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Nametags extends Module
{
    private double scale;
    private double gradualFOVModifier;
    private Character formatChar;
    public static Map<EntityLivingBase, double[]> entityPositions;

    public Nametags() {
        super("Nametags", 0, Category.RENDER, true);
        this.scale = 1.0;
        this.formatChar = new Character('§');
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }

    @SubscribeEvent
    private void onRender3DEvent(final Render3DEvent event) {
        this.updatePositions();
    }

    @SubscribeEvent
    private void onRender2DEvent(final Render2DEvent event) {
        GlStateManager.pushMatrix();
        final ScaledResolution scaledRes = new ScaledResolution(Minecraft.getMinecraft());
        final double twoDscale = scaledRes.getScaleFactor() / Math.pow(scaledRes.getScaleFactor(), 2.0);
        GlStateManager.scale(twoDscale, twoDscale, twoDscale);
        for (final Entity ent : Nametags.entityPositions.keySet()) {
            GlStateManager.pushMatrix();
            if (ent instanceof EntityPlayer) {
                final double[] renderPositions = Nametags.entityPositions.get(ent);
                if (renderPositions[3] < 0.0 || renderPositions[3] >= 1.0) {
                    GlStateManager.popMatrix();
                    continue;
                }
                GlStateManager.translate(renderPositions[0], renderPositions[1], 0.0);
                this.scale(ent);
                GlStateManager.translate(0.0, -2.5, 0.0);
                final int strWidth = (int) Spicy.INSTANCE.fontManager.getFont("FONT 20").getWidth(this.getName((EntityPlayer)ent));
                RenderUtil.drawGradientRect(-strWidth / 2 - 3, -12.0, strWidth / 2 + 3, 1.0, new Color(0, 0, 0, 185).getRGB(), Integer.MIN_VALUE);
                GlStateManager.color(0.0f, 0.0f, 0.0f);
                Spicy.INSTANCE.fontManager.getFont("FONT 20").drawStringWithShadow(this.getName((EntityPlayer)ent), (float)(-strWidth / 2), (float) -11.0, this.getColor((EntityPlayer)ent));
                GlStateManager.color(1.0f, 1.0f, 1.0f);
                List<ItemStack> itemsToRender = new ArrayList<>();
                    for (int i = 0; i < 5; ++i) {
                        final ItemStack stack = ((EntityPlayer)ent).getEquipmentInSlot(i);
                        if (stack != null) {
                            itemsToRender.add(stack);
                        }
                    }
                    int x = -(itemsToRender.size() * 9);
                    for (final ItemStack stack2 : itemsToRender) {
                        RenderHelper.enableGUIStandardItemLighting();
                        Minecraft.getMinecraft().getRenderItem().renderItemIntoGUI(stack2, x, -30);
                        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, stack2, x, -30);
                        x += 4;
                        RenderHelper.disableStandardItemLighting();
                        final String text = "";
                        if (stack2 == null) {
                            continue;
                        }
                        int y = 21;
                        final int sLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.sharpness.effectId, stack2);
                        final int fLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.fireAspect.effectId, stack2);
                        final int kLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.knockback.effectId, stack2);
                        if (sLevel > 0) {
                            drawEnchantTag("Sh" + sLevel, x, y);
                            y -= 9;
                        }
                        if (fLevel > 0) {
                            drawEnchantTag("Fir" + fLevel, x, y);
                            y -= 9;
                        }
                        if (kLevel > 0) {
                            drawEnchantTag("Kb" + kLevel, x, y);
                        }
                        else if (stack2.getItem() instanceof ItemArmor) {
                            final int pLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.protection.effectId, stack2);
                            final int tLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.thorns.effectId, stack2);
                            final int uLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.unbreaking.effectId, stack2);
                            if (pLevel > 0) {
                                drawEnchantTag("P" + pLevel, x, y);
                                y -= 9;
                            }
                            if (tLevel > 0) {
                                drawEnchantTag("Th" + tLevel, x, y);
                                y -= 9;
                            }
                            if (uLevel > 0) {
                                drawEnchantTag("Unb" + uLevel, x, y);
                            }
                        }
                        else if (stack2.getItem() instanceof ItemBow) {
                            final int powLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.power.effectId, stack2);
                            final int punLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.punch.effectId, stack2);
                            final int fireLevel = EnchantmentHelper.getEnchantmentLevel(Enchantment.flame.effectId, stack2);
                            if (powLevel > 0) {
                                drawEnchantTag("Pow" + powLevel, x, y);
                                y -= 9;
                            }
                            if (punLevel > 0) {
                                drawEnchantTag("Pun" + punLevel, x, y);
                                y -= 9;
                            }
                            if (fireLevel > 0) {
                                drawEnchantTag("Fir" + fireLevel, x, y);
                            }
                        }
                        else if (stack2.getRarity() == EnumRarity.EPIC) {
                            drawEnchantTag("§lGod", x, y);
                        }
                        x += 16;
                }
            }
            GlStateManager.popMatrix();
        }
        GlStateManager.popMatrix();
    }

    private static void drawEnchantTag(final String text, int x, int y) {
        GlStateManager.pushMatrix();
        GlStateManager.disableDepth();
        x *= 2;
        y -= 4;
        GL11.glScalef(0.57f, 0.57f, 0.57f);
        Spicy.INSTANCE.fontManager.getFont("FONT 20").drawStringWithShadow(text, (float)(x - 7), (float)(-36 - y), -1286);
        GlStateManager.enableDepth();
        GlStateManager.popMatrix();
    }

    private int getColor(final EntityPlayer player) {
        final int color = -1;

        if (FriendManager.isFriend(player.getName())) {
            return -11157267;
        }
        return -1;
    }

    private String getName(final EntityPlayer player) {
        String name = player.getDisplayName().getFormattedText().replaceAll("\u0131", "i").replaceAll("\u011f", "g").replaceAll("\u015f", "s").replaceAll("\u0130", "I").replaceAll("\u011e", "G").replaceAll("\u015e", "S");

        if (FriendManager.isFriend(player.getName())) {
            name = FriendManager.getAliasName(player.getName());
        }
        final float health = player.getHealth() / 2.0f + player.getAbsorptionAmount() / 2.0f;
        EnumChatFormatting color;
        if (health > 8.0f) {
            color = EnumChatFormatting.GREEN;
        }
        else if (health > 6.0f) {
            color = EnumChatFormatting.GOLD;
        }
        else {
            color = EnumChatFormatting.DARK_RED;
        }
        if (Math.floor(health) == health) {
            name = name + color + " " + ((health > 0.0f) ? Integer.valueOf((int)Math.floor(health)) : "RIP");
        }
        else {
            name = name + color + " " + ((health > 0.0f) ? Integer.valueOf((int)health) : "RIP");
        }
        return name;
    }

    private void scale(final Entity ent) {
        float scale = (float)this.scale;
        GlStateManager.scale(scale *= ((Minecraft.getMinecraft().currentScreen == null && GameSettings.isKeyDown(mc.gameSettings.ofKeyBindZoom)) ? 5 : 2), scale, scale);
    }

    private void updatePositions() {
        Nametags.entityPositions.clear();
        final float pTicks = Minecraft.getMinecraft().timer.renderPartialTicks;
        for (final Object o : Minecraft.getMinecraft().theWorld.loadedEntityList) {
            final Entity ent;
            final Entity entity = ent = (Entity)o;
            if (ent instanceof EntityPlayer && ent != Minecraft.getMinecraft().thePlayer && ent instanceof EntityPlayer) {
                final double x = ent.lastTickPosX + (ent.posX - ent.lastTickPosX) * pTicks - Minecraft.getMinecraft().getRenderManager().viewerPosX;
                double y = ent.lastTickPosY + (ent.posY - ent.lastTickPosY) * pTicks - Minecraft.getMinecraft().getRenderManager().viewerPosY;
                final double z = ent.lastTickPosZ + (ent.posZ - ent.lastTickPosZ) * pTicks - Minecraft.getMinecraft().getRenderManager().viewerPosZ;
                if (this.convertTo2D(x, y += ent.height + 0.2, z)[2] < 0.0) {
                    continue;
                }
                if (this.convertTo2D(x, y, z)[2] >= 1.0) {
                    continue;
                }
                Nametags.entityPositions.put((EntityLivingBase)ent, new double[] { this.convertTo2D(x, y, z)[0], this.convertTo2D(x, y, z)[1], Math.abs(this.convertTo2D(x, y + 1.0, z)[1] - this.convertTo2D(x, y, z)[1]), this.convertTo2D(x, y, z)[2] });
            }
        }
    }

    private double[] convertTo2D(final double x, final double y, final double z) {
        final FloatBuffer screenCoords = BufferUtils.createFloatBuffer(3);
        final IntBuffer viewport = BufferUtils.createIntBuffer(16);
        final FloatBuffer modelView = BufferUtils.createFloatBuffer(16);
        final FloatBuffer projection = BufferUtils.createFloatBuffer(16);
        GL11.glGetFloat(2982, modelView);
        GL11.glGetFloat(2983, projection);
        GL11.glGetInteger(2978, viewport);
        final boolean result = GLU.gluProject((float)x, (float)y, (float)z, modelView, projection, viewport, screenCoords);
        if (result) {
            return new double[] { screenCoords.get(0), Display.getHeight() - screenCoords.get(1), screenCoords.get(2) };
        }
        return null;
    }

    @SubscribeEvent
    private void onNametagsRender(final NametagRenderEvent event) {
        event.setCancelled(true);
    }


    @Override
    public String getModuleDesc() {
        return "Shows players nametags";
    }

    static {
        Nametags.entityPositions = new HashMap<EntityLivingBase, double[]>();
    }
}
