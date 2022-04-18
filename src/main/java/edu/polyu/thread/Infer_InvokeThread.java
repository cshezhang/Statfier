package edu.polyu.thread;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Invoker.invokeCommands;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.*;

/**
 * @Description:
 * @Author: Vanguard
 * @Date: 2022-04-13 13:09
 */
public class Infer_InvokeThread implements Runnable {

    private int iterDepth;
    private String seedFolderPath;
    private String seedFolderName; // equal to rule type


    public Infer_InvokeThread(int iterDepth, String seedFolderPath, String seedFolderName) {
        this.iterDepth = iterDepth;
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
        if(SINGLE_TESTING) {
            System.out.println("---Infer InvokeThread---");
            System.out.println("Iter Depth: " + iterDepth);
            System.out.println("Seed FolderPath: " + seedFolderPath);
            System.out.println("Seed FolderName: " + seedFolderName);
            System.out.println("------");
        }
    }

    // seedFolderPath can be java source file or a folder contains source files
    @Override
    public void run() {
        List<String> filepaths = getFilenamesFromFolder(seedFolderPath + File.separator + seedFolderName, true);
        for(int i = 0; i < filepaths.size(); i++) {
            String srcJavaPath = filepaths.get(i);
            String filename = Path2Last(srcJavaPath);
            String reportFolderPath = InferResultFolder + File.separator + "iter" + iterDepth + "_" + filename;
            String cmd = InferPath + " run -o " + reportFolderPath + " -- " + JAVAC_PATH +
                    " -d " + InferClassFolder.getAbsolutePath() + File.separator + filename +
                    " -cp " + inferJarStr + " " + srcJavaPath;
            String[] invokeCmds = {"/bin/bash", "-c", "python3 cmd.py " + cmd};
//            String[] invokeCmds = {"/bin/bash", "-c",
//                    "\"" + InferPath + " run -o " + reportFolderPath +
//                    " -- javac -d " + InferClassFolder.getAbsolutePath() + File.separator + filename +
//                    " -cp " + inferJarStr + " " + srcJavaPath + "\""};
            invokeCommandsByZT(invokeCmds);
        }
    }

}
