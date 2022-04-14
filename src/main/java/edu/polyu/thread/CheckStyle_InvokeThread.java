package edu.polyu.thread;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.CheckStyleConfigPath;
import static edu.polyu.util.Util.CheckStylePath;
import static edu.polyu.util.Util.CheckStyleResultFolder;
import static edu.polyu.util.Util.getFilenamesFromFolder;

public class CheckStyle_InvokeThread implements Runnable {
    private int iterDepth;
    private String seedFolderPath;
    private String seedFolderName; // equal to rule type


    public CheckStyle_InvokeThread(int iterDepth, String seedFolderPath, String seedFolderName) {
        this.iterDepth = iterDepth;
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
    }

    // seedFolderPath can be java source file or a folder contains source files
    @Override
    public void run() {
        List<String> filenames = getFilenamesFromFolder(seedFolderPath + File.separator + seedFolderName, true);
        for(int i = 0; i < filenames.size(); i++) {
            String configPath = CheckStyleConfigPath + File.separator + seedFolderName + i + ".xml";
            String srcJavaPath = filenames.get(i);
            String reportPath = CheckStyleResultFolder + File.separator + "iter" + iterDepth + "_" + seedFolderName + i + "_Result.xml";
//            String[] invokeCmds = {"/bin/bash", "-c",  // Linux
            String[] invokeCmds = {"cmd.exe", "/c",  // Windows
                    "java -jar " + CheckStylePath + " -f" + " plain" + " -o " + reportPath + " -c "
                            + configPath +  " " + srcJavaPath};
            invokeCommandsByZT(invokeCmds);
        }
    }

}
