package se.liu.ferpe211.api.operation.instruction.impl;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.operation.instruction.InstructionProvider;
import se.liu.ferpe211.api.register.ProcessorFlag;

/**
 * Implementation of the SBC instruction present in the 6502 instruction set.
 * For more information, refer to: <a href="http://www.6502.org/users/obelisk/6502/reference.html#SBC">6502 Reference - SBC</a>
 */
public class SUB extends Instruction
{
    public SUB(final CPU cpu, final int cycles, final AddressingMode mode, final int size) {
	super(cpu, cycles, mode, size);
    }

    /**
     * The SBC instruction subtracts the content of memory location from the accumulator with borrow.
     *
     * @see InstructionProvider#performOperation()
     */
    @Override
    public void performOperation() {
	byte operand = fetchOperand();
	byte accumulatorValue = cpu.getAccumulator().getValue();
	byte carry = cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY) ? (byte) 1 : (byte) 0;

	//A-M-(1-C)
	int difference = (accumulatorValue - operand - (1 - carry));
	boolean overflow = (difference < Byte.MIN_VALUE || difference > Byte.MAX_VALUE);
	cpu.getProcessorStatus().setFlag(ProcessorFlag.OVERFLOW, overflow);
	cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, !overflow);
	cpu.getAccumulator().setValue((byte) difference);

	verifyZeroFlag((byte) difference);
	verifyNegativeFlag((byte) difference);



    }
}
