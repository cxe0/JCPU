package se.liu.ferpe211.api.register;

import se.liu.ferpe211.impl.Emulator;

import java.util.logging.Level;

/**
 * Abstract class implementation of a register unit for the processor
 */
public abstract class Register {

    protected String name;

    /***
     * Constructor for Register
     * Logs creation of itself to Logger object in Emulator {@link Emulator}
     * @param name (Name of the register)
     */
    protected Register(String name) {
	this.name = name;
	Emulator.LOGGER.log(Level.INFO, "Register " + name + " created");
    }


    public abstract String toString();
}