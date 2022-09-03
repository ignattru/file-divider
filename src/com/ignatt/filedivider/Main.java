package com.ignatt.filedivider;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        List<String> guidsArray = new ArrayList<>();
        Scanner guids = null;

        try {
            FileInputStream inputFile = new FileInputStream("file-to-divide.txt");
            guids = new Scanner(inputFile);

            // Read input file with strings and write in array
            int n = 0;
            while (guids.hasNextLine()) {
                guidsArray.add(n, guids.nextLine());
                n++;
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        // Select methods of separation and count lines to divide. 1 - many files in one folder, 2 - many folders and one file in each folder
        Controller c = new Controller();
        c.separate(Integer.valueOf(args[0]), Double.valueOf(args[1]), guidsArray);
    }
}
