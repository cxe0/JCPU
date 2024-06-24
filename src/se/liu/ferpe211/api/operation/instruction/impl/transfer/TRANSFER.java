package se.liu.ferpe211.api.operation.instruction.impl.transfer;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.register.Register8Bit;

public class TRANSFER extends Instruction
{
    private Register8Bit sourceRegister;
    private Register8Bit targetRegister;
    private TransferMode transferMode;

    public TRANSFER(final CPU cpu, final int cycles, final AddressingMode mode, final int size, Register8Bit sourceRegister, Register8Bit targetRegister, TransferMode transferMode) {
	super(cpu, cycles, mode, size);
	this.sourceRegister = sourceRegister;
	this.targetRegister = targetRegister;
	this.transferMode = transferMode;
    }

    @Override
    public void performOperation() {
	byte value = sourceRegister.getValue();

	switch (transferMode) {
	    case REGISTER -> {
		targetRegister.setValue(value);
		verifyNegativeFlag(value);
		verifyZeroFlag(value);
	    }
	    case TO_STACK -> {
		cpu.getStackPointer().setValue(value);
		verifyNegativeFlag(value);
		verifyZeroFlag(value);
	    }
	    case FROM_STACK -> {
		byte stack = cpu.getStackPointer().getValue();
		sourceRegister.setValue(stack);
		verifyNegativeFlag(stack);
		verifyZeroFlag(stack);
	    }
	}
    }
}
