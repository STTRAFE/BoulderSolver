package com.strafe;

import java.util.Arrays;

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
//        System.out.println("================");
//        for (int k = 0; k < 7; k++) {
//            for (int j = 0; j < 7; j++) {
//                System.out.print(grid[k][j]);
//            }
//            System.out.println();
//        }
//        System.out.println("================");
        this.grid[x][y] = data;
    }

    public int[][] getGrid() {
//        System.out.println("================");
//            for (int y = 0; y < 7; y++) {
//                for (int x = 0; x < 7; x++) {
//                    System.out.print(grid[x][y]);
//                }
//                System.out.println();
//            }
//            System.out.println("================");
        return this.grid;
    }
}
