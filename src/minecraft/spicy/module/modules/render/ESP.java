package spicy.module.modules.render;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.tileentity.TileEntityEnderChest;
import net.minecraft.tileentity.TileEntityMobSpawner;
import net.minecraft.util.BlockPos;
import spicy.events.render.Render3DEvent;
import spicy.main.Spicy;
import spicy.module.Category;
import spicy.module.Module;
import spicy.settings.Setting;
import spicy.utils.OutlineUtils;

import java.util.ArrayList;

public class ESP extends Module {

    public ESP() {
        super("ESP", 0, Category.RENDER, true);
    }


    @Override
    public void setup() {
        ArrayList<String> options = new ArrayList<>();
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("player", this, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("line-width", this, 3, 1, 5, true));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("chest", this, false));
        Spicy.INSTANCE.settingsManager.rSetting(new Setting("spawner", this, false));
    }

    @SubscribeEvent
    public void onRender(Render3DEvent event) {
            if(Spicy.INSTANCE.settingsManager.getSettingByName("chest").getValBoolean()) {
                for(Object o : mc.theWorld.loadedTileEntityList) {
                    if(!(o instanceof TileEntity))
                        return;
                    BlockPos pos = new BlockPos(((TileEntity)o).getPos());
                    double x = pos.getX() - RenderManager.renderPosX;
                    double y = pos.getY() - RenderManager.renderPosY;
                    double z = pos.getZ() - RenderManager.renderPosZ;
                    if (o instanceof TileEntityChest) {
                        OutlineUtils.drawOutlinedBlockESP(x, y, z, 0.4f, 1f, 0.7f, 1f, 1f);
                        OutlineUtils.drawSolidBlockESP(x, y, z, 0.4f, 1f, 0.7f, 0.2f);
                    }
                    if (o instanceof TileEntityEnderChest) {
                        OutlineUtils.drawOutlinedBlockESP(x, y, z, 1f, 0.3f, 1f, 1f, 1f);
                        OutlineUtils.drawSolidBlockESP(x, y, z, 1f, 0.3f, 1f, 0.2f);
                    }
                }
            }

            if(Spicy.INSTANCE.settingsManager.getSettingByName("spawner").getValBoolean()) {
                for(Object o : mc.theWorld.loadedTileEntityList) {
                    if(!(o instanceof TileEntityMobSpawner))
                        return;
                    BlockPos pos = new BlockPos(((TileEntity)o).getPos());
                    double x = pos.getX() - RenderManager.renderPosX;
                    double y = pos.getY() - RenderManager.renderPosY;
                    double z = pos.getZ() - RenderManager.renderPosZ;
                        OutlineUtils.drawOutlinedBlockESP(x, y, z, 1.4f, 0f, 0f, 1f, 1f);
                        OutlineUtils.drawSolidBlockESP(x, y, z, 0, 0f, 0f, 0.2f);
                }
        }
    }

    @Override
    public String getModuleDesc() {
        return "ESP for players/entities";
    }

    @Override
    public void onEnabled() {
        super.onEnabled();
    }

    @Override
    public void onDisabled() {
        super.onDisabled();
    }
}
