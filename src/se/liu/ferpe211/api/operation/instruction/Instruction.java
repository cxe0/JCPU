package se.liu.ferpe211.api.operation.instruction;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.memory.RAM;
import se.liu.ferpe211.api.operation.AddressingMode;
import se.liu.ferpe211.api.register.IndexRegisterType;
import se.liu.ferpe211.api.register.ProcessorFlag;
import se.liu.ferpe211.impl.Emulator;

import java.util.logging.Level;

/**
 * Abstract super class for generic instructions in the processor
 */
public abstract class Instruction implements InstructionProvider
{
    protected final int cycles;
    protected CPU cpu;
    private int size;
    protected AddressingMode mode;
    private final static byte BYTE_SIZE = 8;
    private final static short BYTE_MASK = 0xFF;
    private final static int MAX_ARGS = 2;
    private static final byte LAST_BIT = (byte) 0x80;

    /**
     * Operands for the instruction
     * @see Instruction#setOperands(byte[])
     */
    private byte op8;
    private int op16;

    protected Instruction(CPU cpu, int cycles, AddressingMode mode, int size){
        this.cpu= cpu;
        this.cycles = cycles;
        this.mode = mode;
        this.size = size;
    }

    public void execute() {
        byte[] bytes = this.fetchOperandBytes();
        this.setOperands(bytes);
        this.performOperation();
    }

    /**
     * Utility method for instructions to effectivly fetch operand
     * @return operand
     */
    protected byte fetchOperand(){
        return switch (mode){
            case IMMEDIATE -> op8;
            default -> cpu.getMemory().readByte(this.getAddress());
        };
    }

    /**
     * Utility method for instructions to fetch effective address
     * @return address
     */
    protected int getAddress(){
        byte registerX = cpu.getIndexRegister(IndexRegisterType.X).getValue();
        byte registerY = cpu.getIndexRegister(IndexRegisterType.Y).getValue();
        RAM memory = cpu.getMemory();

        return switch (mode){
            case ZERO_PAGE -> Byte.toUnsignedInt(op8);
            //casts to byte then to int since the address should just be 8bits (wrap around)
            case ZERO_PAGE_X -> Byte.toUnsignedInt((byte) (op8 + registerX));
            case ZERO_PAGE_Y -> Byte.toUnsignedInt((byte) (op8 + registerY));
            case ABSOLUTE -> op16;
            case ABSOLUTE_X -> op16 + Byte.toUnsignedInt(registerX);
            /**
             * Regarding the indication "programming::SuspiciousNameCombination" in the code report,
             * it's inaccurate because the parameter name 'x' in {@link Byte#toUnsignedInt(byte)}
             * refers to the value being cast. Therefore its fine for me to pass the registerY value into the method
             */
            case ABSOLUTE_Y -> op16 + Byte.toUnsignedInt(registerY);
            case INDIRECT -> {
                byte targetAddressL = memory.readByte(op16);
                byte targetAddressH = memory.readByte(op16 + 1);

                int targetAddress = ((targetAddressH & BYTE_MASK) << BYTE_SIZE) | (targetAddressL & BYTE_MASK);
                yield targetAddress;
            }
            case INDIRECT_X -> {
                int zpX = Byte.toUnsignedInt((byte) (op8 + registerX));
                byte low = memory.readByte(zpX);
                byte high = memory.readByte(zpX+1);
                int indirectAddress = (high << BYTE_SIZE) | low;
                yield indirectAddress;
            }
            case INDIRECT_Y -> {
                int zp = Byte.toUnsignedInt(op8);
                byte low = memory.readByte(zp);
                byte high = memory.readByte(zp+1);
                int indirectAddress = (high << BYTE_SIZE) | low;
                int indirectAddressY = indirectAddress + registerY;
                yield indirectAddressY;
            }
            case RELATIVE -> op8+cpu.getProgramCounter().getValue();
            default -> {
                String error = "Invalid addressing mode for fetching a effective memory address: "+mode;
                Emulator.LOGGER.log(Level.SEVERE, error);
                throw new IllegalArgumentException(error);
            }
        };
    }

    /**
     * Method for fetching input bytes for an instruction into a byte[]
     * @return byte[] input bytes
     */
    private byte[] fetchOperandBytes(){
        byte[] bytes = new byte[size-1];
        for (int i = 0; i < this.size-1; i++) {
            byte value = cpu.getMemory().readByte(cpu.getProgramCounter().getValue());
            cpu.getProgramCounter().increment();
            bytes[i] = value;
        }
    return bytes;}

    /**
     * Sets operands Op8 and Op16 to their respective byte values
     * If byte is not defined its by default set to 0
     * @param bytes
     */
    public void setOperands(byte[] bytes) {
        if (bytes.length > MAX_ARGS) {
            Emulator.LOGGER.log(Level.SEVERE, "Invalid instruction size");
        }
        op8 = bytes.length >= 1 ? bytes[0] : 0;
        op16 = bytes.length == MAX_ARGS ? ((bytes[1] & BYTE_MASK) << BYTE_SIZE) | (bytes[0] & BYTE_MASK) : 0;
    }

    @Override
    public int getCycles(){
        return cycles;
    }
    public int getSize() {return size;}

    protected void verifyNegativeFlag(byte value){
        cpu.getProcessorStatus().setFlag(ProcessorFlag.NEGATIVE, (value & LAST_BIT) != 0);
    }

    protected void verifyZeroFlag(byte value){
        cpu.getProcessorStatus().setFlag(ProcessorFlag.ZERO, value == 0);
    }
}
