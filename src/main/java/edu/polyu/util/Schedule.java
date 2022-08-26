package edu.polyu.util;

import static edu.polyu.analysis.SelectionAlgorithm.Div_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.transform.Transform.singleLevelExplorer;
import static edu.polyu.util.Invoker.compileJavaSourceFile;
import static edu.polyu.util.Invoker.invokeCheckStyle;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Invoker.invokeInfer;
import static edu.polyu.util.Invoker.invokePMD;
import static edu.polyu.util.Invoker.invokeSonarQube;
import static edu.polyu.util.Invoker.invokeSpotBugs;
import static edu.polyu.util.Invoker.writeSettingFile;
import static edu.polyu.util.Utility.CHECKSTYLE_MUTATION;
import static edu.polyu.util.Utility.DIV_SELECTION;
import static edu.polyu.util.Utility.GUIDED_LOCATION;
import static edu.polyu.util.Utility.INFER_MUTATION;
import static edu.polyu.util.Utility.NO_SELECTION;
import static edu.polyu.util.Utility.PMD_MUTATION;
import static edu.polyu.util.Utility.Path2Last;
import static edu.polyu.util.Utility.RANDOM_LOCATION;
import static edu.polyu.util.Utility.RANDOM_SELECTION;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.SINGLE_TESTING;
import static edu.polyu.util.Utility.SONARQUBE_MUTATION;
import static edu.polyu.util.Utility.SONARQUBE_SEED_PATH;
import static edu.polyu.util.Utility.SPOTBUGS_MUTATION;
import static edu.polyu.util.Utility.SonarQubeResultFolder;
import static edu.polyu.util.Utility.SonarScannerPath;
import static edu.polyu.util.Utility.SpotBugsClassFolder;
import static edu.polyu.util.Utility.SpotBugsPath;
import static edu.polyu.util.Utility.SpotBugsResultFolder;
import static edu.polyu.util.Utility.THREAD_COUNT;
import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;
import static edu.polyu.util.Utility.getFilenamesFromFolder;
import static edu.polyu.util.Utility.getProperty;
import static edu.polyu.util.Utility.initThreadPool;
import static edu.polyu.util.Utility.listAveragePartition;
import static edu.polyu.util.Utility.mutantFolder;
import static edu.polyu.util.Utility.readSonarQubeResultFile;
import static edu.polyu.util.Utility.readSpotBugsResultFile;
import static edu.polyu.util.Utility.sep;
import static edu.polyu.util.Utility.subSeedFolderNameList;
import static edu.polyu.util.Utility.waitThreadPoolEnding;
import static edu.polyu.util.Utility.writeFileByLine;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.report.Report;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.report.SpotBugs_Violation;
import edu.polyu.thread.CheckStyle_TransformThread;
import edu.polyu.thread.Infer_TransformThread;
import edu.polyu.thread.PMD_TransformThread;
import edu.polyu.thread.SpotBugs_TransformThread;
import edu.polyu.transform.SpotBugs_Exec;
import org.eclipse.jdt.core.dom.ParenthesizedExpression;

import javax.swing.*;

/**
 * Description: This file is the main class for our framework
 * Author: Vanguard
 * Date: 2021/8/25 10:03
 */
public class Schedule {

    // Implement singleton pattern
    private static final Schedule tester = new Schedule();

    private Schedule() {
    }

    public static Schedule getInstance() {
        return tester;
    }

