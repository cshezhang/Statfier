package org.detector.thread;

import org.detector.util.OSUtil;
import org.detector.util.Invoker;
import org.detector.util.Utility;

import java.io.File;
import java.util.List;

import static org.detector.util.Schedule.file2config;
import static org.detector.util.Utility.CHECKSTYLE_CONFIG_PATH;
import static org.detector.util.Utility.CHECKSTYLE_PATH;
import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.reg_sep;
import static org.detector.util.Utility.reportFolder;
import static org.detector.util.Utility.sep;

public class CheckStyle_InvokeThread implements Runnable {
//    private int iterDepth;
    private String seedFolderPath;
    private String seedFolderName; // equal to rule type


    public CheckStyle_InvokeThread(int iterDepth, String seedFolderPath, String seedFolderName) {
//        this.iterDepth = iterDepth;
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
    }

    // seedFolderPath can be java source file or a folder contains source files
    @Override
    public void run() {
        List<String> filePaths = Utility.getFilenamesFromFolder(seedFolderPath + File.separator + seedFolderName, true);
        for(int i = 0; i < filePaths.size(); i++) {
            String filePath = filePaths.get(i);
            String[] tokens = filePath.split(reg_sep);
            int configIndex = Character.getNumericValue(tokens[tokens.length - 1].charAt(tokens[tokens.length - 1].indexOf(".") - 1));
            File configFile = new File(CHECKSTYLE_CONFIG_PATH + sep + seedFolderName + configIndex + ".xml");
            String configPath;
            if(configFile.exists()) {
                configPath = configFile.getAbsolutePath();
            } else {
                configPath = CHECKSTYLE_CONFIG_PATH + sep + seedFolderName + 0 + ".xml";
            }
            file2config.put(filePath, configPath);
            String reportPath = reportFolder + File.separator + "iter0" + "_" + seedFolderName + i + "_Result.txt";
            String[] invokeCommands = new String[3];
            if(OSUtil.isWindows()) {
                invokeCommands[0] = "cmd.exe";
                invokeCommands[1] = "/c";
            } else {
                invokeCommands[0] = "/bin/bash";
                invokeCommands[1] = "-c";
            }
            invokeCommands[2] = "java -jar " + CHECKSTYLE_PATH + " -f" + " plain" + " -o " + reportPath + " -c " + configPath +  " " + filePath;
            if(DEBUG) {
                System.out.println(invokeCommands[2]);
            }
            Invoker.invokeCommandsByZT(invokeCommands);
        }
    }

}
