package edu.polyu.thread;

import edu.polyu.analysis.TypeWrapper;
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
import static edu.polyu.analysis.TypeWrapper.mutant2seed;
import static edu.polyu.analysis.TypeWrapper.mutant2seq;
import static edu.polyu.transform.Transform.singleLevelExplorer;
import static edu.polyu.util.Invoker.compileJavaSourceFile;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Utility.COMPILE;
import static edu.polyu.util.Utility.GUIDED_LOCATION;
import static edu.polyu.util.Utility.NO_SELECTION;
import static edu.polyu.util.Utility.RANDOM_LOCATION;
import static edu.polyu.util.Utility.RANDOM_SELECTION;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.SINGLE_TESTING;
import static edu.polyu.util.Utility.SpotBugsClassFolder;
import static edu.polyu.util.Utility.SpotBugsPath;
import static edu.polyu.util.Utility.SpotBugsResultFolder;
import static edu.polyu.util.Utility.DIV_SELECTION;
import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2row;

import static edu.polyu.util.Utility.readSpotBugsResultFile;
import static edu.polyu.util.Utility.sep;

public class SpotBugs_TransformThread implements Runnable {

    private int currentDepth;
    private ArrayDeque<TypeWrapper> wrappers;

    // initWrappers contains different seedFolderPaths and seedFolderNames, so we can get them from wrappers
    public SpotBugs_TransformThread(List<TypeWrapper> initWrappers) {
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
            singleLevelExplorer(this.wrappers, this.currentDepth++);
            for (TypeWrapper wrapper : wrappers) {
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
                    readSpotBugsResultFile(wrapper.getFolderPath(), report_path);
                }
            }
            List<TypeWrapper> validWrappers = new ArrayList<>();
            while (!wrappers.isEmpty()) {
                TypeWrapper head = wrappers.pollFirst();
                if (!head.isBuggy()) {
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }
}
