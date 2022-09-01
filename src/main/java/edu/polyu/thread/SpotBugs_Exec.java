package edu.polyu.thread;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.util.OSUtil;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static edu.polyu.analysis.SelectionAlgorithm.Div_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.util.Invoker.compileJavaSourceFile;
import static edu.polyu.util.Invoker.invokeCommands;
import static edu.polyu.util.Utility.DIV_SELECTION;
import static edu.polyu.util.Utility.GUIDED_LOCATION;
import static edu.polyu.util.Utility.NO_SELECTION;
import static edu.polyu.util.Utility.RANDOM_LOCATION;
import static edu.polyu.util.Utility.RANDOM_SELECTION;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.SpotBugsClassFolder;
import static edu.polyu.util.Utility.SpotBugsPath;
import static edu.polyu.util.Utility.SpotBugsResultFolder;
import static edu.polyu.util.Utility.readSpotBugsResultFile;
import static edu.polyu.util.Utility.sep;

public class SpotBugs_Exec {

    public static void run(List<TypeWrapper> initWrappers) {
        // initWrapper: -> iter1 mutants -> transform -> compile -> detect -> iter2 mutants...
        for(TypeWrapper initWrapper : initWrappers) {
            int currentDepth = 0;
            ArrayDeque<TypeWrapper> wrappers = new ArrayDeque<>();
            wrappers.add(initWrapper);
            for (int iter = 1; iter <= SEARCH_DEPTH; iter++) {
                while (!wrappers.isEmpty()) {
                    TypeWrapper wrapper = wrappers.pollFirst();
                    if (wrapper.depth == currentDepth) {
                        if (!wrapper.isBuggy()) {
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
                        wrappers.addFirst(wrapper);
                        currentDepth += 1;
                        break;
                    }
                }
                for (TypeWrapper tmpWrapper : wrappers) {
                    String seedFilePath = tmpWrapper.getFilePath();
                    String seedFolderPath = tmpWrapper.getFolderPath();
                    String[] tokens = seedFilePath.split(sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String subSeedFolderName = tokens[tokens.length - 2];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // Filename is used to specify class folder name
                    File classFolder = new File(SpotBugsClassFolder.getAbsolutePath()  + File.separator + seedFileName);
                    if (!classFolder.exists()) {
                        classFolder.mkdirs();
                    }
                    compileJavaSourceFile(seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
                    String reportPath = SpotBugsResultFolder.getAbsolutePath() + File.separator + subSeedFolderName + File.separator + seedFileName + "_Result.xml";
                    String[] invokeCmds = new String[3];
                    if(OSUtil.isWindows()) {
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
                    invokeCommands(invokeCmds);
                    String report_path = SpotBugsResultFolder.getAbsolutePath() + File.separator + subSeedFolderName + File.separator + seedFileName + "_Result.xml";
                    readSpotBugsResultFile(tmpWrapper.getFolderPath(), report_path);
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

}
