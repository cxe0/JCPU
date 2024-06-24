package se.liu.ferpe211.api.configuration.settings;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.configuration.Config;

/**
 * Abstract class Setting of type T
 * Setting holds a value of type T which is used in different parts of the program
 * @see CPU#executeThread() for an example of how a setting is used
 * @param <T> type of value that the Setting will store.
 */
public abstract class Setting<T>
{
    public Class<T> getTypeParameterClass() {
	return typeParameterClass;
    }

    /**
     * TypeParameterClass is of type transient to prevent GSON from indexing it when saving config
     * @see Config#save()
     */
    private final transient Class<T> typeParameterClass;
    private String name;
    private String description;

    protected Setting(Class<T> typeParameterClass, String name, String description){
	this.typeParameterClass = typeParameterClass;
	this.name = name;
	this.description = description;
    }

    public String getName() {
	return name;
    }

    public String getDescription(){
	return description;
    }
    public abstract T getValue();
    public abstract void setValue(T value);

}
