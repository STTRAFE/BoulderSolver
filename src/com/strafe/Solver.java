package com.strafe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Solver {

    private int[][] baseGrid;
    private Route solvedRoute;
    private final HashSet<int[][]> visited;
    private long time1;
    private TreeSet<Route> storedRoutes = new TreeSet<>();

    public Solver(int[][] solveGrid) {
        this.baseGrid = solveGrid;
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
                        case "U" ->
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/up.png")))));
                        case "D" ->
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/down.png")))));
                        case "L" ->
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/left.png")))));
                        case "R" ->
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/right.png")))));
                    }
                } catch (IOException ignored) {
                }
            }
        }
    }

    public Stack<Move> getSteps() {
        return solvedRoute.moves;
    }


    public boolean dfs() { // Depth First Search
        int[][] newGrid = copyGrid(baseGrid);
        time1 = System.currentTimeMillis();
        int solutions = 0;
        while (solutions < 5) { // Break out of the loop when 5 solutions are found
            if (System.currentTimeMillis() - time1 > 50) break; // Makes sure the solver exists after 50ms of solving
            Route newRoute = new Route();
            if (search(newGrid, 7, 0, newRoute)) { // If the solution is found
                storedRoutes.add(newRoute); // Add the route to the TreeSet
                solutions++; // Add 1 to the solution count
            }
        }
        solvedRoute = storedRoutes.pollFirst(); // Retrieve the only route in the TreeSet
        return solvedRoute != null; // Return if there are moves in the solvedRoute. If no, it means no solution
    }

    public boolean search(int[][] grid, int x, int y, Route r) {
        if (x < 0 || y < 0 || x >= 8 || y > 8) return false; // Makes sure it does not go outside the bounds of the map
        if (visited.contains(r.grid)) return false; // Makes sure the same path is not visited twice
        visited.add(r.grid);
        if (x == 1) { // Base Case
            return true;
        }
        if (grid[x][y] == 0) { // Error checking
            grid[x][y] = 2; // Mark the grid the DFS algorithm has visited as 2
            if ((canPush(x, y, grid, "D") || canGoThere(x, y, grid, "D")) && search(r.grid = push(x, y, grid, "D"), x + 1, y, r)) {
                Move m = new Move(); // ^ Check if it can search down or push boulder down and run the algorithm again recursively with an updated map and one position down
                m.p.x = x;
                m.p.y = y;
                m.direction = "D"; // set coordinates and set direction
                r.add(m);
                return true;
            } else if ((canPush(x, y, grid, "U") || canGoThere(x, y, grid, "U")) && search(r.grid = push(x, y, grid, "U"), x - 1, y, r)) {
                Move m = new Move(); // ^ Check if it can search up or push boulder up and run the algorithm again recursively with an updated map and one position up
                m.p.x = x;
                m.p.y = y;
                m.direction = "U"; // set coordinates and set direction
                r.add(m);
                return true;
            } else if ((canPush(x, y, grid, "R") || canGoThere(x, y, grid, "R")) && search(r.grid = push(x, y, grid, "R"), x, y + 1, r)) {
                Move m = new Move(); // ^ Check if it can search right or push boulder right and run the algorithm again recursively with an updated map and one position right
                m.p.x = x;
                m.p.y = y;
                m.direction = "R"; // set coordinates and set direction
                r.add(m);
                return true;
            } else if ((canPush(x, y, grid, "L") || canGoThere(x, y, grid, "L")) && search(r.grid = push(x, y, grid, "L"), x, y - 1, r)) {
                Move m = new Move(); // ^ Check if it can search left or push boulder left and run the algorithm again recursively with an updated map and one position left
                m.p.x = x;
                m.p.y = y;
                m.direction = "L"; // set coordinates and set direction
                r.add(m);
                return true;
            }
        }
        return false;
    }

    public boolean canPush(int x, int y, int[][] grid, String direction) {
        switch (direction) { // Check if it is within boundaries, a boulder exists, and the block behind it is empty
            case "U" -> { // up
                return x > 3 && grid[x - 1][y] == 1 && grid[x - 2][y] == 0;
            }
            case "D" -> { // down
                return x < 5 && grid[x + 1][y] == 1 && grid[x + 2][y] == 0;
            }
            case "R" -> { // right
                return y < 5 && grid[x][y + 1] == 1 && grid[x][y + 2] == 0;
            }
            case "L" -> { // left
                return y > 2 && grid[x][y - 1] == 1 && grid[x][y - 2] == 0;
            }

        }
        return false;
    }

    public boolean canGoThere(int x, int y, int[][] grid, String direction) {
        switch (direction) { // If there is no moving required, simply check if there is an air space to move there
            case "U" -> { // up
                if (x > 0 && (grid[x - 1][y] == 0 || grid[x - 1][y] == 2)) {
                    return true;
                }
            }
            case "D" -> { // down
                if (x > 7 && (grid[x + 1][y] == 0 || grid[x + 1][y] == 2)) {
                    return true;
                }
            }
            case "R" -> { // right
                if (y < 6 && (grid[x][y + 1] == 0 || grid[x][y + 1] == 2)) {
                    return true;
                }
            }
            case "L" -> { // left
                if (y > 0 && (grid[x][y - 1] == 0 || grid[x][y - 1] == 2)) {
                    return true;
                }
            }

        }
        return false;
    }


    public int[][] push(int x, int y, int[][] grid, String direction) {
        int[][] g = copyGrid(grid); // create deep copy of grid
        switch (direction) {
            case "U": // up
                if (g[x - 1][y] == 0 || g[x - 1][y] == 2) break; // If there is no boulder there, break
                g[x - 2][y] = 1; // Set the grid behind it as boulder
                g[x - 1][y] = 0; // Set the grid moving to as air
            case "D": // down
                if (x == 7) break;
                if (g[x + 1][y] == 0 || g[x + 1][y] == 2) break;
                g[x + 2][y] = 1;
            case "L": // left
                if (g[x][y - 1] == 0 || g[x][y - 1] == 2) break;
                g[x][y - 2] = 1;
                g[x][y - 1] = 0;
            case "R": // right
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
        private int[][] grid;

        Route() {
            this.movesNum = 0; // Set default moves to 0
            this.moves = new Stack<>(); // Initialize move list
        }

        public void add(Move m) {
            this.movesNum++;
            moves.push(m);
        }

        @Override
        public int compareTo(Route r) {
            return this.movesNum - r.movesNum; // Compare moves of current route and the route is trying to add
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

