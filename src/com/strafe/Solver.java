package com.strafe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Solver {

    private int[][] grid;
    private Route solvedRoute;
    private final HashSet<int[][]> visited;
    private long time1;
    public int solutions = 5;

    public Solver(int[][] solveGrid) {
        this.grid = solveGrid;
        this.visited = new HashSet<>();
        this.solvedRoute = new Route();
    }

    public void showSolution() {
        if (solvedRoute != null) {
            int stepsNum = 0;
            Stack<Move> moveList = solvedRoute.moves;
            while (!moveList.isEmpty()) {
                Move m = moveList.pop();
                stepsNum++;
                GUI.grid[m.p.y][m.p.x - 1].setTextOnButton(String.valueOf(stepsNum));
                try {
                    switch (m.direction) {
                        case "U" -> GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/up.png")))));
                        case "D" -> GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/down.png")))));
                        case "L" -> GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/left.png")))));
                        case "R" -> GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/right.png")))));
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }

    public Stack<Move> getSteps() {
        return solvedRoute.moves;
    }


    public boolean dfs() {
        int[][] newGrid = copyGrid(grid);
        time1 = System.currentTimeMillis();
        boolean solved = search(newGrid, 7,0,solvedRoute);
        return solved;
    }

    public boolean search(int[][] grid, int x, int y, Route r) {
        if (x < 0 || y < 0 || x >= 8 || y > 8) return false;
        if (System.currentTimeMillis() - time1 > 50) return false;
        if (visited.contains(grid)) return false;
        visited.add(grid);
        if (solutions > 5) return true;
        if (x == 1) {
            System.out.println("solved");
            solutions++;
        }
        if (grid[x][y] == 0) {
            grid[x][y] = 2;
            if ((canPush(x, y, grid, "D") || canGoThere(x, y, grid, "D")) && search(push(x, y, grid, "D"), x + 1, y, r)) {
                Move m = new Move();
                m.p.x = x;
                m.p.y = y;
                m.direction = "D";
                r.moves.add(m);
                return true;
            } else if ((canPush(x, y, grid, "U") || canGoThere(x, y, grid, "U")) && search(push(x, y, grid, "U"), x - 1, y, r)) {
                Move m = new Move();
                m.p.x = x;
                m.p.y = y;
                m.direction = "U";
                r.moves.add(m);
                return true;
            } else if ((canPush(x, y, grid, "R") || canGoThere(x, y, grid, "R")) && search(push(x, y, grid, "R"), x, y + 1, r)) {
                Move m = new Move();
                m.p.x = x;
                m.p.y = y;
                m.direction = "R";
                r.moves.add(m);
                return true;
            } else if ((canPush(x, y, grid, "L") || canGoThere(x, y, grid, "L")) && search(push(x, y, grid, "L"), x, y - 1, r)) {
                Move m = new Move();
                m.p.x = x;
                m.p.y = y;
                m.direction = "L";
                r.moves.add(m);
                return true;
            }
        }
        return false;
    }

    public boolean canPush(int x, int y, int[][] grid, String direction) {
        switch (direction) {
            case "U" -> {
                return x > 3 && grid[x - 1][y] == 1 && grid[x - 2][y] == 0;
            }
            case "D" -> {
                return x < 5 && grid[x + 1][y] == 1 && grid[x + 2][y] == 0;
            }
            case "R" -> {
                return y < 5 && grid[x][y + 1] == 1 && grid[x][y + 2] == 0;
            }
            case "L" -> {
                return y > 2 && grid[x][y - 1] == 1 && grid[x][y - 2] == 0;
            }

        }
        return false;
    }

    public boolean canGoThere(int x, int y, int[][] grid, String direction) {
        switch (direction) {
            case "U" -> {
                if (x > 0 && (grid[x - 1][y] == 0 || grid[x - 1][y] == 2)) {
                    return true;
                }
            }
            case "D" -> {
                if (x > 7 && (grid[x + 1][y] == 0 || grid[x + 1][y] == 2)) {
                    return true;
                }
            }
            case "R" -> {
                if (y < 6 && (grid[x][y + 1] == 0 || grid[x][y + 1] == 2)) {
                    return true;
                }
            }
            case "L" -> {
                if (y > 0 && (grid[x][y - 1] == 0 || grid[x][y - 1] == 2)) {
                    return true;
                }
            }

        }
        return false;
    }


    public int[][] push(int x, int y, int[][] grid, String direction) {
        int[][] g = copyGrid(grid);
        switch (direction) {
            case "U":
                if (g[x - 1][y] == 0 || g[x - 1][y] == 2) break;
                g[x - 2][y] = 1;
                g[x - 1][y] = 0;
            case "D":
                if (x == 7) break;
                if (g[x + 1][y] == 0 || g[x + 1][y] == 2) break;
                g[x + 2][y] = 1;
            case "L":
                if (g[x][y - 1] == 0 || g[x][y - 1] == 2) break;
                g[x][y - 2] = 1;
                g[x][y - 1] = 0;
            case "R":
                if (g[x][y + 1] == 0 || g[x][y + 1] == 2) break;
                g[x][y + 2] = 1;
                g[x][y + 1] = 0;
        }
        return g;
    }



    private int[][] copyGrid(int[][] g) {
        int[][] output = new int[g.length][g[0].length];
        for (int i = 0; i < g.length; i++) {
            for (int j = 0; j < g[0].length; j++) {
                output[i][j] = g[i][j];
            }
        }
        return output;
    }

    private static class Route implements Comparable<Route> {
        private int movesNum;
        private final Stack<Move> moves;

        Route() {
            this.movesNum = 0; // Set default moves to 0
            this.moves = new Stack<>(); // Initialize move list
        }

        @Override
        public int compareTo(Route r) {
            return this.movesNum - r.movesNum;
        }
    }

    public static class Move {
        String direction;
        Point p;

        Move() {
            p = new Point();
        }
        /* Move Directions:
        U = Up
        D = Down
        L = Left
        R = Right */
    }
}

