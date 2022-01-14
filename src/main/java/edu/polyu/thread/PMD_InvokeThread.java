package edu.polyu.thread;

import static edu.polyu.Invoker.invokeCommands;
import static edu.polyu.Util.PMDResultFolder;
import static edu.polyu.Util.sep;

public class PMD_InvokeThread implements Runnable {

    private int iterDepth;
    private String seedFolderPath;
    private String seedFolderName;
    private String ruleCategory;
    private String ruleType;

    public PMD_InvokeThread(int iterDepth, String seedFolderPath, String seedFolderName) {
        this.iterDepth = iterDepth;
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
        String[] tokens = seedFolderName.split("_");
        this.ruleCategory = tokens[0];
        this.ruleType = tokens[1];
    }

    // seedFolderPath can be java source file or a folder contains source files
    @Override
    public void run() {
        String[] pmdConfig = {
            "-d", seedFolderPath + sep + seedFolderName,
            "-R", "category/java/" + this.ruleCategory + ".xml/" + this.ruleType,
            "-f", "json",
            "-r", PMDResultFolder.getAbsolutePath() + sep + "iter" + iterDepth + "_" + seedFolderName + "_Result.json"
        };
        invokeCommands(pmdConfig);
    }

}
