package edu.polyu.util;

import static edu.polyu.util.Invoker.compileJavaSourceFile;
import static edu.polyu.util.Invoker.createSonarQubeProject;
import static edu.polyu.util.Invoker.deleteSonarQubeProject;
import static edu.polyu.util.Invoker.failedCommands;
import static edu.polyu.util.Invoker.invokeCheckStyle;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Invoker.invokeCommandsByZTWithOutput;
import static edu.polyu.util.Invoker.invokeFindSecBugs;
import static edu.polyu.util.Invoker.invokeInfer;
import static edu.polyu.util.Invoker.invokePMD;
import static edu.polyu.util.Invoker.invokeSonarQube;
import static edu.polyu.util.Invoker.invokeSpotBugs;
import static edu.polyu.util.Invoker.writeSettingFile;
import static edu.polyu.util.Utility.CHECKSTYLE_PATH;
import static edu.polyu.util.Utility.EVALUATION_PATH;
import static edu.polyu.util.Utility.FINDSECBUGS_PATH;
import static edu.polyu.util.Utility.INFER_MUTATION;
import static edu.polyu.util.Utility.INFER_PATH;
import static edu.polyu.util.Utility.JAVAC_PATH;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.SONARSCANNER_PATH;
import static edu.polyu.util.Utility.SPOTBUGS_MUTATION;
import static edu.polyu.util.Utility.SPOTBUGS_PATH;
import static edu.polyu.util.Utility.SonarQubeRuleNames;
import static edu.polyu.util.Utility.CLASS_FOLDER;
import static edu.polyu.util.Utility.compactIssues;
import static edu.polyu.util.Utility.failedReportPaths;
import static edu.polyu.util.Utility.failedT;
import static edu.polyu.util.Utility.failedToolExecution;
import static edu.polyu.util.Utility.file2row;
import static edu.polyu.util.Utility.getFilePathsFromFolder;
import static edu.polyu.util.Utility.getFilenamesFromFolder;
import static edu.polyu.util.Utility.inferJarStr;
import static edu.polyu.util.Utility.MUTANT_FOLDER;
import static edu.polyu.util.Utility.reg_sep;
import static edu.polyu.util.Utility.REPORT_FOLDER;
import static edu.polyu.util.Utility.sep;
import static edu.polyu.util.Utility.subSeedFolderNameList;
import static edu.polyu.util.Utility.successfulT;
import static edu.polyu.util.Utility.waitTaskEnd;
import static edu.polyu.util.Utility.writeLinesToFile;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import edu.polyu.analysis.TypeWrapper;
import edu.polyu.report.CheckStyleReport;
import edu.polyu.report.FindSecBugsReport;
import edu.polyu.report.InferReport;
import edu.polyu.report.PMDReport;
import edu.polyu.report.SonarQubeReport;
import edu.polyu.report.SpotBugsReport;
import edu.polyu.transform.Transform;
import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import org.json.JSONObject;

