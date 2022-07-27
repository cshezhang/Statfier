package edu.polyu.thread;

import edu.polyu.util.OSUtil;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Utility.CheckStyleConfigPath;
import static edu.polyu.util.Utility.CheckStylePath;
import static edu.polyu.util.Utility.CheckStyleResultFolder;
import static edu.polyu.util.Utility.SINGLE_TESTING;
import static edu.polyu.util.Utility.getFilenamesFromFolder;

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
        List<String> filepaths = getFilenamesFromFolder(seedFolderPath + File.separator + seedFolderName, true);
        for(int i = 0; i < filepaths.size(); i++) {
            String filepath = filepaths.get(i);
//            System.out.println(filepath);
//            int i = Integer.parseInt(Path2Last(filepath));
//            String configPath = CheckStyleConfigPath + File.separator + "google_checks.xml";
            String configPath = CheckStyleConfigPath + File.separator + seedFolderName + i + ".xml";
            File configFile = new File(configPath);
            if(configFile.exists()) {
                configPath = CheckStyleConfigPath + File.separator + seedFolderName + 0 + ".xml";
            }
            String reportPath = CheckStyleResultFolder + File.separator + "iter" + iterDepth + "_" + seedFolderName + i + "_Result.xml";
            String[] invokeCmds = new String[3];
            if(OSUtil.isWindows()) {
                invokeCmds[0] = "cmd.exe";
                invokeCmds[1] = "/c";
            } else {
                invokeCmds[0] = "/bin/bash";
                invokeCmds[1] = "-c";
            }
            invokeCmds[2] = "java -jar " + CheckStylePath + " -f" + " plain" + " -o " + reportPath + " -c " + configPath +  " " + filepath;
            if(SINGLE_TESTING) {
                System.out.println(invokeCmds[2]);
            }
            invokeCommandsByZT(invokeCmds);
        }
    }

}
