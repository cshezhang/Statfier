package org.detector.thread;

import org.detector.analysis.TypeWrapper;
import org.detector.util.OSUtil;
import org.detector.util.Invoker;
import org.detector.util.Utility;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.classFolder;
import static org.detector.util.Utility.inferJarStr;
import static org.detector.util.Utility.reportFolder;
import static org.detector.transform.Transform.singleLevelExplorer;

/**
 * Description: Infer Transformation Thread
 * Author: RainyD4y
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

    /* Iteration process from 1 to SEARCH_DEPTH
    1. Program transform to generate mutant;
    2. Invoke static analyzer to detect bugs;
     */
    @Override
    public void run() {
        for (int depth = 1; depth <= Utility.SEARCH_DEPTH; depth++) {
            singleLevelExplorer(this.wrappers, this.currentDepth++);
            String mutantFolderPath = Utility.mutantFolder + File.separator + "iter" + depth;
            List<String> filePaths = Utility.getFilenamesFromFolder(mutantFolderPath, true);
            if(DEBUG) {
                System.out.println("Seed FolderName: " + this.seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
                System.out.println(this.getName() + "-" + "Begin to Read Reports and File Size: " + filePaths.size());
                System.out.println("Mutant Folder: " + mutantFolderPath);
            }
            for(int i = 0; i < filePaths.size(); i++) {
                String srcJavaPath = filePaths.get(i);
                String filename = Utility.Path2Last(srcJavaPath);
                String reportFolderPath = reportFolder + File.separator + "iter" + depth + "_" + filename;
                String cmd = Utility.INFER_PATH + " run -o " + "" + reportFolderPath + " -- " + Utility.JAVAC_PATH +
                        " -d " + classFolder.getAbsolutePath() + File.separator + filename +
                        " -cp " + inferJarStr + " " + srcJavaPath;
                if(DEBUG) {
                    System.out.println(this.getName() + "-" + cmd);
                }
                String[] invokeCommands = new String[3];
                if(OSUtil.isWindows()) {
                    invokeCommands[0] = "cmd.exe";
                    invokeCommands[1] = "/c";
                } else {
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                }
                invokeCommands[2] = "python3 cmd.py " + cmd;
                Invoker.invokeCommandsByZT(invokeCommands);
                String resultFilePath = reportFolderPath + File.separator + "report.json";
                Utility.readInferResultFile(srcJavaPath, resultFilePath);
            }
            List<TypeWrapper> validWrappers = new ArrayList<>();
            while (!wrappers.isEmpty()) {
                TypeWrapper head = wrappers.pollFirst();
                if (!head.isBuggy()) { // if this mutant is buggy, then we should switch to next mutant
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }

}
