package org.detector.thread;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.detector.util.Utility.REPORT_FOLDER;
import static org.detector.util.Utility.getFilePathsFromFolder;

/**
 * Description: Previous main process for testing PMD with multi thread
 * Author: RainyD4y
 * Date: 2021/11/30 10:03
 */

public class PMD_InvokeThread implements Runnable {

    private int iterDepth;
    private String seedFolderPath;
    private String seedFolderName;
    private List<String> ruleList;

    public PMD_InvokeThread(int iterDepth, String seedFolderPath, String seedFolderName) {
        this.iterDepth = iterDepth;
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
        String[] tokens = seedFolderName.split("_");
        this.ruleList = new ArrayList<> () {
            {
                add("category/java/" + tokens[0] + ".xml/" + tokens[1]);
            }
        };
    }

    // seedFolderPath can be java source file or a folder contains source files
    @Override
    public void run() {
        PMDConfiguration pmdConfig = new PMDConfiguration();
        pmdConfig.setInputPathList(getFilePathsFromFolder(seedFolderPath  + File.separator + seedFolderName));
        pmdConfig.setRuleSets(this.ruleList);
        pmdConfig.setReportFormat("json");
        pmdConfig.setReportFile(Paths.get(REPORT_FOLDER.getAbsolutePath()  + File.separator + "iter" + iterDepth + "_" + seedFolderName + "_Result.json"));
        pmdConfig.setIgnoreIncrementalAnalysis(true);
        PMD.runPmd(pmdConfig);
    }

}
