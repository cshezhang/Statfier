package edu.polyu.thread;

import edu.polyu.util.OSUtil;

import java.io.File;

import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Utility.SINGLE_TESTING;
import static edu.polyu.util.Utility.SonarScannerPath;

public class SonarQube_InvokeThread implements Runnable {

    private String seedFolderPath;
    private String seedFolderName;

    public SonarQube_InvokeThread(String seedFolderPath, String seedFolderName) {
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
    }

    @Override
    public void run() {
        // seedFileName is used to specify class folder name
        if(SINGLE_TESTING) {
            System.out.println("Seed path: " + seedFolderPath + File.separator + seedFolderName);
        }
        String[] invokeCommands = new String[3];
        if(OSUtil.isWindows()) {
            invokeCommands[0] = "cmd.exe";
            invokeCommands[1] = "/c";
        } else {
            invokeCommands[0] = "/bin/bash";
            invokeCommands[1] = "-c";
        }
        invokeCommands[2] = SonarScannerPath
                + " -Dsonar.projectKey=Statfier"
                + " -Dsonar.sources=" + this.seedFolderPath
                + " -Dsonar.host.url=http://localhost:9000"
                + " -Dsonar.login=sqp_b5cd7ba6cd143a589260158df861fcf43a20f5b9";
        invokeCommandsByZT(invokeCommands);
    }

}
