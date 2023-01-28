package org.detector.util;

import static org.detector.analysis.TypeWrapper.failedParse;
import static org.detector.report.SpotBugs_Report.readSpotBugsResultFile;
import static org.detector.transform.Transform.cnt1;
import static org.detector.transform.Transform.cnt2;
import static org.detector.transform.Transform.singleLevelExplorer;
import static org.detector.util.Invoker.compileJavaSourceFile;
import static org.detector.util.Invoker.createSonarQubeProject;
import static org.detector.util.Invoker.deleteSonarQubeProject;
import static org.detector.util.Invoker.failedCommands;
import static org.detector.util.Invoker.invokeCheckStyle;
import static org.detector.util.Invoker.invokeCommandsByZT;
import static org.detector.util.Invoker.invokeCommandsByZTWithOutput;
import static org.detector.util.Invoker.invokePMD;
import static org.detector.util.Invoker.invokeSonarQube;
import static org.detector.util.Invoker.invokeSpotBugs;
import static org.detector.util.Invoker.writeSettingFile;
import static org.detector.util.Utility.CHECKSTYLE_PATH;
import static org.detector.util.Utility.CheckStyleConfigPath;
import static org.detector.util.Utility.EVALUATION_PATH;
import static org.detector.util.Utility.INFER_MUTATION;
import static org.detector.util.Utility.Path2Last;
import static org.detector.util.Utility.SEARCH_DEPTH;
import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.SONARQUBE_LOGIN;
import static org.detector.util.Utility.SONARQUBE_PROJECT_KEY;
import static org.detector.util.Utility.SONAR_SCANNER_PATH;
import static org.detector.util.Utility.SPOTBUGS_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_PATH;
import static org.detector.util.Utility.SonarQubeRuleNames;
import static org.detector.util.Utility.THREAD_COUNT;
import static org.detector.util.Utility.failedReport;
import static org.detector.util.Utility.failedT;
import static org.detector.util.Utility.file2bugs;
import static org.detector.util.Utility.file2report;
import static org.detector.util.Utility.file2row;
import static org.detector.util.Utility.getFilenamesFromFolder;
import static org.detector.util.Utility.initThreadPool;
import static org.detector.util.Utility.listAveragePartition;
import static org.detector.util.Utility.mutantFolder;
import static org.detector.util.Utility.noReport;
import static org.detector.util.Utility.reg_sep;
import static org.detector.util.Utility.reportFolder;
import static org.detector.util.Utility.sep;
import static org.detector.util.Utility.subSeedFolderNameList;
import static org.detector.util.Utility.successfulT;
import static org.detector.util.Utility.waitTaskEnd;
import static org.detector.util.Utility.waitThreadPoolEnding;
import static org.detector.util.Utility.writeLinesToFile;
import static org.detector.analysis.TypeWrapper.transformedSeed;

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
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sourceforge.pmd.PMD;
import org.apache.commons.io.FileUtils;
import org.detector.analysis.TypeWrapper;
import org.detector.report.SonarQube_Report;
import org.detector.thread.CheckStyle_TransformThread;
import org.detector.thread.Infer_TransformThread;
import org.detector.thread.PMD_TransformThread;
import org.detector.thread.SpotBugs_Exec;
import org.detector.thread.SpotBugs_TransformThread;
import org.detector.transform.Transform;
import org.json.JSONObject;

/**
 * Description: This file is the main class for our framework
 * Author: RainyD4y
 * Date: 2021/8/25 10:03
 */
public class Schedule {

    // Implement singleton pattern
    private static final Schedule tester = new Schedule();

    private Schedule() {}

    public static Schedule getInstance() {
        return tester;
    }

    private HashMap<String, List<TypeWrapper>> bug2wrappers = new HashMap<>();

