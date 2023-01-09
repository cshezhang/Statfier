package org.rainyday.thread;

import org.rainyday.util.OSUtil;
import org.rainyday.util.Invoker;
import org.rainyday.util.Utility;

import java.io.File;
import java.util.List;

import static org.rainyday.util.Utility.DEBUG;
import static org.rainyday.util.Utility.reportFolder;

public class SpotBugs_InvokeThread implements Runnable {

    private String seedFolderPath;
    private String seedFolderName;
    private List<String> seedFileNamesWithSuffix;

    public SpotBugs_InvokeThread(String seedFolderPath, String seedFolderName, List<String> seedFileNamesWithSuffix) {
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
        this.seedFileNamesWithSuffix = seedFileNamesWithSuffix;  // only name, not path
    }

    @Override
    public void run() {
        for(int i = 0; i < this.seedFileNamesWithSuffix.size(); i++) {
            String seedFileNameWithSuffix = this.seedFileNamesWithSuffix.get(i);
            String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
            // seedFileName is used to specify class folder name
            File classFolder = new File(Utility.classFolder.getAbsolutePath()  + File.separator + seedFileName);
            if(!classFolder.exists()) {
                classFolder.mkdirs();
            }
            Invoker.compileJavaSourceFile(this.seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
//            String configPath = BASE_SEED_PATH  + File.separator + "SpotBugs_Rule_Config"  + File.separator + this.seedFolderName + ".xml";
            String reportPath = reportFolder.getAbsolutePath()  + File.separator + this.seedFolderName + File.separator + seedFileName + "_Result.xml";
            if(DEBUG) {
                System.out.println("Report: " + reportPath);
            }
            String[] invokeCommands = new String[3];
            if(OSUtil.isWindows()) {
                invokeCommands[0] = "cmd.exe";
                invokeCommands[1] = "/c";
            } else {
                invokeCommands[0] = "/bin/bash";
                invokeCommands[1] = "-c";
            }
            invokeCommands[2] = Utility.SpotBugsPath + " -textui"
                            + " -xml:withMessages" + " -output " + reportPath + " "
                            + classFolder.getAbsolutePath();
            Invoker.invokeCommandsByZT(invokeCommands);
        }
    }

}
