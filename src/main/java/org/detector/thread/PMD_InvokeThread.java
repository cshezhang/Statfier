package org.detector.thread;

import net.sourceforge.pmd.PMD;

import java.io.File;

import static org.detector.util.Utility.REPORT_FOLDER;

/**
 * Description: Previous main process for testing PMD with multi thread
 * Author: RainyD4y
 * Date: 2021/11/30 10:03
 */

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
            "-d", seedFolderPath  + File.separator + seedFolderName,
            "-R", "category/java/" + this.ruleCategory + ".xml/" + this.ruleType,
            "-f", "json",
            "-r", REPORT_FOLDER.getAbsolutePath()  + File.separator + "iter" + iterDepth + "_" + seedFolderName + "_Result.json"
        };
        PMD.runPmd(pmdConfig);
    }

}
