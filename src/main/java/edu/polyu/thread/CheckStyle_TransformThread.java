package edu.polyu.thread;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.report.CheckStyle_Report;
import edu.polyu.report.CheckStyle_Violation;
import edu.polyu.util.OSUtil;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.Div_Selection;
import static edu.polyu.analysis.TypeWrapper.mutant2seed;
import static edu.polyu.analysis.TypeWrapper.mutant2seq;
import static edu.polyu.transform.Transform.singleLevelExplorer;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Utility.COMPILE;
import static edu.polyu.util.Utility.CheckStyleConfigPath;
import static edu.polyu.util.Utility.CheckStylePath;
import static edu.polyu.util.Utility.CheckStyleResultFolder;
import static edu.polyu.util.Utility.DIV_SELECTION;
import static edu.polyu.util.Utility.GUIDED_LOCATION;
import static edu.polyu.util.Utility.NO_SELECTION;
import static edu.polyu.util.Utility.Path2Last;
import static edu.polyu.util.Utility.RANDOM_LOCATION;
import static edu.polyu.util.Utility.RANDOM_SELECTION;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.SINGLE_TESTING;
import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;
import static edu.polyu.util.Utility.getFilenamesFromFolder;
import static edu.polyu.util.Utility.mutantFolder;
import static edu.polyu.util.Utility.readCheckStyleResultFile;

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
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            if(SINGLE_TESTING) {
                System.out.println("TransformThread Depth: " + depth + " Folder: " + this.seedFolderName);
            }
            singleLevelExplorer(this.wrappers, this.currentDepth++);
            // detect mutants of iter i
            String mutantFolderPath = mutantFolder + File.separator + "iter" + depth + File.separator + seedFolderName;
            List<String> mutantFilePaths = getFilenamesFromFolder(mutantFolderPath, true);
//            String configPath = CheckStyleConfigPath + File.separator + "google_checks.xml";
            String configPath = CheckStyleConfigPath + File.separator + seedFolderName + configIndex + ".xml";
            File configFile = new File(configPath);
            if(configFile.exists()) {
                configPath = CheckStyleConfigPath + File.separator + seedFolderName + 0 + ".xml";
            }
            List<CheckStyle_Report> reports = new ArrayList<>();
            // 这里还可以做一个configIndex是否match seedFolderName里边的index
            for(int i = 0; i < mutantFilePaths.size(); i++) {
                String mutantFilePath = mutantFilePaths.get(i);
                String mutantFileName = Path2Last(mutantFilePath);
                String reportFilePath = CheckStyleResultFolder + File.separator + "iter" + depth + "_" + mutantFileName + ".xml";
                String[] invokeCmds = new String[3];
                if(OSUtil.isWindows()) {
                    invokeCmds[0] = "cmd.exe";
                    invokeCmds[1] = "/c";
                } else {
                    invokeCmds[0] = "/bin/bash";
                    invokeCmds[1] = "-c";
                }
                invokeCmds[2] = "java -jar " + CheckStylePath + " -f" + " plain" + " -o " + reportFilePath + " -c "
                                + configPath + " " + mutantFilePath;
                if(SINGLE_TESTING) {
                    System.out.println(invokeCmds[2]);
                }
                invokeCommandsByZT(invokeCmds);
                reports.addAll(readCheckStyleResultFile(reportFilePath));
            }
            for (CheckStyle_Report report : reports) {
                file2report.put(report.getFilepath(), report);
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (CheckStyle_Violation violation : report.getViolations()) {
                    file2row.get(report.getFilepath()).add(violation.getBeginLine());
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                    if (!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
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
