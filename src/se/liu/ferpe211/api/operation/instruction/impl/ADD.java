package se.liu.ferpe211.api.operation.instruction.impl;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.operation.instruction.InstructionProvider;
import se.liu.ferpe211.api.register.ProcessorFlag;
import se.liu.ferpe211.api.register.ProcessorStatus;

/**
 * Implementation of the ADC instruction present in the 6502 instruction set.
 * For more information, refer to: <a href="http://www.6502.org/users/obelisk/6502/reference.html#ADC">6502 Reference - ADC</a>
 */
public class ADD extends Instruction
{
    public ADD(final CPU cpu, final int cycles, final AddressingMode mode, final int size) {
	super(cpu, cycles, mode, size);
    }


    /**
     * The ADC instruction adds the content of memory location to the accumulator with the carry bit.
     * @see InstructionProvider#performOperation()
     */
    @Override public void performOperation() {
	byte operand = fetchOperand();
	byte dataRegister = cpu.getAccumulator().getValue();
	byte carry = cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY) ? (byte)1 : (byte)0;
	int sum = (operand + dataRegister + carry);
	byte finalSum = (byte) (operand + dataRegister + carry);
	boolean overflow= (sum < Byte.MIN_VALUE || sum > Byte.MAX_VALUE);
	ProcessorStatus processorStatus = cpu.getProcessorStatus();

	processorStatus.setFlag(ProcessorFlag.OVERFLOW,overflow);
	processorStatus.setFlag(ProcessorFlag.CARRY, overflow);
	cpu.getAccumulator().setValue(finalSum);

	verifyNegativeFlag(finalSum);
	verifyZeroFlag(finalSum);

    }


}
