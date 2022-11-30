package com.strafe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

public class ButtonGrid extends JPanel implements ActionListener {

    private static final int BORDER = 1;
    public static BoulderButtons[][] grid;
    public static int[][] gridToSolve = new int [8][7];
    private final JButton solveButton = new JButton("Solve");
    private final JButton hintsButton = new JButton("Hints");
    private final JButton historyButton = new JButton("History");
    private final JButton resetButton = new JButton("Reset");
    private final JButton trackerButton = new JButton("I Solved It!");
    private static int ROWS;
    private static int COL;
    private Queue<?> steps;
    private int hintsUsed;
    private static int stepsNum = 0;
    private static boolean allHintsDisplayed = false;

    public ButtonGrid(int r,int c) {
        setLayout(new GridLayout(r,c, BORDER, BORDER));
        setBackground(Color.WHITE);

        ROWS = r;
        COL = c;

        grid = new BoulderButtons[ROWS][COL]; //allocate grid

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
        trackerButton.setBounds(150,30,400,40);

        f.add(solveButton);
        f.add(hintsButton);
        f.add(historyButton);
        f.add(resetButton);
        f.add(trackerButton);


        solveButton.addActionListener(this);
        hintsButton.addActionListener(this);
        historyButton.addActionListener(this);
        resetButton.addActionListener(this);
        trackerButton.addActionListener(this);

        f.pack();
        f.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solveButton) {
            hintsUsed = -1;
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
            if (!boardUpdated(gridToSolve) && allHintsDisplayed) return;
            inputGrid();
            allHintsDisplayed = false;
            Solver solve = new Solver(gridToSolve);
            if (solve.solve()) {
                if (steps == null) steps = solve.getSteps();
                if (!steps.isEmpty()) {
                    Solver.Move m = (Solver.Move) steps.poll();
                    stepsNum++;
                    if (hintsUsed != -1) hintsUsed = stepsNum;
                    ButtonGrid.grid[m.p.y][m.p.x-1].setTextOnButton(String.valueOf(stepsNum));
                    switch (m.offsetType) {
                        case 1:
                            System.out.println("up" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/up.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                            break;
                        case 2:
                            System.out.println("down" + " " + m.p.x + " " + (m.p.y + 1));

                            try {
                                ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/down.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                            break;
                        case 3:
                            System.out.println("left" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/left.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                            break;
                        case 4:
                            System.out.println("right" + " " + m.p.x + " " + (m.p.y + 1));

                            try {
                                ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/right.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                            break;
                        default:
                            break;
                    }
                } else {
                    JOptionPane.showMessageDialog(this,"All steps displayed");
                    steps = null;
                    stepsNum = 0;
                    allHintsDisplayed = true;
                    removeButtonSettings();
                }
            } else {
                JOptionPane.showMessageDialog(this,"No Solution");
            }

        } else if(e.getSource() == historyButton) {
            System.out.println("history");
            SaveLoad.load();
        } else if (e.getSource() == resetButton) {
            resetGrid();

        } else if (e.getSource() == trackerButton) {
            inputGrid();
            System.out.println(hintsUsed);
            SaveLoad.saveLoad(gridToSolve,hintsUsed,ROWS);
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
        stepsNum = 0;
        hintsUsed = 0;
        for (int y = 0; y < COL; y++) {
            for (int x = 0; x < ROWS; x++) {
                grid[x][y].setColor(Color.WHITE);
                grid[x][y].setIcon((Icon) null);
                grid[x][y].setPressed(false);
                grid[x][y].setTextOnButton(null);
            }
        }
    }

    public void removeButtonSettings() {
        for (int y = 0; y < COL; y++) {
            for (int x = 0; x < ROWS; x++) {
                grid[x][y].setIcon((Icon) null);
                grid[x][y].setTextOnButton(null);
                if (grid[x][y].isPressed()) grid[x][y].setColor(Color.RED);
            }
        }
    }

    private boolean boardUpdated(int [][] g) {
        for (int y = 0; y < COL; y++) {
            for (int x = 0; x < ROWS; x++) {
                if (grid[x][y].isPressed()) {
                    if (g[y+1][x] != 1) return true;
                } else {
                    if (g[y+1][x] != 0) return true;
                }
            }
        }
        return false;
    }
}