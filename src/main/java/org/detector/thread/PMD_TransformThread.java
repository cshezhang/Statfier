package org.detector.thread;

import org.detector.analysis.TypeWrapper;
import net.sourceforge.pmd.PMD;
import org.detector.util.Utility;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.detector.transform.Transform.singleLevelExplorer;
import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.SEARCH_DEPTH;
import static org.detector.util.Utility.mutantFolder;
import static org.detector.util.Utility.reportFolder;

/**
 * Description: This file is the MAIN class for testing PMD with multi threads
 * Author: Vanguard
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
            String resultFilePath = reportFolder.getAbsolutePath() + File.separator + "iter" + depth + "_" + seedFolderName + "_Result.json";
            String mutantFolderPath = mutantFolder + File.separator + "iter" + depth + File.separator + seedFolderName;
            String[] pmdConfig = {
                    "-d", mutantFolderPath,
                    "-R", "category/java/" + this.ruleCategory + ".xml/" + this.ruleType,
                    "-f", "json",
                    "-r", resultFilePath
            };
            PMD.runPmd(pmdConfig); // detect mutants of level i
            Utility.readPMDResultFile(resultFilePath);
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
