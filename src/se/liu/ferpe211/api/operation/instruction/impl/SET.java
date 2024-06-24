package se.liu.ferpe211.api.operation.instruction.impl;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.register.ProcessorFlag;
import se.liu.ferpe211.api.register.ProcessorStatus;

public class SET extends Instruction
{
    private ProcessorFlag flag;

    public SET(final CPU cpu, final int cycles, final AddressingMode mode, final int size, ProcessorFlag flag) {
	super(cpu, cycles, mode, size);
	this.flag = flag;
    }

    @Override public void performOperation() {
	ProcessorStatus processorStatus = cpu.getProcessorStatus();
	processorStatus.setFlag(flag,true);
    }
}
