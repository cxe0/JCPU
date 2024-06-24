package se.liu.ferpe211.api.operation.instruction.impl.stack;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;

public class STACK extends Instruction
{
    private StackMode stackMode;
    public STACK(final CPU cpu, final int cycles, final AddressingMode mode, final int size, StackMode stackMode) {
	super(cpu, cycles, mode, size);
	this.stackMode = stackMode;
    }

    @Override
    public void performOperation() {
	byte value;

	switch (stackMode) {
	    case PUSH_ACCUMULATOR:
		value = cpu.getAccumulator().getValue();
		cpu.pushByte(value);
		break;
	    case PUSH_PROCESSOR_STATUS:
		value = cpu.getProcessorStatus().getValue();
		cpu.pushByte(value);
		break;
	    case PULL_ACCUMULATOR:
		value = cpu.pullByte();
		cpu.getAccumulator().setValue(value);
		verifyNegativeFlag(value);
		verifyZeroFlag(value);
		break;
	    case PULL_PROCESSOR_STATUS:
		value = cpu.pullByte();
		cpu.getProcessorStatus().setValue(value);
		break;
	}
    }
}
