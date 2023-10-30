package edu.polyu.thread;

import edu.polyu.util.Invoker;
import edu.polyu.util.Utility;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.CLASS_FOLDER;
import static edu.polyu.util.Utility.inferJarStr;
import static edu.polyu.util.Utility.REPORT_FOLDER;

/**
 * @Description:
 * @Author: RainyD4y
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
        List<String> filePaths = Utility.getFilenamesFromFolder(seedFolderPath + File.separator + seedFolderName, true);
        if(DEBUG) {
            System.out.println("InvokeT Path:" + seedFolderPath + " Name:" + seedFolderName + " Size:" + filePaths.size());
        }
        for(int i = 0; i < filePaths.size(); i++) {
            String srcJavaPath = filePaths.get(i);
            String filename = Utility.Path2Last(srcJavaPath);
            String REPORT_FOLDERPath = REPORT_FOLDER + File.separator + "iter" + iterDepth + "_" + filename;
            String cmd = "\"" + Utility.INFER_PATH + " run -o " + REPORT_FOLDERPath + " -- " + Utility.JAVAC_PATH +
                    " -d " + CLASS_FOLDER.getAbsolutePath() + File.separator + filename +
                    " -cp " + inferJarStr + " " + srcJavaPath + "\"";
            String[] invokeCmds = new String[3];
            invokeCmds[0] = "/bin/bash";
            invokeCmds[1] = "-c";
            invokeCmds[2] = "python3 cmd.py " + cmd;
            File file = new File(REPORT_FOLDERPath);
            file.mkdir();
            Invoker.invokeCommandsByZT(invokeCmds);
        }
    }

}
