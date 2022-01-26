package edu.polyu.casecheck;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.Util.INFER_SEED_PATH;
import static edu.polyu.Util.getFilenamesFromFolder;

public class InferCheck {

    public static void main(String[] args) {
        List<String> filePathList = getFilenamesFromFolder(INFER_SEED_PATH, true);
        List<String> seedPathList = new ArrayList<>();
        List<String> issuePathList = new ArrayList<>();
        for (String filePath : filePathList) {
            if (filePath.endsWith(".java")) {
                seedPathList.add(filePath);
            }
            if (filePath.equals("issue.exp")) {
                issuePathList.add(filePath);
            }
        }
        for(String issuePath : issuePathList) {
        }
    }

}
