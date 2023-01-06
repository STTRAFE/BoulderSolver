package com.strafe;

public class HistoricGrid {
    private int steps;
    private int[][] grid = new int [8][7];

    public void setSteps(int steps) {
        this.steps = steps;
    }

    public int getSteps() {
        return this.steps;
    }

    public void setGrid(int data, int x, int y) {
        this.grid[x][y] = data;
    }

    public int[][] getGrid() {
        return this.grid;
    }
}