    public static Map<String, String> file2config = new HashMap<>();  // source file -> config index
    public void executeCheckStyleTransform(String seedFolderPath) {
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        invokeCheckStyle(seedFolderPath);
        ExecutorService threadPool = initThreadPool();
        List<String> seedFilePaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        List<TypeWrapper> initWrappers = new ArrayList<>();
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
//            int configIndex = Character.getNumericValue(tokens[tokens.length - 1].charAt(tokens[tokens.length - 1].indexOf(".") - 1));
//            File configFile = new File(CheckStyleConfigPath + sep + seedFolderName + configIndex + ".xml");
//            if(configFile.exists()) {
//                file2config.put(seedFilePath, configFile.getAbsolutePath());
//            } else {
//                file2config.put(seedFilePath, CheckStyleConfigPath + sep + seedFolderName + 0 + ".xml");
//            }
        }
        System.out.println("Initial Valid Wrappers Size: " + initValidSeedWrapperSize);
        if(THREAD_COUNT > 1) {
            for (TypeWrapper wrapper : initWrappers) {
                CheckStyle_TransformThread mutationThread = new CheckStyle_TransformThread(wrapper, wrapper.getFolderName(), file2config.get(wrapper.getFilePath()));
                threadPool.submit(mutationThread);
            }
            waitThreadPoolEnding(threadPool);
        } else {
            Set<String> visitedPaths = new HashSet<>();
            for(int wrapperIndex = 0; wrapperIndex < initWrappers.size(); wrapperIndex++) {
                TypeWrapper wrapper = initWrappers.get(wrapperIndex);
                // 设置一次config path，后边每一层都用这个
                String configPath = file2config.get(wrapper.getFilePath());
                int currentDepth = 0;
                String seedFolderName = wrapper.getFolderName();
                ArrayDeque<TypeWrapper> wrappers = new ArrayDeque<>() {
                    {
                        add(wrapper);
                    }
                };
                for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
                    if (DEBUG) {
                        System.out.println("Transform Thread Depth: " + depth + " Folder: " + seedFolderName);
                    }
                    singleLevelExplorer(wrappers, currentDepth++);
                    // detect mutants of iter i
                    String mutantFolderPath = mutantFolder + sep + "iter" + depth + sep + seedFolderName;
                    List<String> mutantFilePaths = getFilenamesFromFolder(mutantFolderPath, true);
                    for (int i = 0; i < mutantFilePaths.size(); i++) {
                        String mutantFilePath = mutantFilePaths.get(i);
                        if(visitedPaths.contains(mutantFilePath)) {
                            continue;
                        }
                        visitedPaths.add(mutantFilePath);
                        String mutantFileName = Path2Last(mutantFilePath);
                        String reportFilePath = reportFolder + sep + "iter" + depth + "_" + mutantFileName + ".txt";
                        String[] invokeCommands = new String[3];
                        if (OSUtil.isWindows()) {
                            invokeCommands[0] = "cmd.exe";
                            invokeCommands[1] = "/c";
                        } else {
                            invokeCommands[0] = "/bin/bash";
                            invokeCommands[1] = "-c";
                        }
                        invokeCommands[2] = "java -jar " + CHECKSTYLE_PATH + " -f" + " plain" + " -o " + reportFilePath + " -c "
                                + configPath + " " + mutantFilePath;
                        if (DEBUG) {
                            System.out.println(invokeCommands[2]);
                        }
                        Invoker.invokeCommandsByZT(invokeCommands);
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
    }

    public void executeSpotBugsTransform(String initSeedFolderPath) {
        System.out.println("Invoke Analyzer for " + initSeedFolderPath + " and Analysis Output Folder is: " + Path2Last(initSeedFolderPath) + ", Depth=0");
        invokeSpotBugs(initSeedFolderPath);
        List<String> seedFilePaths = getFilenamesFromFolder(initSeedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        for (int index = 0; index < seedFilePaths.size(); index++) {
            String seedFilePath = seedFilePaths.get(index);
            String[] tokens = seedFilePath.split(reg_sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedFilePath)) {
                continue;
            }
            initValidSeedWrapperSize++;
            TypeWrapper seedWrapper = new TypeWrapper(seedFilePath, seedFolderName);
            if(!bug2wrappers.containsKey(seedWrapper.getFolderName())) {
                bug2wrappers.put(seedWrapper.getFolderName(), new ArrayList<>());
            }
            bug2wrappers.get(seedWrapper.getFolderName()).add(seedWrapper);
        }
        System.out.println("Initial Valid Wrappers Size: " + initValidSeedWrapperSize);
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            for (Map.Entry<String, List<TypeWrapper>> entry : bug2wrappers.entrySet()) {
                List<TypeWrapper> wrappers = new ArrayList<>();
                wrappers.addAll(entry.getValue());
                entry.getValue().clear();
                List<TypeWrapper> newWrappers = singleLevelExplorer(wrappers);
                for (int j = newWrappers.size() - 1; j >= 0; j--) {
                    TypeWrapper wrapper = newWrappers.get(j);
                    String seedFilePath = wrapper.getFilePath();
                    String seedFolderPath = wrapper.getFolderPath();
                    String[] tokens = seedFilePath.split(reg_sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String subSeedFolderName = tokens[tokens.length - 2];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // Filename is used to specify class folder name
                    File classFolder = new File(Utility.classFolder.getAbsolutePath() + sep + seedFileName);
                    if (!classFolder.exists()) {
                        classFolder.mkdirs();
                    }
                    boolean isCompiled = compileJavaSourceFile(seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
                    if(!isCompiled) { // Failed compilation point
                        noReport.add(wrapper.getFilePath());
                        continue;
                    }
                    String reportPath = reportFolder.getAbsolutePath() + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
                    String[] invokeCommands = new String[3];
                    if (OSUtil.isWindows()) {
                        invokeCommands[0] = "cmd.exe";
                        invokeCommands[1] = "/c";
                    } else {
                        invokeCommands[0] = "/bin/bash";
                        invokeCommands[1] = "-c";
                    }
                    invokeCommands[2] = SPOTBUGS_PATH + " -textui"
//                            + " -include " + configPath
                            + " -xml:withMessages" + " -output " + reportPath + " "
                            + classFolder.getAbsolutePath();
                    boolean hasExec = Invoker.invokeCommandsByZT(invokeCommands);
                    if (hasExec) {
                        String report_path = reportFolder.getAbsolutePath() + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
                        readSpotBugsResultFile(wrapper.getFolderPath(), report_path);
                    }
                }
                List<TypeWrapper> validWrappers = new ArrayList<>();
                while (!newWrappers.isEmpty()) {
                    TypeWrapper head = newWrappers.get(0);
                    newWrappers.remove(0);
                    if(noReport.contains(head.getFilePath())) {
                        continue;
                    }
                    if (!head.isBuggy()) {
                        validWrappers.add(head);
                    }
                }
                entry.getValue().addAll(validWrappers);
            }
        }
    }

    public void executePMDTransform(String seedFolderPath) {
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + Path2Last(seedFolderPath) + ", Depth=0");
        invokePMD(seedFolderPath);
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());

        HashMap<String, HashSet<String>> category2bugTypes = new HashMap<>(); // Here, we used HashSet to avoid duplicated bug types.
        int initSeedWrapperSize = 0;
        for (int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            if (!file2row.containsKey(seedPath)) {
                continue;
            }
            String[] tokens = seedPath.split(reg_sep);
            String seedFolderName = tokens[tokens.length - 2];
            String category = seedFolderName.split("_")[0];
            String bugType = seedFolderName.split("_")[1];
            if (category2bugTypes.containsKey(category)) {
                category2bugTypes.get(category).add(bugType);
            } else {
                HashSet<String> types = new HashSet<>();
                types.add(bugType);
                category2bugTypes.put(category, types);
            }
            initSeedWrapperSize++;
            TypeWrapper seedWrapper = new TypeWrapper(seedPath, seedFolderName);
            if (bug2wrappers.containsKey(seedFolderName)) {
                bug2wrappers.get(seedFolderName).add(seedWrapper);
            } else {
                List<TypeWrapper> wrappers = new ArrayList<>();
                wrappers.add(seedWrapper);
                bug2wrappers.put(seedFolderName, wrappers);
            }
        }
        System.out.println("Initial Wrappers Size: " + initSeedWrapperSize);
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            for (Map.Entry<String, HashSet<String>> entry : category2bugTypes.entrySet()) {
                String category = entry.getKey();
                HashSet<String> bugTypes = entry.getValue();
                for (String bugType : bugTypes) {
                    String seedFolderName = category + "_" + bugType;
                    List<TypeWrapper> wrappers = new ArrayList<>() {
                        {
                            addAll(bug2wrappers.get(seedFolderName));  // init by the wrappers in level 0
                        }
                    };
                    bug2wrappers.get(seedFolderName).clear();
                    if(DEBUG) {
                        System.out.println("Detection: " + seedFolderName);
                        System.out.println("Seed FolderName: " + seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
                    }
                    List<TypeWrapper> newWrappers = singleLevelExplorer(wrappers);
                    String resultFilePath = reportFolder.getAbsolutePath() + File.separator + "iter" + depth + "_" + seedFolderName + "_Result.json";
                    String mutantFolderPath = mutantFolder + File.separator + "iter" + depth + File.separator + seedFolderName;
                    String[] pmdConfig = {
                            "-d", mutantFolderPath,
                            "-R", "category/java/" + category + ".xml/" + bugType,
                            "-f", "json",
                            "-r", resultFilePath
                    };
                    PMD.runPmd(pmdConfig); // detect mutants of level i
                    Utility.readPMDResultFile(resultFilePath);
                    List<TypeWrapper> validWrappers = new ArrayList<>();
                    for(int i = 0; i < newWrappers.size(); i++) {
                        TypeWrapper newWrapper = newWrappers.get(i);
                        if(!newWrapper.isBuggy()) {
                            validWrappers.add(newWrapper);
                        }
                    }
                    if(bug2wrappers.get(seedFolderName).size() > 0) {
                        System.err.println("Exception in Seed Size.");
                        System.exit(-1);
                    }
                    bug2wrappers.get(seedFolderName).addAll(validWrappers);
                }
            }
        }
    }

    public void singleThreadWorker(ArrayDeque<TypeWrapper> wrappers) {
        int currentDepth = 0;
        for (int iter = 1; iter <= SEARCH_DEPTH; iter++) {
            // Before invocation: wrappers includes type wrappers in iter
            // After invocation: wrappers includes type wrappers in iter + 1
            singleLevelExplorer(wrappers, currentDepth++);
            for (String subSeedFolderName : subSeedFolderNameList) {
                String subSeedFolderPath = mutantFolder + File.separator + "iter" + iter + File.separator + subSeedFolderName;
                if(DEBUG) {
                    System.out.println("Seed path: " + subSeedFolderPath);
                }
                String settingPath = subSeedFolderPath + File.separator + "settings";
                String newProjectName = "iter" + iter + "_" + subSeedFolderName;
                deleteSonarQubeProject(newProjectName);
                boolean isCreated = createSonarQubeProject(newProjectName);
                if(!isCreated) {
                    System.err.println("ProjectName: " + newProjectName + " is not created!");
                    continue;
                }
                writeSettingFile(subSeedFolderPath, settingPath, newProjectName);
                String[] invokeCommands = new String[3];
                if (OSUtil.isWindows()) {
                    invokeCommands[0] = "cmd.exe";
                    invokeCommands[1] = "/c";
                } else {
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                }
                invokeCommands[2] = SONAR_SCANNER_PATH + " -Dsonar.projectBaseDir=" + EVALUATION_PATH + " -Dproject.settings=" + settingPath;
                boolean hasExec = invokeCommandsByZT(invokeCommands);
                if(hasExec) {
                    waitTaskEnd(newProjectName);
                } else {
                    return;
                }
                List<String> ruleNames = new ArrayList<>(SonarQubeRuleNames);
                String[] curlCommands = new String[4];
                for (int i = 0; i < ruleNames.size(); i++) {
                    String ruleName = ruleNames.get(i);
                    if(DEBUG) {
                        System.out.println("Request rule: " + ruleName);
                    }
                    curlCommands[0] = "curl";
                    curlCommands[1] = "-u";
                    curlCommands[2] = "admin:123456";
                    curlCommands[3] = "http://localhost:9000/api/issues/search?p=1&ps=500&componentKeys=" + newProjectName + "&rules=java:" + ruleName;
                    String jsonContent = invokeCommandsByZTWithOutput(curlCommands);
                    SonarQube_Report.readSonarQubeResultFile(ruleName, jsonContent);
                    JSONObject root = new JSONObject(jsonContent);
                    int total = root.getInt("total");
                    int count = total % 500 == 0 ? total / 500 : total / 500 + 1;
                    for (int p = 2; p <= count; p++) {
                        curlCommands[0] = "curl";
                        curlCommands[1] = "-u";
                        curlCommands[2] = "admin:123456";
                        curlCommands[3] = "http://localhost:9000/api/issues/search?p=" + p + "&ps=500&componentKeys=" + newProjectName + "&rules=java:" + ruleName;
                        jsonContent = invokeCommandsByZTWithOutput(curlCommands);
                        SonarQube_Report.readSonarQubeResultFile(ruleName, jsonContent);
                    }
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
        invokeSonarQube(seedFolderPath);
        ArrayDeque<TypeWrapper> wrappers = new ArrayDeque<>();
        for (String filepath : file2row.keySet()) {
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
        output.add("All Variants Size: " + cnt1);
        output.add("Reduced variants Size: " + cnt2);
        output.add("Reduction Ratio: " + cnt2.get() / (double) (cnt1.get()));
        output.add("Transformed Seeds: " + transformedSeed++);
        output.add("Successful Transform Size: " + successfulT);
        output.add("Failed Transform Size: " + failedT);
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
        writeLinesToFile(EVALUATION_PATH + sep + "FailedParse.log", failedParse);
    }

}
