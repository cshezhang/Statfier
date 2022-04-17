package edu.polyu.thread;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.InferPath;
import static edu.polyu.util.Util.InferResultFolder;
import static edu.polyu.util.Util.Path2Last;
import static edu.polyu.util.Util.getFilenamesFromFolder;

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
    }

    // seedFolderPath can be java source file or a folder contains source files
    @Override
    public void run() {
        List<String> filepaths = getFilenamesFromFolder(seedFolderPath + File.separator + seedFolderName, true);
        for(int i = 0; i < filepaths.size(); i++) {
            String srcJavaPath = filepaths.get(i);
            String filename = Path2Last(srcJavaPath).split(".")[0];
            String reportFolderPath = InferResultFolder + File.separator + "iter" + iterDepth + "_" + filename;
//            String[] invokeCmds = {"/bin/bash", "-c",  // Linux
            String[] invokeCmds = {"cmd.exe", "/c",  // Windows
                    InferPath + " run -o " + "" + reportFolderPath + " -- javac " + srcJavaPath};
            invokeCommandsByZT(invokeCmds);
        }
    }

}