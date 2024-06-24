package se.liu.ferpe211.api.operation.instruction.impl;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.register.ProcessorFlag;
import se.liu.ferpe211.api.register.Register8Bit;

/**
 * Implementation of the CMP instruction present in the 6502 instruction set.
 * For more information, refer to: <a href="http://www.6502.org/users/obelisk/6502/reference.html#CPM">6502 Reference - CMP</a>
 * This also serves as a template for the CPX and CPY instructions.
 */
public class CMP extends Instruction
{
    private Register8Bit targetRegister;

    public CMP(final CPU cpu, final int cycles, final AddressingMode mode, final int size, Register8Bit register8Bit) {
	super(cpu, cycles, mode, size);
	this.targetRegister=register8Bit;
    }

    @Override public void performOperation() {
	byte operand = fetchOperand();
	int result = targetRegister.getValue() - operand;

	cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, targetRegister.getValue() >= operand);

	verifyNegativeFlag((byte) result);
	verifyZeroFlag((byte) result);


    }
}
