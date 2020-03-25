package edu.hawaii.its.api.service;

import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class CheckForPattern {

    @PostConstruct
    public void CheckForPassword() throws IOException {
        boolean detected = Check("src/main/resources/",".properties","grouperClient.webService.password");

        if (detected) {
            //Assert.isTrue(false, "Please remove the passwords that is listed!");
        }
    }

    /**
     * A helper function to get files from a specific directory.
     *
     * @param path The directory path.
     * @param convention The naming convention of a file.
     * @return A list of files.
     */
    public File[] getFiles(String path, String convention) {
        File dir = new File(path);

        // If the convention param is empty, will grab all files from the path location
        if (!"".equals(convention)) {
            return dir.listFiles((dir1, name) -> name.endsWith(convention));
        } else {
            File[] allFiles = dir.listFiles();
            return allFiles;
        }
    }

    /**
     * Checks for a targeted pattern within the source code.
     *
     * @param path The directory path.
     * @param convention Naming convention of file(s) to check.
     * @param target The targeted pattern within the source code to look for.
     * @returns True if the target exist or false if it doesn't.
     */
    public boolean Check(String path, String convention, String target) throws IOException {
        boolean detected = false;
        ArrayList<Integer> lineNumbers = new ArrayList<>();
        File[] fileResources = getFiles(path, convention);

        Pattern pattern = Pattern.compile(target);
        Matcher matcher;

        // Will check the file contents within fileResources line by line for a pattern.
        for (File fr : fileResources) {
            int lineId = 0;
            Scanner fileScanner = new Scanner(fr);

            if (fr.isFile()) {
                while (fileScanner.hasNextLine()) {
                    String line = fileScanner.nextLine();
                    lineId++;

                    matcher = pattern.matcher(line);

                    if (matcher.find()) {
                        lineNumbers.add(lineId);
                    }
                }

                // If a line number exists, pattern exists. Detected becomes true.
                if (!lineNumbers.isEmpty()) {
                    detected = true;

                    System.out.print("\n------------------------------------------------------\n\n");
                    System.out.println("Pattern detected in file: " + fr + " in lines:");

                    for (int li : lineNumbers) {
                        System.out.println("Line" + li);
                    }

                    System.out.print("\n------------------------------------------------------\n\n");
                }

                lineNumbers.removeAll(lineNumbers);
            }
        }

        return detected;
    }
}