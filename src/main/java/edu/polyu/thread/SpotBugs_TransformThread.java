package edu.polyu.thread;

import edu.polyu.ASTWrapper;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.report.SpotBugs_Violation;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.Invoker.compileJavaSourceFile;
import static edu.polyu.Invoker.invokeCommands;
import static edu.polyu.Util.GUIDED_RANDOM_TESTING;
import static edu.polyu.Util.MAIN_EXECUTION;
import static edu.polyu.Util.SEARCH_DEPTH;
import static edu.polyu.Util.SpotBugsClassFolder;
import static edu.polyu.Util.SpotBugsPath;
import static edu.polyu.Util.SpotBugsResultFolder;
import static edu.polyu.Util.file2bugs;
import static edu.polyu.Util.file2line;

import static edu.polyu.Util.readSpotBugsResultFile;
import static edu.polyu.Util.sep;

public class SpotBugs_TransformThread implements Runnable {

    private int currentDepth;
    private ArrayDeque<ASTWrapper> initWrappers;

    // initWrappers contains different seedFolderPaths and seedFolderNames, so we can get them from wrappers
    public SpotBugs_TransformThread(List<ASTWrapper> initWrappers) {
        this.currentDepth = 0;
        this.initWrappers = new ArrayDeque<>() {
            {
                addAll(initWrappers);
            }
        };
    }

    @Override
    public void run() {
        // initWrapper: -> iter1 mutants -> transform -> compile -> detect -> iter2 mutants...
        for(ASTWrapper initWrapper : this.initWrappers) {
            ArrayDeque<ASTWrapper> tmpWrappers = new ArrayDeque<>(64);
            tmpWrappers.add(initWrapper);
            for (int iter = 1; iter <= SEARCH_DEPTH; iter++) {
                while (!tmpWrappers.isEmpty()) {
                    ASTWrapper wrapper = tmpWrappers.pollFirst();
                    if (wrapper.depth == currentDepth) {
                        if (!wrapper.isBuggy()) {
                            List<ASTWrapper> mutants;
                            if (GUIDED_RANDOM_TESTING) {
                                mutants = wrapper.guidedRandomTransformation();
                            } else {
                                if (MAIN_EXECUTION) {
                                    mutants = wrapper.mainTransform();
                                } else {
                                    mutants = wrapper.pureRandomTransformation();
                                }
                            }
                            tmpWrappers.addAll(mutants);
                        }
                    } else {
                        tmpWrappers.addFirst(wrapper);
                        currentDepth += 1;
                        break;
                    }
                }
                List<SpotBugs_Report> reports = new ArrayList<>();
                for (ASTWrapper tmpWrapper : tmpWrappers) {
                    String seedFilePath = tmpWrapper.getFilePath();
                    String seedFolderPath = tmpWrapper.getFolderPath();
                    String[] tokens = seedFilePath.split(sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // Filename is used to specify class folder name
                    File classFolder = new File(SpotBugsClassFolder.getAbsolutePath() + sep + seedFileName);
                    if (!classFolder.exists()) {
                        classFolder.mkdirs();
                    }
                    compileJavaSourceFile(seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
                    String reportPath = SpotBugsResultFolder.getAbsolutePath() + sep + seedFileName + "_Result.xml";
                    String[] invokeCmds = {"/bin/bash", "-c",
                            SpotBugsPath + " -textui" + " -xml:withMessages" + " -output " + reportPath + " "
                            + classFolder.getAbsolutePath()};
                    invokeCommands(invokeCmds);
                    String report_path = SpotBugsResultFolder.getAbsolutePath() + sep + seedFileName + "_Result.xml";
                    reports.addAll(readSpotBugsResultFile(tmpWrapper.getFolderPath(), report_path));
                }
                for (SpotBugs_Report report : reports) {
                    if (!file2line.containsKey(report.getFilename())) {
                        file2line.put(report.getFilename(), new HashSet<>());
                        file2bugs.put(report.getFilename(), new HashMap<>());
                    }
                    for (SpotBugs_Violation violation : report.getViolations()) {
                        file2line.get(report.getFilename()).add(violation.getBeginLine());
                        HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilename());
                        if (!bug2cnt.containsKey(violation.getBugType())) {
                            bug2cnt.put(violation.getBugType(), new HashSet<>());
                        }
                        bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                    }
                }
                List<ASTWrapper> validWrappers = new ArrayList<>();
                while (!tmpWrappers.isEmpty()) {
                    ASTWrapper head = tmpWrappers.pollFirst();
                    if (!head.isBuggy()) {
                        validWrappers.add(head);
                    }
                }
                tmpWrappers.addAll(validWrappers);
            }
        }
    }

}
