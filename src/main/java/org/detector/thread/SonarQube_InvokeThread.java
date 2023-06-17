package org.detector.thread;

import org.detector.util.OSUtil;
import org.detector.util.Invoker;
import org.detector.util.Utility;

import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.SONAR_SCANNER_PATH;

import java.io.File;

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
        if(DEBUG) {
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
        invokeCommands[2] = SONAR_SCANNER_PATH
                + " -Dsonar.projectKey=" + Utility.SONARQUBE_PROJECT_KEY
                + " -Dsonar.sources=" + this.seedFolderPath
                + " -Dsonar.host.url=http://localhost:9000";
        Invoker.invokeCommandsByZT(invokeCommands);
    }

}
