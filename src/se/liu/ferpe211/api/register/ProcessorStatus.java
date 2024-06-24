package se.liu.ferpe211.api.register;

/**
 * Represents the processor status register in an 8-bit format.
 * This class extends the Register8Bit class and provides functionality specific to the processor status register.
 *
 * <p>
 * The flags in the processor status register are as follows:
 * </p>
 * <ul>
 *     <li>CARRY (bit 0) - Indicates a carry or borrow during arithmetic operations.</li>
 *     <li>ZERO (bit 1) - Indicates that the result of an operation is zero.</li>
 *     <li>INTERRUPT_DISABLE (bit 2) - Disables IRQ interrupts when set.</li>
 *     <li>DECIMAL_MODE (bit 3) - Enables binary-coded decimal (BCD) mode if set.</li>
 *     <li>BREAK_COMMAND (bit 4) - Indicates a software interrupt (BRK instruction).</li>
 *     <li>UNUSED (bit 5) - This bit is unused and always reads as 1.</li>
 *     <li>OVERFLOW (bit 6) - Indicates signed arithmetic overflow.</li>
 *     <li>NEGATIVE (bit 7) - Indicates a negative result from an operation.</li>
 * </ul>
 */
public class ProcessorStatus extends Register8Bit
{

    public ProcessorStatus(final String name) {
	super(name);
    }

    public boolean getFlag(ProcessorFlag processorFlag){
	return ((this.getValue() >> processorFlag.getBitPosition()) & 1) != 0;
    }

    /**
     * Sets the value of a flag in the processor status register.
     * @param processorFlag The flag to set.
     * @param value The value to set the flag to.
     */
    public void setFlag(ProcessorFlag processorFlag, boolean value) {
	// 1   : 00000001
	// mask: 00001000 (if getBitPosition returned 3 (DECIMAL_MODE))
	byte mask = (byte) (1 << processorFlag.getBitPosition());
	byte registerValue = this.getValue();
	if (value) {
	    // Value: X X X X X X X X
	    // Mask:  0 0 0 0 1 0 0 0
	    // AFTER: X X X X 1 X X X
	    /**
	     * Regarding the readability::ImplicitNumericConversion warning in the code report
	     * If were to write this line as registerValue = (byte) (registerValue | mask) which the code report wants me to,
	     * a new severe warning appears readability::ReplaceAssignmentWithOperatorAssignment and prompts me to simplify the line to
	     * registerValue |= mask
	     */
	    registerValue |= mask;
	} else {
	    // Value: X X X X X X X X
	    // Mask:  1 1 1 1 0 1 1 1 (since mask is inverted)
	    // AFTER: X X X X 0 X X X
	    /**
	     * Regarding the readability::ImplicitNumericConversion warning in the code report
	     * If were to write this line as registerValue = (byte) (registerValue & ~mask) which the code report wants me to,
	     * a new severe warning appears readability::ReplaceAssignmentWithOperatorAssignment and prompts me to simplify the line to
	     * registerValue &= ~mask
	     */
	    registerValue &= (byte) ~mask;
	}
	this.setValue(registerValue);
    }

    @Override
    public String toString() {
	StringBuilder stringBuilder = new StringBuilder(String.format("%s: %04X",name,getValue()));
	for (ProcessorFlag flag : ProcessorFlag.values()) {
	    boolean flagValue = getFlag(flag);
	    stringBuilder.append(flag.name()).append(": ").append(flagValue ? "1" : "0").append("\n");
	}
	return stringBuilder.toString();
    }


}
