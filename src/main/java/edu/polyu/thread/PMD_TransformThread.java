package edu.polyu.thread;

import edu.polyu.ASTWrapper;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;
import net.sourceforge.pmd.PMD;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.Util.GUIDED_RANDOM_TESTING;
import static edu.polyu.Util.MAIN_EXECUTION;
import static edu.polyu.Util.PMDResultFolder;
import static edu.polyu.Util.SEARCH_DEPTH;
import static edu.polyu.Util.file2bugs;
import static edu.polyu.Util.file2line;
import static edu.polyu.Util.mutantFolder;
import static edu.polyu.Util.readPMDResultFile;
import static edu.polyu.Util.sep;

/**
 * Description: This file is the main class for testing PMD with multi threads
 * Author: Vanguard
 * Date: 2021/11/30 10:03 上午
 */
public class PMD_TransformThread implements Runnable {

    private int currentDepth;
    private String seedFolderName;
    private String ruleCategory;
    private String ruleType;
    private ArrayDeque<ASTWrapper> wrappers;

    public PMD_TransformThread(List<ASTWrapper> initWrappers, String seedFolderName) {
        this.currentDepth = 0;
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
        for(int i = 1; i <= SEARCH_DEPTH; i++) {
            while(!wrappers.isEmpty()) {
                ASTWrapper wrapper = wrappers.pollFirst();
                if (wrapper.depth == currentDepth) {
                    if(!wrapper.isBuggy()) {
                        List<ASTWrapper> mutants;
                        if(GUIDED_RANDOM_TESTING) {
                            mutants = wrapper.guidedRandomTransformation();
                        } else {
                            if(MAIN_EXECUTION) {
                                mutants = wrapper.mainTransform();
                            } else {
                                mutants = wrapper.pureRandomTransformation();
                            }
                        }

                        wrappers.addAll(mutants);
                    }
                } else {
                    wrappers.addFirst(wrapper);
                    currentDepth += 1;
                    break;
                }
            }
            // detect mutants of iter i
            String resultFilePath = PMDResultFolder.getAbsolutePath() + sep + "iter" + i + "_" + seedFolderName + "_Result.json";
            String mutantFolderPath = mutantFolder + sep + "iter" + i + sep + seedFolderName;
            String[] pmdConfig = {
                    "-d", mutantFolderPath,
                    "-R", "category/java/" + this.ruleCategory + ".xml/" + this.ruleType,
                    "-f", "json",
                    "-r", resultFilePath };
            PMD.runPmd(pmdConfig);
            List<PMD_Report> reports = readPMDResultFile(resultFilePath);
            for (PMD_Report report : reports) {
                if(!file2line.containsKey(report.getFilename())) {
                    file2line.put(report.getFilename(), new HashSet<>());
                    file2bugs.put(report.getFilename(), new HashMap<>());
                }
                for (PMD_Violation violation : report.getViolations()) {
                    file2line.get(report.getFilename()).add(violation.beginLine);
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilename());
                    if(!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
            }
            //
            List<ASTWrapper> validWrappers = new ArrayList<>();
            while(!wrappers.isEmpty()) {
                ASTWrapper head = wrappers.pollFirst();
                if(!head.isBuggy()) {
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }

}
