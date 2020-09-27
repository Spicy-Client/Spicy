/*******************************************************************************
 * This file is part of Minebot.
 *
 * Minebot is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Minebot is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Minebot.  If not, see <http://www.gnu.org/licenses/>.
 *******************************************************************************/
package net.famzangl.minecraft.minebot.ai.render;

import net.famzangl.minecraft.minebot.ai.AIHelper;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.client.renderer.vertex.VertexFormatElement;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import org.lwjgl.opengl.GL11;
import spicy.events.render.Render3DEvent;

/**
 * Helps rendering markers.
 * 
 * @author michael
 * 
 */
public class RenderHelper {

	private static final double MAX = 1.05;
	private static final double MIN = -0.05;

    public static VertexFormat VF = new VertexFormat();
    static {
    	(VF = new VertexFormat()).addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.FLOAT, VertexFormatElement.EnumUsage.POSITION, 3));
        RenderHelper.VF.addElement(new VertexFormatElement(0, VertexFormatElement.EnumType.UBYTE, VertexFormatElement.EnumUsage.COLOR, 4));
    }

	public void renderStart(Render3DEvent event, AIHelper helper) {
		final Entity player = helper.getMinecraft().getRenderViewEntity();
		final double x = player.lastTickPosX
				+ (player.posX - player.lastTickPosX) * event.getPartialTicks();
		final double y = player.lastTickPosY
				+ (player.posY - player.lastTickPosY) * event.getPartialTicks();
		final double z = player.lastTickPosZ
				+ (player.posZ - player.lastTickPosZ) * event.getPartialTicks();

		preRender();
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        worldrenderer.setVertexFormat(RenderHelper.VF);
        worldrenderer.setTranslation(-x, -y, -z);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
       // worldrenderer.markDirty();
	}

    private void preRender()
    {
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GL11.GL_DST_COLOR, GL11.GL_SRC_COLOR, 1, 0);

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.doPolygonOffset(-3.0F, -3.0F);
        GlStateManager.enablePolygonOffset();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableAlpha();
        GlStateManager.pushMatrix();
    }

    private void postRender()
    {
        GlStateManager.disableAlpha();
        GlStateManager.doPolygonOffset(0.0F, 0.0F);
        GlStateManager.disablePolygonOffset();
        GlStateManager.enableAlpha();
        GlStateManager.depthMask(true);
        GlStateManager.popMatrix();
        GlStateManager.disableBlend();
        GlStateManager.enableTexture2D();
    }
	protected void renderEnd() {
		final Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
		tessellator.draw();
		worldrenderer.setTranslation(0.0D, 0.0D, 0.0D);
		postRender();
	}

	protected void renderMarker(BlockPos m, float r, float g, float b, float a) {
		final Tessellator tessellator = Tessellator.getInstance();
		WorldRenderer renderer = tessellator.getWorldRenderer();
		renderer.color(r, g, b, a);
        this.renderMarkerP(renderer, m.getX(), m.getY(), m.getZ());
	}

	private void renderMarkerP(final WorldRenderer worldRenderer, final int x, final int y, final int z) {
        worldRenderer.pos(x - 0.05, y + 1.05, z - 0.05);
        worldRenderer.pos(x - 0.05, y + 1.05, z + 1.05);
        worldRenderer.pos(x + 1.05, y + 1.05, z + 1.05);
        worldRenderer.pos(x + 1.05, y + 1.05, z - 0.05);
        worldRenderer.pos(x - 0.05, y - 0.05, z - 0.05);
        worldRenderer.pos(x - 0.05, y - 0.05, z + 1.05);
        worldRenderer.pos(x - 0.05, y + 1.05, z + 1.05);
        worldRenderer.pos(x - 0.05, y + 1.05, z - 0.05);
        worldRenderer.pos(x + 1.05, y + 1.05, z - 0.05);
        worldRenderer.pos(x + 1.05, y + 1.05, z + 1.05);
        worldRenderer.pos(x + 1.05, y - 0.05, z + 1.05);
        worldRenderer.pos(x + 1.05, y - 0.05, z - 0.05);
        worldRenderer.pos(x - 0.05, y - 0.05, z - 0.05);
        worldRenderer.pos(x - 0.05, y + 1.05, z - 0.05);
        worldRenderer.pos(x + 1.05, y + 1.05, z - 0.05);
        worldRenderer.pos(x + 1.05, y - 0.05, z - 0.05);
        worldRenderer.pos(x - 0.05, y + 1.05, z + 1.05);
        worldRenderer.pos(x - 0.05, y - 0.05, z + 1.05);
        worldRenderer.pos(x + 1.05, y - 0.05, z + 1.05);
        worldRenderer.pos(x + 1.05, y + 1.05, z + 1.05);
        worldRenderer.pos(x - 0.05, y - 0.05, z + 1.05);
        worldRenderer.pos(x - 0.05, y - 0.05, z - 0.05);
        worldRenderer.pos(x + 1.05, y - 0.05, z - 0.05);
        worldRenderer.pos(x + 1.05, y - 0.05, z + 1.05);
    }
}
