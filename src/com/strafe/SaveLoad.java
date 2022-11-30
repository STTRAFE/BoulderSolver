package com.strafe;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveLoad {

    private static File dataFile = new File(new File(System.getProperty("user.dir"), "boulder"), "BoulderData.txt");
    public static HistoricGrid[] historicGrids = new HistoricGrid[6];
    private static final Pattern p  = Pattern.compile("([0,1]), ([0-1]), ([0-1]), ([0-1]), ([0-1]), ([0-1]), ([0-1])");

    public static void saveLoad(int [][] grid, int steps, int ROWS) {
        File dir = new File(System.getProperty("user.dir"), "boulder");
        if (!dir.exists()) {
            dir.mkdir();
        }
        if (!dataFile.exists()) {
            try {
                dataFile.createNewFile();
            } catch (IOException e) {e.printStackTrace();}
        }

        save(grid,steps,ROWS);
    }
    public static void save(int [][] grid, int steps, int ROWS) {
        ArrayList<String> toSave = new ArrayList<>();

        toSave.add("BOULDER:" + steps + ":");

       for (int i = 0; i < ROWS; i ++) {
           toSave.add(Arrays.toString(grid[i+1]));
       }

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(dataFile,true));
            for (String str : toSave) {
                pw.println(str);
            }
            pw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void load() {
        int n = -1;
        boolean full = false;
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line);
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        for (String s : lines) {
            int x = 0;
            if (s.startsWith("BOULDER")) {
                String[] args = s.split(":");
                if (n >= 3) {
                    System.out.println("full 4");
                    full = true;
                } else {
                    System.out.println("Creating new grid object");
                    n++;
                    historicGrids[n] = new HistoricGrid();
                    x = 0;
                }
                historicGrids[n].setSteps(Integer.parseInt(args[1]));
            }

            if (s.startsWith("[") && !full) {
                Matcher m = p.matcher(s);
                if (m.find()) {
                    historicGrids[n].setGrid(Integer.parseInt(m.group(1)),x,0);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(2)),x,1);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(3)),x,2);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(4)),x,3);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(5)),x,4);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(6)),x,5);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(7)),x,6);
                    x++;
                } else {
                    System.out.println("No match found");
                }
            }
        }

        System.out.println(historicGrids[0].getGrid()[1][6]);
        System.out.println(historicGrids[0].getSteps());
        System.out.println(Arrays.deepToString(historicGrids[1].getGrid()));
        System.out.println(Arrays.deepToString(historicGrids[2].getGrid()));
        System.out.println(Arrays.deepToString(historicGrids[3].getGrid()));
    }
}
