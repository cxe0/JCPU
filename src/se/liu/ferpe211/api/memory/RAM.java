package se.liu.ferpe211.api.memory;

import se.liu.ferpe211.impl.Emulator;

import java.util.logging.Level;

/**
 * Represents Random Access Memory (RAM) in a computer system.
 * This class provides functionality related to RAM.
 */
public class RAM
{
    private final static int BYTES_PER_KB = 1024;
    private final static int MEM_SIZE = BYTES_PER_KB * 64;

    public byte[] getData() {
	return data;
    }

    public void setData(final byte[] data) {
	this.data = data;
    }

    private byte[] data;

    public RAM() {
	Emulator.LOGGER.log(Level.INFO, "RAM created with " + MEM_SIZE + " bytes");
	data = new byte[MEM_SIZE];
    }

    public int size() {
	return MEM_SIZE;
    }

    public byte readByte(final int address) {
	if (address < 0 || address >= MEM_SIZE) {
	    throw new IllegalArgumentException("Invalid memory address");
	}
	return data[address];
    }

    public void writeByte(final int address, final byte value) {
	if (address < 0 || address >= MEM_SIZE) {
	    throw new IllegalArgumentException("Invalid memory address");
	}
	data[address] = value;
    }

    public void init() {
	for (int i = 0; i < MEM_SIZE; i++) {
	    data[i] = 0;
	}
    }



}
