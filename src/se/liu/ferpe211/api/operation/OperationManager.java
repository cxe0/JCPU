package se.liu.ferpe211.api.operation;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.operation.instruction.Instruction;
import se.liu.ferpe211.api.operation.instruction.impl.ADD;
import se.liu.ferpe211.api.operation.instruction.impl.CLEAR;
import se.liu.ferpe211.api.operation.instruction.impl.CMP;
import se.liu.ferpe211.api.operation.instruction.impl.JUMP;
import se.liu.ferpe211.api.operation.instruction.impl.LOAD;
import se.liu.ferpe211.api.operation.instruction.impl.SET;
import se.liu.ferpe211.api.operation.instruction.impl.STORE;
import se.liu.ferpe211.api.operation.instruction.impl.SUB;
import se.liu.ferpe211.api.operation.instruction.impl.branch.BRANCH;
import se.liu.ferpe211.api.operation.instruction.impl.branch.BranchMode;
import se.liu.ferpe211.api.operation.instruction.impl.logical.AND;
import se.liu.ferpe211.api.operation.instruction.impl.logical.BIT;
import se.liu.ferpe211.api.operation.instruction.impl.logical.EOR;
import se.liu.ferpe211.api.operation.instruction.impl.logical.ORA;
import se.liu.ferpe211.api.operation.instruction.impl.stack.STACK;
import se.liu.ferpe211.api.operation.instruction.impl.stack.StackMode;
import se.liu.ferpe211.api.operation.instruction.impl.system.BREAK;
import se.liu.ferpe211.api.operation.instruction.impl.system.NOP;
import se.liu.ferpe211.api.operation.instruction.impl.transfer.TRANSFER;
import se.liu.ferpe211.api.operation.instruction.impl.transfer.TransferMode;
import se.liu.ferpe211.api.register.IndexRegisterType;
import se.liu.ferpe211.api.register.ProcessorFlag;
import se.liu.ferpe211.api.register.Register8Bit;

import java.util.HashMap;
import java.util.Map;

/**
 * Manager class for mapping byte values to their corresponding instruction implementations.
 * This class provides functionality for decoding byte values into executable instructions.
 *
 * @see Instruction
 */
public class OperationManager
{

    private Map<Byte, Instruction> operationMap;

