package edu.polyu.thread;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;
import net.sourceforge.pmd.PMD;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.Div_Selection;
import static edu.polyu.analysis.TypeWrapper.mutant2seed;
import static edu.polyu.analysis.TypeWrapper.mutant2seq;
import static edu.polyu.transform.Transform.singleLevelExplorer;
import static edu.polyu.util.Utility.COMPILE;
import static edu.polyu.util.Utility.GUIDED_LOCATION;
import static edu.polyu.util.Utility.NO_SELECTION;
import static edu.polyu.util.Utility.PMDResultFolder;
import static edu.polyu.util.Utility.RANDOM_LOCATION;
import static edu.polyu.util.Utility.RANDOM_SELECTION;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.DIV_SELECTION;
import static edu.polyu.util.Utility.SINGLE_TESTING;
import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;
import static edu.polyu.util.Utility.mutantFolder;
import static edu.polyu.util.Utility.readPMDResultFile;

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
            if(SINGLE_TESTING) {
                System.out.println("Seed FolderName: " + this.seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
            }
            singleLevelExplorer(this.wrappers, this.currentDepth++);
            String resultFilePath = PMDResultFolder.getAbsolutePath() + File.separator + "iter" + depth + "_" + seedFolderName + "_Result.json";
            String mutantFolderPath = mutantFolder + File.separator + "iter" + depth + File.separator + seedFolderName;
            String[] pmdConfig = {
                    "-d", mutantFolderPath,
                    "-R", "category/java/" + this.ruleCategory + ".xml/" + this.ruleType,
                    "-f", "json",
                    "-r", resultFilePath,
                    "--no-cache"
            };
            PMD.runPmd(pmdConfig); // detect mutants of level i
            List<PMD_Report> reports = readPMDResultFile(resultFilePath);
            for (PMD_Report report : reports) {
                file2report.put(report.getFilepath(), report);
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (PMD_Violation violation : report.getViolations()) {
                    file2row.get(report.getFilepath()).add(violation.beginLine);
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                    if (!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
            }
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
