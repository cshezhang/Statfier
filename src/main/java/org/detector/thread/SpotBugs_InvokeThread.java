package org.detector.thread;

import org.detector.util.OSUtil;
import org.detector.util.Invoker;
import org.detector.util.Utility;

import java.io.File;
import java.util.List;

import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.SPOTBUGS_PATH;
import static org.detector.util.Utility.REPORT_FOLDER;

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
            File CLASS_FOLDER = new File(Utility.CLASS_FOLDER.getAbsolutePath()  + File.separator + seedFileName);
            if(!CLASS_FOLDER.exists()) {
                CLASS_FOLDER.mkdirs();
            }
            Invoker.compileJavaSourceFile(this.seedFolderPath, seedFileNameWithSuffix, CLASS_FOLDER.getAbsolutePath());
            String reportPath = REPORT_FOLDER.getAbsolutePath()  + File.separator + this.seedFolderName + File.separator + seedFileName + "_Result.xml";
            if(DEBUG) {
                System.out.println("Report: " + reportPath);
            }
            String[] invokeCommands = new String[3];
            invokeCommands[0] = "/bin/bash";
            invokeCommands[1] = "-c";
            invokeCommands[2] = SPOTBUGS_PATH + " -textui"
                            + " -xml:withMessages" + " -output " + reportPath + " "
                            + CLASS_FOLDER.getAbsolutePath();
            Invoker.invokeCommandsByZT(invokeCommands);
        }
    }

}
