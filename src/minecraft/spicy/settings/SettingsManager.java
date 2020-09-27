package spicy.settings;

import spicy.main.Spicy;
import spicy.module.Module;
import spicy.module.ModuleManager;
import spicy.utils.FileUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class SettingsManager {

	public static File settingDir = FileUtils.getConfigFile("settings");
	private final ArrayList<Setting> settings;
	
	public SettingsManager(){
		this.settings = new ArrayList<>();
	}
	
	public void rSetting(Setting in){
		this.settings.add(in);
	}
	
	public ArrayList<Setting> getSettings(){
		return this.settings;
	}
	
	public ArrayList<Setting> getSettingsByMod(Module mod){
		ArrayList<Setting> out = new ArrayList<>();
		for(Setting s : getSettings()){
			if(s.getParentMod().equals(mod)){
				out.add(s);
			}
		}
		if(out.isEmpty()){
			return null;
		}
		return out;
	}
	
	public Setting getSettingByName(String name){
		for(Setting set : getSettings()){
			if(set.getName().equalsIgnoreCase(name)){
				return set;
			}
		}
		System.err.println("["+ "Spicy" + "] Error Setting NOT found: '" + name +"'!");
		return null;
	}


	public void load() {
		final List<String> fileContent = FileUtils.read(SettingsManager.settingDir);
		for (final String line : fileContent) {
			final String[] split = line.split(":");
			final String optionId = split[0];
			final String optionValue = split[1];
			final String modId = split[2];
			for (final Setting option : Spicy.INSTANCE.settingsManager.getSettingsByMod(ModuleManager.getModule(modId))) {
				if (option != null && option == this.getSettingByName(optionId)) {
					if (option.isSlider()) {
						option.setValDouble(Double.parseDouble(optionValue));
					}
					else if (option.isCheck()) {
						option.setValBoolean(Boolean.parseBoolean(optionValue));
					}
					else {
						option.setValString(optionValue);
					}
				}
			}
		}
	}

	public void save() {
		final List<String> fileContent = new ArrayList<String>();
		for (final Setting option : this.getSettings()) {
			final String optionId = option.getName();
			String optionVal;
			if (option.isCheck()) {
				optionVal = String.valueOf(option.getValBoolean());
			}
			else if (option.isSlider()) {
				optionVal = String.valueOf(option.getValDouble());
			}
			else {
				optionVal = option.getValString();
			}
			final Module mod = option.getParentMod();
			fileContent.add(String.format("%s:%s:%s", optionId, optionVal, mod.getModuleName()));
		}
		FileUtils.write(SettingsManager.settingDir, fileContent, true);
	}
}