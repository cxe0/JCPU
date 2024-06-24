package se.liu.ferpe211.api.operation;
/**
 * Represents the opcodes used in 6502 CPU instructions.
 * Opcodes define specific operations that the CPU can perform.
 */
 public enum OpCode {
    LDA_IM,
    LDA_ZP,
    LDA_ZPX,
    LDA_ABS,
    LDA_ABSX,
    LDA_ABSY,
    LDA_INDX,
    LDA_INDY,

    LDX_IM,
    LDX_ZP,
    LDX_ZPY,
    LDX_ABS,
    LDX_ABSY,

    LDY_IM,
    LDY_ZP,
    LDY_ZPX,
    LDY_ABS,
    LDY_ABSX,

    STA_ZP,
    STA_ZPX,
    STA_ABS,
    STA_ABSX,
    STA_ABSY,
    STA_INDX,
    STA_INDY,

    STX_ZP,
    STX_ZPY,
    STX_ABS,

    STY_ZP,
    STY_ZPX,
    STY_ABS,

    ADC_IM,
    ADC_ZP,
    ADC_ZPX,
    ADC_ABS,
    ADC_ABSX,
    ADC_ABSY,
    ADC_INDX,
    ADC_INDY,

    SBC_IM,
    SBC_ZP,
    SBC_ZPX,
    SBC_ABS,
    SBC_ABSX,
    SBC_ABSY,
    SBC_INDX,
    SBC_INDY,

    CMP_IM,
    CMP_ZP,
    CMP_ZPX,
    CMP_ABS,
    CMP_ABSX,
    CMP_ABSY,
    CMP_INDX,
    CMP_INDY,

    CPX_IM,
    CPX_ZP,
    CPX_ABS,

    CPY_IM,
    CPY_ZP,
    CPY_ABS,

    BCC,
   BCS,
   BEQ,
   BMI,
   BNE,
   BPL,
   BVC,
   BVS,

    TAX,
    TAY,
    TXA,
    TYA,

    TSX,
    TXS,

    PHA,
    PLA,
    PHP,
    PLP,

    AND_IM,
    AND_ZP,
    AND_ZPX,
    AND_ABS,
    AND_ABSX,
    AND_ABSY,
    AND_INDX,
    AND_INDY,

    EOR_IM,
    EOR_ZP,
    EOR_ZPX,
    EOR_ABS,
    EOR_ABSX,
    EOR_ABSY,
    EOR_INDX,
    EOR_INDY,

    ORA_IM,
    ORA_ZP,
    ORA_ZPX,
    ORA_ABS,
    ORA_ABSX,
    ORA_ABSY,
    ORA_INDX,
    ORA_INDY,

    BIT_ZP,
    BIT_ABS,

    JMP_ABS,
    JMP_IND,


    CLC,
    CLD,
    CLI,
    CLV,

    SEC,
    SED,
    SEI,

    BRK,
    NOP
}
