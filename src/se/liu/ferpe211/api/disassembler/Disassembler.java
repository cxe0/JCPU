package se.liu.ferpe211.api.disassembler;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.event.EventType;
import se.liu.ferpe211.api.operation.OpCode;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for disassembling the contents of the cpu memory
 * <p>
 *	The disassembling is done by analyzing the memory and creating entries for each instruction {@link Entry}
 *	It then stops disassembling when encountering a break instruction
 * </p>
 */
public class Disassembler
{
    private List<Entry> entries;
    private CPU cpu;

    public Disassembler(CPU cpu){
	entries = new ArrayList<>();
	this.cpu = cpu;
    }

    public void disasemble(){
	entries.clear();
	int currentAddress = 0;
	while (true){
	    Entry entry = new Entry(currentAddress, cpu );
	    currentAddress+=entry.getOffset();
	    entries.add(entry);
	    if(entry.getOpCode().equals(OpCode.BRK))
		break;
	}
	cpu.notifyListeners(EventType.MEMORY_ACCESS);
    }



    public List<Entry> getEntries() {
	return entries;
    }

}
