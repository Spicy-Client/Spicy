package spicy.command.commands;

import spicy.command.Command;
import spicy.main.Spicy;
import spicy.module.ModuleManager;
import spicy.settings.Setting;

public class SetValue extends Command
{
    
    @Override
    public String getNames() {
        return "value";
    }
    
    @Override
    public String getDesc() {
        return "Sets value for modules.";
    }
    
    @Override
    public String getError() {
        return "$value (moduleName) (value)/(mode)";
    }
    
    @Override
    public void onCommand(final String command, final String[] args) throws Exception {
            for (final Setting set : Spicy.INSTANCE.settingsManager.getSettingsByMod(ModuleManager.getModule(args[0]))) {
                if (set != null && set == Spicy.INSTANCE.settingsManager.getSettingByName(args[1])) {
                    if (set.isSlider()) {
                        set.setValDouble(Double.parseDouble(args[2]));
                    }
                    else if (set.isCheck()) {
                        set.setValBoolean(Boolean.parseBoolean(args[2]));
                    }
                    else {
                        if (!set.isCombo()) {
                            continue;
                        }
                        for (int i = 0; i < set.getOptions().size(); ++i) {
                            final String modes = set.getOptions().get(i);
                            if (args[2].equalsIgnoreCase(modes)) {
                                set.setValString(modes);
                            }
                        }
                    }
                }
            }
    }
}
