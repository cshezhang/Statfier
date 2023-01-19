package org.detector.casecheck;

import org.detector.util.Utility;
import org.detector.util.Invoker;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static org.detector.report.SpotBugs_Report.readSpotBugsResultFile;
import static org.detector.util.Utility.*;

public class SpotBugsChecker {

    private static final String spotBugsPath = toolPath  + File.separator + "SpotBugs"  + File.separator + "bin"  + File.separator + "spotbugs";

    public static void compileFirstIterJavaFiles(List<String> filePaths) {
        File classFolder = new File("/home/vanguard/evaluation/InitClasses");
        if(!classFolder.exists()) {
            classFolder.mkdir();
        }
        for(String filePath : filePaths) {
            int lastSepIndex = filePath.lastIndexOf(sep);
            String folderPath = filePath.substring(0, lastSepIndex);
            String fileName = filePath.substring(lastSepIndex + 1);
            Invoker.compileJavaSourceFile(folderPath, fileName, classFolder.getAbsolutePath());
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
        List<String> allSeedList = Utility.getFilenamesFromFolder(SPOTBUGS_SEED_PATH, true);
        HashMap<String, List<String>> rule2seedlist = new HashMap<>();
//        compileFirstIterJavaFiles(allSeedList);
        HashSet<String> ruleCounter = new HashSet<>();
        HashMap<String, Integer> rule2cnt = new HashMap<>();
        int seedCounter = 0;
        for(int i = 0; i < allSeedList.size(); i++) {
            String seedPath = allSeedList.get(i);
            if(!seedPath.endsWith(".java")) {
                continue;
            }
            List<String> warningLines = getWarningLabels(seedPath);
            if(warningLines.size() > 0) {
                seedCounter++;
                for (String warningLine : warningLines) {
                    int startIndex = warningLine.indexOf("\"") + 1;
                    int endIndex = warningLine.lastIndexOf("\"");
                    String ruleType = null;
                    try {
                        // Get bug type by @tag
                        ruleType = warningLine.substring(startIndex, endIndex);
                    } catch (Exception e) {
                        System.err.println(warningLine);
                        e.printStackTrace();
                    }
                    ruleCounter.add(ruleType);
                    if(!rule2cnt.containsKey(ruleType)) {
                        rule2cnt.put(ruleType, 0);
                        rule2seedlist.put(ruleType, new ArrayList<>());
                    }
                    // rule2seedlist saves the absolute paths.
                    rule2cnt.put(ruleType, rule2cnt.get(ruleType) + 1);
                    rule2seedlist.get(ruleType).add(seedPath);
                }
            }
        }
        System.out.println("Covered Rules: " + ruleCounter.size());
        System.out.println("Seed Count: " + seedCounter);
        for(String key : rule2cnt.keySet()) {
            System.out.println(key + "  " + rule2cnt.get(key));
//            System.out.println(rule2seedlist.get(key));
        }
        // Following is used to move seed to new folder with rule type name
//        for(String rule : ruleCounter) {
//            File ruleDir = new File(SPOTBUGS_SEED_PATH  + File.separator + rule);
//            ruleDir.mkdir();
//            List<String> seedList = rule2seedlist.get(rule);
//            for(String seedPath : seedList) {
//                File srcFile = new File(seedPath);
//                String filename = srcFile.getName();
//                File dstFile = new File(ruleDir.getAbsolutePath()  + File.separator + filename);
//                try {
//                    FileUtils.copyFile(srcFile, dstFile);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//        }
//        initThreadPool();
//        for(String ruleType : ruleCounter) {
//            threadPool.submit(new SpotBugs_InvokeThread(SPOTBUGS_SEED_PATH  + File.separator + ruleType, ruleType, getFilenamesFromFolder(SPOTBUGS_SEED_PATH  + File.separator + ruleType, false)));
//        }
//        waitThreadPoolEnding();
        List<String> reportPaths = getFilenamesFromFolder(EVALUATION_PATH  + File.separator + "results", true);
        for(String reportPath : reportPaths) {
            try {
                String[] tokens = reportPath.split(reg_sep);
                String reportName = tokens[tokens.length - 1].substring(0, tokens[tokens.length - 1].length() - 11);
                String seedPath = "/home/vanguard/projects/SAMutator/seeds/" + reportName + ".java";
                readSpotBugsResultFile(seedPath, reportPath);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}