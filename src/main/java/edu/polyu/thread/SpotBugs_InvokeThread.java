package edu.polyu.thread;

import edu.polyu.util.OSUtil;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Invoker.compileJavaSourceFile;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Utility.DEBUG_STATFIER;
import static edu.polyu.util.Utility.SpotBugsClassFolder;
import static edu.polyu.util.Utility.SpotBugsPath;
import static edu.polyu.util.Utility.SpotBugsResultFolder;

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
            File classFolder = new File(SpotBugsClassFolder.getAbsolutePath()  + File.separator + seedFileName);
            if(!classFolder.exists()) {
                classFolder.mkdirs();
            }
            compileJavaSourceFile(this.seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
//            String configPath = BASE_SEED_PATH  + File.separator + "SpotBugs_Rule_Config"  + File.separator + this.seedFolderName + ".xml";
            String reportPath = SpotBugsResultFolder.getAbsolutePath()  + File.separator + this.seedFolderName + File.separator + seedFileName + "_Result.xml";
            if(DEBUG_STATFIER) {
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
            invokeCommands[2] = SpotBugsPath + " -textui"
                            + " -xml:withMessages" + " -output " + reportPath + " "
                            + classFolder.getAbsolutePath();
            invokeCommandsByZT(invokeCommands);
        }
    }

}
