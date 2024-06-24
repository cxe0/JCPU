package se.liu.ferpe211.api.register;

/**
 * Enum representation of the different processor flags for the Processor Status Register
 * Please see the {@link ProcessorStatus} class to see their implementation.
 */
public enum ProcessorFlag {
    CARRY(0), // 0 = False, 1 = True
    ZERO(1), // 0 = Result Not Zero, 1 = Result Zero
    INTERRUPT_DISABLE(2), // 0 = Enable, 1 = Disable
    DECIMAL_MODE(3), // 0 = False, 1 = True
    BREAK_COMMAND(4), // 0 = No Break, 1 = Break
    UNUSED(5), // 0 = No Break, 1 = Break
    OVERFLOW(6), // 0 = False, 1 = True
    NEGATIVE(7); // 0 = Positive, 1 = Negative

    private final int bitPosition;

    ProcessorFlag(int bitPosition) {
	this.bitPosition = bitPosition;
    }

    public int getBitPosition() {
	return bitPosition;
    }
}
