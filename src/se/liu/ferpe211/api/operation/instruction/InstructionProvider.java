package se.liu.ferpe211.api.operation.instruction;

/**
 * Interface for the abstract class {@link Instruction}
 *
 * <p>
 *     This interface provides the method {@link #execute()} which is called by the {@link se.liu.ferpe211.api.operation.Operation}
 *     class to perform the instruction.
 * </p>
 *
 */
public interface InstructionProvider
{
    public abstract void performOperation();
    public void execute();
    public int getCycles();
}
