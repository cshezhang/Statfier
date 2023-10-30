package edu.polyu.casecheck;

import edu.polyu.util.Utility;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class SonarQubeCheck {

    private static final String testFolder = "/home/vanguard/projects/sonar-java/java-checks/src/main/java/org/sonar/java/checks";

    public static List<String> getRuleFromFile(String filePath) {
        List<String> rules = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String tmp;
            while((tmp = reader.readLine()) != null) {
                if(tmp.contains("@Rule(key")) {
                    if(tmp.contains("NoSonar")) {
                        continue;
                    }
                    System.out.println(tmp);
                    rules.add(tmp);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return rules;
    }

    public static void main(String[] args) {
        List<String> filePaths = Utility.getFilenamesFromFolder(testFolder, true);
//        System.out.println("File Size: " + filePaths.size());
        HashSet<String> res = new HashSet<>();
        for(String filePath : filePaths) {
            List<String> rules = getRuleFromFile(filePath);
            res.addAll(rules);
        }
//        System.out.println(res);
        System.out.println(res.size());
    }

}
