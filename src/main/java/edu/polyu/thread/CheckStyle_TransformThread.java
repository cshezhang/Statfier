package edu.polyu.thread;

import edu.polyu.analysis.ASTWrapper;
import edu.polyu.report.CheckStyle_Report;
import edu.polyu.report.CheckStyle_Violation;
import edu.polyu.util.Util;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.TS_Selection;
import static edu.polyu.util.Invoker.invokeCommands;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.CheckStyleConfigPath;
import static edu.polyu.util.Util.CheckStylePath;
import static edu.polyu.util.Util.CheckStyleResultFolder;
import static edu.polyu.util.Util.GUIDED_LOCATION;
import static edu.polyu.util.Util.NO_SELECTION;
import static edu.polyu.util.Util.RANDOM_LOCATION;
import static edu.polyu.util.Util.RANDOM_SELECTION;
import static edu.polyu.util.Util.SEARCH_DEPTH;
import static edu.polyu.util.Util.TS_SELECTION;
import static edu.polyu.util.Util.file2bugs;
import static edu.polyu.util.Util.file2report;
import static edu.polyu.util.Util.file2row;
import static edu.polyu.util.Util.getFilenamesFromFolder;
import static edu.polyu.util.Util.mutantFolder;
import static edu.polyu.util.Util.readCheckStyleResultFile;

public class CheckStyle_TransformThread implements Runnable {

    private int currentDepth;
    private String seedFolderName; // equal to rule type
    private ArrayDeque<ASTWrapper> wrappers;
    private int configIndex;

    public CheckStyle_TransformThread(ASTWrapper initWrapper, String seedFolderName, int configIndex) {
        this.currentDepth = 0;
        this.seedFolderName = seedFolderName;
        this.wrappers = new ArrayDeque<>() {
            {
                add(initWrapper);
            }
        };
        this.configIndex = configIndex;
    }

    // iter 1 -> SEARCH_DEPTH: 1. transformation to generate mutant; 2. invoke PMD to detect bugs
    @Override
    public void run() {
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            if(Util.DEBUG) {
                System.out.println("Depth: " + depth + " Folder: " + this.seedFolderName);
            }
            while (!wrappers.isEmpty()) {
                ASTWrapper wrapper = wrappers.pollFirst();
                if (wrapper.depth == currentDepth) {
                    if (!wrapper.isBuggy()) { // Insert to queue only wrapper is not buggy
                        List<ASTWrapper> mutants = new ArrayList<>();
                        if (GUIDED_LOCATION) {
                            mutants = wrapper.TransformByGuidedLocation();
                        } else if (RANDOM_LOCATION) {
                            mutants = wrapper.TransformByRandomLocation();
                        }
                        if(NO_SELECTION) {
                            wrappers.addAll(mutants);
                        }
                        if(RANDOM_SELECTION) {
                            wrappers.addAll(Random_Selection(mutants));
                        }
                        if(TS_SELECTION) {
                            wrappers.addAll(TS_Selection(mutants));
                        }
                    }
                } else {
                    wrappers.addFirst(wrapper); // The last wrapper in current depth
                    currentDepth += 1;
                    break;
                }
            }
            // detect mutants of iter i
            String mutantFolderPath = mutantFolder + File.separator + "iter" + depth + File.separator + seedFolderName;
            List<String> mutantFilePaths = getFilenamesFromFolder(mutantFolderPath, true);
            String configPath = CheckStyleConfigPath + File.separator + seedFolderName + configIndex + ".xml";
            List<CheckStyle_Report> reports = new ArrayList<>();
            // 这里还可以做一个configIndex是否match seedFolderName里边的index
            for(String mutantFilePath : mutantFilePaths) {
                String reportFilePath = CheckStyleResultFolder + File.separator + "iter" + depth + "_" + seedFolderName + configIndex + ".xml";
//            String[] invokeCmds = {"/bin/bash", "-c",
                String[] invokeCmds = {"cmd.exe", "/c",  // Windows
                        "java -jar " + CheckStylePath + " -f" + " plain" + " -o " + reportFilePath + " -c "
                                + configPath + " " + mutantFilePath};
                invokeCommandsByZT(invokeCmds);
                reports.addAll(readCheckStyleResultFile(reportFilePath));
            }
            for (CheckStyle_Report report : reports) {
                file2report.put(report.getFilepath(), report);
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (CheckStyle_Violation violation : report.getViolations()) {
                    file2row.get(report.getFilepath()).add(violation.getBeginLine());
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
