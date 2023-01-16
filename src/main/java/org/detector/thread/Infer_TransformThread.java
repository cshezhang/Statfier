package org.detector.thread;

import org.detector.analysis.TypeWrapper;
import org.detector.report.Infer_Report;
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

    /* Iteration process from 1 to SEARCH_DEPTH
    1. Program transform to generate mutant;
    2. Invoke static analyzer to detect bugs;
     */
    @Override
    public void run() {
        for (int depth = 1; depth <= Utility.SEARCH_DEPTH; depth++) {
            if(DEBUG) {
                System.out.println("Seed FolderName: " + this.seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
            }
            singleLevelExplorer(this.wrappers, this.currentDepth++);
            String mutantFolderPath = Utility.mutantFolder + File.separator + "iter" + depth;
            List<String> filepaths = Utility.getFilenamesFromFolder(mutantFolderPath, true);
            List<Infer_Report> reports = new ArrayList<>();
            System.out.println(this.getName() + "-" + "Begin to Read Reports and File Size: " + filepaths.size());
            System.out.println("Mutant Folder: " + mutantFolderPath);
            for(int i = 0; i < filepaths.size(); i++) {
                String srcJavaPath = filepaths.get(i);
                String filename = Utility.Path2Last(srcJavaPath);
                String reportFolderPath = reportFolder + File.separator + "iter" + depth + "_" + filename;
                String cmd = Utility.INFER_PATH + " run -o " + "" + reportFolderPath + " -- " + Utility.JAVAC_PATH +
                        " -d " + classFolder.getAbsolutePath() + File.separator + filename +
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
                Invoker.invokeCommandsByZT(invokeCmds);
                String resultFilePath = reportFolderPath + File.separator + "report.json";
                Utility.readInferResultFile(srcJavaPath, resultFilePath);
            }
            System.out.println("Report Size: " + reports.size());
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
