package se.liu.ferpe211.impl.gui;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.impl.Emulator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.logging.Level;

/**
 * Main GUI component for visualizing the CPU and its content
 * Eg. Memory, Register
 * Additionally, provides accessible tools for interacting with the CPU, such as executing and stepping through instructions.
 */
public class CPUViewer
{
    private JFrame frame = null;
    private MemoryPanel memoryPanel = null;
    private Emulator emulator;

    /**
     * Constants for the resolutions of the window aswell as radix for parsing input as hexadecimal
     */
    private static final int WIDTH = 800;
    private static final int HEIGHT = 600;
    private static final int RADIX = 16;

    public CPUViewer(Emulator emulator) {
	this.emulator = emulator;
    }

    private void setUpMenuBar(){
	JMenuBar menuBar = new JMenuBar();
	frame.setJMenuBar(menuBar);

	CPU cpu = emulator.getCpu();

	JMenu fileMenu = new JMenu("File");
	menuBar.add(fileMenu);

	JMenuItem openMenuItem = new JMenuItem("Open");
	openMenuItem.addActionListener((ActionEvent e) -> {
	    JFileChooser fileChooser = new JFileChooser();
	    int result = fileChooser.showOpenDialog(frame);
	    if (result == JFileChooser.APPROVE_OPTION) {
		File selectedFile = fileChooser.getSelectedFile();
		cpu.loadFile(selectedFile);
	    }
	});
	fileMenu.add(openMenuItem);

	JMenuItem exportMenuItem = new JMenuItem("Export");
	exportMenuItem.addActionListener((ActionEvent e) -> {
	    JFileChooser fileChooser = new JFileChooser();
	    int result = fileChooser.showOpenDialog(frame);
	    if (result == JFileChooser.APPROVE_OPTION) {
		File selectedFile = fileChooser.getSelectedFile();
		cpu.dump(selectedFile);
	    }
	});
	fileMenu.add(exportMenuItem);

	JMenuItem exitMenuItem = new JMenuItem("Exit");
	exitMenuItem.addActionListener((ActionEvent e) -> System.exit(0));
	fileMenu.add(exitMenuItem);

	JMenu editMenu = new JMenu("Edit");
	menuBar.add(editMenu);

	JMenuItem resetMenuItem = new JMenuItem("Reset");
	resetMenuItem.addActionListener((ActionEvent e) -> {
	    cpu.reset();
	});
	editMenu.add(resetMenuItem);


	JMenuItem stepMenuItem = new JMenuItem("Step");
	stepMenuItem.addActionListener((ActionEvent e) -> {
	    cpu.executeNextInstruction();
	});
	editMenu.add(stepMenuItem);

	JMenuItem executeMenuItem = new JMenuItem("Execute");
	executeMenuItem.addActionListener((ActionEvent e) -> cpu.executeThread());
	editMenu.add(executeMenuItem);

	JMenuItem disassembleMenuItem = new JMenuItem("Disassemble");
	disassembleMenuItem.addActionListener((ActionEvent e) -> {
	    this.emulator.getDisassembler().disasemble();
	    frame.repaint();
	});
	editMenu.add(disassembleMenuItem);


	JMenuItem gotoAddress = new JMenuItem("Goto Address");
	gotoAddress.addActionListener(e -> handleGotoAddress());

	editMenu.add(gotoAddress);

    }

    /**
     * Method for handling when user wants to scroll to a target address
     * if invalid address is set, will prompt to input a new one or cancel the operation
     */
    private void handleGotoAddress(){
	{
	    boolean validInput = false;
	    while (!validInput) {
		String input = JOptionPane.showInputDialog(this, "Enter an address (e.g., 0x1000 or 4096):");
		if (input != null && !input.isEmpty()) {
		    try {
			int address;
			if (input.startsWith("0x")) {
			    address = Integer.parseInt(input.substring(2), RADIX);
			} else {
			    address = Integer.parseInt(input);
			}
			memoryPanel.gotoAddress(address);
			validInput = true;
		    } catch (NumberFormatException ex) {
			String errorMessage = "Encountered exception when parsing input address: " + ex;
			Emulator.LOGGER.log(Level.SEVERE, errorMessage);
			int response = JOptionPane.showConfirmDialog(frame, errorMessage + "\nWould you like to try again?", "Error", JOptionPane.YES_NO_OPTION, JOptionPane.ERROR_MESSAGE);
			if (response == JOptionPane.NO_OPTION) {
			    validInput = true;
			}
		    }
		} else {
		    validInput = true;
		}
	    }
	}
    }

    public void show(){
	frame = new JFrame();

	frame.setTitle("CPU Emulator");

	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setPreferredSize(new Dimension(WIDTH, HEIGHT));

	this.setUpMenuBar();
	this.setUpMainPanel();


	frame.pack();
	frame.setVisible(true);
    }


    private void setUpMainPanel(){
	JPanel mainPanel = new JPanel(new GridBagLayout());
	GridBagConstraints gbc = new GridBagConstraints();
	gbc.fill = GridBagConstraints.BOTH;
	gbc.weightx = 1.0;
	gbc.weighty = 1.0;

	memoryPanel = new MemoryPanel(emulator.getCpu());
	mainPanel.add(memoryPanel, createGridBagConst(gbc, 0, 0, 1, 1));
	emulator.getCpu().addListener(memoryPanel);

	DisasemblerPanel disasemblerPanel = new DisasemblerPanel(emulator.getDisassembler());
	mainPanel.add(disasemblerPanel, createGridBagConst(gbc, 1, 0, 1, 1));
	emulator.getCpu().addListener(disasemblerPanel);


	JPanel rightPanel = new JPanel();
	rightPanel.setBorder(BorderFactory.createLineBorder(Color.BLACK));
	mainPanel.add(rightPanel, createGridBagConst(gbc, 2, 0, 1, 1));

	RegisterPanel registerPanel = new RegisterPanel(emulator.getCpu());
	rightPanel.add(registerPanel, createGridBagConst(new GridBagConstraints(), 0, 0, 1, 1));
	frame.getContentPane().add(mainPanel);
	emulator.getCpu().addListener(registerPanel);
    }

    private static GridBagConstraints createGridBagConst(GridBagConstraints gbc, int x, int y, int width, int height) {
	GridBagConstraints gbcCopy = copyGridBagConstraints(gbc);
	gbcCopy.gridx = x;
	gbcCopy.gridy = y;
	gbcCopy.gridwidth = width;
	gbcCopy.gridheight = height;
	return gbcCopy;
    }

    private static GridBagConstraints copyGridBagConstraints(GridBagConstraints gbc) {
	GridBagConstraints copy = new GridBagConstraints();
	copy.gridx = gbc.gridx;
	copy.gridy = gbc.gridy;
	copy.gridwidth = gbc.gridwidth;
	copy.gridheight = gbc.gridheight;
	copy.weightx = gbc.weightx;
	copy.weighty = gbc.weighty;
	copy.anchor = gbc.anchor;
	copy.fill = gbc.fill;
	copy.insets = gbc.insets;
	copy.ipadx = gbc.ipadx;
	copy.ipady = gbc.ipady;
	return copy;
    }
}


