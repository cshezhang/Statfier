package edu.polyu;

import org.apache.commons.io.FileUtils;
import edu.polyu.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Utility.FINDSECBUGS_PATH;
import static edu.polyu.util.Utility.FINDSECBUGS_SEED_PATH;
import static edu.polyu.util.Utility.SPOTBUGS_SEED_PATH;
import static edu.polyu.util.Utility.getFilenamesFromFolder;
import static edu.polyu.util.Utility.readFileByLine;
import static edu.polyu.util.Utility.writeLinesToFile;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/11/24 17:31
 */
public class SeedCleaner {

    public static Set<String> getRuleNames() {
        String seedFolderPath = "/Users/austin/projects/sonar-java/java-checks/src/main/java/org/sonar/java/checks";
        List<String> filePaths = Utility.getFilenamesFromFolder(seedFolderPath, true);
        Set<String> ruleNameSet = new HashSet<>();
        for(String filePath : filePaths) {
            List<String> lines = readFileByLine(filePath);
            for(String line : lines) {
                if(line.contains("@Rule(")) {
                    String ruleName = line.substring(line.indexOf('\"') + 1, line.lastIndexOf('\"'));
                    ruleNameSet.add(ruleName);
                }
            }
        }
        return ruleNameSet;
    }

    public static void invokeCurl(String ruleName) {
        String[] curlCmd = new String[2];
        curlCmd[0] = "curl";
        curlCmd[1] = "https://localhost:9000/api/issues/search?p=1&ps=500&componentKeys=Official_Seed_Test&rules=java:S1144";
        invokeCommandsByZT(curlCmd);
    }

    public static void filterDuplicateSeedFiles(String seedFolderPath) {
        List<String> filePaths = getFilenamesFromFolder(seedFolderPath, true);
        HashMap<Integer, ArrayList<String>> hash2fileList = new HashMap<>();
        for(String filePath : filePaths) {
            if (!filePath.endsWith(".java")) {
                System.out.println(filePath);
            }
        }
        HashSet<Integer> fileHashSet = new HashSet<>();
        for(String filePath : filePaths) {
            File file = new File(filePath);
            try {
                String content = FileUtils.readFileToString(file, "UTF-8");
                int hash = content.hashCode();
                fileHashSet.add(hash);
                if(!hash2fileList.containsKey(hash)) {
                    ArrayList<String> pathList = new ArrayList<>();
                    hash2fileList.put(hash, pathList);
                }
                hash2fileList.get(hash).add(filePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Hash Size: " + fileHashSet.size());
        System.out.println("List Size: " + filePaths.size());
        for(ArrayList<String> pathList : hash2fileList.values()) {
            if(pathList.size() > 1) {
                for(int i = 1; i < pathList.size(); i++) {
                    try {
                        FileUtils.delete(new File(pathList.get(i)));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
    Remove useless package definitions in the seed files.
     */
    public static void removePkgDefinition(List<String> seedPaths) {
        for(int i = 0; i < seedPaths.size(); i++) {
            String seedPath = seedPaths.get(i);
            List<String> lines = readFileByLine(seedPath);
            List<String> newLines = new ArrayList<>();
            for(int j = 0; j < lines.size(); j++) {
                String line = lines.get(j);
                if(!line.strip().startsWith("package")) {
                    newLines.add(line);
                }
            }
            writeLinesToFile(seedPath, newLines);
        }
    }

    public static void removeFirstEmptyLines(List<String> filePaths) {
        for(String filePath : filePaths) {
            List<String> lines = readFileByLine(filePath);
            int newStartIndex = -1;
            for(int i = 0; i < lines.size(); i++) {
                if(lines.get(i).strip().length() != 0) {
                    newStartIndex = i;
                    break;
                }
            }
            if(newStartIndex == -1) {
                System.out.println("Error File Path: " + filePath);
            } else {
                List<String> newLines = lines.subList(newStartIndex, lines.size());
                writeLinesToFile(filePath, newLines);
            }
        }
    }

    public static void main(String[] args) {
        removeFirstEmptyLines(getFilenamesFromFolder(FINDSECBUGS_SEED_PATH, true));
//        removePkgDefinition(getFilenamesFromFolder(FINDSECBUGS_SEED_PATH, true));
//        filterDuplicateSeedFiles(FINDSECBUGS_SEED_PATH);
//        Set<String> ruleNames = getRuleNames();
////        for(String ruleName : ruleNames) {
////            invokeCurl(ruleName);
////        }
//        System.out.println("Rule Size: " + ruleNames.size());
//        invokeCurl("null");
    }

}
