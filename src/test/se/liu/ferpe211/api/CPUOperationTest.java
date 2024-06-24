package se.liu.ferpe211.api;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import se.liu.ferpe211.api.operation.OpCode;
import se.liu.ferpe211.api.operation.OpCodeMapper;
import se.liu.ferpe211.api.register.IndexRegisterType;
import se.liu.ferpe211.api.register.ProcessorFlag;

/**
 * This class stores different test cases related to the CPU
 */
public class CPUOperationTest extends TestCase
{

    /** Tests the reset method */
    @Test
    public void testReset() {
        CPU cpu = new CPU();
        cpu.reset();

        int expectedPC = 0x0000;
        int actualPC = cpu.getProgramCounter().getValue();
        Assert.assertEquals(expectedPC, actualPC);

        byte expectedSP = (byte) 0xFF;
        byte actualSP = cpu.getStackPointer().getValue();
        Assert.assertEquals(expectedSP, actualSP);
    }

    /** Tests the LDA instruction */
    @Test
    public void testLdaInstruction() {
        CPU cpu = new CPU();

        cpu.reset();

        //LDA IM
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_IM));
        cpu.getMemory().writeByte(1, (byte) 0x69);
        cpu.execute();
        Assert.assertEquals((byte) 0x69, cpu.getAccumulator().getValue());

        // Test LDA Zero Page mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0xAB);
        cpu.execute();
        Assert.assertEquals((byte) 0xAB, cpu.getAccumulator().getValue());

        // Test LDA Zero Page X mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ZPX));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x10);
        cpu.getMemory().writeByte(0x42 + 0x10, (byte) 0xCD);
        cpu.execute();
        Assert.assertEquals((byte) 0xCD, cpu.getAccumulator().getValue());

        // Test LDA Absolute mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0xEF);
        cpu.execute();
        Assert.assertEquals((byte) 0xEF, cpu.getAccumulator().getValue());

        // Test LDA Absolute X mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ABSX));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0x1234 + 0x02, (byte) 0x45);
        cpu.execute();
        Assert.assertEquals((byte) 0x45, cpu.getAccumulator().getValue());

        // Test LDA Absolute Y mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ABSY));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x03);
        cpu.getMemory().writeByte(0x1234 + 0x03, (byte) 0x78);
        cpu.execute();
        Assert.assertEquals((byte) 0x78, cpu.getAccumulator().getValue());

        // Test LDA Indirect X mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_INDX));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x05);
        cpu.getMemory().writeByte(0x20 + 0x05, (byte) 0x56);
        cpu.getMemory().writeByte(0x20 + 0x05 + 1, (byte) 0x34);
        cpu.getMemory().writeByte(0x3456, (byte) 0xCD);
        cpu.execute();
        Assert.assertEquals((byte) 0xCD, cpu.getAccumulator().getValue());

        // Test LDA Indirect Y mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_INDY));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0x20, (byte) 0x56);
        cpu.getMemory().writeByte(0x20 + 1, (byte) 0x34);
        cpu.getMemory().writeByte(0x3456 + 0x02, (byte) 0x89);
        cpu.execute();
        Assert.assertEquals((byte) 0x89, cpu.getAccumulator().getValue());
    }

    /** Tests the LDY instruction */
    @Test
    public void testLdyInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test LDY Immediate mode
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDY_IM));
        cpu.getMemory().writeByte(1, (byte) 0x69);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x69, cpu.getIndexRegister(IndexRegisterType.Y).getValue());

        // Test LDY Zero Page mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDY_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0xAB);
        cpu.execute();
        Assert.assertEquals((byte) 0xAB, cpu.getIndexRegister(IndexRegisterType.Y).getValue());

        // Test LDY Zero Page X mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDY_ZPX));
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(1, (byte) 0x40);
        cpu.getMemory().writeByte(0x42, (byte) 0xCD);
        cpu.execute();
        Assert.assertEquals((byte) 0xCD, cpu.getIndexRegister(IndexRegisterType.Y).getValue());

        // Test LDY Absolute mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDY_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0xEF);
        cpu.execute();
        Assert.assertEquals((byte) 0xEF, cpu.getIndexRegister(IndexRegisterType.Y).getValue());

        // Test LDY Absolute X mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDY_ABSX));
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x01);
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0xCD);
        cpu.execute();
        Assert.assertEquals((byte) 0xCD, cpu.getIndexRegister(IndexRegisterType.Y).getValue());
    }

    /** Tests the LDX instruction */
    @Test
    public void testLdxInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test LDX Immediate mode
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDX_IM));
        cpu.getMemory().writeByte(1, (byte) 0x69);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x69, cpu.getIndexRegister(IndexRegisterType.X).getValue());

        // Test LDX Zero Page mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDX_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0xAB);
        cpu.execute();
        Assert.assertEquals((byte) 0xAB, cpu.getIndexRegister(IndexRegisterType.X).getValue());

        // Test LDX Absolute mode
        cpu.reset();
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDX_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0xEF);
        cpu.execute();
        Assert.assertEquals((byte) 0xEF, cpu.getIndexRegister(IndexRegisterType.X).getValue());

    }

    /** Tests the NOP instruction */
    @Test
    public void testNopInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.NOP));
        cpu.executeNextInstruction();
        Assert.assertEquals(0x0001, cpu.getProgramCounter().getValue());
    }

    /** Tests the JMP instruction */
    @Test
    public void testJmpInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.JMP_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.executeNextInstruction();
        Assert.assertEquals(0x1234, cpu.getProgramCounter().getValue());
    }

    /** Tests a simple program with a few instructions */
    @Test
    public void testInstructionExecution() {
        CPU cpu = new CPU();
        cpu.reset();

        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.LDA_IM));
        cpu.getMemory().writeByte(1, (byte) 0x69);

        cpu.getMemory().writeByte(2, OpCodeMapper.getByteFromOpCode(OpCode.NOP));
        cpu.getMemory().writeByte(3, OpCodeMapper.getByteFromOpCode(OpCode.NOP));

        cpu.getMemory().writeByte(4, OpCodeMapper.getByteFromOpCode(OpCode.LDA_IM));
        cpu.getMemory().writeByte(5, (byte) 0x91);

        cpu.getMemory().writeByte(6, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ZP));
        cpu.getMemory().writeByte(7, (byte) 0x00);

        cpu.getMemory().writeByte(8, OpCodeMapper.getByteFromOpCode(OpCode.LDX_IM));
        cpu.getMemory().writeByte(9, (byte) 0xFF);

        cpu.getMemory().writeByte(10, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ABS));
        cpu.getMemory().writeByte(11, (byte) 0xFF);
        cpu.getMemory().writeByte(12, (byte) 0xFF);

        cpu.getMemory().writeByte(0xFFFF, (byte) 0x68);

        cpu.getProcessorStatus().setFlag(ProcessorFlag.NEGATIVE, true);
        cpu.execute();

        Assert.assertEquals(cpu.getAccumulator().getValue(), (byte) 0x68);
        Assert.assertEquals(cpu.getIndexRegister(IndexRegisterType.X).getValue(), (byte) 0xFF);
        Assert.assertEquals(cpu.getProgramCounter().getValue(), 0x0E);
        Assert.assertEquals(cpu.getStackPointer().getValue(), (byte) 0xFF);
    }



    /** Tests the STA instruction */
    @Test
    public void testStaInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test STA Zero Page mode
        cpu.getAccumulator().setValue((byte) 0x69);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.STA_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x69, cpu.getMemory().readByte(0x42));

        // Test STA Zero Page X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0xAB);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.STA_ZPX));
        cpu.getMemory().writeByte(1, (byte) 0x40);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0xAB, cpu.getMemory().readByte(0x42));

        // Test STA Absolute mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0xCD);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.STA_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0xCD, cpu.getMemory().readByte(0x1234));

        // Test STA Absolute X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0xEF);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.STA_ABSX));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0xEF, cpu.getMemory().readByte(0x1234));

        // Test STA Absolute Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x78);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.STA_ABSY));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x78, cpu.getMemory().readByte(0x1234));

        // Test STA Indirect X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x56);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x05);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.STA_INDX));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x20 + 0x05, (byte) 0x34);
        cpu.getMemory().writeByte(0x20 + 0x05 + 1, (byte) 0x12);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x56, cpu.getMemory().readByte(0x1234));

        // Test STA Indirect Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x89);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.STA_INDY));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x20, (byte) 0x56);
        cpu.getMemory().writeByte(0x20 + 1, (byte) 0x34);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x89, cpu.getMemory().readByte(0x3456 + 0x02));
    }

    @Test
    public void testADCInstruction() {
        CPU cpu = new CPU();

        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ADC_IM));
        cpu.getMemory().writeByte(1, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x09, cpu.getAccumulator().getValue());
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test ADC Zero Page mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ADC_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x09, cpu.getAccumulator().getValue());
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test ADC Zero Page X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ADC_ZPX));
        cpu.getMemory().writeByte(1, (byte) 0x40);
        cpu.getMemory().writeByte(0x42, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x09, cpu.getAccumulator().getValue());
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test ADC Absolute mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ADC_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x09, cpu.getAccumulator().getValue());
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test ADC Absolute X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ADC_ABSX));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x09, cpu.getAccumulator().getValue());
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test ADC Absolute Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ADC_ABSY));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x09, cpu.getAccumulator().getValue());
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test ADC Indirect X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ADC_INDX));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x22, (byte) 0x34);
        cpu.getMemory().writeByte(0x23, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x09, cpu.getAccumulator().getValue());
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test ADC Indirect Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ADC_INDY));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x20, (byte) 0x34);
        cpu.getMemory().writeByte(0x21, (byte) 0x12);
        cpu.getMemory().writeByte(0x1236, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x09, cpu.getAccumulator().getValue());
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
    }

    @Test
    public void testArithmeticsWithCarryInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test SBC Immediate mode with carry true
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_IM));
        cpu.getMemory().writeByte(1, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x02, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test SBC Zero Page mode with carry true
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x02, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

    }

    @Test
    public void testSBCInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test SBC Immediate mode
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false); // Carry is false
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_IM));
        cpu.getMemory().writeByte(1, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x01, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test SBC Zero Page mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x01, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test SBC Zero Page X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_ZPX));
        cpu.getMemory().writeByte(1, (byte) 0x40);
        cpu.getMemory().writeByte(0x42, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x01, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test SBC Absolute mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x01, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test SBC Absolute X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_ABSX));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x01, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test SBC Absolute Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_ABSY));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x01, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test SBC Indirect X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_INDX));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x22, (byte) 0x34);
        cpu.getMemory().writeByte(0x23, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x01, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));

        // Test SBC Indirect Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SBC_INDY));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x20, (byte) 0x34);
        cpu.getMemory().writeByte(0x21, (byte) 0x12);
        cpu.getMemory().writeByte(0x1236, (byte) 0x03);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x01, cpu.getAccumulator().getValue());
	Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
	Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
    }

    @Test
    public void testCompareInstructions() {
        // Initialize CPU
        CPU cpu = new CPU();
        cpu.reset();

        // Test CMP Immediate mode
        cpu.getAccumulator().setValue((byte) 0x05);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.CMP_IM));
        cpu.getMemory().writeByte(1, (byte) 0x05);
        cpu.executeNextInstruction();
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));

        // Test CPY Zero Page mode
        cpu.reset();
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x05);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.CPY_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0x05);
        cpu.executeNextInstruction();
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));

        // Test CPX Absolute mode
        cpu.reset();
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x05);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.CPX_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0x05);
        cpu.executeNextInstruction();
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
    }

    @Test
    public void testBranchInstructions(){
        CPU cpu = new CPU();
        cpu.reset();

        // Test BCS - Branch if Carry Set
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BCS));
        cpu.getMemory().writeByte(1, (byte) 0x09);
        cpu.executeNextInstruction();
        Assert.assertEquals(11,cpu.getProgramCounter().getValue());

        // Test BEQ - Branch if Equal
        cpu.reset();
        cpu.getProcessorStatus().setFlag(ProcessorFlag.ZERO, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BEQ));
        cpu.getMemory().writeByte(1, (byte) 0x09);
        cpu.executeNextInstruction();
        Assert.assertEquals(11,cpu.getProgramCounter().getValue());

        // Test BMI - Branch if Minus
        cpu.reset();
        cpu.getProcessorStatus().setFlag(ProcessorFlag.NEGATIVE, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BMI));
        cpu.getMemory().writeByte(1, (byte) 0x09);
        cpu.executeNextInstruction();
        Assert.assertEquals(11,cpu.getProgramCounter().getValue());

        // Test BNE - Branch if Not Equal
        cpu.reset();
        cpu.getProcessorStatus().setFlag(ProcessorFlag.ZERO, false);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BNE));
        cpu.getMemory().writeByte(1, (byte) 0x09);
        cpu.executeNextInstruction();
        Assert.assertEquals(11,cpu.getProgramCounter().getValue());

        // Test BPL - Branch if Positive
        cpu.reset();
        cpu.getProcessorStatus().setFlag(ProcessorFlag.NEGATIVE, false);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BPL));
        cpu.getMemory().writeByte(1, (byte) 0x09);
        cpu.executeNextInstruction();
        Assert.assertEquals(11,cpu.getProgramCounter().getValue());

        // Test BVC - Branch if Overflow Clear
        cpu.reset();
        cpu.getProcessorStatus().setFlag(ProcessorFlag.OVERFLOW, false);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BVC));
        cpu.getMemory().writeByte(1, (byte) 0x09);
        cpu.executeNextInstruction();
        Assert.assertEquals(11,cpu.getProgramCounter().getValue());

        // Test BVS - Branch if Overflow Set
        cpu.reset();
        cpu.getProcessorStatus().setFlag(ProcessorFlag.OVERFLOW, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BVS));
        cpu.getMemory().writeByte(1, (byte) 0x09);
        cpu.executeNextInstruction();
        Assert.assertEquals(11,cpu.getProgramCounter().getValue());
    }

    @Test
    public void testTransferInstructions() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test TAX - Transfer Accumulator to X
        cpu.getAccumulator().setValue((byte) 0x42);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TAX));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x42, cpu.getIndexRegister(IndexRegisterType.X).getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x00);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TAX));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x00, cpu.getIndexRegister(IndexRegisterType.X).getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test TAY - Transfer Accumulator to Y
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x7F);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TAY));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x7F, cpu.getIndexRegister(IndexRegisterType.Y).getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0x80);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TAY));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x80, cpu.getIndexRegister(IndexRegisterType.Y).getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test TXA - Transfer X to Accumulator
        cpu.reset();
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0xFF);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TXA));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0xFF, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test TYA - Transfer Y to Accumulator
        cpu.reset();
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x00);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TYA));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x00, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));
    }

    @Test
    public void testTransferInstructionsWithStackPointer() {
        CPU cpu = new CPU();
        cpu.reset();
        // Test TSX - Transfer Stack Pointer to X
        cpu.getStackPointer().setValue((byte) 0xAB);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TSX));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0xAB, cpu.getIndexRegister(IndexRegisterType.X).getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        cpu.reset();
        cpu.getStackPointer().setValue((byte) 0x00);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TSX));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x00, cpu.getIndexRegister(IndexRegisterType.X).getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test TXS - Transfer X to Stack Pointer
        cpu.reset();
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x7F);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TXS));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x7F, cpu.getStackPointer().getValue());

        cpu.reset();
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0xFF);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.TXS));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0xFF, cpu.getStackPointer().getValue());
    }

    @Test
    public void testStackInstructions() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test PHA (Push Accumulator on Stack)
        cpu.getAccumulator().setValue((byte) 0xAB);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.PHA));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0xAB, cpu.pullByte());

        // Test PLA (Pull Accumulator from Stack)
        cpu.reset();
        cpu.pushByte((byte) 0x7F);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.PLA));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x7F, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        cpu.reset();
        cpu.pushByte((byte) 0x00);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.PLA));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x00, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        cpu.reset();
        cpu.pushByte((byte) 0x80);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.PLA));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0x80, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test PHP (Push Processor Status on Stack)
        cpu.reset();
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.ZERO, true);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.INTERRUPT_DISABLE, true);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.DECIMAL_MODE, true);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.BREAK_COMMAND, true);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.OVERFLOW, true);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.NEGATIVE, true);
        cpu.getProcessorStatus().setFlag(ProcessorFlag.UNUSED, true);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.PHP));
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0xFF, cpu.pullByte());

        // Test PLP (Pull Processor Status from Stack)
        cpu.reset();
        cpu.pushByte((byte) 0xC3);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.PLP));

        cpu.executeNextInstruction();
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.INTERRUPT_DISABLE));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.DECIMAL_MODE));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.BREAK_COMMAND));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.UNUSED));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.OVERFLOW));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));
    }

    @Test
    public void testANDInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test AND Immediate mode
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.AND_IM));
        cpu.getMemory().writeByte(1, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b00000000, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test AND Zero Page mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.AND_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b00000000, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test AND Zero Page X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.AND_ZPX));
        cpu.getMemory().writeByte(1, (byte) 0x40);
        cpu.getMemory().writeByte(0x42, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b00000000, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test AND Absolute mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.AND_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b00000000, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test AND Absolute X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.AND_ABSX));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b00000000, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test AND Absolute Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.AND_ABSY));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b00000000, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test AND Indirect X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.AND_INDX));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x22, (byte) 0x34);
        cpu.getMemory().writeByte(0x23, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b00000000, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test AND Indirect Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.AND_INDY));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x20, (byte) 0x34);
        cpu.getMemory().writeByte(0x21, (byte) 0x12);
        cpu.getMemory().writeByte(0x1236, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b00000000, cpu.getAccumulator().getValue());
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));
    }

    @Test
    public void testEORInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test EOR Immediate mode
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.EOR_IM));
        cpu.getMemory().writeByte(1, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test EOR Zero Page mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.EOR_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test EOR Zero Page X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.EOR_ZPX));
        cpu.getMemory().writeByte(1, (byte) 0x40);
        cpu.getMemory().writeByte(0x42, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test EOR Absolute mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.EOR_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test EOR Absolute X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.EOR_ABSX));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test EOR Absolute Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.EOR_ABSY));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test EOR Indirect X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.EOR_INDX));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x22, (byte) 0x34);
        cpu.getMemory().writeByte(0x23, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test EOR Indirect Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.EOR_INDY));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x20, (byte) 0x34);
        cpu.getMemory().writeByte(0x21, (byte) 0x12);
        cpu.getMemory().writeByte(0x1236, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));
    }

    @Test
    public void testORAInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test ORA Immediate mode
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ORA_IM));
        cpu.getMemory().writeByte(1, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test ORA Zero Page mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ORA_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test ORA Zero Page X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ORA_ZPX));
        cpu.getMemory().writeByte(1, (byte) 0x40);
        cpu.getMemory().writeByte(0x42, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test ORA Absolute mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ORA_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test ORA Absolute X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ORA_ABSX));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test ORA Absolute Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x01);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ORA_ABSY));
        cpu.getMemory().writeByte(1, (byte) 0x33);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test ORA Indirect X mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.X).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ORA_INDX));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x22, (byte) 0x34);
        cpu.getMemory().writeByte(0x23, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));

        // Test ORA Indirect Y mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getIndexRegister(IndexRegisterType.Y).setValue((byte) 0x02);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.ORA_INDY));
        cpu.getMemory().writeByte(1, (byte) 0x20);
        cpu.getMemory().writeByte(0x20, (byte) 0x34);
        cpu.getMemory().writeByte(0x21, (byte) 0x12);
        cpu.getMemory().writeByte(0x1236, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertEquals((byte) 0b11111111, cpu.getAccumulator().getValue());
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));
    }

    @Test
    public void testBITInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Test BIT Zero Page mode
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BIT_ZP));
        cpu.getMemory().writeByte(1, (byte) 0x42);
        cpu.getMemory().writeByte(0x42, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.OVERFLOW));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));



        // Test BIT Absolute mode
        cpu.reset();
        cpu.getAccumulator().setValue((byte) 0b10101010);
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.BIT_ABS));
        cpu.getMemory().writeByte(1, (byte) 0x34);
        cpu.getMemory().writeByte(2, (byte) 0x12);
        cpu.getMemory().writeByte(0x1234, (byte) 0b01010101);
        cpu.executeNextInstruction();
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.ZERO));
        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.OVERFLOW));
        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.NEGATIVE));
    }
    @Test
    public void testCLCInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Set carry flag
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, true);

        // Execute CLC instruction
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.CLC));
        cpu.executeNextInstruction();

        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
    }

    @Test
    public void testCLDInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Set decimal mode flag
        cpu.getProcessorStatus().setFlag(ProcessorFlag.DECIMAL_MODE, true);

        // Execute CLD instruction
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.CLD));
        cpu.executeNextInstruction();

        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.DECIMAL_MODE));
    }

    @Test
    public void testCLIInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Set interrupt disable flag
        cpu.getProcessorStatus().setFlag(ProcessorFlag.INTERRUPT_DISABLE, true);

        // Execute CLI instruction
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.CLI));
        cpu.executeNextInstruction();

        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.INTERRUPT_DISABLE));
    }

    @Test
    public void testCLVInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Set overflow flag
        cpu.getProcessorStatus().setFlag(ProcessorFlag.OVERFLOW, true);

        // Execute CLV instruction
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.CLV));
        cpu.executeNextInstruction();

        Assert.assertFalse(cpu.getProcessorStatus().getFlag(ProcessorFlag.OVERFLOW));
    }

    @Test
    public void testSECInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Clear carry flag
        cpu.getProcessorStatus().setFlag(ProcessorFlag.CARRY, false);

        // Execute SEC instruction
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SEC));
        cpu.executeNextInstruction();

        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.CARRY));
    }

    @Test
    public void testSEDInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Clear decimal mode flag
        cpu.getProcessorStatus().setFlag(ProcessorFlag.DECIMAL_MODE, false);

        // Execute SED instruction
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SED));
        cpu.executeNextInstruction();

        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.DECIMAL_MODE));
    }

    @Test
    public void testSEIInstruction() {
        CPU cpu = new CPU();
        cpu.reset();

        // Clear interrupt disable flag
        cpu.getProcessorStatus().setFlag(ProcessorFlag.INTERRUPT_DISABLE, false);

        // Execute SEI instruction
        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.SEI));
        cpu.executeNextInstruction();

        Assert.assertTrue(cpu.getProcessorStatus().getFlag(ProcessorFlag.INTERRUPT_DISABLE));
    }
}
