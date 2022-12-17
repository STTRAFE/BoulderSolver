package com.strafe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Queue;

public class GUI implements ActionListener {

    private static final int BORDER = 1;
    public static BoulderButtons[][] grid;
    public static int[][] gridToSolve = new int [8][7];
    private final JButton solveButton = new JButton("Solve");
    private final JButton hintsButton = new JButton("Hints");
    private final JButton historyButton = new JButton("History");
    private final JButton resetButton = new JButton("Reset");
    private final JButton trackerButton = new JButton("I Solved It!");
    private final JButton backButton = new JButton("Back");
    private final JLabel l1 = new JLabel("Steps: ");
    private final JLabel l2 = new JLabel("Steps: ");
    private final JLabel l3 = new JLabel("Steps: ");
    private final JLabel l4 = new JLabel("Steps: ");
    private final JPanel hp1 = new JPanel();
    private final JPanel hp2 = new JPanel();
    private final JPanel hp3 = new JPanel();
    private final JPanel hp4 = new JPanel();
    private final JPanel buttonGrid = new JPanel();
    private final JFrame main = new JFrame("Boulder Solver");
    private final JFrame history = new JFrame("History");
    private final static BoulderButtons[][] grid1 = new BoulderButtons[7][7];
    private final static BoulderButtons[][] grid2 = new BoulderButtons[7][7];
    private final static BoulderButtons[][] grid3 = new BoulderButtons[7][7];
    private final static BoulderButtons[][] grid4 = new BoulderButtons[7][7];
    private static int ROWS;
    private static int COL;
    private Queue<?> steps;
    private int hintsUsed;
    private static int stepsNum = 0;
    private static boolean allHintsDisplayed = false;

