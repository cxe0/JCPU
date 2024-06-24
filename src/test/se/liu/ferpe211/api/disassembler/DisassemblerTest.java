package se.liu.ferpe211.api.disassembler;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;
import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.OpCode;
import se.liu.ferpe211.api.operation.OpCodeMapper;



/**
 * Test cases for the Disassembler {@link Disassembler}
 * Here we test if disassembling memory produces correct entries {@link Entry}
 */
public class DisassemblerTest extends TestCase
{

    @Test
    public void testDisasemble() {
        EntryComparator entryComparator = new EntryComparator();
        CPU cpu = new CPU();
        Disassembler disassembler = new Disassembler(cpu);
        cpu.reset();

        cpu.getMemory().writeByte(0, OpCodeMapper.getByteFromOpCode(OpCode.NOP));


        cpu.getMemory().writeByte(1, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ZP));
        cpu.getMemory().writeByte(2, (byte) 0x42);

        cpu.getMemory().writeByte(3, OpCodeMapper.getByteFromOpCode(OpCode.LDA_ABS));
        cpu.getMemory().writeByte(4, (byte) 0x34);
        cpu.getMemory().writeByte(5, (byte) 0x12);


        cpu.getMemory().writeByte(6, OpCodeMapper.getByteFromOpCode(OpCode.BRK));


        disassembler.disasemble();

        Assert.assertFalse(disassembler.getEntries().isEmpty());
        Assert.assertEquals(disassembler.getEntries().size(), 4);

        Entry entryNoArg = new Entry(0, cpu);
        Entry entryOneArg = new Entry(1, cpu);
        Entry entryTwoArg = new Entry(3, cpu);

        Assert.assertEquals(0, entryComparator.compare(entryNoArg, disassembler.getEntries().get(0)));
        Assert.assertEquals(0, entryComparator.compare(entryOneArg, disassembler.getEntries().get(1)));
        Assert.assertEquals(0, entryComparator.compare(entryTwoArg, disassembler.getEntries().get(2)));





    }

}