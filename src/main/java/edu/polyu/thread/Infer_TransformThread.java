package edu.polyu.thread;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.transform.Transform;
import edu.polyu.util.OSUtil;
import edu.polyu.util.Invoker;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static edu.polyu.report.Infer_Report.readSingleInferResultFile;
import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.CLASS_FOLDER;
import static edu.polyu.util.Utility.INFER_PATH;
import static edu.polyu.util.Utility.JAVAC_PATH;
import static edu.polyu.util.Utility.MUTANT_FOLDER;
import static edu.polyu.util.Utility.Path2Last;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.getFilenamesFromFolder;
import static edu.polyu.util.Utility.inferJarStr;
import static edu.polyu.util.Utility.REPORT_FOLDER;
import static edu.polyu.transform.Transform.singleLevelExplorer;
import static edu.polyu.util.Utility.sep;

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
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            Transform.singleLevelExplorer(this.wrappers, this.currentDepth++);
            String MUTANT_FOLDERPath = MUTANT_FOLDER + sep + "iter" + depth;
            List<String> filePaths = getFilenamesFromFolder(MUTANT_FOLDERPath, true);
            if(DEBUG) {
                System.out.println("Seed FolderName: " + this.seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
                System.out.println(this.getName() + "-" + "Begin to Read Reports and File Size: " + filePaths.size());
                System.out.println("Mutant Folder: " + MUTANT_FOLDERPath);
            }
            for(int i = 0; i < filePaths.size(); i++) {
                String srcJavaPath = filePaths.get(i);
                String filename = Path2Last(srcJavaPath);
                String reportFolderPath = REPORT_FOLDER + sep + "iter" + depth + "_" + filename;
                String cmd = INFER_PATH + " run -o " + "" + reportFolderPath + " -- " + JAVAC_PATH +
                        " -d " + CLASS_FOLDER.getAbsolutePath() + sep + filename +
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
                String resultFilePath = reportFolderPath + sep + "report.json";
                readSingleInferResultFile(srcJavaPath, resultFilePath);
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
