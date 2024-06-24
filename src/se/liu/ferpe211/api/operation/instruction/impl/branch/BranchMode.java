package se.liu.ferpe211.api.operation.instruction.impl.branch;

/**
 * Enum representing different branching modes for conditional branching instructions.
 *
 * <p>
 * Conditional branching instructions allow altering the flow of program execution based on certain conditions.
 * This enum defines the various conditions that can be used for branching.
 * </p>
 *
 */
public enum BranchMode
{

    CARRY_CLEAR,
    CARRY_SET,
    EQUAL,
    MINUS,
    NOT_EQUAL,
    POSITIVE,
    OVERFLOW_CLEAR,
    OVERFLOW_SET,

}
