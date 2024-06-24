package se.liu.ferpe211.api.operation.instruction.impl;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.register.Register8Bit;

/**
 * Implementation of the STX, STA and STY instruction present in the 6502 instruction set.
 * For more information, refer to: <a href="http://www.6502.org/users/obelisk/6502/reference.html#STA">Refrence - STA</a>
 */
public class STORE extends Instruction
{
    /**
     * Target register for the STA, STX and STY instruction.
     */
    private Register8Bit targetRegister;

    public STORE(final CPU cpu, final int cycles, final AddressingMode mode,int size, Register8Bit register8Bit) {
	super(cpu, cycles, mode,size);
	this.targetRegister = register8Bit;
    }

    @Override public void performOperation() {
	byte data = targetRegister.getValue();
	int address = getAddress();
	cpu.getMemory().writeByte(address, data);
    }



}
