package com.strafe;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ButtonGrid extends JPanel implements ActionListener {

    private static final int SIZE = 100, BORDER = 1;
    public static BoulderButtons[][] grid;
    public static int[][] gridToSolve = new int [8][7];
    private JButton solveButton = new JButton("Solve");
    private JButton hintsButton = new JButton("Hints");
    private JButton historyButton = new JButton("History");
    private int ROWS;
    private int COL;

    public ButtonGrid(int r,int c) {
        setLayout(new GridLayout(r+1,c, BORDER, BORDER));
        setBackground(Color.WHITE);

        ROWS = r;
        COL = c;

        grid = new BoulderButtons[ROWS][COL]; //allocate grid

        for (int i=0; i<COL; i++) {
            JButton finishLine = new JButton();
            finishLine.setBackground(Color.GREEN);
            add(finishLine);
        }

        for(int cc=0; cc<COL; cc++){
            for(int rr=0; rr<ROWS; rr++){
                grid[rr][cc] = new BoulderButtons(rr,cc);
                add(grid[rr][cc]); //add button to panel
            }
        }
        this.initBoard();
    }

    public void initBoard() {
        JFrame f = new JFrame("Boulder Solver");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setLayout(new GridBagLayout());
        f.add(this);

        solveButton.setBounds(150,10,200,40);
        hintsButton.setBounds(150,10,200,40);
        historyButton.setBounds(150,10,200,40);

        f.add(solveButton);
        f.add(hintsButton);
        f.add(historyButton);

        solveButton.addActionListener(this);
        hintsButton.addActionListener(this);
        historyButton.addActionListener(this);

        f.pack();
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solveButton) {
            System.out.println("================");
            for (int y = 0; y < COL; y++) {
                for (int x = 0; x < ROWS; x++) {
                    if (grid[x][y].isPressed()) {
                        System.out.print("0 ");
                        gridToSolve[y+1][x] = 1;
                    } else {
                        System.out.print("x ");
                        gridToSolve[y+1][x] = 0;
                    }
                }
                System.out.println();
            }
            System.out.println("================");
            Solver solve = new Solver(gridToSolve);
            if (solve.solve()) {
                solve.showSolution();
                JOptionPane.showMessageDialog(this,"Solvable");
            } else {
                JOptionPane.showMessageDialog(this,"No Solution");
            }
        } else if (e.getSource() == hintsButton) {
//            Solver solve = new Solver();
//            if (solve.solve()) {
//                for (int y = 0; y<COL; y++) {
//                    for (int x = 0; x < ROWS; x++) {
//                        System.out.println(solve.getStep());
//                        if (solve.getStep().getX() == y && solve.getStep().getY() == x) {
//                            grid[x][y].setColor(Color.BLUE);
//                        }
//                    }
//                }
//            }
//            System.out.println("hints");
        } else if(e.getSource() == historyButton) {
            System.out.println("history");
        }
    }
}