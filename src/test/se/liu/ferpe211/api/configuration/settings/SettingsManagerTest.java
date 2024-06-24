package se.liu.ferpe211.api.configuration.settings;

import junit.framework.TestCase;
import org.junit.Assert;
import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.configuration.settings.impl.SettingBoolean;
import se.liu.ferpe211.api.configuration.settings.impl.SettingDouble;

/**
 * Test cases for the SettingsManager {@link SettingsManager}
 * Here we tests registers settings and setting their values
 */
public class SettingsManagerTest extends TestCase
{

    public void testRegister(){
        CPU cpu = new CPU();
        SettingsManager settingsManager = new SettingsManager(cpu);

        SettingDouble cycleRate = new SettingDouble("CycleRate", "How fast cycles should be executed");
        settingsManager.register(cycleRate);

        SettingBoolean patching = new SettingBoolean("Patching", "Enable patching of memory");
        settingsManager.register(patching);

        SettingDouble testCycleRate = (SettingDouble) settingsManager.getSetting("cyclerate");

        SettingBoolean testPatching = (SettingBoolean) settingsManager.getSetting("patching");

        Assert.assertEquals(cycleRate, testCycleRate);
        Assert.assertEquals(patching, testPatching);
    }

    public void testSettingSettingValue(){
        CPU cpu = new CPU();
        SettingsManager settingsManager = new SettingsManager(cpu);

        SettingDouble cycleRate = new SettingDouble("CycleRate", "How fast cycles should be executed");
        settingsManager.register(cycleRate);

        SettingBoolean patching = new SettingBoolean("Patching", "Enable patching of memory");
        settingsManager.register(patching);

        boolean expectedPatching = true;
        double expectedCycleRate = 13;

        settingsManager.setSettingValue("patching", expectedPatching);
        settingsManager.setSettingValue("cycleRate", expectedCycleRate);

        SettingDouble testCycleRate = (SettingDouble) settingsManager.getSetting("cyclerate");

        SettingBoolean testPatching = (SettingBoolean) settingsManager.getSetting("patching");

        Assert.assertEquals(Double.valueOf(expectedCycleRate), testCycleRate.getValue());
        Assert.assertEquals(expectedPatching, testPatching.getValue());

        /**
         * Try to set a setting to an unsupported type of value (String in this case)
         * The target setting should then keep the old value if not supports the new one
         */
        settingsManager.setSettingValue("cycleRate", "unsupported type of value");
        settingsManager.setSettingValue("cycleRate", expectedCycleRate);



    }

}