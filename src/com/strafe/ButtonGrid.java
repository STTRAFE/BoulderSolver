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
    private final JButton backButton = new JButton("Back");
    private JFrame main = new JFrame("Boulder Solver");
    private JFrame history = new JFrame("History");
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

        // Makes look and feel the same for all platforms
        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
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
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setLayout(new GridBagLayout());
        main.add(this);
        main.setSize(730,480);
        main.setResizable(false);

        solveButton.setBounds(150,10,200,40);
        hintsButton.setBounds(150,10,200,40);
        historyButton.setBounds(150,10,200,40);
        resetButton.setBounds(150,10,200,40);
        trackerButton.setBounds(720,250,500,40);

        JPanel buttons = new JPanel();
        buttons.add(solveButton);
        buttons.add(hintsButton);
        buttons.add(historyButton);
        buttons.add(resetButton);
        buttons.add(trackerButton);
        main.add(buttons);


        solveButton.addActionListener(this);
        hintsButton.addActionListener(this);
        historyButton.addActionListener(this);
        resetButton.addActionListener(this);
        trackerButton.addActionListener(this);

        main.setVisible(true);
        history.setVisible(false);
        historyFrame();
    }

    public void historyFrame() {
        history.setLayout(new GridLayout());
        history.setResizable(false);
        history.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        history.setSize(730,480);

        backButton.setBounds(620,400,100,40);
        backButton.addActionListener(this);

        HistoricGrid[] g = SaveLoad.load();
        JLabel l1 = new JLabel("Steps: ");
        JLabel l2 = new JLabel("Steps: ");
        JLabel l3 = new JLabel("Steps: ");
        JLabel l4 = new JLabel("Steps: ");
        try {
            l1.setText("Steps: " + g[0].getSteps());
            l2.setText("Steps: " + g[1].getSteps());
            l3.setText("Steps: " + g[2].getSteps());
            l4.setText("Steps: " + g[3].getSteps());
        } catch (Exception e) {}

        l1.setBounds(300,10,400,40);
        l2.setBounds(300,250,400,40);
        l3.setBounds(650,10,400,40);
        l4.setBounds(650,250,400,40);

        JPanel hp1 = new JPanel();
        JPanel hp2 = new JPanel();
        JPanel hp3 = new JPanel();
        JPanel hp4 = new JPanel();
        hp1.setLayout(new GridLayout(7,7, BORDER, BORDER));
        hp2.setLayout(new GridLayout(7,7, BORDER, BORDER));
        hp3.setLayout(new GridLayout(7,7, BORDER, BORDER));
        hp4.setLayout(new GridLayout(7,7, BORDER, BORDER));

        BoulderButtons[][] grid1 = new BoulderButtons[7][7];
        BoulderButtons[][] grid2 = new BoulderButtons[7][7];
        BoulderButtons[][] grid3 = new BoulderButtons[7][7];
        BoulderButtons[][] grid4 = new BoulderButtons[7][7]; //allocate grid

        for(int cc=0; cc<7; cc++){
            for(int rr=0; rr<7; rr++){
                grid1[rr][cc] = new BoulderButtons(rr,cc);
                grid2[rr][cc] = new BoulderButtons(rr,cc);
                grid3[rr][cc] = new BoulderButtons(rr,cc);
                grid4[rr][cc] = new BoulderButtons(rr,cc);

                grid1[rr][cc].historyBoardConfig(10,false);
                grid2[rr][cc].historyBoardConfig(10,false);
                grid3[rr][cc].historyBoardConfig(10,false);
                grid4[rr][cc].historyBoardConfig(10,false);

                try {
                    int g1[][] = g[0].getGrid();
                    int g2[][] = g[1].getGrid();
                    int g3[][] = g[2].getGrid();
                    int g4[][] = g[3].getGrid();
                    if (g1[rr][cc] == 1) {
                        grid1[rr][cc].setColor(Color.RED);
                    }
                    if (g2[rr][cc] == 1) {
                        grid2[rr][cc].setColor(Color.RED);
                    }
                    if (g3[rr][cc] == 1) {
                        grid3[rr][cc].setColor(Color.RED);
                    }
                    if (g4[rr][cc] == 1) {
                        grid4[rr][cc].setColor(Color.RED);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hp1.add(grid1[rr][cc]);
                hp2.add(grid2[rr][cc]);
                hp3.add(grid3[rr][cc]);
                hp4.add(grid4[rr][cc]);//add button to panel
            }
        }

        history.add(hp1);
        history.add(l1);
        history.add(hp2);
        history.add(l2);
        history.add(hp3);
        history.add(l3);
        history.add(hp4);
        history.add(l4);
        history.add(backButton);
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
                        case 1 -> {
                            System.out.println("up" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/up.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                        }
                        case 2 -> {
                            System.out.println("down" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/down.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                        }
                        case 3 -> {
                            System.out.println("left" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/left.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                        }
                        case 4 -> {
                            System.out.println("right" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                ButtonGrid.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/right.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                        }
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
            main.setVisible(false);
            history.setVisible(true);
        } else if (e.getSource() == resetButton) {
            resetGrid();
        } else if (e.getSource() == trackerButton) {
            inputGrid();
            System.out.println(hintsUsed);
            SaveLoad.saveLoad(gridToSolve,hintsUsed,ROWS);
        } else if (e.getSource() == backButton) {
            main.setVisible(true);
            history.setVisible(false);
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
        for (int y = 0; y < COL-1; y++) {
            for (int x = 0; x < ROWS; x++) {
                grid[x][y].setColor(Color.WHITE);
                grid[x][y].setIcon((Icon) null);
                grid[x][y].setPressed(false);
                grid[x][y].setTextOnButton(null);
                if (y == 0) grid[x][y].setColor(Color.green);
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