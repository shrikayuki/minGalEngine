package main.java.Running.panel;

import javax.swing.*;
import java.awt.*;

public class StartBackgroundPanel extends JPanel {
    public StartBackgroundPanel() {
        setOpaque(true);
        setBackground(new Color(24, 26, 32));
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g.create();
        try {
            GradientPaint gradient = new GradientPaint(
                    0, 0, new Color(25, 29, 40),
                    getWidth(), getHeight(), new Color(48, 38, 58)
            );
            g2.setPaint(gradient);
            g2.fillRect(0, 0, getWidth(), getHeight());
        } finally {
            g2.dispose();
        }
    }
}
