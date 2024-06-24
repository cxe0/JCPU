package se.liu.ferpe211.api.operation;

import com.google.common.collect.ImmutableBiMap;
import se.liu.ferpe211.impl.Emulator;

import java.util.logging.Level;

/**
 * Class for mapping operation codes to the correct byte values compatible with the 6502 architecture.
 * The byte values for each operation can be found at <a href="http://www.6502.org/users/obelisk/">http://www.6502.org/users/obelisk/</a>.
 * This class utilizes a ImmutableBiMap to map byte values to operation codes and vice versa.
 * For efficient bidirectional mapping, it employs the Google Guava BiMap data structure.
 * @see com.google.common.collect.ImmutableBiMap
 */
public class OpCodeMapper {
    private static final ImmutableBiMap<Byte, OpCode> BYTE_OP_CODE_BI_MAP = new ImmutableBiMap.Builder<Byte, OpCode>()
	    // LDA
	    .put((byte) 0xA9, OpCode.LDA_IM)
	    .put((byte) 0xA5, OpCode.LDA_ZP)
	    .put((byte) 0xB5, OpCode.LDA_ZPX)
	    .put((byte) 0xAD, OpCode.LDA_ABS)
	    .put((byte) 0xBD, OpCode.LDA_ABSX)
	    .put((byte) 0xB9, OpCode.LDA_ABSY)
	    .put((byte) 0xA1, OpCode.LDA_INDX)
	    .put((byte) 0xB1, OpCode.LDA_INDY)

	    // LDX
	    .put((byte) 0xA2, OpCode.LDX_IM)
	    .put((byte) 0xA6, OpCode.LDX_ZP)
	    .put((byte) 0xB6, OpCode.LDX_ZPY)
	    .put((byte) 0xAE, OpCode.LDX_ABS)
	    .put((byte) 0xBE, OpCode.LDX_ABSY)

	    //LDY
	    .put((byte) 0xA0, OpCode.LDY_IM)
	    .put((byte) 0xA4, OpCode.LDY_ZP)
	    .put((byte) 0xB4, OpCode.LDY_ZPX)
	    .put((byte) 0xAC, OpCode.LDY_ABS)
	    .put((byte) 0xBC, OpCode.LDY_ABSX)

	    //STA
	    .put((byte) 0x85, OpCode.STA_ZP)
	    .put((byte) 0x95, OpCode.STA_ZPX)
	    .put((byte) 0x8D, OpCode.STA_ABS)
	    .put((byte) 0x9D, OpCode.STA_ABSX)
	    .put((byte) 0x99, OpCode.STA_ABSY)
	    .put((byte) 0x81, OpCode.STA_INDX)
	    .put((byte) 0x91, OpCode.STA_INDY)

	//STX
	    .put((byte) 0x86, OpCode.STX_ZP)
	    .put((byte) 0x96, OpCode.STX_ZPY)
	    .put((byte) 0x8E, OpCode.STX_ABS)

	//STY
	    .put((byte) 0x84, OpCode.STY_ZP)
	    .put((byte) 0x94, OpCode.STY_ZPX)
	    .put((byte) 0x8C, OpCode.STY_ABS)

	//ADC
	    .put((byte) 0x69, OpCode.ADC_IM)
	    .put((byte) 0x65, OpCode.ADC_ZP)
	    .put((byte) 0x75, OpCode.ADC_ZPX)
	    .put((byte) 0x6D, OpCode.ADC_ABS)
	    .put((byte) 0x7D, OpCode.ADC_ABSX)
	    .put((byte) 0x79, OpCode.ADC_ABSY)
	    .put((byte) 0x61, OpCode.ADC_INDX)
	    .put((byte) 0x71, OpCode.ADC_INDY)

	//SBC
	    .put((byte) 0xE9, OpCode.SBC_IM)
	    .put((byte) 0xE5, OpCode.SBC_ZP)
	    .put((byte) 0xF5, OpCode.SBC_ZPX)
	    .put((byte) 0xED, OpCode.SBC_ABS)
	    .put((byte) 0xFD, OpCode.SBC_ABSX)
	    .put((byte) 0xF9, OpCode.SBC_ABSY)
	    .put((byte) 0xE1, OpCode.SBC_INDX)
	    .put((byte) 0xF1, OpCode.SBC_INDY)

	//CMP
	    .put((byte) 0xC9, OpCode.CMP_IM)
	    .put((byte) 0xC5, OpCode.CMP_ZP)
	    .put((byte) 0xD5, OpCode.CMP_ZPX)
	    .put((byte) 0xCD, OpCode.CMP_ABS)
	    .put((byte) 0xDD, OpCode.CMP_ABSX)
	    .put((byte) 0xD9, OpCode.CMP_ABSY)
	    .put((byte) 0xC1, OpCode.CMP_INDX)
	    .put((byte) 0xD1, OpCode.CMP_INDY)

