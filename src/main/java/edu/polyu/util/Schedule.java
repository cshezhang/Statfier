package edu.polyu.util;

import static edu.polyu.util.Invoker.invokeCheckStyle;
import static edu.polyu.util.Invoker.invokeInfer;
import static edu.polyu.util.Invoker.invokePMD;
import static edu.polyu.util.Invoker.invokeSpotBugs;
import static edu.polyu.util.Utility.CHECKSTYLE_MUTATION;
import static edu.polyu.util.Utility.INFER_MUTATION;
import static edu.polyu.util.Utility.PMD_MUTATION;
import static edu.polyu.util.Utility.Path2Last;
import static edu.polyu.util.Utility.SINGLE_TESTING;
import static edu.polyu.util.Utility.SONARQUBE_MUTATION;
import static edu.polyu.util.Utility.SONARQUBE_SEED_PATH;
import static edu.polyu.util.Utility.SPOTBUGS_MUTATION;
import static edu.polyu.util.Utility.THREAD_COUNT;
import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;
import static edu.polyu.util.Utility.getFilenamesFromFolder;
import static edu.polyu.util.Utility.getProperty;
import static edu.polyu.util.Utility.initThreadPool;
import static edu.polyu.util.Utility.listAveragePartition;
import static edu.polyu.util.Utility.readSonarQubeResultFile;
import static edu.polyu.util.Utility.sep;
import static edu.polyu.util.Utility.waitThreadPoolEnding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.report.CheckStyle_Report;
import edu.polyu.report.CheckStyle_Violation;
import edu.polyu.report.Infer_Report;
import edu.polyu.report.Infer_Violation;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;
import edu.polyu.report.SonarQube_Report;
import edu.polyu.report.SonarQube_Violation;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.report.SpotBugs_Violation;
import edu.polyu.thread.CheckStyle_TransformThread;
import edu.polyu.thread.Infer_TransformThread;
import edu.polyu.thread.PMD_TransformThread;
import edu.polyu.thread.SonarQube_TransformThread;
import edu.polyu.thread.SpotBugs_TransformThread;
import edu.polyu.transform.SpotBugs_Exec;

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
        locateMutationCode(0, seedFolderPath);
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
        locateMutationCode(0, seedFolderPath);
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
        locateMutationCode(0, seedFolderPath);
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

    public void executeSonarQubeMutation(String seedFolderPath) {
        locateMutationCode(0, seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<TypeWrapper> initWrappers = new ArrayList<>();
        for(String filepath : file2report.keySet()) {
            String[] tokens = filepath.split(sep);
            String folderName = tokens[tokens.length - 2];
            TypeWrapper wrapper = new TypeWrapper(filepath, folderName);
            initWrappers.add(wrapper);
        }
        System.out.println("All Initial Wrapper Size: " + initWrappers.size());
        List<List<TypeWrapper>> lists = listAveragePartition(initWrappers, THREAD_COUNT);
        for(int i = 0; i < lists.size(); i++) {
            SonarQube_TransformThread thread = new SonarQube_TransformThread(lists.get(i));
            threadPool.submit(thread);
        }
        waitThreadPoolEnding(threadPool);
    }

    public void executeInferMutation(String seedFolderPath) {
        locateMutationCode(0, seedFolderPath);
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
    public void locateMutationCode(int iterDepth, String seedFolderPath) {
        String seedFolderName = Path2Last(seedFolderPath);
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + seedFolderName + ", Depth=" + iterDepth);
        if (SONARQUBE_MUTATION) {
            System.out.println("Begin to Analyze first round SonarQube Result File...");
            String initReportPath = "/home/vanguard/evaluation/SonarQube_Seeds1.csv";
            readSonarQubeResultFile(initReportPath, SONARQUBE_SEED_PATH);
            System.out.println("Init Finished! Iteration Level: " + iterDepth + ", File Size: " + file2bugs.keySet().size());
            return;
        }
        if (INFER_MUTATION) {
            invokeInfer(iterDepth, seedFolderPath);
        }
        if (CHECKSTYLE_MUTATION) {
            invokeCheckStyle(iterDepth, seedFolderPath);
        }
        if (PMD_MUTATION) {
            invokePMD(iterDepth, seedFolderPath);
        }
        if (SPOTBUGS_MUTATION) {
            invokeSpotBugs(seedFolderPath);
        }
        if (SINGLE_TESTING) {
            System.out.println("Iteration Level: " + iterDepth + ", File Size: " + file2bugs.keySet().size());
        }
    }

}
