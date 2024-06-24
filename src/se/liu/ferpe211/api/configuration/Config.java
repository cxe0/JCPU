package se.liu.ferpe211.api.configuration;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.ToNumberPolicy;
import com.google.gson.reflect.TypeToken;
import se.liu.ferpe211.api.configuration.settings.SettingsManager;
import se.liu.ferpe211.api.event.CPUListener;
import se.liu.ferpe211.api.event.EventType;
import se.liu.ferpe211.impl.Emulator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

/**
 * Class for saving and loading configurations of {@link se.liu.ferpe211.api.configuration.settings.Setting}'s
 * The config is stored in a json file configFile, this file is defined in the constructor
 */
public class Config implements CPUListener
{

    private String configFile;
    private SettingsManager settingsManager;
    private Gson gson;

    public Config(final String configFile, final SettingsManager settingsManager) {
	this.configFile = configFile;
	this.settingsManager = settingsManager;

	/**
	 * Creating of the GsonBuilder, note the object to number strategy for how Gson chooses to handle objects of Number
	 * @see https://stackoverflow.com/a/70229243
	 */
	this.gson = new GsonBuilder().setPrettyPrinting().setObjectToNumberStrategy(ToNumberPolicy.LONG_OR_DOUBLE).create();

	this.load();
    }

    /**
     * Method for loading values from config file to {@link se.liu.ferpe211.api.configuration.settings.Setting}
     */
    private void load() {
	File file = new File(configFile + ".json");
	if (!file.exists()) {
	    save();
	    return;
	}
	try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
	    Type settingStructure = new TypeToken<List<Map<String, Object>>>(){}.getType();
	    List<Map<String, Object>> settingDataRegistry = gson.fromJson(reader, settingStructure);
	    if(settingDataRegistry != null) {
		for (Map<String, Object> settingData : settingDataRegistry) {
		    String name = (String) settingData.get("name");
		    Object value = settingData.get("value");
		    settingsManager.setSettingValue(name, value);
		}
	    }
	} catch (IOException e) {
	    Emulator.LOGGER.log(Level.SEVERE, "Encountered exception when trying to read config file: " + e);
	}
    }

    /**
     * Method for saving settings values to config file
     * A temporary json file is created to test writing before actually writing to the main config file due to prevent corruptation
     * of the config file
     */
    private void save() {
	String settingJson = gson.toJson(settingsManager.getSettings().values());

	//tries to write to temp file
	try (PrintWriter writer = new PrintWriter(new FileWriter(configFile + ".temp.json"))) {
	    writer.println(settingJson);
	}catch (IOException e){
	    Emulator.LOGGER.log(Level.SEVERE, "Encountered exception when trying to write to temp config file: "+ e);
	    return;
	}

	try (PrintWriter writer = new PrintWriter(new FileWriter(configFile + ".json"))) {
	    writer.println(settingJson);
	} catch (IOException e) {
	    Emulator.LOGGER.log(Level.SEVERE, "Encountered exception when trying to write to config file: " + e);
	}

    }

    @Override public void cpuChanged(final EventType eventType) {
	this.save();
    }
}
