package org.detector.thread;

import net.sourceforge.pmd.PMDConfiguration;
import org.detector.analysis.TypeWrapper;
import net.sourceforge.pmd.PMD;
import org.detector.util.Utility;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.detector.report.PMD_Report.readPMDResultFile;
import static org.detector.transform.Transform.singleLevelExplorer;
import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.SEARCH_DEPTH;
import static org.detector.util.Utility.MUTANT_FOLDER;
import static org.detector.util.Utility.REPORT_FOLDER;
import static org.detector.util.Utility.getFilePathsFromFolder;

/**
 * Description: This file is the MAIN class for testing PMD with multi threads
 * Author: RainyD4y
 * Date: 2021/11/30 10:03
 */
public class PMD_TransformThread implements Runnable {

    private int currentDepth = 0;
    private String seedFolderName;
    private String ruleCategory;
    private String ruleType;
    private ArrayDeque<TypeWrapper> wrappers;

    public PMD_TransformThread(List<TypeWrapper> initWrappers, String seedFolderName) {
        this.seedFolderName = seedFolderName;
        String[] tokens = seedFolderName.split("_");
        this.ruleCategory = tokens[0];
        this.ruleType = tokens[1];
        this.wrappers = new ArrayDeque<>() {
            {
                addAll(initWrappers);
            }
        };
    }

    // iter 1 -> SEARCH_DEPTH: 1. transformation to generate mutant; 2. invoke PMD to detect bugs
    @Override
    public void run() {
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            if(DEBUG) {
                System.out.println("Seed FolderName: " + this.seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
            }
            singleLevelExplorer(this.wrappers, this.currentDepth++);
            String resultFilePath = REPORT_FOLDER.getAbsolutePath() + File.separator + "iter" + depth + "_" + seedFolderName + "_Result.json";
            String MUTANT_FOLDER_PATH = MUTANT_FOLDER + File.separator + "iter" + depth + File.separator + seedFolderName;
            PMDConfiguration pmdConfig = new PMDConfiguration();
            pmdConfig.setInputPathList(getFilePathsFromFolder(MUTANT_FOLDER_PATH));
            pmdConfig.setRuleSets(new ArrayList<>() {
                {
                    add("category/java/" + ruleCategory + ".xml/" + ruleType);
                }
            });
            pmdConfig.setReportFormat("json");
            pmdConfig.setReportFile(Paths.get(resultFilePath));
            PMD.runPmd(pmdConfig); // detect mutants of level i
            readPMDResultFile(resultFilePath);
            List<TypeWrapper> validWrappers = new ArrayList<>();
            while (!wrappers.isEmpty()) {
                TypeWrapper head = wrappers.pollFirst();
                if (!head.isBuggy()) { // if this mutant is buggy, then we should switch to next mutant
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }

}
