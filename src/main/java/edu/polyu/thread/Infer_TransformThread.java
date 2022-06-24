package edu.polyu.thread;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.report.Infer_Report;
import edu.polyu.report.Infer_Violation;
import edu.polyu.util.OSUtil;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.Div_Selection;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.*;

/**
 * Description: Infer Transformation Thread
 * Author: Vanguard
 * Date: 2022/2/4 1:23 PM
 */
public class Infer_TransformThread extends Thread {

    private int currentDepth;
    private String seedFolderName;
    private ArrayDeque<TypeWrapper> wrappers;

    public Infer_TransformThread(List<TypeWrapper> initWrappers, String seedFolderName) {
        this.currentDepth = 0;
        this.seedFolderName = seedFolderName;
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
            System.out.println("Seed FolderName: " + this.seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
            while (!wrappers.isEmpty()) {
                TypeWrapper wrapper = wrappers.pollFirst();
                if (wrapper.depth == currentDepth) {
                    if (!wrapper.isBuggy()) { // Insert to queue only wrapper is not buggy
                        List<TypeWrapper> mutants = new ArrayList<>();
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
                        if(DIV_SELECTION) {
                            wrappers.addAll(Div_Selection(mutants));
                        }
                    }
                } else {
                    wrappers.addFirst(wrapper); // The last wrapper in current depth
                    currentDepth += 1;
                    break;
                }
            }
            // detect mutants of iter i
            String mutantFolderPath = mutantFolder + File.separator + "iter" + depth;
            List<String> filepaths = getFilenamesFromFolder(mutantFolderPath, true);
            List<Infer_Report> reports = new ArrayList<>();
            System.out.println(this.getName() + "-" + "Begin to Read Reports and File Size: " + filepaths.size());
            System.out.println("Mutant Folder: " + mutantFolderPath);
            for(int i = 0; i < filepaths.size(); i++) {
                String srcJavaPath = filepaths.get(i);
                String filename = Path2Last(srcJavaPath);
                String reportFolderPath = InferResultFolder + File.separator + "iter" + depth + "_" + filename;
                String cmd = InferPath + " run -o " + "" + reportFolderPath + " -- " + JAVAC_PATH +
                        " -d " + InferClassFolder.getAbsolutePath() + File.separator + filename +
                        " -cp " + inferJarStr + " " + srcJavaPath;
                System.out.println(this.getName() + "-" + cmd);
                String[] invokeCmds = new String[3];
                if(OSUtil.isWindows()) {
                    invokeCmds[0] = "cmd.exe";
                    invokeCmds[1] = "/c";
                } else {
                    invokeCmds[0] = "/bin/bash";
                    invokeCmds[1] = "-c";
                }
                invokeCmds[2] = "python3 cmd.py " + cmd;
                invokeCommandsByZT(invokeCmds);
                String resultFilePath = reportFolderPath + File.separator + "report.json";
                reports.addAll(readInferResultFile(srcJavaPath, resultFilePath));
            }
            System.out.println("Report Size: " + reports.size());
            for (Infer_Report report : reports) {
                file2report.put(report.getFilepath(), report);
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (Infer_Violation violation : report.getViolations()) {
                    file2row.get(report.getFilepath()).add(violation.getBeginLine());
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
            System.out.println("End Iteration!");
        }
    }

}
