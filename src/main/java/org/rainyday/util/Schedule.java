package org.rainyday.util;

import static org.rainyday.transform.Transform.cnt1;
import static org.rainyday.transform.Transform.cnt2;
import static org.rainyday.transform.Transform.singleLevelExplorer;
import static org.rainyday.util.Invoker.failedCommands;
import static org.rainyday.util.Invoker.invokePMD;
import static org.rainyday.util.Utility.EVALUATION_PATH;
import static org.rainyday.util.Utility.INFER_MUTATION;
import static org.rainyday.util.Utility.Path2Last;
import static org.rainyday.util.Utility.SEARCH_DEPTH;
import static org.rainyday.util.Utility.DEBUG;
import static org.rainyday.util.Utility.SONARQUBE_LOGIN;
import static org.rainyday.util.Utility.SONARQUBE_PROJECT_KEY;
import static org.rainyday.util.Utility.SONAR_SCANNER_PATH;
import static org.rainyday.util.Utility.SPOTBUGS_MUTATION;
import static org.rainyday.util.Utility.THREAD_COUNT;
import static org.rainyday.util.Utility.failedReport;
import static org.rainyday.util.Utility.failedT;
import static org.rainyday.util.Utility.file2report;
import static org.rainyday.util.Utility.file2row;
import static org.rainyday.util.Utility.getFilenamesFromFolder;
import static org.rainyday.util.Utility.initThreadPool;
import static org.rainyday.util.Utility.listAveragePartition;
import static org.rainyday.util.Utility.mutantFolder;
import static org.rainyday.util.Utility.readSonarQubeResultFile;
import static org.rainyday.util.Utility.reg_sep;
import static org.rainyday.util.Utility.reportFolder;
import static org.rainyday.util.Utility.sep;
import static org.rainyday.util.Utility.subSeedFolderNameList;
import static org.rainyday.util.Utility.successfulT;
import static org.rainyday.util.Utility.waitThreadPoolEnding;
import static org.rainyday.util.Utility.writeLinesToFile;
import static org.rainyday.analysis.TypeWrapper.transformedSeed;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sourceforge.pmd.PMD;
import org.rainyday.analysis.TypeWrapper;
import org.rainyday.thread.CheckStyle_TransformThread;
import org.rainyday.thread.Infer_TransformThread;
import org.rainyday.thread.PMD_TransformThread;
import org.rainyday.thread.SpotBugs_Exec;
import org.rainyday.thread.SpotBugs_TransformThread;

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

    public void executeCheckStyleTransform(String seedFolderPath) {
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        Invoker.invokeCheckStyle(seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<String> seedFilePaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        List<TypeWrapper> initWrappers = new ArrayList<>();
        Map<String, Integer> file2index = new HashMap<>();  // source file -> config index
        for (int i = 0; i < seedFilePaths.size(); i++) {
            String seedFilePath = seedFilePaths.get(i);  // Absolute Path
            String[] tokens = seedFilePath.split(reg_sep);
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
        for (TypeWrapper wrapper : initWrappers) {
            CheckStyle_TransformThread mutationThread = new CheckStyle_TransformThread(wrapper, wrapper.getFolderName(), file2index.get(wrapper.getFilePath()));
            threadPool.submit(mutationThread);
        }
        waitThreadPoolEnding(threadPool);
    }

    public void executeSpotBugsTransform(String seedFolderPath) {
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        Invoker.invokeSpotBugs(seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<String> seedFilePaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        List<String> failSeedPaths = new ArrayList<>();
        List<TypeWrapper> initWrappers = new ArrayList<>();
        for (int index = 0; index < seedFilePaths.size(); index++) {
            String seedFilePath = seedFilePaths.get(index);
            String[] tokens = seedFilePath.split(reg_sep);
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
        if (DEBUG) {
            for (TypeWrapper wrapper : initWrappers) {
                System.out.println("Init Path: " + wrapper.getFilePath());
            }
            System.out.println("Fail Seed Size: " + failSeedPaths.size());
            for (String path : failSeedPaths) {
                System.out.println("Fail Seed Path: " + path);
            }
        }
        if (threadPool == null) {
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

    public void executePMDTransform(String seedFolderPath) {
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        invokePMD(seedFolderPath);
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
        HashMap<String, List<TypeWrapper>> bug2wrapper = new HashMap<>();
        HashMap<String, HashSet<String>> category2bugTypes = new HashMap<>(); // Here, we used HashSet to avoid duplicated bug types.
        int initSeedWrapperSize = 0;
        for (int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            String[] tokens = seedPath.split(reg_sep);
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
        if (THREAD_COUNT > 1) {
            List<PMD_TransformThread> transformThreads = new ArrayList<>();
            ExecutorService threadPool = initThreadPool();
            for (Map.Entry<String, HashSet<String>> entry : category2bugTypes.entrySet()) {
                String category = entry.getKey();
                HashSet<String> bugTypes = entry.getValue();
                for (String bugType : bugTypes) {
                    String seedFolderName = category + "_" + bugType;
                    List<TypeWrapper> wrappers = bug2wrapper.get(seedFolderName);
                    PMD_TransformThread mutationThread = new PMD_TransformThread(wrappers, seedFolderName);
                    transformThreads.add(mutationThread);
                }
            }
            for (int i = 0; i < transformThreads.size(); i++) {
                threadPool.submit(transformThreads.get(i));
            }
            waitThreadPoolEnding(threadPool);
        } else {
            for (Map.Entry<String, HashSet<String>> entry : category2bugTypes.entrySet()) {
                String category = entry.getKey();
                HashSet<String> bugTypes = entry.getValue();
                for (String bugType : bugTypes) {
                    int currentDepth = 0;
                    String seedFolderName = category + "_" + bugType;
                    ArrayDeque<TypeWrapper> wrappers = new ArrayDeque<>() {
                        {
                            addAll(bug2wrapper.get(seedFolderName));  // init by the wrappers in level 0
                        }
                    };
                    for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
                        if (DEBUG) {
                            System.out.println("Seed FolderName: " + seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
                        }
                        singleLevelExplorer(wrappers, currentDepth++);
                        String resultFilePath = reportFolder.getAbsolutePath() + File.separator + "iter" + depth + "_" + seedFolderName + "_Result.json";
                        String mutantFolderPath = mutantFolder + File.separator + "iter" + depth + File.separator + seedFolderName;
                        String[] pmdConfig = {
                                "-d", mutantFolderPath,
                                "-R", "category/java/" + category + ".xml/" + bugType,
                                "-f", "json",
                                "-r", resultFilePath,
                                "--no-cache"
                        };
                        PMD.runPmd(pmdConfig); // detect mutants of level i
                        Utility.readPMDResultFile(resultFilePath);
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
        }
    }

    public void singleThreadWorker(ArrayDeque<TypeWrapper> wrappers) {
        int currentDepth = 0;
        for (int iter = 1; iter <= SEARCH_DEPTH; iter++) {
            singleLevelExplorer(wrappers, currentDepth++);
            for (String subSeedFolderName : subSeedFolderNameList) {
                String subSeedFolderPath = mutantFolder + File.separator + "iter" + iter + File.separator + subSeedFolderName;
                String settingPath = subSeedFolderPath + File.separator + "settings";
                Invoker.writeSettingFile(subSeedFolderPath, settingPath);
                String[] invokeCommands = new String[3];
                if (OSUtil.isWindows()) {
                    invokeCommands[0] = "cmd.exe";
                    invokeCommands[1] = "/c";
                } else {
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                }
                invokeCommands[2] = SONAR_SCANNER_PATH + " -Dsonar.projectBaseDir=" + EVALUATION_PATH + " -Dproject.settings=" + settingPath;
                boolean hasExec = Invoker.invokeCommandsByZT(invokeCommands);
                try {
                    Thread.currentThread().sleep(5000);
                } catch (InterruptedException e) {
                    System.err.println("Interruption error!");
                }
                if (hasExec) {
                    List<String> CNES_Commands = new ArrayList<>();
                    CNES_Commands.add("java");
                    CNES_Commands.add("-jar");
                    CNES_Commands.add("-p");
                    CNES_Commands.add(SONARQUBE_PROJECT_KEY);
                    CNES_Commands.add("-t");
                    CNES_Commands.add(SONARQUBE_LOGIN);
                    CNES_Commands.add("-m");
                    CNES_Commands.add("-w");
                    CNES_Commands.add("-e");
                    CNES_Commands.add("-o");
                    String CNES_ReportFolderPath = "";
                    String reportPath = CNES_ReportFolderPath + File.separator + "CNES_ReportName";
                    Invoker.invokeCommandsByZT(CNES_Commands.toArray(new String[CNES_Commands.size()]));
                    if (DEBUG) {
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

    public void executeSonarQubeTransform(String seedFolderPath) {
        System.out.println("Invoke SonarQube for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        Invoker.invokeSonarQube(seedFolderPath);
        ArrayDeque<TypeWrapper> wrappers = new ArrayDeque<>();
        for (String filepath : file2report.keySet()) {
            String[] tokens = filepath.split(reg_sep);
            String folderName = tokens[tokens.length - 2];
            TypeWrapper wrapper = new TypeWrapper(filepath, folderName);
            wrappers.add(wrapper);
        }
        System.out.println("All Initial Wrapper Size: " + wrappers.size());
        singleThreadWorker(wrappers);
    }

    public void executeInferTransform(String seedFolderPath) {
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        Invoker.invokeInfer(seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
        HashMap<String, List<TypeWrapper>> bug2wrapper = new HashMap<>();
        List<Infer_TransformThread> mutationThreads = new ArrayList<>();
        int initSeedWrapperSize = 0;
        for (int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            String[] tokens = seedPath.split(reg_sep);
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

    public static void writeEvaluationResult() {
        int rules = Utility.compactIssues.keySet().size();
        int seqCount = 0;
        int allValidVariantNumber = 0;
        for (Map.Entry<String, HashMap<String, List<TriTuple>>> entry : Utility.compactIssues.entrySet()) {
            String rule = entry.getKey();
            HashMap<String, List<TriTuple>> seq2mutants = entry.getValue();
            seqCount += seq2mutants.size();
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("Rule", rule);
            root.put("SeqSize", seq2mutants.size());
            ArrayNode bugs = mapper.createArrayNode();
            for (Map.Entry<String, List<TriTuple>> subEntry : seq2mutants.entrySet()) {
                ObjectNode bug = mapper.createObjectNode();
                bug.put("Transform_Sequence", subEntry.getKey());
                ArrayNode tuples = mapper.createArrayNode();
                for (TriTuple triTuple : subEntry.getValue()) {
                    ObjectNode tuple = mapper.createObjectNode();
                    tuple.put("Seed", triTuple.first);
                    tuple.put("Mutant", triTuple.second);
                    tuple.put("BugType", triTuple.third);
                    tuples.add(tuple);
                }
                bug.put("Bugs", tuples);
                bugs.add(bug);
                allValidVariantNumber += subEntry.getValue().size();
            }
            root.put("Results", bugs);
            File jsonFile = new File(Utility.EVALUATION_PATH + File.separator + "results" + File.separator + rule + ".json");
            try {
                if (!jsonFile.exists()) {
                    jsonFile.createNewFile();
                }
                FileWriter jsonWriter = new FileWriter(jsonFile);
                BufferedWriter jsonBufferedWriter = new BufferedWriter(jsonWriter);
                jsonBufferedWriter.write(root.toString());
                jsonBufferedWriter.close();
                jsonWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        List<String> output = new ArrayList<>();
        output.add("All variants size: " + cnt1);
        output.add("Reduced variants size: " + cnt2);
        output.add("Reduction Ratio: " + cnt2.get() / (double) (cnt1.get()));
        output.add("Transformed Seeds: " + transformedSeed++);
        output.add("Successful Transform size: " + successfulT);
        output.add("Failed Transform size: " + failedT);
        output.add("Successful Transform Ratio: " + (successfulT) / (double) (successfulT + failedT));
        output.add("Rule Size: " + rules + "\n");
        output.add("Detected Rules: " + Utility.compactIssues.keySet());
        output.add("Unique Sequence: " + seqCount);
        output.add("Valid Mutant Size (Potential Bug): " + allValidVariantNumber);
        List<String> mutant2seed = new ArrayList<>();
        mutant2seed.add("Mutant2Seed:");
        for (Map.Entry<String, String> entry : TypeWrapper.mutant2seed.entrySet()) {
            mutant2seed.add(entry.getKey() + "->" + entry.getValue() + "#" + TypeWrapper.mutant2seq.get(entry.getKey()));
        }
        writeLinesToFile(EVALUATION_PATH + sep + "mutant2seed.log", mutant2seed);
        if (INFER_MUTATION) {
            writeLinesToFile(EVALUATION_PATH + sep + "FailedReports.log", failedReport);
        }
        if (SPOTBUGS_MUTATION) {
            writeLinesToFile(EVALUATION_PATH + sep + "FailedCommands.log", failedCommands);
        }
        long executionTime = System.currentTimeMillis() - Utility.startTimeStamp;
        output.add(String.format(
                "Overall Execution Time is: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(executionTime),
                        TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))) + "\n")
        );
        writeLinesToFile(EVALUATION_PATH + File.separator + "Output.log", output);
    }

}