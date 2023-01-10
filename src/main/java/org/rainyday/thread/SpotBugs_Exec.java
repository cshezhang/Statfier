package org.rainyday.thread;

import org.rainyday.analysis.TypeWrapper;
import org.rainyday.util.OSUtil;
import org.rainyday.analysis.SelectionAlgorithm;
import org.rainyday.util.Invoker;
import org.rainyday.util.Utility;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.rainyday.util.Utility.DIV_SELECTION;
import static org.rainyday.util.Utility.GUIDED_LOCATION;
import static org.rainyday.util.Utility.NO_SELECTION;
import static org.rainyday.util.Utility.RANDOM_LOCATION;
import static org.rainyday.util.Utility.RANDOM_SELECTION;
import static org.rainyday.util.Utility.SEARCH_DEPTH;
import static org.rainyday.util.Utility.SpotBugsPath;
import static org.rainyday.util.Utility.readSpotBugsResultFile;
import static org.rainyday.util.Utility.reg_sep;
import static org.rainyday.util.Utility.reportFolder;
import static org.rainyday.util.Utility.sep;

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
                                wrappers.addAll(SelectionAlgorithm.Random_Selection(mutants));
                            }
                            if(DIV_SELECTION) {
                                wrappers.addAll(SelectionAlgorithm.Div_Selection(mutants));
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
                    String[] tokens = seedFilePath.split(reg_sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String subSeedFolderName = tokens[tokens.length - 2];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // Filename is used to specify class folder name
                    File classFolder = new File(Utility.classFolder.getAbsolutePath()  + File.separator + seedFileName);
                    if (!classFolder.exists()) {
                        classFolder.mkdirs();
                    }
                    Invoker.compileJavaSourceFile(seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
                    String reportPath = reportFolder.getAbsolutePath() + File.separator + subSeedFolderName + File.separator + seedFileName + "_Result.xml";
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
                    Invoker.invokeCommandsByZT(invokeCmds);
                    String report_path = reportFolder.getAbsolutePath() + File.separator + subSeedFolderName + File.separator + seedFileName + "_Result.xml";
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