    public void executeCheckStyleMutation(String seedFolderPath) {
        locateMutationCode(seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<String> seedFilePaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        List<TypeWrapper> initWrappers = new ArrayList<>();
        Map<String, Integer> file2index = new HashMap<>();  // source file -> config index
        for (int i = 0; i < seedFilePaths.size(); i++) {
            String seedFilePath = seedFilePaths.get(i);  // Absolute Path
            String[] tokens = seedFilePath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedFilePath)) {  // Check whether this seed has warning
                continue;
            }
            initValidSeedWrapperSize++;
            TypeWrapper seedWrapper = new TypeWrapper(seedFilePath, seedFolderName);
            initWrappers.add(seedWrapper);
            int configIndex = Character.getNumericValue(tokens[tokens.length - 1].charAt(tokens[tokens.length - 1].indexOf(".") - 1));
            file2index.put(seedFilePath, configIndex);
        }
        System.out.println("Initial Valid Wrappers Size: " + initValidSeedWrapperSize);
        for(TypeWrapper wrapper : initWrappers) {
            CheckStyle_TransformThread mutationThread = new CheckStyle_TransformThread(wrapper, wrapper.getFolderName(), file2index.get(wrapper.getFilePath()));
            threadPool.submit(mutationThread);
        }
        waitThreadPoolEnding(threadPool);
    }

    public void testSpotBugsCoverage(String seedFolderPath) {
        String seedFolderName = Path2Last(seedFolderPath);
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + seedFolderName + ", Depth=0");
        invokeSpotBugs(seedFolderPath);
        HashSet<String> bugTypes = new HashSet<>();
        for(Map.Entry<String, Report> entry : file2report.entrySet()) {
            System.out.println("Processing file: " + entry.getKey());
            SpotBugs_Report report = (SpotBugs_Report) entry.getValue();
            for(SpotBugs_Violation violation : report.getViolations()) {
                bugTypes.add(violation.getBugType());
                System.out.println(violation.getBugType());
            }
        }
        for(String bugType : bugTypes) {
            System.out.println(bugType);
        }
        System.out.println("Bug Type Size: " + bugTypes.size());
    }

    public void executeSpotBugsMutation(String seedFolderPath) {
        locateMutationCode(seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<String> seedFilePaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        List<String> failSeedPaths = new ArrayList<>();
        List<TypeWrapper> initWrappers = new ArrayList<>();
        for (int index = 0; index < seedFilePaths.size(); index++) {
            String seedFilePath = seedFilePaths.get(index);
            String[] tokens = seedFilePath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedFilePath)) {
                failSeedPaths.add(seedFilePath);
                continue;
            }
            initValidSeedWrapperSize++;
            TypeWrapper seedWrapper = new TypeWrapper(seedFilePath, seedFolderName);
            initWrappers.add(seedWrapper);
        }
        System.out.println("Initial Valid Wrappers Size: " + initValidSeedWrapperSize);
        if(SINGLE_TESTING) {
            for (TypeWrapper wrapper : initWrappers) {
                System.out.println("Init Path: " + wrapper.getFilePath());
            }
            System.out.println("Fail Seed Size: " + failSeedPaths.size());
            for(String path : failSeedPaths) {
                System.out.println("Fail Seed Path: " + path);
            }
        }
        if(threadPool == null) {
            System.out.println("No Thread!");
            SpotBugs_Exec.run(initWrappers);
        } else {
            List<List<TypeWrapper>> lists = listAveragePartition(initWrappers, THREAD_COUNT);
            for (int i = 0; i < lists.size(); i++) {
                SpotBugs_TransformThread thread = new SpotBugs_TransformThread(lists.get(i));
                threadPool.submit(thread);
            }
            waitThreadPoolEnding(threadPool);
        }
    }

    public void executePMDMutation(String seedFolderPath) {
        locateMutationCode(seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
        HashMap<String, List<TypeWrapper>> bug2wrapper = new HashMap<>();
        List<PMD_TransformThread> mutationThreads = new ArrayList<>();
        HashMap<String, HashSet<String>> category2bugTypes = new HashMap<>(); // Here, we used HashSet to avoid duplicated bug types.
        int initSeedWrapperSize = 0;
        for (int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            String[] tokens = seedPath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedPath)) {
                continue;
            }
            String key = tokens[tokens.length - 2];
            String category = key.split("_")[0];
            String bugType = key.split("_")[1];
            if (category2bugTypes.containsKey(category)) {
                category2bugTypes.get(category).add(bugType);
            } else {
                HashSet<String> types = new HashSet<>();
                types.add(bugType);
                category2bugTypes.put(category, types);
            }
            initSeedWrapperSize++;
            TypeWrapper seedWrapper = new TypeWrapper(seedPath, seedFolderName);
            if (bug2wrapper.containsKey(key)) {
                bug2wrapper.get(key).add(seedWrapper);
            } else {
                List<TypeWrapper> wrappers = new ArrayList<>();
                wrappers.add(seedWrapper);
                bug2wrapper.put(key, wrappers);
            }
        }
        System.out.println("Initial Wrappers Size: " + initSeedWrapperSize);
        for (Map.Entry<String, HashSet<String>> entry : category2bugTypes.entrySet()) {
            String category = entry.getKey();
            HashSet<String> bugTypes = entry.getValue();
            for (String bugType : bugTypes) {
                String seedFolderName = category + "_" + bugType;
                List<TypeWrapper> wrappers = bug2wrapper.get(seedFolderName);
                PMD_TransformThread mutationThread = new PMD_TransformThread(wrappers, seedFolderName);
                mutationThreads.add(mutationThread);
            }
        }
        for (int i = 0; i < mutationThreads.size(); i++) {
            threadPool.submit(mutationThreads.get(i));
        }
        waitThreadPoolEnding(threadPool);
    }

    public void singleThreadWorker(ArrayDeque<TypeWrapper> wrappers) {
        int currentDepth = 0;
        for (int i = 1; i <= SEARCH_DEPTH; i++) {  // perform BFS-based program transformation
            singleLevelExplorer(wrappers, currentDepth++);
            for(String subSeedFolderName : subSeedFolderNameList) {
                String subSeedFolderPath = mutantFolder + File.separator + "iter" + (i - 1) + "_" + subSeedFolderName;
                String settingPath = subSeedFolderPath + File.separator + "settings";
                writeSettingFile(subSeedFolderPath, settingPath);
                String[] invokeCommands = new String[3];
                if (OSUtil.isWindows()) {
                    invokeCommands[0] = "cmd.exe";
                    invokeCommands[1] = "/c";
                } else {
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                }
                invokeCommands[2] = SonarScannerPath + " -Dproject.settings=" + settingPath;
                boolean hasExec = invokeCommandsByZT(invokeCommands);
                if(hasExec) {
                    String[] curlCommands = new String[4];
                    curlCommands[0] = "curl";
                    curlCommands[1] = "-u";
                    curlCommands[2] = "sqp_b5cd7ba6cd143a589260158df861fcf43a20f5b9:";
                    curlCommands[3] = "http://localhost:9000/api/issues/search?componentKeys=Statfier&facets=types&facetMode=count";
                    String jsonContent = invokeCommandsByZT(curlCommands, "json");
                    String reportPath = SonarQubeResultFolder.getAbsolutePath() + File.separator + "iter" + i + "_" + subSeedFolderNameList.get(i) + ".json";
                    writeFileByLine(reportPath, jsonContent);
                    readSonarQubeResultFile(jsonContent, subSeedFolderPath);
                } else {
                    System.err.println("Fail to execute SonarQube in: " + subSeedFolderPath);
                }
            }
//            for (TypeWrapper wrapper : wrappers) {
//                String seedFilePath = wrapper.getFilePath();
//                String[] tokens = seedFilePath.split(sep);
//                String seedFileNameWithSuffix = tokens[tokens.length - 1];
//                String subSeedFolderName = tokens[tokens.length - 2];
//                String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
//                String reportPath = SonarQubeResultFolder.getAbsolutePath() + File.separator + subSeedFolderName + File.separator + seedFileName + "_Result.xml";
//                String[] invokeCmds = new String[3];
//                String settingPath = "";
//                if (OSUtil.isWindows()) {
//                    invokeCmds[0] = "cmd.exe";
//                    invokeCmds[1] = "/c";
//                } else {
//                    invokeCmds[0] = "/bin/bash";
//                    invokeCmds[1] = "-c";
//                }
//                invokeCmds[2] = SonarScannerPath + " -Dproject.settings=" + settingPath;
//                boolean hasExec = invokeCommandsByZT(invokeCmds);
//                if (hasExec) {
//                    String report_path = SpotBugsResultFolder.getAbsolutePath() + File.separator + subSeedFolderName + File.separator + seedFileName + "_Result.xml";
//                    readSpotBugsResultFile(wrapper.getFolderPath(), report_path);
//                }
//            }
            List<TypeWrapper> validWrappers = new ArrayList<>();
            while (!wrappers.isEmpty()) {
                TypeWrapper head = wrappers.pollFirst();
                if (!head.isBuggy()) {
                    validWrappers.add(head);
                }
            }
            wrappers.addAll(validWrappers);
        }
    }

    public void executeSonarQubeMutation(String seedFolderPath) {
        locateMutationCode(seedFolderPath);
        ArrayDeque<TypeWrapper> wrappers = new ArrayDeque<>();
        for(String filepath : file2report.keySet()) {
            String[] tokens = filepath.split(sep);
            String folderName = tokens[tokens.length - 2];
            TypeWrapper wrapper = new TypeWrapper(filepath, folderName);
            wrappers.add(wrapper);
        }
        System.out.println("All Initial Wrapper Size: " + wrappers.size());
        singleThreadWorker(wrappers);
    }

    public void executeInferMutation(String seedFolderPath) {
        locateMutationCode(seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
        HashMap<String, List<TypeWrapper>> bug2wrapper = new HashMap<>();
        List<Infer_TransformThread> mutationThreads = new ArrayList<>();
        int initSeedWrapperSize = 0;
        for (int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            String[] tokens = seedPath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedPath)) {
                continue;
            }
            initSeedWrapperSize++;
            TypeWrapper seedWrapper = new TypeWrapper(seedPath, seedFolderName);
            if (bug2wrapper.containsKey(seedFolderName)) {
                bug2wrapper.get(seedFolderName).add(seedWrapper);
            } else {
                List<TypeWrapper> wrappers = new ArrayList<>();
                wrappers.add(seedWrapper);
                bug2wrapper.put(seedFolderName, wrappers);
            }
        }
        System.out.println("Initial Wrappers Size: " + initSeedWrapperSize);
        for (Map.Entry<String, List<TypeWrapper>> entry : bug2wrapper.entrySet()) {
            String seedFolderName = entry.getKey();
            List<TypeWrapper> wrappers = bug2wrapper.get(seedFolderName);
            Infer_TransformThread mutationThread = new Infer_TransformThread(wrappers, seedFolderName);
            mutationThreads.add(mutationThread);
        }
        for (int i = 0; i < mutationThreads.size(); i++) {
            threadPool.submit(mutationThreads.get(i));
        }
        waitThreadPoolEnding(threadPool);
    }

    // This function only can invoke static analysis tool and cannot include other parts.
    public void locateMutationCode(String seedFolderPath) {
        String seedFolderName = Path2Last(seedFolderPath);
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + seedFolderName + ", Depth=0");
        if (INFER_MUTATION) {
            invokeInfer(seedFolderPath);
        }
        if (CHECKSTYLE_MUTATION) {
            invokeCheckStyle(seedFolderPath);
        }
        if (PMD_MUTATION) {
            invokePMD(seedFolderPath);
        }
        if (SPOTBUGS_MUTATION) {
            invokeSpotBugs(seedFolderPath);
        }
        if (SONARQUBE_MUTATION) {
            invokeSonarQube(seedFolderPath);
        }
        if (SINGLE_TESTING) {
            System.out.println("Init File Size: " + file2bugs.keySet().size());
        }
    }

}
