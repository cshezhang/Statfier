package edu.polyu.thread;

import edu.polyu.ASTWrapper;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;

import java.util.*;

import static edu.polyu.Invoker.invokeCommands;
import static edu.polyu.Util.*;
import static edu.polyu.Util.sep;

public class MutationThread implements Runnable {

    private int currentDepth;
    private String seedFolderName;
    private String ruleCategory;
    private String ruleType;
    private ArrayDeque<ASTWrapper> wrappers;

    public MutationThread(List<ASTWrapper> initWrappers, String seedFolderName) {
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

    @Override
    public void run() {
        for(int i = 1; i <= SEARCH_DEPTH; i++) {
            while(!wrappers.isEmpty()) {
                ASTWrapper wrapper = wrappers.pollFirst();
                if(wrapper.depth == 1) {
                    int a = 10;
                }
                if (wrapper.depth == currentDepth) {
                    if(!wrapper.isBuggy()) {
                        List<ASTWrapper> mutants = wrapper.mainTransform();
                        wrappers.addAll(mutants);
                    }
                } else {
                    wrappers.addFirst(wrapper);
                    currentDepth += 1;
                    break;
                }
            }
            // detect mutants of iter i
            String resultFilePath = PMDResultsFolder.getAbsolutePath() + sep + "iter" + i + "_" + seedFolderName + "_Result.json";
            String mutantFolderPath = mutantFolder + sep + "iter" + i + sep + seedFolderName;
            String pmdConfig = pmdPath
                    + " -d " + mutantFolderPath
                    + " -R " + "category/java/" + this.ruleCategory + ".xml/" + this.ruleType
                    + " -f " + "json"
                    + " -r " + resultFilePath;
            String[] pmdArgs = {"cmd", "/k", pmdConfig};
//            String[] pmdArgs = {"/bin/sh", "-c", pmdConfig};  // Linux Command
            invokeCommands(pmdArgs);
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
