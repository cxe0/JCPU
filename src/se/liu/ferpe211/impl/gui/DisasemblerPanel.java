package se.liu.ferpe211.impl.gui;

import se.liu.ferpe211.api.disassembler.Disassembler;
import se.liu.ferpe211.api.disassembler.Entry;
import se.liu.ferpe211.api.event.CPUListener;
import se.liu.ferpe211.api.event.EventType;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import static se.liu.ferpe211.impl.gui.util.TextSizes.MEDIUM_TEXT_SIZE;
import static se.liu.ferpe211.impl.gui.util.TextSizes.SMALL_TEXT_SIZE;

/**
 * A GUI component for rendering disassembled instructions.
 *
 * <p>
 *     This component is responsible for rendering the disassembled instructions of the current memory. It is a JPanel that is a CPUListener.
 *     See the disassembler for how the instructions are disassembled {@link Disassembler}
 * </p>
 *
 * @see CPUListener
 */
public class DisasemblerPanel extends JPanel implements CPUListener
{
    private Disassembler disassembler;
    private static final int X_VAL = 10;
    private static final int MARGIN = 2;
    private static final int DOUBLE_MARGIN = MARGIN*2;

    private static final int SPACING = 20;
    private static final int HEIGHT = 16;

    public DisasemblerPanel(Disassembler disassembler){
        this.disassembler = disassembler;
        setPreferredSize(new Dimension(200, Integer.MAX_VALUE));
        Border border = BorderFactory.createLineBorder(Color.BLACK);
        setBorder(border);
    }


    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        int y = 40;
        g2d.setFont(new Font("Arial", Font.BOLD, MEDIUM_TEXT_SIZE));
        g2d.drawString("Disassembled instructions:", X_VAL, 20);

        for (Entry entry : disassembler.getEntries()) {
            g2d.setFont(new Font("Arial", Font.PLAIN, SMALL_TEXT_SIZE));
            String text = entry.toString();
            int stringWidth = g2d.getFontMetrics().stringWidth(text);
            g2d.drawRect(X_VAL, y, stringWidth+DOUBLE_MARGIN, HEIGHT);
            g2d.drawString(text, X_VAL+ MARGIN, y+HEIGHT-DOUBLE_MARGIN);
            y+=SPACING;

        }
    }

    @Override public void cpuChanged(final EventType eventType) {
        this.repaint();
    }
}
