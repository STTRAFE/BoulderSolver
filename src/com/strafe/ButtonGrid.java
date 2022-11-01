package com.strafe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class ButtonGrid extends JPanel {

    private static final int SIZE = 35, BORDER = 0;
    private JButton[][] grid;

    public ButtonGrid(int ROWS,int COL) {
        setLayout(new GridLayout(ROWS, COL, BORDER, BORDER));
        setBackground(Color.ORANGE);

        grid=new JButton[ROWS][COL]; //allocate the size of grid
        for(int y=0; y<COL; y++){
            for(int x=0; x<ROWS; x++){
                grid[x][y]=new BoulderButtons(x,y);
                add(grid[x][y]); //adds button to grid
            }
        }

        this.initBoard();
    }

    public void initBoard() {
        JFrame f = new JFrame("Boulder Solver");
        f.add(new JButton("Solve"));
        f.add(new JButton("Hints"));
        f.add(new JButton("History"));
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridBagLayout());
        f.add(this);
        f.pack();
        f.setVisible(true);
    }
}