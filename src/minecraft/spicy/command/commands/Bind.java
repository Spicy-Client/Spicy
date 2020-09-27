package spicy.command.commands;


import org.lwjgl.input.Keyboard;
import spicy.command.Command;
import spicy.main.Wrapper;
import spicy.module.Module;
import spicy.module.ModuleManager;

public class Bind extends Command
{
    @Override
    public String getNames() {
        return "bind";
    }
    
    @Override
    public String getDesc() {
        return "Keybinds the modules.";
    }
    
    @Override
    public String getError() {
        return "$bind add/del (moduleName) (Key)";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
        if (args[0].equalsIgnoreCase("add")) {
            args[2] = args[2].toUpperCase();
            final int key = Keyboard.getKeyIndex(args[2]);
            for (final Module mod : ModuleManager.getModules()) {
                if (args[1].equalsIgnoreCase(mod.getModuleName())) {
                    mod.setModuleKey(Keyboard.getKeyIndex(Keyboard.getKeyName(key)));
                    Wrapper.addChatMessage(args[1] + " bound to " + args[2]);
                }
            }
        }
        else if (args[0].equalsIgnoreCase("del")) {
            for (final Module mod2 : ModuleManager.getModules()) {
                if (mod2.getModuleName().equalsIgnoreCase(args[1])) {
                    mod2.setModuleKey(0);
                    Wrapper.addChatMessage(args[1] + " bind cleared.");
                }
            }
        }
    }
}
