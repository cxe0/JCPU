package se.liu.ferpe211.api.register;

/**
 * A register that stores a 16bit value
 * @see Register for true identity
 */
public class Register16Bit extends Register
{
    private int value;

    public Register16Bit(final String name) {
	super(name);
	value = 0;
    }

    public void setValue(int value){
	this.value = value;
    }

    public int getValue() {
	return value;
    }

    @Override public String toString() {
	return String.format("%s: %04X",name,value);
    }



}
