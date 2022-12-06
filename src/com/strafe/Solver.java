package com.strafe;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.util.*;

public class Solver {

    private int[][] grid;
    private Queue<Route> routes;
    private TreeSet<Route> solvedRoutes;
    private Set<Long> visited;
    private Route solvedRoute;

    public Solver(int [][] solveGrid) {
        this.grid = solveGrid;
        this.routes = new ArrayDeque<>();
        this.solvedRoutes = new TreeSet<>();
        this.visited = new HashSet<>();
        this.solvedRoute = null;
    }

    public void showSolution() {
        if (solvedRoute != null) {
            int stepsNum = 0;
            Queue<Move> moveList = solvedRoute.moveList;
            while (!moveList.isEmpty()) {
                Move m = moveList.poll();
                stepsNum++;
                ButtonGrid.grid[m.p.y][m.p.x-1].setTextOnButton(String.valueOf(stepsNum));
                ButtonGrid.grid[m.p.y][m.p.x-1].setColor(Color.pink);
                switch (m.offsetType) {
                    case 1:
//                        System.out.println("up" + " " + m.p.x + " " + (m.p.y + 1));
                        try {
                            ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/up.png"))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
//                        System.out.println("down" + " " + m.p.x + " " + (m.p.y + 1));
                        try {
                            ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/down.png"))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 3:
//                        System.out.println("left" + " " + m.p.x + " " + (m.p.y + 1));
                        try {
                            ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/left.png"))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 4:
//                        System.out.println("right" + " " + m.p.x + " " + (m.p.y + 1));
                        try {
                            ButtonGrid.grid[m.p.y][m.p.x-1].setIcon(new ImageIcon(ImageIO.read(getClass().getResource("/resources/right.png"))));
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    default:
                        break;
                }
//                if (moveList.peek()!=null) System.out.println("Next Step:");
            }
        }
    }

    public Queue<?> getSteps() {
        return solvedRoute.moveList;
    }

    public boolean solve() {
        int solutions = 0;
        int[][] newGrid = copyGrid(grid);

        solvedRoutes = new TreeSet<>();
        visited = new HashSet<>();
        Route initial = new Route();

        initial.grid = grid;
        initial.player = new Point(7, 0); // Initial location
        routes.add(initial); //root node

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
            Point start = r.player;
            if (start.x == 0) { // Stops searching once it reaches goal
                solvedRoutes.add(r); //Adds path to solvedRoutes treeset
                solutions++;
                continue;
            }
            grid = r.grid;
            HashSet<Point> playerLocs = new HashSet<>();
            ArrayDeque<Point> pMoves = new ArrayDeque<>();
            pMoves.add(start);
            findPaths:
            while (!pMoves.isEmpty()) { // Iterates through pMoves queue
                Point loc = pMoves.poll();
                if (playerLocs.contains(loc)) continue; // Pulls player location from pMoves and checks if it already existed
                playerLocs.add(loc);
                Set<Point> moves = validMoves(loc); // Generates possible moves from location, checks if it is valid
                for (Point p1 : moves) {
                    if (p1.x == 0) { // Check if it reaches finishing line (x=0)
                        solvedRoutes.add(r);
                        solutions++;
                        pMoves.clear();
                        continue findPaths;
                    }
                    if (grid[p1.x][p1.y] == 1) continue; // Checks if there is a boulder, if it does not add to possible Moves
                    pMoves.add(p1);
                }
            }
            for (Point p : playerLocs) { // Iterates through every possible position player can get to without moving a Boulder
                getValidBoulderPush(p, grid, validMoves(p), r); // Checks if there is a valid boulder movement
            }
        }
        if (solutions != 0) {
            Route r = solvedRoutes.pollFirst();
            if (r == null) { // Catching error
                return false;
            }
            solvedRoute = r;
            ArrayDeque<Move> cloned = new ArrayDeque<>(r.moveList);
            while (!cloned.isEmpty()) { // copies the possible moves
                Move poll = cloned.poll();
                pushBoulder(poll, newGrid);
            }
            return true;
        }
        return false;
    }

    private void getValidBoulderPush(Point start, int[][] grid, Set<Point> moves, Route r) {
        for (Point p : moves) {
            int[][] newGrid = copyGrid(grid);
            Move push = pushBoulder(start.x, start.y, p.x, p.y, newGrid);
            if (push == null || visited.contains(getGridCode(newGrid))) continue;
            Route move = new Route(r);
            move.moveList.add(push);
            move.moves++;
            move.grid = newGrid;
            move.player = push.p;
            routes.add(move);
        }
    }

    private void pushBoulder(Move move, int[][] grid) {
        int x = move.p.x;
        int y = move.p.y;
        switch (move.offsetType) {
            case 1 -> {
                grid[x - 1][y] = 1;
                grid[x][y] = 0;
            }
            case 2 -> {
                grid[x + 1][y] = 1;
                grid[x][y] = 0;
            }
            case 3 -> {
                grid[x][y - 1] = 1;
                grid[x][y] = 0;
            }
            case 4 -> {
                grid[x][y + 1] = 1;
                grid[x][y] = 0;
            }
        }
    }

    private Move pushBoulder(int playerX, int playerY, int boulderX, int boulderY, int[][] grid) {

        if (grid[boulderX][boulderY] != 1 || boulderX == 0 || boulderX == 7) return null;

        if (boulderX - playerX == -1) {
            if (grid[boulderX - 1][boulderY] == 0) {
                grid[boulderX - 1][boulderY] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.offsetType = 1;
                return move;
            }
        } else if (boulderX - playerX == 1) {
            if (grid[boulderX + 1][boulderY] == 0) {
                grid[boulderX + 1][boulderY] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.offsetType = 2;
                return move;
            }
        } else if (boulderY - playerY == -1) {
            if (boulderY == 0) {
                return null;
            }
            if (grid[boulderX][boulderY - 1] == 0) {
                grid[boulderX][boulderY - 1] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.offsetType = 3;
                return move;
            }
        } else if (boulderY - playerY == 1) {
            if (boulderY == 6) {
                return null;
            }
            if (grid[boulderX][boulderY + 1] == 0) {
                grid[boulderX][boulderY + 1] = 1;
                grid[boulderX][boulderY] = 0;
                Move move = new Move();
                move.p = new Point(boulderX, boulderY);
                move.offsetType = 4;
                return move;
            }
        }
        return null;
    }

    private Set<Point> validMoves(Point start) {
        HashSet<Point> out = new HashSet<>();
        int x = start.x;
        int y = start.y;
        if (y > 0 && y < 6) {
            out.add(new Point(x, y + 1));
            out.add(new Point(x, y - 1));
        } else if (y == 0) {
            out.add(new Point(x, y + 1));
        } else if (y == 6) {
            out.add(new Point(x, y - 1));
        }
        if (x > 0 && x < 7) {
            out.add(new Point(x + 1, y));
            out.add(new Point(x - 1, y));
        } else if (x == 0) {
            out.add(new Point(x + 1, y));
        } else if (x == 7) {
            out.add(new Point(x - 1, y));
        }
//        System.out.println(out);
        return out;
    }

    private int[][] copyGrid(int[][] g) {
        int[][] grid = new int[g.length][g[0].length];
        for (int i = 0; i < g.length; ++i) {
            System.arraycopy(g[i], 0, grid[i], 0, g[0].length);
        }
        return grid;
    }

    private long getGridCode(int[][] grid) {
        long sum = 0L;
        for (int[] i : grid) {
            sum = 7L * sum + (long) Arrays.hashCode(i);
        }
        return sum;
    }

    private static class Route implements Comparable<Route> {
        private Point player;
        private Queue<Move> moveList;
        private int[][] grid;
        private int moves;

        Route() {
            this.moveList = new ArrayDeque<>();
            this.moves = 0;
        }

        Route(Route r) {
            this.grid = r.grid;
            this.player = r.player;
            this.moveList = new ArrayDeque<>(r.moveList);
            this.moves = r.moves;
        }

        @Override
        public int compareTo(Route r) {
            return this.moves - r.moves;
        }
    }

    public static class Move {
        Point p;
        int offsetType;
        /*
        Offset Types:
        1 = Up
        2 = Down
        3 = Left
        4 = Right
         */
    }
}