    public OperationManager(CPU cpu){
	operationMap = new HashMap<>();
	Register8Bit accumulator = cpu.getAccumulator();
	Register8Bit indexRegisterY = cpu.getIndexRegister(IndexRegisterType.Y);
	Register8Bit indexRegisterX = cpu.getIndexRegister(IndexRegisterType.X);

	//LDA
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDA_IM), new LOAD(cpu, 2, AddressingMode.IMMEDIATE,2, accumulator)); // LDA_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDA_ZP), new LOAD(cpu, 3, AddressingMode.ZERO_PAGE,2, accumulator)); // LDA_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDA_ZPX), new LOAD(cpu, 4, AddressingMode.ZERO_PAGE_X,2, accumulator)); // LDA_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDA_ABS), new LOAD(cpu, 4, AddressingMode.ABSOLUTE,3, accumulator)); // LDA_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDA_ABSX), new LOAD(cpu, 4, AddressingMode.ABSOLUTE_X,3, accumulator)); // LDA_ABSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDA_ABSY), new LOAD(cpu, 4, AddressingMode.ABSOLUTE_Y,3, accumulator)); // LDA_ABSY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDA_INDX), new LOAD(cpu, 6, AddressingMode.INDIRECT_X,2, accumulator)); // LDA_INDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDA_INDY), new LOAD(cpu, 5, AddressingMode.INDIRECT_Y,2, accumulator)); // LDA_INDY

	//LDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDX_IM), new LOAD(cpu, 2, AddressingMode.IMMEDIATE,2, indexRegisterX)); // LDX_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDX_ZP), new LOAD(cpu, 3, AddressingMode.ZERO_PAGE,2, indexRegisterX)); // LDX_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDX_ZPY), new LOAD(cpu, 4, AddressingMode.ZERO_PAGE_Y,2, indexRegisterX)); // LDX_ZPY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDX_ABS), new LOAD(cpu, 4, AddressingMode.ABSOLUTE,3, indexRegisterX)); // LDX_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDX_ABSY), new LOAD(cpu, 4, AddressingMode.ABSOLUTE_Y,3, indexRegisterX)); // LDX_ABSY

	// LDY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDY_IM), new LOAD(cpu, 2, AddressingMode.IMMEDIATE, 2, indexRegisterY)); // LDY_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDY_ZP), new LOAD(cpu, 3, AddressingMode.ZERO_PAGE, 2, indexRegisterY)); // LDY_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDY_ZPX), new LOAD(cpu, 4, AddressingMode.ZERO_PAGE_X, 2, indexRegisterY)); // LDY_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDY_ABS), new LOAD(cpu, 4, AddressingMode.ABSOLUTE, 3, indexRegisterY)); // LDY_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.LDY_ABSX), new LOAD(cpu, 4, AddressingMode.ABSOLUTE_X, 3, indexRegisterY)); // LDY_ABSX

	//STA
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STA_ZP), new STORE(cpu, 3, AddressingMode.ZERO_PAGE, 2, accumulator)); // STA_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STA_ZPX), new STORE(cpu, 4, AddressingMode.ZERO_PAGE_X,2, accumulator)); // STA_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STA_ABS), new STORE(cpu, 4, AddressingMode.ABSOLUTE,3, accumulator)); // STA_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STA_ABSX), new STORE(cpu, 5, AddressingMode.ABSOLUTE_X,3, accumulator)); // STA_ABSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STA_ABSY), new STORE(cpu, 5, AddressingMode.ABSOLUTE_Y,3, accumulator)); // STA_ABSY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STA_INDX), new STORE(cpu, 6, AddressingMode.INDIRECT_X,2, accumulator)); // STA_INDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STA_INDY), new STORE(cpu, 6, AddressingMode.INDIRECT_Y,2, accumulator)); // STA_INDY

	//STX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STX_ZP), new STORE(cpu, 3, AddressingMode.ZERO_PAGE, 2, indexRegisterX)); // STX_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STX_ZPY), new STORE(cpu, 4, AddressingMode.ZERO_PAGE_Y, 2, indexRegisterX)); // STX_ZPY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STX_ABS), new STORE(cpu, 4, AddressingMode.ABSOLUTE, 3, indexRegisterX)); // STX_ABS

	//STY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STY_ZP), new STORE(cpu, 3, AddressingMode.ZERO_PAGE, 2, indexRegisterY)); // STY_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STY_ZPX), new STORE(cpu, 4, AddressingMode.ZERO_PAGE_X, 2, indexRegisterY)); // STY_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.STY_ABS), new STORE(cpu, 4, AddressingMode.ABSOLUTE, 3, indexRegisterY)); // STY_ABS

	//ADC
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ADC_IM), new ADD(cpu, 2, AddressingMode.IMMEDIATE, 2)); // ADC_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ADC_ZP), new ADD(cpu, 3, AddressingMode.ZERO_PAGE, 2)); // ADC_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ADC_ZPX), new ADD(cpu, 4, AddressingMode.ZERO_PAGE_X, 2)); // ADC_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ADC_ABS), new ADD(cpu, 4, AddressingMode.ABSOLUTE, 3)); // ADC_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ADC_ABSX), new ADD(cpu, 4, AddressingMode.ABSOLUTE_X, 3)); // ADC_ABSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ADC_ABSY), new ADD(cpu, 4, AddressingMode.ABSOLUTE_Y, 3)); // ADC_ABSY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ADC_INDX), new ADD(cpu, 6, AddressingMode.INDIRECT_X, 2)); // ADC_INDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ADC_INDY), new ADD(cpu, 5, AddressingMode.INDIRECT_Y, 2)); // ADC_INDY

	//SBC
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SBC_IM), new SUB(cpu, 2, AddressingMode.IMMEDIATE, 2)); // SBC_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SBC_ZP), new SUB(cpu, 3, AddressingMode.ZERO_PAGE, 2)); // SBC_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SBC_ZPX), new SUB(cpu, 4, AddressingMode.ZERO_PAGE_X, 2)); // SBC_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SBC_ABS), new SUB(cpu, 4, AddressingMode.ABSOLUTE, 3)); // SBC_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SBC_ABSX), new SUB(cpu, 4, AddressingMode.ABSOLUTE_X, 3)); // SBC_ABSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SBC_ABSY), new SUB(cpu, 4, AddressingMode.ABSOLUTE_Y, 3)); // SBC_ABSY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SBC_INDX), new SUB(cpu, 6, AddressingMode.INDIRECT_X, 2)); // SBC_INDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SBC_INDY), new SUB(cpu, 5, AddressingMode.INDIRECT_Y, 2)); // SBC_INDY

	//CMP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CMP_IM), new CMP(cpu, 2, AddressingMode.IMMEDIATE, 2, accumulator)); // CMP_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CMP_ZP), new CMP(cpu, 3, AddressingMode.ZERO_PAGE, 2, accumulator)); // CMP_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CMP_ZPX), new CMP(cpu, 4, AddressingMode.ZERO_PAGE_X, 2, accumulator)); // CMP_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CMP_ABS), new CMP(cpu, 4, AddressingMode.ABSOLUTE, 3, accumulator)); // CMP_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CMP_ABSX), new CMP(cpu, 4, AddressingMode.ABSOLUTE_X, 3, accumulator)); // CMP_ABSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CMP_ABSY), new CMP(cpu, 4, AddressingMode.ABSOLUTE_Y, 3, accumulator)); // CMP_ABSY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CMP_INDX), new CMP(cpu, 6, AddressingMode.INDIRECT_X, 2, accumulator)); // CMP_INDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CMP_INDY), new CMP(cpu, 5, AddressingMode.INDIRECT_Y, 2, accumulator)); // CMP_INDY

	//CPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CPX_IM), new CMP(cpu, 2, AddressingMode.IMMEDIATE, 2, indexRegisterX));
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CPX_ZP), new CMP(cpu, 3, AddressingMode.ZERO_PAGE, 2, indexRegisterX));
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CPX_ABS), new CMP(cpu, 4, AddressingMode.ABSOLUTE, 3, indexRegisterX));

	//CPY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CPY_IM), new CMP(cpu, 2, AddressingMode.IMMEDIATE, 2, indexRegisterY));
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CPY_ZP), new CMP(cpu, 3, AddressingMode.ZERO_PAGE, 2, indexRegisterY));
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CPY_ABS), new CMP(cpu, 4, AddressingMode.ABSOLUTE, 3, indexRegisterY));

	//BRANCHES
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BCC), new BRANCH(cpu, 2, AddressingMode.RELATIVE, 2, BranchMode.CARRY_CLEAR)); // BCC
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BCS), new BRANCH(cpu, 2, AddressingMode.RELATIVE, 2, BranchMode.CARRY_SET)); // BCS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BEQ), new BRANCH(cpu, 2, AddressingMode.RELATIVE, 2, BranchMode.EQUAL)); // BEQ
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BMI), new BRANCH(cpu, 2, AddressingMode.RELATIVE, 2, BranchMode.MINUS)); // BMI
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BNE), new BRANCH(cpu, 2, AddressingMode.RELATIVE, 2, BranchMode.NOT_EQUAL)); // BNE
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BPL), new BRANCH(cpu, 2, AddressingMode.RELATIVE, 2, BranchMode.POSITIVE)); // BPL
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BVC), new BRANCH(cpu, 2, AddressingMode.RELATIVE, 2, BranchMode.OVERFLOW_CLEAR)); // BVC
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BVS), new BRANCH(cpu, 2, AddressingMode.RELATIVE, 2, BranchMode.OVERFLOW_SET)); // BVS

	//JMP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.JMP_ABS), new JUMP(cpu, 3, AddressingMode.ABSOLUTE)); // JMP_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.JMP_IND), new JUMP(cpu, 5, AddressingMode.INDIRECT)); // JMP_IND

	//TRANSFER
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.TAX), new TRANSFER(cpu, 2, AddressingMode.IMPLIED, 2, accumulator, indexRegisterX, TransferMode.REGISTER)); //TAX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.TAY), new TRANSFER(cpu, 2, AddressingMode.IMPLIED, 2, accumulator, indexRegisterY,TransferMode.REGISTER)); //TAY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.TXA), new TRANSFER(cpu, 2, AddressingMode.IMPLIED, 2, indexRegisterX, accumulator,TransferMode.REGISTER)); //TXA
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.TYA), new TRANSFER(cpu, 2, AddressingMode.IMPLIED, 2, indexRegisterY, accumulator, TransferMode.REGISTER)); //TYA

	//STACK_TRANSFER
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.TSX), new TRANSFER(cpu, 2, AddressingMode.IMPLIED, 2, indexRegisterX, null, TransferMode.FROM_STACK)); //TSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.TXS), new TRANSFER(cpu, 2, AddressingMode.IMPLIED, 2, indexRegisterX, null, TransferMode.TO_STACK)); //TXS

	//PUSH PULL
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.PHA), new STACK(cpu, 4, AddressingMode.IMPLIED, 1, StackMode.PUSH_ACCUMULATOR)); // PHA
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.PLA), new STACK(cpu, 4, AddressingMode.IMPLIED, 1, StackMode.PULL_ACCUMULATOR)); // PLA
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.PHP), new STACK(cpu, 4, AddressingMode.IMPLIED, 1, StackMode.PUSH_PROCESSOR_STATUS)); // PHP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.PLP), new STACK(cpu, 4, AddressingMode.IMPLIED, 1, StackMode.PULL_PROCESSOR_STATUS)); // PLP

	//AND
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.AND_IM), new AND(cpu, 2, AddressingMode.IMMEDIATE, 2)); // AND_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.AND_ZP), new AND(cpu, 3, AddressingMode.ZERO_PAGE, 2)); // AND_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.AND_ZPX), new AND(cpu, 4, AddressingMode.ZERO_PAGE_X, 2)); // AND_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.AND_ABS), new AND(cpu, 4, AddressingMode.ABSOLUTE, 3)); // AND_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.AND_ABSX), new AND(cpu, 4, AddressingMode.ABSOLUTE_X, 3)); // AND_ABSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.AND_ABSY), new AND(cpu, 4, AddressingMode.ABSOLUTE_Y, 3)); // AND_ABSY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.AND_INDX), new AND(cpu, 6, AddressingMode.INDIRECT_X, 2)); // AND_INDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.AND_INDY), new AND(cpu, 5, AddressingMode.INDIRECT_Y, 2)); // AND_INDY

	//EOR
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.EOR_IM), new EOR(cpu, 2, AddressingMode.IMMEDIATE, 2)); // EOR_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.EOR_ZP), new EOR(cpu, 3, AddressingMode.ZERO_PAGE, 2)); // EOR_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.EOR_ZPX), new EOR(cpu, 4, AddressingMode.ZERO_PAGE_X, 2)); // EOR_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.EOR_ABS), new EOR(cpu, 4, AddressingMode.ABSOLUTE, 3)); // EOR_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.EOR_ABSX), new EOR(cpu, 4, AddressingMode.ABSOLUTE_X, 3)); // EOR_ABSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.EOR_ABSY), new EOR(cpu, 4, AddressingMode.ABSOLUTE_Y, 3)); // EOR_ABSY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.EOR_INDX), new EOR(cpu, 6, AddressingMode.INDIRECT_X, 2)); // EOR_INDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.EOR_INDY), new EOR(cpu, 5, AddressingMode.INDIRECT_Y, 2)); // EOR_INDY

	//ORA
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ORA_IM), new ORA(cpu, 2, AddressingMode.IMMEDIATE, 2)); // ORA_IM
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ORA_ZP), new ORA(cpu, 3, AddressingMode.ZERO_PAGE, 2)); // ORA_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ORA_ZPX), new ORA(cpu, 4, AddressingMode.ZERO_PAGE_X, 2)); // ORA_ZPX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ORA_ABS), new ORA(cpu, 4, AddressingMode.ABSOLUTE, 3)); // ORA_ABS
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ORA_ABSX), new ORA(cpu, 4, AddressingMode.ABSOLUTE_X, 3)); // ORA_ABSX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ORA_ABSY), new ORA(cpu, 4, AddressingMode.ABSOLUTE_Y, 3)); // ORA_ABSY
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ORA_INDX), new ORA(cpu, 6, AddressingMode.INDIRECT_X, 2)); // ORA_INDX
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.ORA_INDY), new ORA(cpu, 5, AddressingMode.INDIRECT_Y, 2)); // ORA_INDY

	//BIT
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BIT_ZP), new BIT(cpu, 3, AddressingMode.ZERO_PAGE, 2)); // BIT_ZP
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BIT_ABS), new BIT(cpu, 4, AddressingMode.ABSOLUTE, 3)); // BIT_ABS

	//CLEAR
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CLC), new CLEAR(cpu, 2, AddressingMode.IMPLIED, 1, ProcessorFlag.CARRY)); // CLEAR CARRY FLAG
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CLD), new CLEAR(cpu, 2, AddressingMode.IMPLIED, 1, ProcessorFlag.DECIMAL_MODE)); // CLEAR DECIMAL FLAG
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CLI), new CLEAR(cpu, 2, AddressingMode.IMPLIED, 1, ProcessorFlag.INTERRUPT_DISABLE)); // CLEAR INTERRUPT_DISABLE
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.CLV), new CLEAR(cpu, 2, AddressingMode.IMPLIED, 1, ProcessorFlag.OVERFLOW)); // CLEAR OVERFLOW

	//SET
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SEC), new SET(cpu, 2, AddressingMode.IMPLIED, 1, ProcessorFlag.CARRY)); // SET CARRY FLAG
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SED), new SET(cpu, 2, AddressingMode.IMPLIED, 1, ProcessorFlag.DECIMAL_MODE)); // SET DECIMAL FLAG
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.SEI), new SET(cpu, 2, AddressingMode.IMPLIED, 1, ProcessorFlag.INTERRUPT_DISABLE)); // SET INTERRUPT_DISABLE



	//NOP
	operationMap.put((byte) 0xEA, new NOP(cpu, 2, AddressingMode.IMPLIED));
	//BRK
	operationMap.put(OpCodeMapper.getByteFromOpCode(OpCode.BRK), new BREAK(cpu, 7, AddressingMode.IMPLIED));


    }
    public Instruction getInstruction(byte opcode) {
	return operationMap.getOrDefault(opcode, null);
    }

}
