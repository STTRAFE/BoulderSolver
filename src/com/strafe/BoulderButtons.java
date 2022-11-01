package com.strafe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoulderButtons extends JButton implements ActionListener {

    private int x;
    private int y;
    private boolean pressed = false;

    public BoulderButtons(int x, int y) {
        this.addActionListener(this);
        this.x = x;
        this.y = y;
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        int s = (int)(d.getWidth()<d.getHeight() ? d.getHeight() : d.getWidth());
        return new Dimension (s,s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        System.out.println(x + " " + y);
        if (!pressed) {
            this.setBackground(Color.RED);
            this.setOpaque(true);
            pressed = true;
        } else {
            this.setBackground(Color.ORANGE);
            this.setOpaque(true);
            pressed = false;
        }
    }
}
