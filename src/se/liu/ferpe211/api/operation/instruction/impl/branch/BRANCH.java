package se.liu.ferpe211.api.operation.instruction.impl.branch;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.register.ProcessorFlag;
import se.liu.ferpe211.api.register.ProcessorStatus;

/**
 * Implementation of the BRANCH instructions present in the 6502 instruction set.
 * For more information, refer to: <a href="http://www.6502.org/users/obelisk/6502/reference.html#BCC">6502 Reference - BCC</a>
 */
public class BRANCH extends Instruction
{
    /**
     * Stores current branch mode (which condition to branch)
     * @see BranchMode for more info
     */
    private BranchMode branchMode;

    public BRANCH(final CPU cpu, final int cycles, final AddressingMode mode, final int size, BranchMode branchMode) {
	super(cpu, cycles, mode, size);
	this.branchMode = branchMode;
    }

    @Override public void performOperation() {
	boolean condition = false;
	ProcessorStatus processorStatus = cpu.getProcessorStatus();
	switch (branchMode){
	    case EQUAL ->
		condition = processorStatus.getFlag(ProcessorFlag.ZERO);
	    case NOT_EQUAL ->
		condition = !processorStatus.getFlag(ProcessorFlag.ZERO);
	    case MINUS ->
		    condition = processorStatus.getFlag(ProcessorFlag.NEGATIVE);
	    case POSITIVE ->
		    condition = !processorStatus.getFlag(ProcessorFlag.NEGATIVE);
	    case CARRY_SET ->
		    condition = processorStatus.getFlag(ProcessorFlag.CARRY);
	    case CARRY_CLEAR ->
		    condition = !processorStatus.getFlag(ProcessorFlag.CARRY);
	    case OVERFLOW_SET ->
		    condition = processorStatus.getFlag(ProcessorFlag.OVERFLOW);
	    case OVERFLOW_CLEAR ->
		    condition = !processorStatus.getFlag(ProcessorFlag.OVERFLOW);
	}

	if(condition){
	    cpu.getProgramCounter().setValue(getAddress());
	}
	
    }
}
