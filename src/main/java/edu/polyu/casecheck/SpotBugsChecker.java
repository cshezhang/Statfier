package edu.polyu.casecheck;

import edu.polyu.Util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.Invoker.compileJavaSourceFile;
import static edu.polyu.Util.*;

public class SpotBugsChecker {

    private static final String spotBugsPath = toolPath + sep + "SpotBugs" + sep + "bin" + sep + "spotbugs";

    public static void compileFirstIterJavaFiles(List<String> filePaths) {
        File classFolder = new File("/home/vanguard/evaluation/InitClasses");
        if(!classFolder.exists()) {
            classFolder.mkdir();
        }
        for(String filePath : filePaths) {
            int lastSepIndex = filePath.lastIndexOf(sep);
            String folderPath = filePath.substring(0, lastSepIndex);
            String fileName = filePath.substring(lastSepIndex + 1);
            compileJavaSourceFile(folderPath, fileName, classFolder.getAbsolutePath());
        }
    }

    public static List<String> getWarningLabels(String filePath) {
        List<String> warningLabels = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filePath);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line, tmp;
            while((tmp = reader.readLine()) != null) {
//                String lowerStr = line.toLowerCase();
//                if(lowerStr.contains("expect") && lowerStr.contains("warning") && !lowerStr.contains("import")) {
                line = tmp.trim();
                if(line.startsWith("//") || line.contains("SuppressWarning") || line.contains("SuppressFBWarning")
                || line.startsWith("*")) {
                    continue;
                }
                if(line.contains("@") && line.contains("Warning")) {
                    warningLabels.add(line);
                }
            }
            fis.close();
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return warningLabels;
    }

    public static void main(String[] args) {
//        List<String> seedList = Util.getFilenamesFromFolder("/home/vanguard/projects/spotbugs", true);
        List<String> seedList = Util.getFilenamesFromFolder(SPOTBUGS_SEED_PATH, true);
//        compileFirstIterJavaFiles(seedList);
        HashSet<String> ruleCounter = new HashSet<>();
        HashMap<String, Integer> rule2cnt = new HashMap<>();
        int seedCounter = 0;
        for(int i = 0; i < seedList.size(); i++) {
            String seedPath = seedList.get(i);
            if(!seedPath.endsWith(".java")) {
                continue;
            }
            List<String> warningLines = getWarningLabels(seedPath);
            if(warningLines.size() > 0) {
                seedCounter++;
//                System.out.println(seedPath);
                for (String warningLine : warningLines) {
//                    System.out.println(warningLine);
                    int startIndex = warningLine.indexOf("\"") + 1;
                    int endIndex = warningLine.lastIndexOf("\"");
                    String ruleType = null;
                    try {
                        ruleType = warningLine.substring(startIndex, endIndex);
                    } catch (Exception e) {
                        System.out.println(warningLine);
                        e.printStackTrace();
                    }
//                    System.out.println(ruleType);
                    ruleCounter.add(ruleType);
                    if(rule2cnt.containsKey(ruleType)) {
                        rule2cnt.put(ruleType, rule2cnt.get(ruleType) + 1);
                    } else {
                        rule2cnt.put(ruleType, 1);
                    }
                }
            }
        }
        System.out.println(ruleCounter.size());
        for(String key : rule2cnt.keySet()) {
            System.out.println(key + "  " + rule2cnt.get(key));
        }
        System.out.println(seedCounter);
    }

}