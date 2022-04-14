package edu.polyu.thread;

import edu.polyu.analysis.ASTWrapper;
import edu.polyu.report.Infer_Report;
import edu.polyu.report.Infer_Violation;
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
import static edu.polyu.analysis.SelectionAlgorithm.TS_Selection;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.GUIDED_LOCATION;
import static edu.polyu.util.Util.InferPath;
import static edu.polyu.util.Util.InferResultFolder;
import static edu.polyu.util.Util.NO_SELECTION;
import static edu.polyu.util.Util.PMDResultFolder;
import static edu.polyu.util.Util.Path2Last;
import static edu.polyu.util.Util.RANDOM_LOCATION;
import static edu.polyu.util.Util.RANDOM_SELECTION;
import static edu.polyu.util.Util.SEARCH_DEPTH;
import static edu.polyu.util.Util.TS_SELECTION;
import static edu.polyu.util.Util.file2bugs;
import static edu.polyu.util.Util.file2report;
import static edu.polyu.util.Util.file2row;
import static edu.polyu.util.Util.getFilenamesFromFolder;
import static edu.polyu.util.Util.mutantFolder;
import static edu.polyu.util.Util.readInferResultFile;
import static edu.polyu.util.Util.readPMDResultFile;


/**
 * Description: Infer Transformation Thread
 * Author: Vanguard
 * Date: 2022/2/4 1:23 PM
 */
public class Infer_TransformThread implements Runnable {

    private int currentDepth;
    private String seedFolderName;
    private ArrayDeque<ASTWrapper> wrappers;

    public Infer_TransformThread(List<ASTWrapper> initWrappers, String seedFolderName) {
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
            String resultFolderPath = InferResultFolder.getAbsolutePath() + File.separator + "iter" + depth + "_" + seedFolderName;
            String mutantFolderPath = mutantFolder + File.separator + "iter" + depth + File.separator + seedFolderName;
            List<String> filepaths = getFilenamesFromFolder(mutantFolderPath, true);
            for(int i = 0; i < filepaths.size(); i++) {
                String srcJavaPath = filepaths.get(i);
                String filename = Path2Last(srcJavaPath).split(".")[0];
                String reportFolderPath = InferResultFolder + File.separator + "iter" + depth + "_" + filename;
//            String[] invokeCmds = {"/bin/bash", "-c",  // Linux
                String[] invokeCmds = {"cmd.exe", "/c",  // Windows
                        InferPath + " run -o " + "" + reportFolderPath + " -- javac " + srcJavaPath};
                invokeCommandsByZT(invokeCmds);
            }
            String resultFilePath = resultFolderPath + File.separator + "result.json";
            List<Infer_Report> reports = readInferResultFile(depth, resultFilePath);
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
            List<ASTWrapper> validWrappers = new ArrayList<>();
            while (!wrappers.isEmpty()) {
                ASTWrapper head = wrappers.pollFirst();
                // Used for debugging
//                String file = head.filename;
//                String content = head.getCode();
//                int cnt = head.getViolations();
                if (!head.isBuggy()) { // if this mutant is buggy, then we should switch to next mutant
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }

}
