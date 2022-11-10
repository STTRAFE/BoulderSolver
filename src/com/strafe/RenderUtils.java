package com.strafe;

import javax.swing.*;
import java.awt.*;

public class RenderUtils extends JPanel {
    public RenderUtils() {
        this.setBackground(Color.GREEN);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.RED);

        g.drawLine(0,0,3,3);
        g.drawLine(0,0,3,30);
        g.drawLine(0,0,3,40);
    }
}
