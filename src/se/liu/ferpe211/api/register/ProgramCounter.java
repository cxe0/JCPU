package se.liu.ferpe211.api.register;

/**
 * Represents the Program Counter register.
 * It is a 16-bit register used to hold the current execution address in the program.
 */
public class ProgramCounter extends Register16Bit
{
    public ProgramCounter(final String name) {
	super(name);
    }

    public void increment(){
	this.setValue((this.getValue() + 1));
    }

}
