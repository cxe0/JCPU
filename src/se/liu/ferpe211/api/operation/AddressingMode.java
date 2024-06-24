package se.liu.ferpe211.api.operation;

/**
 * Represents the addressing modes available in the 6502 CPU unit.
 * These addressing modes determine how operands are fetched for instructions.
 * Each mode specifies a different way of interpreting operand addresses.
 *
 * <p>
 * The addressing modes are as follows:
 * <ul>
 * <li>{@code IMPLICIT}: Operand is implied by the instruction.
 * <li>{@code ACCUMULATOR}: Operand is in the accumulator register.
 * <li>{@code IMMEDIATE}: Operand is given explicitly in the instruction.
 * <li>{@code ZERO_PAGE}: Operand is an address in the zero page.
 * <li>{@code ABSOLUTE}: Operand is a full 16-bit address.
 * <li>{@code ZERO_PAGE_X}: Operand is an address in the zero page indexed by X register.
 * <li>{@code ZERO_PAGE_Y}: Operand is an address in the zero page indexed by Y register.
 * <li>{@code ABSOLUTE_X}: Operand is a full 16-bit address indexed by X register.
 * <li>{@code ABSOLUTE_Y}: Operand is a full 16-bit address indexed by Y register.
 * <li>{@code INDIRECT}: Operand is a full 16-bit address pointing to the location of another address.
 * <li>{@code INDIRECT_X}: Operand is an address computed by adding the value in the X register to a zero-page address.
 * <li>{@code INDIRECT_Y}: Operand is an address computed by adding the value in the Y register to a zero-page address.
 * <li>{@code IMPLIED}: Operand is implied by the instruction.
 * </ul>
 */
public enum AddressingMode {
    IMMEDIATE,
    ZERO_PAGE,
    ABSOLUTE,
    ZERO_PAGE_X,
    ZERO_PAGE_Y,
    ABSOLUTE_X,
    ABSOLUTE_Y,
    INDIRECT,
    INDIRECT_X,
    INDIRECT_Y,
    IMPLIED,
    RELATIVE
}