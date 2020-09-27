package spicy.command;

import net.minecraft.client.*;

public abstract class Command
{
    protected Minecraft mc;
    
    public Command() {
        this.mc = Minecraft.getMinecraft();
    }
    
    public abstract String getNames();
    
    public abstract String getDesc();
    
    public abstract String getError();
    
    public abstract void onCommand(final String p0, final String[] p1) throws Exception;
}
