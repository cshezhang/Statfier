package org.rainyday;

import org.rainyday.util.Utility;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

import static org.rainyday.util.Invoker.invokeCommandsByZT;
import static org.rainyday.util.Utility.PROJECT_PATH;
import static org.rainyday.util.Utility.SONARQUBE_SEED_PATH;
import static org.rainyday.util.Utility.readFileByLine;
import static org.rainyday.util.Utility.sep;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/11/24 17:31
 */
public class CleanSeeds {

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

    public static void main(String[] args) {
        Set<String> ruleNames = getRuleNames();
//        for(String ruleName : ruleNames) {
//            invokeCurl(ruleName);
//        }
        System.out.println("Rule Size: " + ruleNames.size());
        invokeCurl("null");
    }

}
