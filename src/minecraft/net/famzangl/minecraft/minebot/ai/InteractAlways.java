// 
// Decompiled by Procyon v0.5.36
// 

package net.famzangl.minecraft.minebot.ai;

import net.minecraft.client.settings.KeyBinding;

public final class InteractAlways extends KeyBinding
{
    private boolean isPressed;
    
    InteractAlways(final String p_i45001_1_, final int p_i45001_2_, final String p_i45001_3_, final boolean pressed) {
        super(p_i45001_1_, p_i45001_2_, p_i45001_3_);
        this.isPressed = pressed;
    }
    
    public boolean func_151470_d() {
        return true;
    }
    
    public boolean func_151468_f() {
        final boolean ret = this.isPressed;
        if (this.isPressed) {
            System.out.println("Sending key press event.");
        }
        this.isPressed = false;
        return ret;
    }
}
