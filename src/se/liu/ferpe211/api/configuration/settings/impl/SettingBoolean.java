package se.liu.ferpe211.api.configuration.settings.impl;

import se.liu.ferpe211.api.configuration.settings.Setting;

/**
 * Setting implemention of type Boolean
 * Here we store values of type boolean, which defaults to false if no other value is provided in the config {@link se.liu.ferpe211.api.configuration.Config}
 */
public class SettingBoolean extends Setting<Boolean>
{
    private boolean value;
    public SettingBoolean(final String name, final String description) {
	super(Boolean.class,name, description);
	this.value=false;

    }
    @Override public Boolean getValue() {
	return value;
    }

    @Override public void setValue(final Boolean value) {
	this.value = value;
    }

}
