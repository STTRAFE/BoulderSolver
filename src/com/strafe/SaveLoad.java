package com.strafe;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SaveLoad {

    private static final File dataFile = new File(new File(System.getProperty("user.dir"), "boulder"), "BoulderData.txt");
    public static HistoricGrid[] historicGrids = new HistoricGrid[4];
    private static final Pattern p  = Pattern.compile("([0,1]), ([0-1]), ([0-1]), ([0-1]), ([0-1]), ([0-1]), ([0-1])");

    public static void save(int [][] grid, int steps, int ROWS) {
        File dir = new File(System.getProperty("user.dir"), "boulder");
        if (!dir.exists()) { // Checks if directory already exists
            dir.mkdir(); // If not, make directory
        }
        if (!dataFile.exists()) { // Check if the data file, BoulderData.txt, exists
            try {
                dataFile.createNewFile(); // Create new File if it doesn't
            } catch (IOException e) {e.printStackTrace();} // Catch exception if error occurs
        }

        ArrayList<String> toSave = new ArrayList<>();
        for (int i = 0; i < ROWS; i ++) toSave.add(Arrays.toString(grid[i+1])); // Add the grid one row at a time to the toSave ArrayList
        toSave.add("BOULDER:" + steps + ":"); // Add the keyword "BOULDER" with the number of hints user used to the toSave ArrayList

        try {
            PrintWriter pw = new PrintWriter(new FileWriter(dataFile,true));
            for (String str : toSave) { // Iterate through toSave ArrayList
                pw.println(str); // Write to file using PrintWriter
            }
            pw.close();
        } catch (IOException e) { // Catch exception if error occurs
            e.printStackTrace();
        }
    }

    public static HistoricGrid[] load() {
        int n = -1;
        int x = 6;
        boolean full = false;
        ArrayList<String> lines = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dataFile));
            String line = reader.readLine();
            while (line != null) {
                lines.add(line); // Read through the entire data file line by line until it contains nothing (null)
                line = reader.readLine();
            }
            reader.close();
        } catch (Exception e) { // Catch Error
            System.out.println("File does not exist, save some data first..");
            e.printStackTrace();
        }

        if (!lines.isEmpty()) Collections.reverse(lines); // Reverse the ArrayList so the most recent save can be displayed

        for (String s : lines) {
            if (full) break; // Stops saving if all four history slots are occupied
            if (s.startsWith("BOULDER")) { // If the line starts with Boulder, it means the line contains the number of hints used
                String[] args = s.split(":"); // Split the string so the integer of hints can be extracted
                if (n >= 3) {
                    full = true; // If the number of saves is more than four, make boolean full true
                } else {
                    n++; // Add 1 to n after every save to keep track of how many items saved
                    historicGrids[n] = new HistoricGrid(); // Create new HistoricGrid object in the array
                    x = 6; // set x to be 6 since it will be used later to assign the grid slots.
                }
                historicGrids[n].setSteps(Integer.parseInt(args[1])); // Set the amount of steps
            }
            if (n == -1) break; // Makes sure the ArrayList contains information (default is n=-1)
            if (s.startsWith("[")) { // If the line starts with "[", it contains information of the Array saved
                Matcher m = p.matcher(s); // Uses Regex to match each element of the array
                if (m.find()) {
                    historicGrids[n].setGrid(Integer.parseInt(m.group(1)),x,0); // Assign 1 or 0 to the 2D array in the HistoricGrid
                    historicGrids[n].setGrid(Integer.parseInt(m.group(2)),x,1); // x = column
                    historicGrids[n].setGrid(Integer.parseInt(m.group(3)),x,2); // 0-6 = row
                    historicGrids[n].setGrid(Integer.parseInt(m.group(4)),x,3);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(5)),x,4);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(6)),x,5);
                    historicGrids[n].setGrid(Integer.parseInt(m.group(7)),x,6);
                    x--; // Decrease x by 1 after every iteration to save another column of the 2D array
                } else {
                    System.out.println("No match found");
                }
            }
        }

        return historicGrids; // returns the Array of HistoricGrids to be displayed later
    }
}
