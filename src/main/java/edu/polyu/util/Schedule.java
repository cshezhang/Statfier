package edu.polyu.util;

import static edu.polyu.transform.Transform.singleLevelExplorer;
import static edu.polyu.util.Invoker.invokeCheckStyle;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Invoker.invokeInfer;
import static edu.polyu.util.Invoker.invokePMD;
import static edu.polyu.util.Invoker.invokeSonarQube;
import static edu.polyu.util.Invoker.invokeSpotBugs;
import static edu.polyu.util.Invoker.writeSettingFile;
import static edu.polyu.util.Utility.CNES_PATH;
import static edu.polyu.util.Utility.CNES_ReportName;
import static edu.polyu.util.Utility.EVALUATION_PATH;
import static edu.polyu.util.Utility.Path2Last;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.DEBUG_STATFIER;
import static edu.polyu.util.Utility.SONARQUBE_LOGIN;
import static edu.polyu.util.Utility.SONARQUBE_PROJECT_KEY;
import static edu.polyu.util.Utility.SONAR_SCANNER_PATH;
import static edu.polyu.util.Utility.SonarQubeResultFolder;
import static edu.polyu.util.Utility.THREAD_COUNT;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;
import static edu.polyu.util.Utility.getFilenamesFromFolder;
import static edu.polyu.util.Utility.initThreadPool;
import static edu.polyu.util.Utility.listAveragePartition;
import static edu.polyu.util.Utility.mutantFolder;
import static edu.polyu.util.Utility.readSonarQubeResultFile;
import static edu.polyu.util.Utility.sep;
import static edu.polyu.util.Utility.subSeedFolderNameList;
import static edu.polyu.util.Utility.waitThreadPoolEnding;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.thread.CheckStyle_TransformThread;
import edu.polyu.thread.Infer_TransformThread;
import edu.polyu.thread.PMD_TransformThread;
import edu.polyu.thread.SpotBugs_Exec;
import edu.polyu.thread.SpotBugs_TransformThread;

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
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        invokeCheckStyle(seedFolderPath);
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

    public void executeSpotBugsMutation(String seedFolderPath) {
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        invokeSpotBugs(seedFolderPath);
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
        if(DEBUG_STATFIER) {
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
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        invokePMD(seedFolderPath);
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
        for (int iter = 1; iter <= SEARCH_DEPTH; iter++) {
            singleLevelExplorer(wrappers, currentDepth++);
            for(String subSeedFolderName : subSeedFolderNameList) {
                String subSeedFolderPath = mutantFolder + File.separator + "iter" + iter + File.separator + subSeedFolderName;
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
                invokeCommands[2] = SONAR_SCANNER_PATH + " -Dsonar.projectBaseDir=" + EVALUATION_PATH +  " -Dproject.settings=" + settingPath;
                boolean hasExec = invokeCommandsByZT(invokeCommands);
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    System.err.println("Interruption error!");
                }
                if(hasExec) {
                    List<String> CNES_Commands = new ArrayList<>();
                    CNES_Commands.add("java");
                    CNES_Commands.add("-jar");
                    CNES_Commands.add(CNES_PATH);
                    CNES_Commands.add("-p");
                    CNES_Commands.add(SONARQUBE_PROJECT_KEY);
                    CNES_Commands.add("-t");
                    CNES_Commands.add(SONARQUBE_LOGIN);
                    CNES_Commands.add("-m");
                    CNES_Commands.add("-w");
                    CNES_Commands.add("-e");
                    CNES_Commands.add("-o");
                    String CNES_ReportFolderPath = SonarQubeResultFolder.getAbsolutePath() + File.separator + "iter" + iter + "_" + subSeedFolderName;
                    CNES_Commands.add(CNES_ReportFolderPath);
                    String reportPath = CNES_ReportFolderPath + File.separator + CNES_ReportName;
                    invokeCommandsByZT(CNES_Commands.toArray(new String[CNES_Commands.size()]));
                    if(DEBUG_STATFIER) {
                        System.out.println("Reading result file: " + reportPath);
                    }
                    readSonarQubeResultFile(reportPath);
                } else {
                    System.err.println("Fail to execute SonarQube in: " + subSeedFolderPath);
                }
            }
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
        System.out.println("Invoke SonarQube for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        invokeSonarQube(seedFolderPath);
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
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        invokeInfer(seedFolderPath);
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

}
