package se.liu.ferpe211.impl.gui;

import se.liu.ferpe211.api.CPU;
import se.liu.ferpe211.api.event.CPUListener;
import se.liu.ferpe211.api.event.EventType;
import se.liu.ferpe211.api.register.Register;

import javax.swing.*;
import java.awt.*;

import static se.liu.ferpe211.impl.gui.util.TextSizes.MEDIUM_TEXT_SIZE;
import static se.liu.ferpe211.impl.gui.util.TextSizes.SMALL_TEXT_SIZE;

/**
 * This is a GUI component for rendering the different registrers and their respective values
 */
public class RegisterPanel extends JPanel
    implements CPUListener
{
    private CPU cpu;
    private final static int HEIGHT = 20;
    private final static int X_PADDING = 10;

    public RegisterPanel(CPU cpu) {
	this.cpu = cpu;
	setPreferredSize(new Dimension(200, 300));
    }



    @Override
    protected void paintComponent(Graphics g) {
	super.paintComponent(g);
	Graphics2D g2d = (Graphics2D) g;
	int y = HEIGHT;

	g2d.setFont(new Font("Arial", Font.BOLD,MEDIUM_TEXT_SIZE ));
	g2d.drawString("Registers", X_PADDING, HEIGHT);

	y += HEIGHT;
	g2d.setFont(new Font("Arial", Font.BOLD, MEDIUM_TEXT_SIZE));
	g2d.drawString("Registers", X_PADDING, HEIGHT);

	for (Register register : cpu.getRegisters()) {
	    g2d.setFont(new Font("Arial", Font.PLAIN, SMALL_TEXT_SIZE));
	    String[] lines = register.toString().split("\n");
	    for (String line : lines) {
		g2d.drawString(line, X_PADDING, y);
		y += HEIGHT;
	    }
	}
    }

    @Override
    public void cpuChanged(final EventType eventType) {
	this.repaint();
    }
}
