package edu.polyu.util;

import static edu.polyu.util.Invoker.invokeCheckStyle;
import static edu.polyu.util.Invoker.invokeInfer;
import static edu.polyu.util.Invoker.invokePMD;
import static edu.polyu.util.Invoker.invokeSpotBugs;
import static edu.polyu.util.Util.CHECKSTYLE_MUTATION;
import static edu.polyu.util.Util.INFER_MUTATION;
import static edu.polyu.util.Util.PMD_MUTATION;
import static edu.polyu.util.Util.Path2Last;
import static edu.polyu.util.Util.SINGLE_TESTING;
import static edu.polyu.util.Util.SONARQUBE_MUTATION;
import static edu.polyu.util.Util.SONARQUBE_SEED_PATH;
import static edu.polyu.util.Util.SPOTBUGS_MUTATION;
import static edu.polyu.util.Util.THREAD_COUNT;
import static edu.polyu.util.Util.file2bugs;
import static edu.polyu.util.Util.file2report;
import static edu.polyu.util.Util.file2row;
import static edu.polyu.util.Util.getFilenamesFromFolder;
import static edu.polyu.util.Util.getProperty;
import static edu.polyu.util.Util.listAveragePartition;
import static edu.polyu.util.Util.readSonarQubeResultFile;
import static edu.polyu.util.Util.sep;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.polyu.analysis.ASTWrapper;
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
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        if (Boolean.parseBoolean(getProperty("FIXED_THREADPOOL"))) {
            threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        } else {
            if (Boolean.parseBoolean(getProperty("CACHED_THREADPOOL"))) {
                threadPool = Executors.newCachedThreadPool();
            }
        }
        List<String> seedFilePaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        List<ASTWrapper> initWrappers = new ArrayList<>();
        Map<String, Integer> file2index = new HashMap<>();  // source file -> config index
        for (int i = 0; i < seedFilePaths.size(); i++) {
            String seedFilePath = seedFilePaths.get(i);  // Absolute Path
            String[] tokens = seedFilePath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedFilePath)) {  // Check whether this seed has warning
                continue;
            }
            initValidSeedWrapperSize++;
            ASTWrapper seedWrapper = new ASTWrapper(seedFilePath, seedFolderName);
            initWrappers.add(seedWrapper);
            int configIndex = Character.getNumericValue(tokens[tokens.length - 1].charAt(tokens[tokens.length - 1].indexOf(".") - 1));
            file2index.put(seedFilePath, configIndex);
        }
        System.out.println("Initial Valid Wrappers Size: " + initValidSeedWrapperSize);
        for(ASTWrapper wrapper : initWrappers) {
            CheckStyle_TransformThread mutationThread = new CheckStyle_TransformThread(wrapper, wrapper.getFolderName(), file2index.get(wrapper.getFilePath()));
            threadPool.submit(mutationThread);
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void executeSpotBugsMutation(String seedFolderPath) {
        locateMutationCode(0, seedFolderPath);
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        if (Boolean.parseBoolean(getProperty("FIXED_THREADPOOL"))) {
            threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        } else {
            if (Boolean.parseBoolean(getProperty("CACHED_THREADPOOL"))) {
                threadPool = Executors.newCachedThreadPool();
            }
        }
        List<String> seedFilePaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        List<String> failSeedPaths = new ArrayList<>();
        List<ASTWrapper> initWrappers = new ArrayList<>();
        for (int index = 0; index < seedFilePaths.size(); index++) {
            String seedFilePath = seedFilePaths.get(index);
            String[] tokens = seedFilePath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedFilePath)) {
                failSeedPaths.add(seedFilePath);
                continue;
            }
            initValidSeedWrapperSize++;
            ASTWrapper seedWrapper = new ASTWrapper(seedFilePath, seedFolderName);
            initWrappers.add(seedWrapper);
        }
        System.out.println("Initial Valid Wrappers Size: " + initValidSeedWrapperSize);
        if(SINGLE_TESTING) {
            for (ASTWrapper wrapper : initWrappers) {
                System.out.println("Init Path: " + wrapper.getFilePath());
            }
            System.out.println("Fail Seed Size: " + failSeedPaths.size());
            for(String path : failSeedPaths) {
                System.out.println("Fail Seed Path: " + path);
            }
        }
        List<List<ASTWrapper>> lists = listAveragePartition(initWrappers, THREAD_COUNT);
        for (int i = 0; i < lists.size(); i++) {
            SpotBugs_TransformThread thread = new SpotBugs_TransformThread(lists.get(i));
            threadPool.submit(thread);
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void executePMDMutation(String seedFolderPath) {
        locateMutationCode(0, seedFolderPath);
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        if (Boolean.parseBoolean(getProperty("FIXED_THREADPOOL"))) {
            threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        } else {
            if (Boolean.parseBoolean(getProperty("CACHED_THREADPOOL"))) {
                threadPool = Executors.newCachedThreadPool();
            }
        }
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
        HashMap<String, List<ASTWrapper>> bug2wrapper = new HashMap<>();
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
            ASTWrapper seedWrapper = new ASTWrapper(seedPath, seedFolderName);
            if (bug2wrapper.containsKey(key)) {
                bug2wrapper.get(key).add(seedWrapper);
            } else {
                List<ASTWrapper> wrappers = new ArrayList<>();
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
                List<ASTWrapper> wrappers = bug2wrapper.get(seedFolderName);
                PMD_TransformThread mutationThread = new PMD_TransformThread(wrappers, seedFolderName);
                mutationThreads.add(mutationThread);
            }
        }
        for (int i = 0; i < mutationThreads.size(); i++) {
            threadPool.submit(mutationThreads.get(i));
        }
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void executeSonarQubeMutation(String seedFolderPath) {
        locateMutationCode(0, seedFolderPath);
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        if (Boolean.parseBoolean(getProperty("FIXED_THREADPOOL"))) {
            threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        } else {
            if (Boolean.parseBoolean(getProperty("CACHED_THREADPOOL"))) {
                threadPool = Executors.newCachedThreadPool();
            }
        }
        List<ASTWrapper> initWrappers = new ArrayList<>();
        for(String filepath : file2report.keySet()) {
            String[] tokens = filepath.split(sep);
            String foldername = tokens[tokens.length - 2];
            ASTWrapper wrapper = new ASTWrapper(filepath, foldername);
            initWrappers.add(wrapper);
        }
        System.out.println("All Initial Wrapper Size: " + initWrappers.size());
        List<List<ASTWrapper>> lists = listAveragePartition(initWrappers, THREAD_COUNT);
        for(int i = 0; i < lists.size(); i++) {
            SonarQube_TransformThread thread = new SonarQube_TransformThread(lists.get(i));
            threadPool.submit(thread);
        }
        threadPool.shutdown();
        System.out.println("Init Transform Thread Finished!");
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Exec Transform Thread Finished!");
    }

    public void executeInferMutation(String seedFolderPath) {
        locateMutationCode(0, seedFolderPath);
        ExecutorService threadPool = Executors.newSingleThreadExecutor();
        if (Boolean.parseBoolean(getProperty("FIXED_THREADPOOL"))) {
            threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        } else {
            if (Boolean.parseBoolean(getProperty("CACHED_THREADPOOL"))) {
                threadPool = Executors.newCachedThreadPool();
            }
        }
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
        HashMap<String, List<ASTWrapper>> bug2wrapper = new HashMap<>();
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
            ASTWrapper seedWrapper = new ASTWrapper(seedPath, seedFolderName);
            if (bug2wrapper.containsKey(seedFolderName)) {
                bug2wrapper.get(seedFolderName).add(seedWrapper);
            } else {
                List<ASTWrapper> wrappers = new ArrayList<>();
                wrappers.add(seedWrapper);
                bug2wrapper.put(seedFolderName, wrappers);
            }
        }
        System.out.println("Initial Wrappers Size: " + initSeedWrapperSize);
        for (Map.Entry<String, List<ASTWrapper>> entry : bug2wrapper.entrySet()) {
            String seedFolderName = entry.getKey();
            List<ASTWrapper> wrappers = bug2wrapper.get(seedFolderName);
            Infer_TransformThread mutationThread = new Infer_TransformThread(wrappers, seedFolderName);
            mutationThreads.add(mutationThread);
        }
        for (int i = 0; i < mutationThreads.size(); i++) {
            threadPool.submit(mutationThreads.get(i));
        }
        threadPool.shutdown();
        System.out.println("Init Transform Thread Finished!");
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("Exec Transform Thread Finished!");
    }

    // This function only can invoke static analysis tool and cannot include other parts.
    public void locateMutationCode(int iterDepth, String seedFolderPath) {
        String seedFolderName = Path2Last(seedFolderPath);
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + seedFolderName + ", Depth=" + iterDepth);
        if (SONARQUBE_MUTATION) {
            System.out.println("Begin to Analyze first round SonarQube Result File...");
            String initReportPath = "/home/vanguard/evaluation/SonarQube_Seeds1.csv";
            List<SonarQube_Report> reports = readSonarQubeResultFile(initReportPath, SONARQUBE_SEED_PATH);
            System.out.println("Report Size: " + reports.size());
            for(SonarQube_Report report : reports) {
                file2report.put(report.getFilepath(), report);
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (SonarQube_Violation violation : report.getViolations()) {
                    file2row.get(report.getFilepath()).add(violation.getBeginLine());
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                    if (!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
            }
            System.out.println("Init Finished! Iteration Level: " + iterDepth + ", File Size: " + file2bugs.keySet().size());
            return;
        }
        if (INFER_MUTATION) {
            System.out.println("Begin to Inokve Infer for Init...");
            List<Infer_Report> reports = invokeInfer(iterDepth, seedFolderPath);
            for (Infer_Report report : reports) {
                file2report.put(report.getFilepath(), report);
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (Infer_Violation violation : report.getViolations()) {
                    file2row.get(report.getFilepath()).add(violation.getBeginLine());
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                    if (!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
            }
            System.out.println("Init Finished! Iteration Level: " + iterDepth + ", File Size: " + file2bugs.keySet().size());
            return;
        }
        if (CHECKSTYLE_MUTATION) {
            List<CheckStyle_Report> reports = invokeCheckStyle(iterDepth, seedFolderPath);
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
            System.out.println("Iteration Level: " + iterDepth + ", File Size: " + file2bugs.keySet().size());
            return;
        }
        if (PMD_MUTATION) {
            List<PMD_Report> reports = invokePMD(iterDepth, seedFolderPath);
            for (PMD_Report report : reports) {
                file2report.put(report.getFilepath(), report);
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (PMD_Violation violation : report.getViolations()) {
                    file2row.get(report.getFilepath()).add(violation.beginLine);
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                    if (!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
            }
            if (SINGLE_TESTING) {
                System.out.println("Iteration Level: " + iterDepth + ", File Size: " + file2bugs.keySet().size());
            }
            return;
        }
        if (SPOTBUGS_MUTATION) {
            List<SpotBugs_Report> reports = invokeSpotBugs(seedFolderPath);
            for (SpotBugs_Report report : reports) {
                if (!file2row.containsKey(report.getFilepath())) {
                    file2row.put(report.getFilepath(), new HashSet<>());
                    file2bugs.put(report.getFilepath(), new HashMap<>());
                }
                for (SpotBugs_Violation violation : report.getViolations()) {
                    file2row.get(report.getFilepath()).add(violation.getBeginLine());
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                    if (!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
            }
            if (SINGLE_TESTING) {
                System.out.println("Iteration Level: " + iterDepth + ", File Size: " + file2bugs.keySet().size());
            }
        }
    }

}
