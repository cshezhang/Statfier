package edu.polyu.thread;

import edu.polyu.util.OSUtil;
import edu.polyu.util.Invoker;
import edu.polyu.util.Utility;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Schedule.file2config;
import static edu.polyu.util.Utility.CHECKSTYLE_CONFIG_PATH;
import static edu.polyu.util.Utility.CHECKSTYLE_PATH;
import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.reg_sep;
import static edu.polyu.util.Utility.REPORT_FOLDER;
import static edu.polyu.util.Utility.sep;

public class CheckStyleInvokeThread implements Runnable {
    private String seedFolderPath;
    private String seedFolderName;


    public CheckStyleInvokeThread(int iterDepth, String seedFolderPath, String seedFolderName) {
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
    }

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
            String reportPath = REPORT_FOLDER + File.separator + "iter0" + "_" + seedFolderName + i + "_Result.txt";
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
