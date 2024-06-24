package se.liu.ferpe211.api.register;

/**
 * A register that stores an 8bit value
 * @see Register for true identity
 */
public class Register8Bit extends Register
{
    private byte value;
    public Register8Bit(final String name) {
	super(name);
	value = 0x00;
    }
    public void setValue(byte value) {
	this.value = value;
    }

    public byte getValue() {
	return value;
    }

    public void increment() {
	value++;
    }
    public void decrement() {
	value--;
    }

    @Override public String toString() {
	return String.format("%s: %04X",name,value);
    }

}
