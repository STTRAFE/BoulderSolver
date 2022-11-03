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
        this.setBackground(Color.WHITE);
        this.addActionListener(this);
        this.x = x;
        this.y = y;

        //Colors
        if (this.y == 6) this.setBackground(Color.gray);
        if (this.x == 0 && this.y == 6) this.setBackground(Color.BLACK);
        if (this.y == 0) this.setBackground(Color.GREEN);
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void setColor(Color c) {
        this.setBackground(c);
    }

    @Override
    public Dimension getPreferredSize() {
        Dimension d = super.getPreferredSize();
        int s = (int)(d.getWidth()<d.getHeight() ? d.getHeight() : d.getWidth());
        return new Dimension (s,s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!pressed && this.y != 6 && this.y!= 0) {
            this.setBackground(Color.RED);
            this.setOpaque(true);
            pressed = true;
//            System.out.println("Pressed " + x + " " + y);
        } else if (this.y != 6 && this.y != 0){
            this.setBackground(Color.WHITE);
            this.setOpaque(true);
            pressed = false;
//            System.out.println("Unpressed " + x + " " + y);
        } else if (this.x == 0 && this.y == 6) {
            JOptionPane.showMessageDialog(this,"Starting Position, cannot interact");
        } else {
            JOptionPane.showMessageDialog(this,"You cannot interact with this row");
        }
    }

}
