package com.strafe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;

public class Solver {

    private int[][] grid;
    private final Queue<Route> routes;
    private TreeSet<Route> solvedRoutes;
    private Route solvedRoute;
    private final HashSet<int[][]> visited;
    private long time1;
    public int solutions = 5;
    private int solutionsLength = 1;
    private int fastestSolution = 0;

    public Solver(int[][] solveGrid) {
        this.grid = solveGrid;
        this.routes = new LinkedList<>();
        this.solvedRoutes = new TreeSet<>();
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
        return search(newGrid, 7,0,solvedRoute);
//        for (int r = 0; r < grid.length; r++) {
//            for (int c = 0; c < grid[0].length; c++) {
//                search(newGrid,7,0,solvedRoute);
//            }
//            System.out.println();
//        }
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
                System.out.println("test");
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
                return y < 4 && grid[x][y + 1] == 1 && grid[x][y + 2] == 0;
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


    public boolean solve() {
        int solutions = 0;

        solvedRoutes = new TreeSet<>();
        Route initial = new Route();

        initial.grid = grid;
        initial.p = new Point(7, 0); // Initial location
        routes.add(initial); // root node

        while (!routes.isEmpty()) {
            if (solutions > 5) break; // Prevents more than 5 solutions from created
            Route r = routes.poll(); // already visited
            if (r == null || visited.contains(r.grid)) continue; // Catching error
            visited.add(r.grid);

            if (r.p.x == 0) { // Stops searching once it reaches goal
                solvedRoutes.add(r); //Adds path to solvedRoutes set
                solutions++;
                continue;
            }

            grid = r.grid;
            HashSet<Point> playerLocations = new HashSet<>();
            Queue<Point> nextMove = new LinkedList<>();

            nextMove.add(r.p);
            while (!nextMove.isEmpty()) { // Iterates through nextMove queue
                Point cPos = nextMove.poll();
                if (playerLocations.contains(cPos))
                    continue; // Pulls player location from nextMove and checks if it already existed
                playerLocations.add(cPos);
                Set<Point> moves = validMoves(cPos); // Generates possible moves from location, checks if it is valid
                for (Point p1 : moves) {
                    if (p1.x == 0) { // Check if it reaches finishing line (x=0)
                        solvedRoutes.add(r);
                        solutions++;
                        nextMove.clear();
                        continue;
                    }
                    if (grid[p1.x][p1.y] != 1) {
                        nextMove.add(p1);  // Checks if there is a boulder, if it does not add to possible Moves
                    }
                }
            }
//            for (Point p : playerLocations) { // Iterates through every possible position player can get to without moving a Boulder
//                generateMoves(p, grid, validMoves(p), r); // Checks if there is a valid boulder movement
//            }
        }
        if (solutions != 0) {
            Route r = solvedRoutes.pollFirst();
            if (r == null) return false; // Catching error
            solvedRoute = r;
            return true; // returns true if number of solution is greater than 0
        }
        return false;
    }

//    private void generateMoves(Point start, int[][] grid, Set<Point> moves, Route r) {
//        for (Point p : moves) {
//            int[][] newGrid = copyGrid(grid);
//            Move push = simulatePush(start.x, start.y, p.x, p.y, newGrid);
//            if (push == null || visited.contains(newGrid)) continue;
//            Route move = new Route(r);
//            move.moves.add(push);
//            move.movesNum++;
//            move.grid = newGrid;
//            move.p = push.p;
//            routes.add(move);
//        }
//    }


    private Move simulatePush(int playerX, int playerY, int boulderX, int boulderY, int[][] grid) {
        if (boulderX < 0 || boulderY < 0 || boulderX >= 7 || boulderY > 7 || grid[boulderX][boulderY] != 1)
            return null; // Making sure it is within boundaries

        if (boulderX - playerX == -1) { // go up
            if (boulderX == 2) return null; // the row below finish line
            if (grid[boulderX - 1][boulderY] == 0) { // Check if there is a box
                grid[boulderX - 1][boulderY] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.direction = "U";
                return move;
            }
        } else if (boulderX - playerX == 1) { // go down
            if (grid[boulderX + 1][boulderY] == 0) { // check for box
                grid[boulderX + 1][boulderY] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.direction = "D";
                return move;
            }
        } else if (boulderY - playerY == -1) { // go left
            if (boulderY == 0) return null; // check for boundaries
            if (grid[boulderX][boulderY - 1] == 0) { // check for box
                grid[boulderX][boulderY - 1] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.direction = "L";
                return move;
            }
        } else if (boulderY - playerY == 1) { // go right
            if (boulderY == 6) return null;
            if (grid[boulderX][boulderY + 1] == 0) { // check for box
                grid[boulderX][boulderY + 1] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.direction = "R";
                return move;
            }
        }
        return null;
    }

    private Set<Point> validMoves(Point s) {
        HashSet<Point> vMoves = new HashSet<>();
        int x = s.x;
        int y = s.y;
        if (y > 0 && y < 6) {
            vMoves.add(new Point(x, y + 1));
            vMoves.add(new Point(x, y - 1));
        } else if (y == 0) {
            vMoves.add(new Point(x, y + 1));
        } else if (y == 6) {
            vMoves.add(new Point(x, y - 1));
        }
        if (x > 0 && x < 7) {
            vMoves.add(new Point(x + 1, y));
            vMoves.add(new Point(x - 1, y));
        } else if (x == 0) {
            vMoves.add(new Point(x + 1, y));
        } else if (x == 7) {
            vMoves.add(new Point(x - 1, y));
        }
        return vMoves;
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
        private int[][] grid;
        private int movesNum;
        private final Stack<Move> moves;
        private Point p;

        Route() {
            this.movesNum = 0; // Set default moves to 0
            this.moves = new Stack<>(); // Initialize move list
        }

//        Route(Route r) {
//            this.moves = new Stack<>(r.moves); // Insert another moveList to current moveList
//            this.grid = r.grid; // Insert another grids, players, and moves to current Route
//            this.p = r.p;
//            this.movesNum = r.movesNum;
//        }

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

