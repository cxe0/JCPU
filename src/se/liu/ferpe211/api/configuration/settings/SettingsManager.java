package se.liu.ferpe211.api.configuration.settings;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.event.EventType;
import se.liu.ferpe211.impl.Emulator;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.logging.Level;

/**
 * Class for managing {@link Setting}'s that is added during runtime
 * Supports getting Settings by name aswell as setting their current value
 */
public class SettingsManager
{
    private CPU cpu;
    private Map<String, Setting<?>> settings = new HashMap<>();

    public SettingsManager(CPU cpu){
	this.cpu = cpu;
    }

    public void register(Setting<?> setting){
	settings.put(setting.getName().toLowerCase(),setting);
    }

    public Setting<?> getSetting(String name){
	return settings.get(name.toLowerCase());
    }

    /**
     * Method for setting their current value with a generic type T
     * This is done by obtaining the Setting with the provided name and validating if the provided T value is of the same type as
     * the target Settings value type. If this is true it casts the setting to setting of type T and can set value.
     * @param name, name of setting
     * @param value, value you want to set
     * @param <T>, the type of the value being set
     */
    public <T> void setSettingValue(String name, T value) {
	Setting<?> setting = getSetting(name);
	/**
	 * Here we check if the typeParameterClass field in the target setting is equal to the class of value
	 * This is done to safely cast the setting to setting of correct type T
	 */
	if (setting != null && setting.getTypeParameterClass() == value.getClass()) {
	    /**
	     * We can then conclude that the generic type T is a support type of Setting
	     * @see Setting
	     * This means we can safely cast the setting to Setting<T>
	     */
	    @SuppressWarnings("unchecked")
	    Setting<T> typedSetting = (Setting<T>) setting;
	    typedSetting.setValue(value);
	    cpu.notifyListeners(EventType.SETTINGS_CHANGED);
	    Emulator.LOGGER.log(Level.INFO, setting.getName() + " - " + setting.getDescription() + ": now has value " + setting.getValue());
	}else {
	    String settingName = Optional.ofNullable(setting.getName()).orElse("Undefined setting");
	    Emulator.LOGGER.log(Level.SEVERE, "Value is of wrong type for setting: " + settingName);
	}
    }

    public Map<String, Setting<?>> getSettings(){
	return this.settings;
    }




}
