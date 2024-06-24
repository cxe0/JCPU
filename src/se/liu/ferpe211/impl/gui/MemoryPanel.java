package se.liu.ferpe211.impl.gui;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.event.CPUListener;
import se.liu.ferpe211.api.event.EventType;
import se.liu.ferpe211.impl.Emulator;
import se.liu.jonkv82.annotations.BorrowedCode;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumnModel;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

/**
 * This is a GUI component for rendering the contents of the memory
 * If patching is enabled in config, the user is also able to modify the contents of the memory
 */
public class MemoryPanel extends JPanel implements CPUListener
{

    private CPU cpu;
    private JTable memoryTable;
    private DefaultTableModel tableModel;
    private Map<Integer, String> memoryValues;
    private int highlightedAddress = -1;
    private boolean patching;
    private static final int WIDTH = 200;
    private static final int INSTRUCTION_HEIGHT = 20;
    private static final int RADIX = 16;

    public MemoryPanel(CPU cpu) {
	this.cpu = cpu;
	initializeMemoryValues();
	setUpUI();
	patching = (boolean) cpu.getSettingsManager().getSetting("patching").getValue();
    }

    private void initializeMemoryValues() {
	memoryValues = new HashMap<>();
	for (int i = 0; i < cpu.getMemory().size(); i++) {
	    memoryValues.put(i, String.format("%02X", cpu.getMemory().readByte(i)));
	}
    }

    /**
     * Method for setting up the memory panel
     * The memory panel consists of a JTable with two columns (Address and value)
     * These columns are not editable unless you enable patching in config
     * The program counter is also rendered as a red box around the address which it points to
     */
    private void setUpUI() {
	setLayout(new BorderLayout());

	/**
	 * Custom Table model for making "values" cell editable when patching is true
	 * Also for parsing new values that the user can set into memory
	 */
	tableModel = new DefaultTableModel(0, 2){

	    @Override
	    public boolean isCellEditable(int row, int column) {
		return patching && column == 1;
	    }

	    @Override
	    public void setValueAt(Object newValue, int row, int column)
	    {
		Object oldValue = getValueAt(row, column);
		try {
		    int value = Integer.parseInt((String) newValue, RADIX);
		    super.setValueAt(String.format("%02X", value), row, column);
		    int address = Integer.parseInt((String) getValueAt(row, 0), RADIX);
		    memoryValues.put(address, String.format("%02X", value));
		    cpu.getMemory().writeByte(address, (byte) value);
		} catch (NumberFormatException e) {
		    super.setValueAt(oldValue, row, column);
		    Emulator.LOGGER.log(Level.SEVERE, "Failed to parse new value for memory: " + e);
		}
	    }
	};


	tableModel.setColumnIdentifiers(new String[]{"Address", "Value"});
	memoryTable = new JTable(tableModel){
	    @Override
	    public TableCellRenderer getCellRenderer(int row, int column) {
		if (row == highlightedAddress) {
		    return new HighlightRenderer();
		}
		return super.getCellRenderer(row, column);
	    }
	};
	memoryTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
	memoryTable.getTableHeader().setReorderingAllowed(false);
	memoryTable.setRowSelectionAllowed(false);
	memoryTable.setColumnSelectionAllowed(false);
	memoryTable.setCellSelectionEnabled(false);


	TableColumnModel columnModel = memoryTable.getColumnModel();
	Dimension panelSize = getPreferredSize();
	int columnWidth = (int) (panelSize.getWidth() / 2);
	columnModel.getColumn(0).setPreferredWidth(columnWidth);
	columnModel.getColumn(1).setPreferredWidth(columnWidth);

	DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
	centerRenderer.setHorizontalAlignment(JLabel.CENTER);
	memoryTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
	memoryTable.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

	JScrollPane scrollPane = new JScrollPane(memoryTable);
	add(scrollPane, BorderLayout.CENTER);

	updateMemoryTable();
    }

    private void updateMemoryTable() {
	tableModel.setRowCount(0);
	for (int i = 0; i < cpu.getMemory().size(); i++) {
	    String address = String.format("%04X", i);
	    String value = memoryValues.get(i);
	    tableModel.addRow(new String[]{address, value});
	}
    }

    /**
     * Method for scrolling to target address
     * @param address
     */
    public void gotoAddress(int address) {
	highlightedAddress = address;
	memoryTable.setRowSelectionInterval(address, address);
	memoryTable.scrollRectToVisible(memoryTable.getCellRect(address, 0, true));
    }

    @Override
    public Dimension getPreferredSize() {
	return new Dimension(WIDTH, cpu.getMemory().size() * INSTRUCTION_HEIGHT);
    }

    /**
     * Method for handling cpu change events
     * When memory is changed, we update the memory values and memory table
     * When settings are changed, we update the patching boolean in order to make the user able to modify the memory contents
     * @param eventType
     */
    @Override public void cpuChanged(final EventType eventType) {
	this.highlightedAddress = cpu.getProgramCounter().getValue();
	if(eventType == EventType.MEMORY_CHANGED || eventType == EventType.CPU_RESET){
	    initializeMemoryValues();
	    updateMemoryTable();
	    tableModel.fireTableDataChanged();
	}

	if(eventType == EventType.SETTINGS_CHANGED){
	    patching = (boolean) cpu.getSettingsManager().getSetting("patching").getValue();
	}
	this.repaint();
    }

    /**
     * Inner class for having a custom cell renderer
     * This is used to draw a highlight box over the current address in the program counter
     */
    @BorrowedCode(source = "ChatGPT")
    private static class HighlightRenderer extends DefaultTableCellRenderer {
	private HighlightRenderer() {
	    setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
	    super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
	    setBorder(BorderFactory.createLineBorder(Color.RED));
	    return this;
	}
    }

}
