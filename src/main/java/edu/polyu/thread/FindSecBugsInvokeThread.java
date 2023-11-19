package edu.polyu.thread;

import edu.polyu.util.Invoker;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.CLASS_FOLDER;
import static edu.polyu.util.Utility.REPORT_FOLDER;
import static edu.polyu.util.Utility.FINDSECBUGS_PATH;


public class FindSecBugsInvokeThread implements Runnable {

    private String seedFolderPath;
    private String seedFolderName;
    private List<String> seedFileNamesWithSuffix;

    public FindSecBugsInvokeThread(String seedFolderPath, String seedFolderName, List<String> seedFileNamesWithSuffix) {
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
            File classFolder = new File(CLASS_FOLDER.getAbsolutePath()  + File.separator + seedFileName);
            if(!classFolder.exists()) {
                classFolder.mkdirs();
            }
            boolean isCompiled = Invoker.compileJavaSourceFile(this.seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
            if(!isCompiled) {
                continue;
            }
            String reportPath = REPORT_FOLDER.getAbsolutePath()  + File.separator + this.seedFolderName + File.separator + seedFileName + "_Result.xml";
            if(DEBUG) {
                System.out.println("Report: " + reportPath);
            }
            String[] invokeCommands = new String[3];
            invokeCommands[0] = "/bin/bash";
            invokeCommands[1] = "-c";
            invokeCommands[2] = FINDSECBUGS_PATH + " -xml -output " + reportPath + " " + classFolder.getAbsolutePath();
            Invoker.invokeCommandsByZT(invokeCommands);
        }
    }

}
