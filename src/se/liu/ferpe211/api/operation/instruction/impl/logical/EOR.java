package se.liu.ferpe211.api.operation.instruction.impl.logical;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;

public class EOR extends Instruction
{
    public EOR(final CPU cpu, final int cycles, final AddressingMode mode, final int size) {
	super(cpu, cycles, mode, size);
    }

    @Override
    public void performOperation() {
	byte accumulator = cpu.getAccumulator().getValue();
	byte operand = fetchOperand();
	byte result = (byte) (accumulator ^ operand);

	cpu.getAccumulator().setValue(result);
	verifyNegativeFlag(result);
	verifyZeroFlag(result);
    }
}
