package edu.polyu.thread;

import java.io.File;
import java.util.List;

import static edu.polyu.Invoker.compileJavaSourceFile;
import static edu.polyu.Invoker.invokeCommands;
import static edu.polyu.Util.SpotBugsClassFolder;
import static edu.polyu.Util.SpotBugsPath;
import static edu.polyu.Util.SpotBugsResultFolder;
import static edu.polyu.Util.sep;

public class SpotBugs_InvokeThread implements Runnable {

    private String seedFolderPath;
    private String seedFolderName;
    private List<String> seedFileNamesWithSuffix;

    public SpotBugs_InvokeThread(String seedFolderPath, String seedFolderName, List<String> seedFileNamesWithSuffix) {
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
        this.seedFileNamesWithSuffix = seedFileNamesWithSuffix;
    }

    @Override
    public void run() {
        for(int i = 0; i < seedFileNamesWithSuffix.size(); i++) {
            String seedFileNameWithSuffix = seedFileNamesWithSuffix.get(i);
            String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
            // Why needs a name here? -> To specify class folder name
            File classFolder = new File(SpotBugsClassFolder.getAbsolutePath() + sep + seedFolderName + sep + seedFileName);
            if(!classFolder.exists()) {
                classFolder.mkdirs();
            }
            compileJavaSourceFile(seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
            String reportPath = SpotBugsResultFolder.getAbsolutePath() + sep + seedFileName + "_Result.xml";
            String[] invokeCmds = {"/bin/bash", "-cc",
                    SpotBugsPath + " -textui" + " -xml:withMessages" + " -output " + reportPath + " "
                            + classFolder.getAbsolutePath()};
            invokeCommands(invokeCmds);
        }
    }

}
