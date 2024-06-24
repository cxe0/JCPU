package se.liu.ferpe211.api.operation.instruction.impl;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.operation.instruction.InstructionProvider;

/**
 * Implementation of the JUMP instruction present in the 6502 instruction set.
 * For more information, refer to: <a href="http://www.6502.org/users/obelisk/6502/reference.html#JMP">Refrence - JMP</a>
 */
public class JUMP extends Instruction
{
    public JUMP(final CPU cpu, final int cycles, final AddressingMode mode) {
	super(cpu, cycles, mode, 3);
    }

    /**
     * The JUMP instruction jumps to a new location in memory.
     * @see InstructionProvider#performOperation()
     */
    @Override public void performOperation() {
	cpu.getProgramCounter().setValue(getAddress());
    }
}