	//CPX
	    .put((byte) 0xE0, OpCode.CPX_IM)
	    .put((byte) 0xE4, OpCode.CPX_ZP)
	    .put((byte) 0xEC, OpCode.CPX_ABS)

	//CPY
	    .put((byte) 0xC0, OpCode.CPY_IM)
	    .put((byte) 0xC4, OpCode.CPY_ZP)
	    .put((byte) 0xCC, OpCode.CPY_ABS)

	    //BRANCHES
	    .put((byte) 0x90, OpCode.BCC)
	    .put((byte) 0xB0, OpCode.BCS)
	    .put((byte) 0xF0, OpCode.BEQ)
	    .put((byte) 0x30, OpCode.BMI)
	    .put((byte) 0xD0, OpCode.BNE)
	    .put((byte) 0x10, OpCode.BPL)
	    .put((byte) 0x50, OpCode.BVC)
	    .put((byte) 0x70, OpCode.BVS)

	    //TRANSFER
	    .put((byte) 0xAA, OpCode.TAX)
	    .put((byte) 0xA8, OpCode.TAY)
	    .put((byte) 0x8A, OpCode.TXA)
	    .put((byte) 0x98, OpCode.TYA)
	    .put((byte) 0xBA, OpCode.TSX)
	    .put((byte) 0x9A, OpCode.TXS)

	    //PULL/PUSH
	    .put((byte) 0x48, OpCode.PHA)
	    .put((byte) 0x68, OpCode.PLA)
	    .put((byte) 0x08, OpCode.PHP)
	    .put((byte) 0x28, OpCode.PLP)


	    //AND
	    .put((byte) 0x29, OpCode.AND_IM)
	    .put((byte) 0x25, OpCode.AND_ZP)
	    .put((byte) 0x35, OpCode.AND_ZPX)
	    .put((byte) 0x2D, OpCode.AND_ABS)
	    .put((byte) 0x3D, OpCode.AND_ABSX)
	    .put((byte) 0x39, OpCode.AND_ABSY)
	    .put((byte) 0x21, OpCode.AND_INDX)
	    .put((byte) 0x31, OpCode.AND_INDY)

	    //EOR
	    .put((byte) 0x49, OpCode.EOR_IM)
	    .put((byte) 0x45, OpCode.EOR_ZP)
	    .put((byte) 0x55, OpCode.EOR_ZPX)
	    .put((byte) 0x4D, OpCode.EOR_ABS)
	    .put((byte) 0x5D, OpCode.EOR_ABSX)
	    .put((byte) 0x59, OpCode.EOR_ABSY)
	    .put((byte) 0x41, OpCode.EOR_INDX)
	    .put((byte) 0x51, OpCode.EOR_INDY)

	    //ORA
	    .put((byte) 0x09, OpCode.ORA_IM)
	    .put((byte) 0x05, OpCode.ORA_ZP)
	    .put((byte) 0x15, OpCode.ORA_ZPX)
	    .put((byte) 0x0D, OpCode.ORA_ABS)
	    .put((byte) 0x1D, OpCode.ORA_ABSX)
	    .put((byte) 0x19, OpCode.ORA_ABSY)
	    .put((byte) 0x01, OpCode.ORA_INDX)
	    .put((byte) 0x11, OpCode.ORA_INDY)

	    //BIT
	    .put((byte) 0x24, OpCode.BIT_ZP)
	    .put((byte) 0x2C, OpCode.BIT_ABS)

	    // JMP
	    .put((byte) 0x4C, OpCode.JMP_ABS)
	    .put((byte) 0x6C, OpCode.JMP_IND)

	    //CLEAR
	    .put((byte) 0x18, OpCode.CLC)
	    .put((byte) 0xD8, OpCode.CLD)
	    .put((byte) 0x58, OpCode.CLI)
	    .put((byte) 0xB8, OpCode.CLV)

	    //SET
	    .put((byte) 0x38, OpCode.SEC)
	    .put((byte) 0xF8, OpCode.SED)
	    .put((byte) 0x78, OpCode.SEI)


	    .put((byte) 0x00, OpCode.BRK)
	    .put((byte) 0xEA, OpCode.NOP)
	    .build();



    public static OpCode getOpCodeFromByte(byte value) {
	if (BYTE_OP_CODE_BI_MAP.containsKey(value)) {
	    return BYTE_OP_CODE_BI_MAP.get(value);
	}
	Emulator.LOGGER.log(Level.SEVERE, "Could not match opcode for provided byte value: " + value);
	return OpCode.NOP;
    }

    public static byte getByteFromOpCode(OpCode opCode) {
	if (BYTE_OP_CODE_BI_MAP.containsValue(opCode)) {
	    return BYTE_OP_CODE_BI_MAP.inverse().get(opCode);
	}
	Emulator.LOGGER.log(Level.SEVERE, "Unknown opcode provided: " + opCode.name());
	return 0;
    }
}

