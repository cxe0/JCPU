package se.liu.ferpe211.api.operation.instruction.impl.system;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.operation.instruction.InstructionProvider;

/**
 * Implementation of the NOP instruction present in the 6502 instruction set.
 * For more information, refer to: <a href="http://www.6502.org/users/obelisk/6502/reference.html#NOP">Refrence - NOP</a>
 */
public class NOP extends Instruction
{
    public NOP(CPU cpu, int cycles, AddressingMode mode){
	super(cpu, cycles, mode,1);
    }

    /**
     * The NOP instruction does nothing, so it is a no-op.
     * @see InstructionProvider#performOperation()
     */
    @Override public void performOperation() {
	//No operation
    }
}
