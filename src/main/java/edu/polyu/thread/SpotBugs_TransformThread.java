package edu.polyu.thread;

import edu.polyu.analysis.ASTWrapper;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.report.SpotBugs_Violation;
import edu.polyu.util.OSUtil;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.analysis.SelectionAlgorithm.Div_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.util.Invoker.compileJavaSourceFile;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.GUIDED_LOCATION;
import static edu.polyu.util.Util.NO_SELECTION;
import static edu.polyu.util.Util.RANDOM_LOCATION;
import static edu.polyu.util.Util.RANDOM_SELECTION;
import static edu.polyu.util.Util.SEARCH_DEPTH;
import static edu.polyu.util.Util.SINGLE_TESTING;
import static edu.polyu.util.Util.SpotBugsClassFolder;
import static edu.polyu.util.Util.SpotBugsPath;
import static edu.polyu.util.Util.SpotBugsResultFolder;
import static edu.polyu.util.Util.DIV_SELECTION;
import static edu.polyu.util.Util.file2bugs;
import static edu.polyu.util.Util.file2row;

import static edu.polyu.util.Util.readSpotBugsResultFile;
import static edu.polyu.util.Util.sep;

public class SpotBugs_TransformThread implements Runnable {

    private int currentDepth;
    private ArrayDeque<ASTWrapper> wrappers;

    // initWrappers contains different seedFolderPaths and seedFolderNames, so we can get them from wrappers
    public SpotBugs_TransformThread(List<ASTWrapper> initWrappers) {
        this.currentDepth = 0;
        this.wrappers = new ArrayDeque<>() {
            {
                addAll(initWrappers);
            }
        };
    }

    @Override
    public void run() {
        // initWrapper: -> iter1 mutants -> transform -> compile -> detect -> iter2 mutants...
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            if (SINGLE_TESTING) {
                System.out.println("TransformThread Depth: " + depth);
            }
            while (!wrappers.isEmpty()) {
                ASTWrapper wrapper = wrappers.pollFirst();
                if (wrapper.depth == currentDepth) {
                    if (!wrapper.isBuggy()) {
                        List<ASTWrapper> mutants = new ArrayList<>();
                        if (GUIDED_LOCATION) {
                            mutants = wrapper.TransformByGuidedLocation();
                        } else if (RANDOM_LOCATION) {
                            mutants = wrapper.TransformByRandomLocation();
                        }
                        if (NO_SELECTION) {
                            wrappers.addAll(mutants);
                        }
                        if (RANDOM_SELECTION) {
                            wrappers.addAll(Random_Selection(mutants));
                        }
                        if (DIV_SELECTION) {
                            wrappers.addAll(Div_Selection(mutants));
                        }
                    }
                } else {
                    wrappers.addFirst(wrapper);
                    currentDepth += 1;
                    break;
                }
            }
            List<SpotBugs_Report> reports = new ArrayList<>();
            for (ASTWrapper wrapper : wrappers) {
                String seedFilePath = wrapper.getFilePath();
                String seedFolderPath = wrapper.getFolderPath();
                String[] tokens = seedFilePath.split(sep);
                String seedFileNameWithSuffix = tokens[tokens.length - 1];
                String subSeedFolderName = tokens[tokens.length - 2];
                String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                // Filename is used to specify class folder name
                File classFolder = new File(SpotBugsClassFolder.getAbsolutePath() + File.separator + seedFileName);
                if (!classFolder.exists()) {
                    classFolder.mkdirs();
                }
                compileJavaSourceFile(seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
                String reportPath = SpotBugsResultFolder.getAbsolutePath() + File.separator + subSeedFolderName + File.separator + seedFileName + "_Result.xml";
                String[] invokeCmds = new String[3];
                if (OSUtil.isWindows()) {
                    invokeCmds[0] = "cmd.exe";
                    invokeCmds[1] = "/c";
                } else {
                    invokeCmds[0] = "/bin/bash";
                    invokeCmds[1] = "-c";
                }
                invokeCmds[2] = SpotBugsPath + " -textui"
//                            + " -include " + configPath
                        + " -xml:withMessages" + " -output " + reportPath + " "
                        + classFolder.getAbsolutePath();
                boolean hasExec = invokeCommandsByZT(invokeCmds);
                if (hasExec) {
                    String report_path = SpotBugsResultFolder.getAbsolutePath() + File.separator + subSeedFolderName + File.separator + seedFileName + "_Result.xml";
                    reports.addAll(readSpotBugsResultFile(wrapper.getFolderPath(), report_path));
                }
            }
            for (SpotBugs_Report report : reports) {
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (SpotBugs_Violation violation : report.getViolations()) {
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
                if (!head.isBuggy()) {
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }
}
