package se.liu.ferpe211.api.disassembler;


import java.util.Comparator;

/**
 * Used to validate if two entries would be considered the same
 * This is used in testing of the {@link Disassembler}
 */
public class EntryComparator implements Comparator<Entry>
{
    /**
     * Compare method for comparing two {@link Entry}
     * @param o1 the first Entry to be compared.
     * @param o2 the second Entry to be compared.
     * @return 0 when the two entries are the same, 1 if entry o1 is of larger size (offset) than o2 and -1 if else
     */
    @Override
    public int compare(final Entry o1, final Entry o2) {
	if (o1.getOpCode().equals(o2.getOpCode()) && o1.getArgs().equals(o2.getArgs()) && o1.getOffset() == o2.getOffset() && o1.toString().equals(o2.toString())) {
	    return 0;
	}

	if(o1.getOffset() > o2.getOffset())
	    return 1;

	return -1;
    }
}
