package edu.polyu.thread;

import edu.polyu.Invoker;

import static edu.polyu.Invoker.invokeCommands;
import static edu.polyu.Util.PMDResultsFolder;
import static edu.polyu.Util.pmdPath;
import static edu.polyu.Util.sep;

public class PMD_Invoker implements Runnable {

    private int iterDepth;
    private String seedFolderPath;
    private String seedFolderName;
    private String ruleCategory;
    private String ruleType;

    public PMD_Invoker(int iterDepth, String seedFolderPath, String seedFolderName) {
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
        String pmdConfig = pmdPath + " pmd"
                + " -d " + seedFolderPath + sep + seedFolderName
                + " -R " + "category/java/" + this.ruleCategory + ".xml/" + this.ruleType
                + " -f " + "json"
                + " -r " +  PMDResultsFolder.getAbsolutePath() + sep + "iter" + iterDepth + "_" + seedFolderName + "_Result.json"
                + " --no-cache";
        String[] pmdArgs = {"/bin/sh", "-c", pmdConfig};
        invokeCommands(pmdArgs);
    }

}
