package com.strafe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class BoulderButtons extends JButton implements ActionListener {

    private int x;
    private int y;
    private boolean pressed = false;
    private boolean canInteract = true;

    public BoulderButtons(int x, int y) {
        this.setPreferredSize(new Dimension(60,60));
        this.setBackground(Color.WHITE);
        this.addActionListener(this);
        this.x = x;
        this.y = y;

        //Colors
        if (this.y == 6) this.setBackground(Color.gray);
        if (this.x == 0 && this.y == 6) this.setBackground(Color.black);
        if (this.y == 0) this.setBackground(Color.green);
//        this.setTextOnButton((y+1) + "," + x);
    }

    public boolean isPressed() {
        return this.pressed;
    }

    public void setPressed(boolean b) {
        this.pressed = b;
    }

    public void historyBoardConfig(int size, boolean b) {
        this.setPreferredSize(new Dimension(size,size));
        canInteract = b;
    }

    public void setColor(Color c) {
        this.setBackground(c);
    }

    public void setIcon(Image img) {
        this.setIcon(img);
    }

    public void setTextOnButton(String s) {
        this.setText(s);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!canInteract) return;
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
