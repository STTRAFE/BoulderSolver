package com.strafe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

public class ButtonGrid extends JPanel implements ActionListener {

    private static final int SIZE = 100, BORDER = 1;
    public static BoulderButtons[][] grid;
    public static int[][] gridToSolve = new int [8][7];
    private JButton solveButton = new JButton("Solve");
    private JButton hintsButton = new JButton("Hints");
    private JButton historyButton = new JButton("History");
    private JButton resetButton = new JButton("Reset");
    private int ROWS;
    private int COL;
    private Queue steps;

    public ButtonGrid(int r,int c) {
        setLayout(new GridLayout(r,c, BORDER, BORDER));
        setBackground(Color.WHITE);

        ROWS = r;
        COL = c;

        grid = new BoulderButtons[ROWS][COL]; //allocate grid

//        for (int i=0; i<COL; i++) {
//            JButton finishLine = new JButton();
//            finishLine.setBackground(Color.GREEN);
//            add(finishLine);
//        }

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
//        f.getContentPane().add(new RenderUtils());

        solveButton.setBounds(150,10,200,40);
        hintsButton.setBounds(150,10,200,40);
        historyButton.setBounds(150,10,200,40);
        resetButton.setBounds(150,10,200,40);

        f.add(solveButton);
        f.add(hintsButton);
        f.add(historyButton);
        f.add(resetButton);

        solveButton.addActionListener(this);
        hintsButton.addActionListener(this);
        historyButton.addActionListener(this);
        resetButton.addActionListener(this);

        f.pack();
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solveButton) {
            removeButtonSettings();
            inputGrid();
//            System.out.println("================");
//            for (int y = 0; y < COL; y++) {
//                for (int x = 0; x < ROWS; x++) {
//                    if (grid[x][y].isPressed()) {
//                        System.out.print("0 ");
//                    } else {
//                        System.out.print("x ");
//                    }
//                }
//                System.out.println();
//            }
//            System.out.println("================");
            Solver solve = new Solver(gridToSolve);
            if (solve.solve()) {
                solve.showSolution();
                JOptionPane.showMessageDialog(this,"Solvable");
            } else {
                JOptionPane.showMessageDialog(this,"No Solution");
            }
        } else if (e.getSource() == hintsButton) {
            inputGrid();
            Solver solve = new Solver(gridToSolve);
            if (solve.solve()) {
                if (steps == null) steps = solve.getSteps();
                if (!steps.isEmpty()) {
                    Solver.Move m = (Solver.Move) steps.poll();
                    switch (m.offsetType) {
                        case 1:
                            System.out.println("up" + " " + m.p.x + " " + (m.p.y + 1));
//                            ButtonGrid.grid[m.p.y][m.p.x-1].setColor(Color.pink);
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/up.png"))));
                            } catch (Exception k) {
                            }
                            break;
                        case 2:
                            System.out.println("down" + " " + m.p.x + " " + (m.p.y + 1));
//                            ButtonGrid.grid[m.p.y][m.p.x-1].setColor(Color.pink);
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/down.png"))));
                            } catch (Exception k) {
                            }
                            break;
                        case 3:
                            System.out.println("left" + " " + m.p.x + " " + (m.p.y + 1));
//                            ButtonGrid.grid[m.p.y][m.p.x-1].setColor(Color.pink);
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/left.png"))));
                            } catch (Exception k) {
                            }
                            break;
                        case 4:
                            System.out.println("right" + " " + m.p.x + " " + (m.p.y + 1));
//                            ButtonGrid.grid[m.p.y][m.p.x-1].setColor(Color.pink);
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/right.png"))));
                            } catch (Exception k) {
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(this,"All steps displayed");
                    steps = null;
                }
            }

        } else if(e.getSource() == historyButton) {
            System.out.println("history");
        } else if (e.getSource() == resetButton) {
            resetGrid();
        }
    }

    public void inputGrid() {
        for (int y = 0; y < COL; y++) {
            for (int x = 0; x < ROWS; x++) {
                if (grid[x][y].isPressed()) {
                    gridToSolve[y+1][x] = 1;
                } else {
                    gridToSolve[y+1][x] = 0;
                }
            }
        }
    }

    public void resetGrid() {
        for (int y = 0; y < COL; y++) {
            for (int x = 0; x < ROWS; x++) {
                grid[x][y].setColor(Color.WHITE);
                grid[x][y].setIcon((Icon) null);
                grid[x][y].setPressed(false);
            }
        }
    }

    public void removeButtonSettings() {
        for (int y = 0; y < COL; y++) {
            for (int x = 0; x < ROWS; x++) {
                grid[x][y].setIcon((Icon) null);
                if (grid[x][y].isPressed()) grid[x][y].setColor(Color.RED);
            }
        }
    }
}