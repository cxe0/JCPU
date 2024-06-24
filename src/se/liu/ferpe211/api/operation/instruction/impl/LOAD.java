package se.liu.ferpe211.api.operation.instruction.impl;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.operation.instruction.InstructionProvider;
import se.liu.ferpe211.api.register.Register8Bit;

/**
 * Implementation of the LDX, LDA and LDY instruction present in the 6502 instruction set.
 * For more information, refer to: refer to: <a href="http://www.6502.org/users/obelisk/6502/reference.html#LDA">6502 Reference - LDA</a>
 */
public class LOAD extends Instruction
{
    /**
     * Target register for the LDA, LDX and LDY instruction.
     */
    private Register8Bit targetRegister;

    public LOAD(final CPU cpu, final int cycles, final AddressingMode mode,int size, Register8Bit register8Bit) {
	super(cpu, cycles, mode, size);
	this.targetRegister=register8Bit;
    }

    /**
     * The LOAD instruction loads a value into a register.
     * @see InstructionProvider#performOperation()
     */
    @Override public void performOperation() {
	byte operand = fetchOperand();
	this.targetRegister.setValue(operand);

	verifyZeroFlag(operand);
	verifyNegativeFlag(operand);

    }


}