    public GUI(int r, int c) {

        buttonGrid.setLayout(new GridLayout(r,c, BORDER, BORDER));
        buttonGrid.setBackground(Color.WHITE);
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
                buttonGrid.add(grid[rr][cc]); //add button to panel
            }
        }
        this.initBoard();
    }

    public void initBoard() {
        main.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main.setLayout(new GridBagLayout());
        main.add(buttonGrid);
//        main.setSize(730,480);
        main.setSize(900,600);
        main.setResizable(false);

        solveButton.setBounds(150,10,400,80);
        hintsButton.setBounds(150,10,400,80);
        historyButton.setBounds(150,10,400,80);
        resetButton.setBounds(150,10,400,80);
        trackerButton.setBounds(720,250,500,80);

        JPanel buttons = new JPanel();
        buttons.setLayout(new BorderLayout());

        buttons.add(solveButton,BorderLayout.PAGE_START);
        buttons.add(hintsButton,BorderLayout.CENTER);
        buttons.add(historyButton,BorderLayout.LINE_END);
        buttons.add(resetButton,BorderLayout.LINE_START);
        buttons.add(trackerButton,BorderLayout.PAGE_END);
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
        history.setLayout(new GridBagLayout());
        history.setResizable(false);
        history.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        history.setSize(730,480);
        history.setSize(900,600);

        backButton.setBounds(620,400,100,40);
        backButton.addActionListener(this);

        l1.setBounds(300,10,400,40);
        l2.setBounds(300,250,400,40);
        l3.setBounds(650,10,400,40);
        l4.setBounds(650,250,400,40);

        hp1.setLayout(new GridLayout(7,7, BORDER, BORDER));
        hp2.setLayout(new GridLayout(7,7, BORDER, BORDER));
        hp3.setLayout(new GridLayout(7,7, BORDER, BORDER));
        hp4.setLayout(new GridLayout(7,7, BORDER, BORDER));


        HistoricGrid[] g = SaveLoad.load();
        try {
            if (g[0].getSteps() == -1) {
                l1.setText("Used Solver");
            } else l1.setText("Steps: " + g[0].getSteps());
            if (g[1].getSteps() == -1) {
                l2.setText("Used Solver");
            } else l2.setText("Steps: " + g[1].getSteps());
            if (g[2].getSteps() == -1) {
                l3.setText("Used Solver");
            } else l3.setText("Steps: " + g[2].getSteps());
            if (g[3].getSteps() == -1) {
                l4.setText("Used Solver");
            } else l4.setText("Steps: " + g[3].getSteps());

        } catch (Exception ignored) {}

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
                    int[][] g1 = g[0].getGrid();
                    int[][] g2 = g[1].getGrid();
                    int[][] g3 = g[2].getGrid();
                    int[][] g4 = g[3].getGrid();
                    if (g1[cc][rr] == 1) {
                        grid1[rr][cc].setColor(Color.RED);
                    }
                    if (g2[cc][rr] == 1) {
                        grid2[rr][cc].setColor(Color.RED);
                    }
                    if (g3[cc][rr] == 1) {
                        grid3[rr][cc].setColor(Color.RED);
                    }
                    if (g4[cc][rr] == 1) {
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

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 1;
        gbc.weighty = 1;
        gbc.gridx = 0;
        gbc.gridy = 0;
        history.add(hp1,gbc);
        gbc.gridx = 1;
        gbc.gridy = 0;
        history.add(l1,gbc);
        gbc.gridx = 0;
        gbc.gridy = 1;
        history.add(hp2,gbc);
        gbc.gridx = 1;
        gbc.gridy = 1;
        history.add(l2,gbc);
        gbc.gridx = 2;
        gbc.gridy = 0;
        history.add(hp3,gbc);
        gbc.gridx = 3;
        gbc.gridy = 0;
        history.add(l3,gbc);
        gbc.gridx = 2;
        gbc.gridy = 1;
        history.add(hp4,gbc);
        gbc.gridx = 3;
        gbc.gridy = 1;
        history.add(l4,gbc);
        gbc.gridx = 3;
        gbc.gridy = 2;
        gbc.weighty = 0.5;
        gbc.gridheight = GridBagConstraints.REMAINDER;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        history.add(backButton,gbc);
    }


    public void updateHistoryFrame() {
        HistoricGrid[] g = SaveLoad.load();
        try {
            if (g[0].getSteps() == -1) {
                l1.setText("Used Solver");
            } else l1.setText("Steps: " + g[0].getSteps());
            if (g[1].getSteps() == -1) {
                l2.setText("Used Solver");
            } else l2.setText("Steps: " + g[1].getSteps());
            if (g[2].getSteps() == -1) {
                l3.setText("Used Solver");
            } else l3.setText("Steps: " + g[2].getSteps());
            if (g[3].getSteps() == -1) {
                l4.setText("Used Solver");
            } else l4.setText("Steps: " + g[3].getSteps());

            for(int cc=1; cc<6; cc++) {
                for (int rr = 0; rr < 7; rr++) {

                        int[][] g1 = g[0].getGrid();
                        int[][] g2 = g[1].getGrid();
                        int[][] g3 = g[2].getGrid();
                        int[][] g4 = g[3].getGrid();
                        if (g1[cc][rr] == 1) {
                            grid1[rr][cc].setColor(Color.RED);
                        } else {
                            grid1[rr][cc].setColor(Color.WHITE);
                        }
                        if (g2[cc][rr] == 1) {
                            grid2[rr][cc].setColor(Color.RED);
                        } else {
                            grid2[rr][cc].setColor(Color.WHITE);
                        }
                        if (g3[cc][rr] == 1) {
                            grid3[rr][cc].setColor(Color.RED);
                        } else {
                            grid3[rr][cc].setColor(Color.WHITE);
                        }
                        if (g4[cc][rr] == 1) {
                            grid4[rr][cc].setColor(Color.RED);
                        } else {
                            grid4[rr][cc].setColor(Color.WHITE);
                        }
                }
            }
        } catch (Exception ignored) {}
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == solveButton) {
            hintsUsed = -1;
            removeButtonSettings();
            inputGrid();
            Solver solve = new Solver(gridToSolve);
            if (solve.solve()) {
                solve.showSolution();
                JOptionPane.showMessageDialog(buttonGrid,"Solvable");
            } else {
                JOptionPane.showMessageDialog(buttonGrid,"No Solution");
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
                    GUI.grid[m.p.y][m.p.x-1].setTextOnButton(String.valueOf(stepsNum));
                    switch (m.offsetType) {
                        case 1 -> {
                            System.out.println("up" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/up.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                        }
                        case 2 -> {
                            System.out.println("down" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/down.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                        }
                        case 3 -> {
                            System.out.println("left" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/left.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                        }
                        case 4 -> {
                            System.out.println("right" + " " + m.p.x + " " + (m.p.y + 1));
                            try {
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/right.png"))));
                            } catch (Exception k) {
                                k.printStackTrace();
                            }
                        }
                    }
                } else {
                    JOptionPane.showMessageDialog(buttonGrid,"All steps displayed");
                    steps = null;
                    stepsNum = 0;
                    allHintsDisplayed = true;
                }
            } else {
                JOptionPane.showMessageDialog(buttonGrid,"No Solution");
            }

        } else if(e.getSource() == historyButton) {
            main.setVisible(false);
            history.setVisible(true);
            updateHistoryFrame();
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