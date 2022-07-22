package edu.polyu.thread;

import edu.polyu.util.OSUtil;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Invoker.compileJavaSourceFile;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.BASE_SEED_PATH;
import static edu.polyu.util.Util.SINGLE_TESTING;
import static edu.polyu.util.Util.SpotBugsClassFolder;
import static edu.polyu.util.Util.SpotBugsPath;
import static edu.polyu.util.Util.SpotBugsResultFolder;

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
            // Why needs a name here? -> To specify class folder name
            File classFolder = new File(SpotBugsClassFolder.getAbsolutePath()  + File.separator + seedFileName);
            if(!classFolder.exists()) {
                classFolder.mkdirs();
            }
            compileJavaSourceFile(this.seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
            String configPath = BASE_SEED_PATH  + File.separator + "SpotBugs_Rule_Config"  + File.separator + this.seedFolderName + ".xml";
            String reportPath = SpotBugsResultFolder.getAbsolutePath()  + File.separator + this.seedFolderName + File.separator + seedFileName + "_Result.xml";
            if(SINGLE_TESTING) {
                System.out.println("Report: " + reportPath);
            }
            String[] invokeCmds = new String[3];
            if(OSUtil.isWindows()) {
                invokeCmds[0] = "cmd.exe";
                invokeCmds[1] = "/c";
            } else {
                invokeCmds[0] = "/bin/bash";
                invokeCmds[1] = "-c";
            }
            invokeCmds[2] = SpotBugsPath + " -textui"
//                            + " -include " + configPath
                            + " -xml:withMessages" + " -output " + reportPath + " "
                            + classFolder.getAbsolutePath()  + File.separator + seedFileName + ".class";
            invokeCommandsByZT(invokeCmds);
        }
    }

}
