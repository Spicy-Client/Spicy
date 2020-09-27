package spicy.module;

import spicy.main.Spicy;
import spicy.module.modules.combat.*;
import spicy.module.modules.movement.*;
import spicy.module.modules.player.*;
import spicy.module.modules.render.*;
import spicy.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Viserys
 * @since 5/06/2020
 */
public class ModuleManager {
    public static File moduleDir = FileUtils.getConfigFile("mods");
    public static List<Module> modules = new ArrayList<Module>();

    public ModuleManager() {
        /**
         * Example module add.
         * modules.add(new Test());
         */
        modules.add(new Sprint());
        modules.add(new GUI());
        modules.add(new Fullbright());
        modules.add(new BetterView());
        modules.add(new Velocity());
        modules.add(new ContainerWalk());
        modules.add(new NoSkin());
        modules.add(new MiniMap());
        modules.add(new Nametags());
        modules.add(new NoCameraClip());
        modules.add(new Freecam());
        modules.add(new NoSlowdown());
        modules.add(new Jesus());
        modules.add(new Step());
        modules.add(new Speed());
        modules.add(new KillAura());
        modules.add(new FastUse());
        modules.add(new NoRotation());
        modules.add(new Aimbot());
        modules.add(new Fly());
        modules.add(new NoFall());
        modules.add(new NCPhase());
        modules.add(new AutoArmor());
        modules.add(new Regen());
        modules.add(new ESP());
        modules.add(new TargetHUD());
        modules.add(new NoHurtCam());
        modules.add(new ItemPhysics());
        modules.add(new FastFall());
        modules.add(new AutoTool());
        modules.add(new EnchantColor());
        modules.add(new Music());

        load();
    }


    public void save() {
        final List<String> fileContent = new ArrayList<String>();
        for (final Module module : getModules()) {
            final String displayName = module.getModuleName();
            final String moduleKey = (module.getModuleKey() <= 0) ? "null" : Integer.toString(module.getModuleKey());
            final String enabled = Boolean.toString(module.isModuleState());
            final String visibled = Boolean.toString(module.isModuleVisible());
            fileContent.add(String.format("%s:%s:%s:%s", displayName, moduleKey, enabled, visibled));
        }
        FileUtils.write(ModuleManager.moduleDir, fileContent, true);
    }

    public void load() {
        try {
            final List<String> fileContent = FileUtils.read(ModuleManager.moduleDir);
            for (final String line : fileContent) {
                final String[] split = line.split(":");
                final String displayName = split[0];
                final String keybind = split[1];
                final String strEnabled = split[2];
                final String visible = split[3];
                final Module module = getModule(displayName);
                module.setModuleName(displayName);
                module.setModuleCategory(module.getModuleCategory());
                module.setModuleKey(module.getModuleKey());
                module.setModuleVisible(module.isModuleVisible());
                if (module.getModuleName().equalsIgnoreCase("null")) {
                    continue;
                }
                final int moduleKeybind = keybind.equalsIgnoreCase("null") ? 0 : Integer.parseInt(keybind);

                final boolean enabled = Boolean.parseBoolean(strEnabled);
                final boolean visibled = Boolean.parseBoolean(visible);

                if (enabled != module.isModuleState()) {
                    module.toggle();
                }
                module.setModuleName(displayName);
                module.setModuleKey(moduleKeybind);
                module.setModuleVisible(visibled);
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static Module getModule(String moduleName) {
        if (moduleName != null) {
            for (Module mod : getModules()) {
                if (mod.getModuleName().equalsIgnoreCase(moduleName)) {
                    return mod;
                }
            }
        }
        return null;
    }

    public static Module getModule(final Class<? extends Module> clazz) {
        if (clazz != null) {
            for (final Module mod : getModules()) {
                if (mod.getClass() == clazz) {
                    return mod;
                }
            }
        }
        return null;
    }

    public static List<Module> getSortedModules() {
        final List<Module> renderList = ModuleManager.modules;
        renderList.sort(new Comparator<Module>() {
            @Override
            public int compare(final Module m1, final Module m2) {
                final String s1 = String.format("%s" + ((m1.getModuleSuffix().length() > 0) ? " §7[%s]" : ""), m1.getModuleName(), m1.getModuleSuffix());
                final String s2 = String.format("%s" + ((m2.getModuleSuffix().length() > 0) ? " §7[%s]" : ""), m2.getModuleName(), m2.getModuleSuffix());
                return (int) (Spicy.INSTANCE.fontManager.getFont("FONT 15").getWidth(s2) - Spicy.INSTANCE.fontManager.getFont("FONT 15").getWidth(s1));
            }
        });
        return renderList;
    }

    public static List<Module> getModules() {
        return modules;
    }

    public ArrayList<Module> getModulesInCategory(final Category cat) {
        final ArrayList<Module> modsInCat = new ArrayList<Module>();
        for (final Module mod : getModules()) {
            if (mod.getModuleCategory().equals(cat)) {
                modsInCat.add(mod);
            }
        }
        return modsInCat;
    }
}