/**
 * Description: This file is the main class for our framework
 * Author: RainyD4y
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

    private HashMap<String, List<TypeWrapper>> bug2wrappers = new HashMap<>();

    public void executePMDTransform(String seedFolderPath) {
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
                String ruleCategory = entry.getKey();
                HashSet<String> bugTypes = entry.getValue();
                for (String bugType : bugTypes) {
                    String seedFolderName = ruleCategory + "_" + bugType;
                    List<TypeWrapper> wrappers = new ArrayList<>() {
                        {
                            addAll(bug2wrappers.get(seedFolderName));  // init by the wrappers in level 0
                        }
                    };
                    bug2wrappers.get(seedFolderName).clear();
                    if (DEBUG) {
                        System.out.println("Detection: " + seedFolderName);
                        System.out.println("Seed FolderName: " + seedFolderName + " Depth: " + depth + " Wrapper Size: " + wrappers.size());
                    }
                    List<TypeWrapper> newWrappers = Transform.singleLevelExplorer(wrappers);
                    String resultFilePath = REPORT_FOLDER.getAbsolutePath() + sep + "iter" + depth + "_" + seedFolderName + "_Result.json";
                    String mutantFolderPath = MUTANT_FOLDER + sep + "iter" + depth + sep + seedFolderName;
                    PMDConfiguration pmdConfig = new PMDConfiguration();
                    pmdConfig.setInputPathList(getFilePathsFromFolder(mutantFolderPath));
                    pmdConfig.setRuleSets(new ArrayList<>() {
                        {
                            add("category/java/" + ruleCategory + ".xml/" + bugType);
                        }
                    });
                    pmdConfig.setReportFormat("json");
                    pmdConfig.setReportFile(Paths.get(resultFilePath));
                    pmdConfig.setIgnoreIncrementalAnalysis(true);
                    PMD.runPmd(pmdConfig); // detect mutants of level i
                    PMDReport.readPMDResultFile(resultFilePath);
                    List<TypeWrapper> validWrappers = new ArrayList<>();
                    for (int i = 0; i < newWrappers.size(); i++) {
                        TypeWrapper newWrapper = newWrappers.get(i);
                        if (!newWrapper.isBuggy()) {
                            validWrappers.add(newWrapper);
                        }
                    }
                    if (bug2wrappers.get(seedFolderName).size() > 0) {
                        System.err.println("Exception in Seed Size.");
                        System.exit(-1);
                    }
                    bug2wrappers.get(seedFolderName).addAll(validWrappers);
                }
            }
        }
    }

    public void executeSpotBugsTransform(String initSeedFolderPath) {
        invokeSpotBugs(initSeedFolderPath);
        List<String> seedFilePaths = getFilenamesFromFolder(initSeedFolderPath, true);
        System.out.println("All Initial Seed Summary Number: " + seedFilePaths.size());
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
            if (!bug2wrappers.containsKey(seedWrapper.getFolderName())) {
                bug2wrappers.put(seedWrapper.getFolderName(), new ArrayList<>());
            }
            bug2wrappers.get(seedWrapper.getFolderName()).add(seedWrapper);
        }
        System.out.println("Initial Valid Wrappers Size: " + initValidSeedWrapperSize);
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            for (Map.Entry<String, List<TypeWrapper>> entry : bug2wrappers.entrySet()) {
                List<TypeWrapper> seedWrappers = new ArrayList<>();
                seedWrappers.addAll(entry.getValue());
                entry.getValue().clear();
                List<TypeWrapper> mutantWrappers = Transform.singleLevelExplorer(seedWrappers);
                for (int wrapperIndex = 0; wrapperIndex < mutantWrappers.size(); wrapperIndex++) {
                    TypeWrapper mutantWrapper = mutantWrappers.get(wrapperIndex);
                    String[] tokens = mutantWrapper.getFilePath().split(reg_sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String subSeedFolderName = tokens[tokens.length - 2];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // Filename is used to specify class folder name
                    File mutantClassFolder = new File(CLASS_FOLDER.getAbsolutePath() + sep + seedFileName);
                    if (!mutantClassFolder.exists()) {
                        mutantClassFolder.mkdirs();
                    }
                    boolean isCompiled = compileJavaSourceFile(mutantWrapper.getFolderPath(), seedFileNameWithSuffix, mutantClassFolder.getAbsolutePath());
                    if (!isCompiled) {
                        continue;
                    }
                    String reportPath = REPORT_FOLDER.getAbsolutePath() + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
                    String[] invokeCommands = new String[3];
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                    invokeCommands[2] = SPOTBUGS_PATH + " -textui"
//                            + " -include " + configPath
                            + " -xml:withMessages" + " -output " + reportPath + " "
                            + mutantClassFolder.getAbsolutePath();
                    boolean hasExec = Invoker.invokeCommandsByZT(invokeCommands);
                    if (hasExec) {
                        String report_path = REPORT_FOLDER.getAbsolutePath() + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
                        SpotBugsReport.readSpotBugsResultFile(mutantWrapper.getFolderPath(), report_path);
                        if (!mutantWrapper.isBuggy()) {
                            entry.getValue().add(mutantWrapper);
                        }
                    } else {
                        failedToolExecution.add(invokeCommands[2]);
                    }
                }
            }
        }
    }

    public static Map<String, String> file2config = new HashMap<>();  // source file -> config index

    public void executeCheckStyleTransform(String initSeedFolderPath) {
        invokeCheckStyle(initSeedFolderPath);
        List<String> seedFilePaths = getFilenamesFromFolder(initSeedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
        for (int i = 0; i < seedFilePaths.size(); i++) {
            String seedFilePath = seedFilePaths.get(i);  // Absolute Path
            String[] tokens = seedFilePath.split(reg_sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedFilePath)) {  // Check whether this seed has warning
                continue;
            }
            initValidSeedWrapperSize++;
            TypeWrapper seedWrapper = new TypeWrapper(seedFilePath, seedFolderName);
            if (!bug2wrappers.containsKey(seedWrapper.getFolderName())) {
                bug2wrappers.put(seedWrapper.getFolderName(), new ArrayList<>());
            }
            bug2wrappers.get(seedWrapper.getFolderName()).add(seedWrapper);
        }
        System.out.println("Initial Valid Wrappers Size: " + initValidSeedWrapperSize);
        Set<String> visitedPaths = new HashSet<>();
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            for (Map.Entry<String, List<TypeWrapper>> entry : bug2wrappers.entrySet()) {
                List<TypeWrapper> wrappers = new ArrayList<>();
                wrappers.addAll(entry.getValue());
                entry.getValue().clear();
                List<TypeWrapper> newWrappers = Transform.singleLevelExplorer(wrappers);
                for (int wrapperIndex = 0; wrapperIndex < newWrappers.size(); wrapperIndex++) {
                    TypeWrapper wrapper = newWrappers.get(wrapperIndex);
                    String mutantFilePath = wrapper.getFilePath();
                    if (visitedPaths.contains(mutantFilePath)) {
                        System.err.println("Error in visiting...");
                        System.exit(-1);
                    }
                    visitedPaths.add(mutantFilePath);
                    File reportFile = new File(REPORT_FOLDER + sep + "iter" + depth + "_" + wrapper.getFileName() + ".txt");
                    String[] invokeCommands = new String[3];
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                    String configPath = file2config.get(wrapper.getInitSeedPath());
                    invokeCommands[2] = "java -jar " + CHECKSTYLE_PATH + " -f" + " plain" + " -o " + reportFile.getAbsolutePath() + " -c " + configPath + " " + mutantFilePath;
                    invokeCommandsByZT(invokeCommands);
                    if (DEBUG) {
                        System.out.println(invokeCommands[2]);
                    }
                    if (!reportFile.exists() || reportFile.length() == 0) {
                        failedToolExecution.add(invokeCommands[2]);
                    }
                    CheckStyleReport.readCheckStyleResultFile(reportFile.getAbsolutePath());
                    if (!wrapper.isBuggy()) {
                        bug2wrappers.get(entry.getKey()).add(wrapper);
                    }
                }
            }
        }
    }

    public void executeInferTransform(String initSeedFolderPath) {
        invokeInfer(initSeedFolderPath);
        List<String> seedPaths = getFilenamesFromFolder(initSeedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
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
            if (!bug2wrappers.containsKey(seedFolderName)) {
                bug2wrappers.put(seedFolderName, new ArrayList<>());
            }
            bug2wrappers.get(seedFolderName).add(seedWrapper);
        }
        System.out.println("Initial Wrappers Size: " + initSeedWrapperSize);
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            for (Map.Entry<String, List<TypeWrapper>> entry : bug2wrappers.entrySet()) {
                List<TypeWrapper> wrappers = new ArrayList<>();
                wrappers.addAll(entry.getValue());
                entry.getValue().clear();
                List<TypeWrapper> mutantWrappers = Transform.singleLevelExplorer(wrappers);
                for (int i = 0; i < mutantWrappers.size(); i++) {
                    TypeWrapper mutantWrapper = mutantWrappers.get(i);
                    String mutantPath = mutantWrapper.getFilePath();
                    String mutantFileName = mutantWrapper.getFileName();
                    String REPORT_FOLDERPath = REPORT_FOLDER + sep + "iter" + depth + "_" + mutantFileName;
                    String cmd = INFER_PATH + " run -o " + "" + REPORT_FOLDERPath + " -- " + JAVAC_PATH +
                            " -d " + CLASS_FOLDER.getAbsolutePath() + sep + mutantFileName +
                            " -cp " + inferJarStr + " " + mutantPath;
                    String[] invokeCommands = new String[3];
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                    invokeCommands[2] = "python3 cmd.py " + cmd;
                    Invoker.invokeCommandsByZT(invokeCommands);
                    String resultFilePath = REPORT_FOLDERPath + sep + "report.json";
                    InferReport.readSingleInferResultFile(mutantPath, resultFilePath);
                    if (!mutantWrapper.isBuggy()) {
                        entry.getValue().add(mutantWrapper);
                    }
                }
            }
        }
    }

    public void executeSonarQubeTransform(String initSeedFolderPath) {
        invokeSonarQube(initSeedFolderPath);
        ArrayDeque<TypeWrapper> wrappers = new ArrayDeque<>();
        for (String filepath : file2row.keySet()) {
            String[] tokens = filepath.split(reg_sep);
            String folderName = tokens[tokens.length - 2];
            TypeWrapper wrapper = new TypeWrapper(filepath, folderName);
            wrappers.add(wrapper);
        }
        System.out.println("All Initial Wrappers Size: " + wrappers.size());
        int currentDepth = 0;
        for (int iter = 1; iter <= SEARCH_DEPTH; iter++) {
            // Before invocation: wrappers includes type wrappers in iter
            // After invocation: wrappers includes type wrappers in iter + 1
            Transform.singleLevelExplorer(wrappers, currentDepth++);
            for (String subSeedFolderName : subSeedFolderNameList) {
                String subSeedFolderPath = MUTANT_FOLDER + sep + "iter" + iter + sep + subSeedFolderName;
                if (DEBUG) {
                    System.out.println("Seed path: " + subSeedFolderPath);
                }
                String settingPath = subSeedFolderPath + sep + "settings";
                String newProjectName = "iter" + iter + "_" + subSeedFolderName;
                deleteSonarQubeProject(newProjectName);
                boolean isCreated = createSonarQubeProject(newProjectName);
                if (!isCreated) {
                    System.err.println("ProjectName: " + newProjectName + " is not created!");
                    continue;
                }
                writeSettingFile(subSeedFolderPath, settingPath, newProjectName);
                String[] invokeCommands = new String[3];
                invokeCommands[0] = "/bin/bash";
                invokeCommands[1] = "-c";
                invokeCommands[2] = SONARSCANNER_PATH + " -Dsonar.projectBaseDir=" + EVALUATION_PATH + " -Dproject.settings=" + settingPath;
                boolean hasExec = invokeCommandsByZT(invokeCommands);
                if (hasExec) {
                    waitTaskEnd(newProjectName);
                } else {
                    return;
                }
                List<String> ruleNames = new ArrayList<>(SonarQubeRuleNames);
                String[] curlCommands = new String[4];
                for (int i = 0; i < ruleNames.size(); i++) {
                    String ruleName = ruleNames.get(i);
                    if (DEBUG) {
                        System.out.println("Request rule: " + ruleName);
                    }
                    curlCommands[0] = "curl";
                    curlCommands[1] = "-u";
                    curlCommands[2] = "admin:123456";
                    curlCommands[3] = "http://localhost:9000/api/issues/search?p=1&ps=500&componentKeys=" + newProjectName + "&rules=java:" + ruleName;
                    String jsonContent = invokeCommandsByZTWithOutput(curlCommands);
                    SonarQubeReport.readSonarQubeResultFile(ruleName, jsonContent);
                    JSONObject root = new JSONObject(jsonContent);
                    int total = root.getInt("total");
                    int count = total % 500 == 0 ? total / 500 : total / 500 + 1;
                    for (int p = 2; p <= count; p++) {
                        curlCommands[0] = "curl";
                        curlCommands[1] = "-u";
                        curlCommands[2] = "admin:123456";
                        curlCommands[3] = "http://localhost:9000/api/issues/search?p=" + p + "&ps=500&componentKeys=" + newProjectName + "&rules=java:" + ruleName;
                        jsonContent = invokeCommandsByZTWithOutput(curlCommands);
                        SonarQubeReport.readSonarQubeResultFile(ruleName, jsonContent);
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

    public void executeFindSecBugsTransform(String initSeedFolderPath) {
        invokeFindSecBugs(initSeedFolderPath);
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
            if (!bug2wrappers.containsKey(seedWrapper.getFolderName())) {
                bug2wrappers.put(seedWrapper.getFolderName(), new ArrayList<>());
            }
            bug2wrappers.get(seedWrapper.getFolderName()).add(seedWrapper);
        }
        System.out.println("Size of Initial Valid Wrappers: " + initValidSeedWrapperSize);
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            for (Map.Entry<String, List<TypeWrapper>> entry : bug2wrappers.entrySet()) {
                List<TypeWrapper> seedWrappers = new ArrayList<>();
                seedWrappers.addAll(entry.getValue());
                entry.getValue().clear();
                List<TypeWrapper> mutantWrappers = Transform.singleLevelExplorer(seedWrappers);
                for (int wrapperIndex = 0; wrapperIndex < mutantWrappers.size(); wrapperIndex++) {
                    TypeWrapper mutantWrapper = mutantWrappers.get(wrapperIndex);
                    String[] tokens = mutantWrapper.getFilePath().split(reg_sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String subSeedFolderName = tokens[tokens.length - 2];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // File Name is used to specify class folder name
                    File mutantClassFolder = new File(CLASS_FOLDER.getAbsolutePath() + sep + seedFileName);
                    if (!mutantClassFolder.exists()) {
                        mutantClassFolder.mkdirs();
                    }
                    boolean isCompiled = compileJavaSourceFile(mutantWrapper.getFolderPath(), seedFileNameWithSuffix, mutantClassFolder.getAbsolutePath());
                    if (!isCompiled) {
                        continue;
                    }
                    String reportPath = REPORT_FOLDER.getAbsolutePath() + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
                    String[] invokeCommands = new String[3];
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                    invokeCommands[2] = FINDSECBUGS_PATH + " -xml -output " + reportPath + " " + mutantClassFolder.getAbsolutePath();
                    boolean hasExec = Invoker.invokeCommandsByZT(invokeCommands);
                    if (hasExec) {
                        String report_path = REPORT_FOLDER.getAbsolutePath() + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
                        FindSecBugsReport.readFindSecBugsResultFile(mutantWrapper.getFolderPath(), report_path);
                        if (!mutantWrapper.isBuggy()) {
                            entry.getValue().add(mutantWrapper);
                        }
                    } else {
                        failedToolExecution.add(invokeCommands[2]);
                    }
                }
            }
        }
    }


    public static void writeEvaluationResult() {
        int rules = compactIssues.keySet().size();
        int seqCount = 0;
        int allValidVariantNumber = 0;
        for (Map.Entry<String, HashMap<String, List<TriTuple>>> entry : compactIssues.entrySet()) {
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
            File jsonFile = new File(Utility.EVALUATION_PATH + sep + "results" + sep + rule + ".json");
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
        output.add("All Variants Size: " + Transform.cnt1);
        output.add("Reduced variants Size: " + Transform.cnt2);
        output.add("Reduction Ratio: " + Transform.cnt2.get() / (double) (Transform.cnt1.get()));
        output.add("Transformed Seeds: " + TypeWrapper.transformedSeed++);
        output.add("Successful Transform Size: " + successfulT);
        output.add("Failed Transform Size: " + failedT);
        output.add("Successful Transform Ratio: " + (successfulT) / (double) (successfulT + failedT));
        output.add("Rule Size: " + rules + "\n");
        output.add("Detected Rules: " + compactIssues.keySet());
        output.add("Unique Sequence: " + seqCount);
        output.add("Valid Mutant Size (Potential Bug): " + allValidVariantNumber);
        List<String> mutant2seed = new ArrayList<>();
        mutant2seed.add("Mutant2Seed:");
        for (Map.Entry<String, String> entry : TypeWrapper.mutant2seed.entrySet()) {
            mutant2seed.add(entry.getKey() + "->" + entry.getValue() + "#" + TypeWrapper.mutant2seq.get(entry.getKey()));
        }
        writeLinesToFile(EVALUATION_PATH + sep + "mutant2seed.log", mutant2seed);
        if (INFER_MUTATION) {
            writeLinesToFile(EVALUATION_PATH + sep + "FailedReports.log", failedReportPaths);
        }
        if (SPOTBUGS_MUTATION) {
            writeLinesToFile(EVALUATION_PATH + sep + "FailedCommands.log", failedCommands);
            writeLinesToFile(EVALUATION_PATH + sep + "FailedToolExecution.log", failedToolExecution);
        }
        long executionTime = System.currentTimeMillis() - Utility.startTimeStamp;
        output.add(String.format(
                "Overall Execution Time is: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(executionTime),
                        TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))) + "\n")
        );
        writeLinesToFile(EVALUATION_PATH + sep + "Output.log", output);
        if (TypeWrapper.failedParse.size() > 0) {
            writeLinesToFile(EVALUATION_PATH + sep + "FailedParse.log", TypeWrapper.failedParse);
        }
        if (PMDReport.errorReportPaths.size() > 0) {
            writeLinesToFile(EVALUATION_PATH + sep + "ErrorReportPaths.log", PMDReport.errorReportPaths);
        }
    }

}
