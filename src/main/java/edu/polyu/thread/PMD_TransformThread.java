package edu.polyu.thread;

import edu.polyu.analysis.ASTWrapper;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;
import net.sourceforge.pmd.PMD;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.Div_Selection;
import static edu.polyu.util.Util.GUIDED_LOCATION;
import static edu.polyu.util.Util.NO_SELECTION;
import static edu.polyu.util.Util.PMDResultFolder;
import static edu.polyu.util.Util.RANDOM_LOCATION;
import static edu.polyu.util.Util.RANDOM_SELECTION;
import static edu.polyu.util.Util.SEARCH_DEPTH;
import static edu.polyu.util.Util.DIV_SELECTION;
import static edu.polyu.util.Util.SINGLE_TESTING;
import static edu.polyu.util.Util.file2bugs;
import static edu.polyu.util.Util.file2report;
import static edu.polyu.util.Util.file2row;
import static edu.polyu.util.Util.mutantFolder;
import static edu.polyu.util.Util.readPMDResultFile;

/**
 * Description: This file is the MAIN class for testing PMD with multi threads
 * Author: Vanguard
 * Date: 2021/11/30 10:03
 */
public class PMD_TransformThread implements Runnable {

    private String seedFolderName;
    private String ruleCategory;
    private String ruleType;
    private ArrayDeque<ASTWrapper> wrappers;

    public static long cnt1 = 0;
    public static long cnt2 = 0;

    public PMD_TransformThread(List<ASTWrapper> initWrappers, String seedFolderName) {
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
        int currentDepth = 0;
        for (int i = 1; i <= SEARCH_DEPTH; i++) {
            while (!wrappers.isEmpty()) {
                ASTWrapper wrapper = wrappers.pollFirst();
//                if(SINGLE_TESTING) {
//                    System.out.println("Processing: " + wrapper.getFilePath());
//                }
                if (wrapper.depth == currentDepth) {
                    if (!wrapper.isBuggy()) { // Insert to queue only wrapper is not buggy
                        List<ASTWrapper> mutants = new ArrayList<>();
                        if (GUIDED_LOCATION) {
                            mutants = wrapper.TransformByGuidedLocation();
                        } else if (RANDOM_LOCATION) {
                            mutants = wrapper.TransformByRandomLocation();
                        }
                        cnt1 += mutants.size();
                        if(NO_SELECTION) {
                            wrappers.addAll(mutants);
                        }
                        List<ASTWrapper> reducedMutants = null;
                        if(RANDOM_SELECTION) {
                            reducedMutants = Random_Selection(mutants);
                        }
                        if(DIV_SELECTION) {
                            reducedMutants = Div_Selection(mutants);
                        }
                        cnt2 += reducedMutants.size();
                        wrappers.addAll(reducedMutants);
                    }
                } else {
                    wrappers.addFirst(wrapper); // The last wrapper in current depth
                    currentDepth += 1;
                    break;
                }
            }
            String resultFilePath = PMDResultFolder.getAbsolutePath() + File.separator + "iter" + i + "_" + seedFolderName + "_Result.json";
            String mutantFolderPath = mutantFolder + File.separator + "iter" + i + File.separator + seedFolderName;
            String[] pmdConfig = {
                    "-d", mutantFolderPath,
                    "-R", "category/java/" + this.ruleCategory + ".xml/" + this.ruleType,
                    "-f", "json",
                    "-r", resultFilePath,
                    "--no-cache"
            };
            PMD.runPmd(pmdConfig); // detect mutants of iter i
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
            List<ASTWrapper> validWrappers = new ArrayList<>();
            while (!wrappers.isEmpty()) {
                ASTWrapper head = wrappers.pollFirst();
                if (!head.isBuggy()) { // if this mutant is buggy, then we should switch to next mutant
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }

}
