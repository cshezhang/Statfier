package org.rainyday.thread;

import org.rainyday.analysis.TypeWrapper;
import org.rainyday.report.CheckStyle_Report;
import org.rainyday.util.OSUtil;
import org.rainyday.util.Invoker;
import org.rainyday.util.Utility;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static org.rainyday.util.Utility.DEBUG;
import static org.rainyday.util.Utility.Path2Last;
import static org.rainyday.util.Utility.reportFolder;
import static org.rainyday.transform.Transform.singleLevelExplorer;

public class CheckStyle_TransformThread implements Runnable {

    private int currentDepth;
    private String seedFolderName; // equal to rule type
    private ArrayDeque<TypeWrapper> wrappers;
    private int configIndex;

    public CheckStyle_TransformThread(TypeWrapper initWrapper, String seedFolderName, int configIndex) {
        this.currentDepth = 0;
        this.seedFolderName = seedFolderName;
        this.wrappers = new ArrayDeque<>() {
            {
                add(initWrapper);
            }
        };
        this.configIndex = configIndex;
    }

    // iter 1 -> SEARCH_DEPTH: 1. transformation to generate mutant; 2. invoke PMD to detect bugs
    @Override
    public void run() {
        for (int depth = 1; depth <= Utility.SEARCH_DEPTH; depth++) {
            if (DEBUG) {
                System.out.println("TransformThread Depth: " + depth + " Folder: " + this.seedFolderName);
            }
            singleLevelExplorer(this.wrappers, this.currentDepth++);
            // detect mutants of iter i
            String mutantFolderPath = Utility.mutantFolder + File.separator + "iter" + depth + File.separator + seedFolderName;
            List<String> mutantFilePaths = Utility.getFilenamesFromFolder(mutantFolderPath, true);
//            String configPath = CheckStyleConfigPath + File.separator + "google_checks.xml";
            String configPath = Utility.CheckStyleConfigPath + File.separator + seedFolderName + configIndex + ".xml";
            File configFile = new File(configPath);
            if (configFile.exists()) {
                configPath = Utility.CheckStyleConfigPath + File.separator + seedFolderName + 0 + ".xml";
            }
            List<CheckStyle_Report> reports = new ArrayList<>();
            // 这里还可以做一个configIndex是否match seedFolderName里边的index
            for (int i = 0; i < mutantFilePaths.size(); i++) {
                String mutantFilePath = mutantFilePaths.get(i);
                String mutantFileName = Path2Last(mutantFilePath);
                String reportFilePath = reportFolder + File.separator + "iter" + depth + "_" + mutantFileName + ".xml";
                String[] invokeCmds = new String[3];
                if (OSUtil.isWindows()) {
                    invokeCmds[0] = "cmd.exe";
                    invokeCmds[1] = "/c";
                } else {
                    invokeCmds[0] = "/bin/bash";
                    invokeCmds[1] = "-c";
                }
                invokeCmds[2] = "java -jar " + Utility.CheckStylePath + " -f" + " plain" + " -o " + reportFilePath + " -c "
                        + configPath + " " + mutantFilePath;
                if (DEBUG) {
                    System.out.println(invokeCmds[2]);
                }
                Invoker.invokeCommandsByZT(invokeCmds);
                Utility.readCheckStyleResultFile(reportFilePath);
            }
            List<TypeWrapper> validWrappers = new ArrayList<>();
            while (!wrappers.isEmpty()) {
                TypeWrapper head = wrappers.pollFirst();
                if (!head.isBuggy()) { // if this mutant is buggy, then we should switch to next mutant
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }

}
