// 
// Decompiled by Procyon v0.5.30
// 

package spicy.module.modules.player;

import com.darkmagician6.eventapi.SubscribeEvent;
import net.minecraft.util.BlockPos;
import spicy.events.game.GameTickEvent;
import spicy.module.Category;
import spicy.module.Module;
import spicy.utils.BlockUtil;
import spicy.utils.Timer;

public class AutoTool extends Module
{
    private Timer timer;
    
    public AutoTool() {
        super("AutoTool", 0, Category.PLAYER, true);
        this.timer = new Timer();
    }
    
    @SubscribeEvent
    public void onEvent(final GameTickEvent event) {
        if (!AutoTool.mc.gameSettings.keyBindAttack.pressed) {
            return;
        }
        if (AutoTool.mc.objectMouseOver == null) {
            return;
        }
        final BlockPos pos = AutoTool.mc.objectMouseOver.getBlockPos();
        if (pos == null) {
            return;
        }
        BlockUtil.updateTool(pos);
    }

    @Override
    public String getModuleDesc() {
        return "Auto change slot";
    }
}
