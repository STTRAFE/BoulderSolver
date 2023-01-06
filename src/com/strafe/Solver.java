package com.strafe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Solver {

    private int[][] grid;
    private final Queue<Route> routes;
    private TreeSet<Route> solvedRoutes;
    private Set<Long> visited;
    private Route solvedRoute;

    public Solver(int[][] solveGrid) {
        this.grid = solveGrid;
        this.routes = new ArrayDeque<>();
        this.solvedRoutes = new TreeSet<>();
        this.visited = new HashSet<>();
        this.solvedRoute = null;
    }

    public void showSolution() {
        if (solvedRoute != null) {
            int stepsNum = 0;
            Queue<Move> moveList = solvedRoute.moves;
            while (!moveList.isEmpty()) {
                Move m = moveList.poll();
                stepsNum++;
                GUI.grid[m.p.y][m.p.x - 1].setTextOnButton(String.valueOf(stepsNum));
                try {
                    switch (m.moveType) {
                        case 1 ->
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/up.png")))));
                        case 2 ->
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/down.png")))));
                        case 3 ->
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/left.png")))));
                        case 4 ->
                                GUI.grid[m.p.y][m.p.x - 1].setIcon(new ImageIcon(ImageIO.read(Objects.requireNonNull(getClass().getResource("/resources/right.png")))));
                    }
                } catch (Exception ignored) {}
            }
        }
    }

    public Queue<?> getSteps() {
        return solvedRoute.moves;
    }

    public boolean solve() {
        int solutions = 0;

        solvedRoutes = new TreeSet<>();
        visited = new HashSet<>();
        Route initial = new Route();

        initial.grid = grid;
        initial.p = new Point(7, 0); // Initial location
        routes.add(initial); // root node

        while (!routes.isEmpty()) {
//            System.out.println(routes.peek().moves + " " + routes.peek().player);
            long gridCode;
            if (solutions > 5) break; // Prevents more than 5 solutions from created
            Route r = routes.poll(); // already visited
            if (r == null || visited.contains(gridCode = getGridCode(r.grid))) continue;
            visited.add(gridCode);
            /*
             A code is generated based off the grid, then checked against the list of visited locations
            */
            Point start = r.p;
            if (start.x == 0) { // Stops searching once it reaches goal
                solvedRoutes.add(r); //Adds path to solvedRoutes set
                solutions++;
                continue;
            }
            grid = r.grid;
            HashSet<Point> playerLocations = new HashSet<>();
            ArrayDeque<Point> playerMoves = new ArrayDeque<>();
            playerMoves.add(start);
            while (!playerMoves.isEmpty()) { // Iterates through playerMoves queue
                Point loc = playerMoves.poll();
                if (playerLocations.contains(loc))
                    continue; // Pulls player location from playerMoves and checks if it already existed
                playerLocations.add(loc);
                Set<Point> moves = validMoves(loc); // Generates possible moves from location, checks if it is valid
                for (Point p1 : moves) {
                    if (p1.x == 0) { // Check if it reaches finishing line (x=0)
                        solvedRoutes.add(r);
                        solutions++;
                        playerMoves.clear();
                        continue;
                    }
                    if (grid[p1.x][p1.y] != 1) {
                        playerMoves.add(p1);  // Checks if there is a boulder, if it does not add to possible Moves
                    }
                }
            }
            for (Point p : playerLocations) { // Iterates through every possible position player can get to without moving a Boulder
                getValidBoulderPush(p, grid, validMoves(p), r); // Checks if there is a valid boulder movement
            }
        }
        if (solutions != 0) {
            Route r = solvedRoutes.pollFirst();
            if (r == null) { // Catching error
                return false;
            }
            solvedRoute = r;
            return true; // returns true if number of solution is greater than 0
        }
        return false;
    }

    private void getValidBoulderPush(Point start, int[][] grid, Set<Point> moves, Route r) {
        for (Point p : moves) {
            int[][] newGrid = copyGrid(grid);
            Move push = simulatePush(start.x, start.y, p.x, p.y, newGrid);
            if (push == null || visited.contains(getGridCode(newGrid))) continue;
            Route move = new Route(r);
            move.moves.add(push);
            move.movesNum++;
            move.grid = newGrid;
            move.p = push.p;
            routes.add(move);
        }
    }


    private Move simulatePush(int playerX, int playerY, int boulderX, int boulderY, int[][] grid) {

        if (grid[boulderX][boulderY] != 1 || boulderX == 0 || boulderX == 7) return null;

        if (boulderX - playerX == -1) {
            if (grid[boulderX - 1][boulderY] == 0) {
                grid[boulderX - 1][boulderY] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.moveType = 1;
                return move;
            }
        } else if (boulderX - playerX == 1) {
            if (grid[boulderX + 1][boulderY] == 0) {
                grid[boulderX + 1][boulderY] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.moveType = 2;
                return move;
            }
        } else if (boulderY - playerY == -1) {
            if (boulderY == 0) return null;
            if (grid[boulderX][boulderY - 1] == 0) {
                grid[boulderX][boulderY - 1] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.moveType = 3;
                return move;
            }
        } else if (boulderY - playerY == 1) {
            if (boulderY == 6) return null;
            if (grid[boulderX][boulderY + 1] == 0) {
                grid[boulderX][boulderY + 1] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.moveType = 4;
                return move;
            }
        }
        return null;
    }

    private Set<Point> validMoves(Point s) {
        HashSet<Point> o = new HashSet<>();
        int x = s.x;
        int y = s.y;
        if (y > 0 && y < 6) {
            o.add(new Point(x, y + 1));
            o.add(new Point(x, y - 1));
        } else if (y == 0) {
            o.add(new Point(x, y + 1));
        } else if (y == 6) {
            o.add(new Point(x, y - 1));
        }
        if (x > 0 && x < 7) {
            o.add(new Point(x + 1, y));
            o.add(new Point(x - 1, y));
        } else if (x == 0) {
            o.add(new Point(x + 1, y));
        } else if (x == 7) {
            o.add(new Point(x - 1, y));
        }
//        System.o.println(o);
        return o;
    }

    private int[][] copyGrid(int[][] g) {
        int[][] output = new int[g.length][g[0].length];
        for (int i = 0; i < g.length; i++) {
            System.arraycopy(g[i], 0, output[i], 0, g[0].length);
        }
        return output;
    }

    private long getGridCode(int[][] grid) {
        long sum = 0;
        for (int[] i : grid) {
            sum = 7 * sum + Arrays.hashCode(i);
        }
        return sum;
    }

    private class Route implements Comparable<Route> {
        private int[][] grid;
        private int movesNum;
        private final Queue<Move> moves;
        private Point p;

        Route() {
            this.movesNum = 0; // Set default moves to 0
            this.moves = new ArrayDeque<>(); // Initialize move list
        }

        Route(Route r) {
            this.moves = new ArrayDeque<>(r.moves); // Insert another moveList to current moveList
            this.grid = r.grid; // Insert another grids, players, and moves to current Route
            this.p = r.p;
            this.movesNum = r.movesNum;
        }

        @Override
        public int compareTo(Route r) {
            return this.movesNum - r.movesNum;
        }
    }

    public class Move {
        int moveType;
        Point p;
        /*
        Move Types:
        1 = Up
        2 = Down
        3 = Left
        4 = Right
         */
    }
}

