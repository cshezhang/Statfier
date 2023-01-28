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
import static org.detector.util.Invoker.invokeInfer;
import static org.detector.util.Invoker.invokePMD;
import static org.detector.util.Invoker.invokeSonarQube;
import static org.detector.util.Invoker.invokeSpotBugs;
import static org.detector.util.Invoker.writeSettingFile;
import static org.detector.util.Utility.CHECKSTYLE_PATH;
import static org.detector.util.Utility.EVALUATION_PATH;
import static org.detector.util.Utility.INFER_MUTATION;
import static org.detector.util.Utility.INFER_PATH;
import static org.detector.util.Utility.JAVAC_PATH;
import static org.detector.util.Utility.Path2Last;
import static org.detector.util.Utility.SEARCH_DEPTH;
import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.SONAR_SCANNER_PATH;
import static org.detector.util.Utility.SPOTBUGS_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_PATH;
import static org.detector.util.Utility.SonarQubeRuleNames;
import static org.detector.util.Utility.classFolder;
import static org.detector.util.Utility.failedReport;
import static org.detector.util.Utility.failedT;
import static org.detector.util.Utility.file2row;
import static org.detector.util.Utility.getFilenamesFromFolder;
import static org.detector.util.Utility.inferJarStr;
import static org.detector.util.Utility.mutantFolder;
import static org.detector.util.Utility.readCheckStyleResultFile;
import static org.detector.util.Utility.readInferResultFile;
import static org.detector.util.Utility.reg_sep;
import static org.detector.util.Utility.reportFolder;
import static org.detector.util.Utility.sep;
import static org.detector.util.Utility.subSeedFolderNameList;
import static org.detector.util.Utility.successfulT;
import static org.detector.util.Utility.waitTaskEnd;
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
import java.util.concurrent.TimeUnit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sourceforge.pmd.PMD;
import org.detector.analysis.TypeWrapper;
import org.detector.report.SonarQube_Report;
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
        List<String> seedFilePaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedFilePaths.size());
        int initValidSeedWrapperSize = 0;
//        List<TypeWrapper> initWrappers = new ArrayList<>();
        for (int i = 0; i < seedFilePaths.size(); i++) {
            String seedFilePath = seedFilePaths.get(i);  // Absolute Path
            String[] tokens = seedFilePath.split(reg_sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2row.containsKey(seedFilePath)) {  // Check whether this seed has warning
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
        Set<String> visitedPaths = new HashSet<>();
        for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
            for(Map.Entry<String, List<TypeWrapper>> entry : bug2wrappers.entrySet()) {
                List<TypeWrapper> wrappers = new ArrayList<>();
                wrappers.addAll(entry.getValue());
                entry.getValue().clear();
                List<TypeWrapper> newWrappers = singleLevelExplorer(wrappers);
                for (int wrapperIndex = 0; wrapperIndex < newWrappers.size(); wrapperIndex++) {
                    TypeWrapper wrapper = newWrappers.get(wrapperIndex);
                    String mutantFilePath = wrapper.getFilePath();
                    if (visitedPaths.contains(mutantFilePath)) {
                        System.err.println("Error in visiting...");
                        System.exit(-1);
                    }
                    visitedPaths.add(mutantFilePath);
                    String reportFilePath = reportFolder + sep + "iter" + depth + "_" + wrapper.getFileName() + ".txt";
                    String[] invokeCommands = new String[3];
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                    String configPath = file2config.get(wrapper.getInitSeedPath());
                    invokeCommands[2] = "java -jar " + CHECKSTYLE_PATH + " -f" + " plain" + " -o " + reportFilePath + " -c " + configPath + " " + mutantFilePath;
                    invokeCommandsByZT(invokeCommands);
                    readCheckStyleResultFile(reportFilePath);
                    if(!wrapper.isBuggy()) {
                        bug2wrappers.get(entry.getKey()).add(wrapper);
                    }
                }
            }
        }
    }

    public void executeSpotBugsTransform(String initSeedFolderPath) {
        System.out.println("Invoke Analyzer for " + initSeedFolderPath + " and Analysis Output Folder is: " + Path2Last(initSeedFolderPath) + ", Depth=0");
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
            if(!bug2wrappers.containsKey(seedWrapper.getFolderName())) {
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
                List<TypeWrapper> mutantWrappers = singleLevelExplorer(seedWrappers);
                for (int wrapperIndex = 0; wrapperIndex < mutantWrappers.size(); wrapperIndex++) {
                    TypeWrapper mutantWrapper = mutantWrappers.get(wrapperIndex);
                    String[] tokens = mutantWrapper.getFilePath().split(reg_sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String subSeedFolderName = tokens[tokens.length - 2];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // Filename is used to specify class folder name
                    File mutantClassFolder = new File(classFolder.getAbsolutePath() + sep + seedFileName);
                    if (!mutantClassFolder.exists()) {
                        mutantClassFolder.mkdirs();
                    }
                    boolean isCompiled = compileJavaSourceFile(mutantWrapper.getFolderPath(), seedFileNameWithSuffix, mutantClassFolder.getAbsolutePath());
                    if(!isCompiled) {
                        continue;
                    }
                    String reportPath = reportFolder.getAbsolutePath() + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
                    String[] invokeCommands = new String[3];
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                    invokeCommands[2] = SPOTBUGS_PATH + " -textui"
//                            + " -include " + configPath
                            + " -xml:withMessages" + " -output " + reportPath + " "
                            + mutantClassFolder.getAbsolutePath();
                    boolean hasExec = Invoker.invokeCommandsByZT(invokeCommands);
                    if (hasExec) {
                        String report_path = reportFolder.getAbsolutePath() + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
                        readSpotBugsResultFile(mutantWrapper.getFolderPath(), report_path);
                        if(!mutantWrapper.isBuggy()) {
                            entry.getValue().add(mutantWrapper);
                        }
                    }
                }
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
        invokeInfer(seedFolderPath);
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
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
                List<TypeWrapper> mutantWrappers = singleLevelExplorer(wrappers);
                for(int i = 0; i < mutantWrappers.size(); i++) {
                    TypeWrapper mutantWrapper = mutantWrappers.get(i);
                    String mutantPath = mutantWrapper.getFilePath();
                    String mutantFileName = mutantWrapper.getFileName();
                    String reportFolderPath = reportFolder + sep + "iter" + depth + "_" + mutantFileName;
                    String cmd = INFER_PATH + " run -o " + "" + reportFolderPath + " -- " + JAVAC_PATH +
                            " -d " + classFolder.getAbsolutePath() + sep + mutantFileName +
                            " -cp " + inferJarStr + " " + mutantPath;
                    String[] invokeCommands = new String[3];
                    invokeCommands[0] = "/bin/bash";
                    invokeCommands[1] = "-c";
                    invokeCommands[2] = "python3 cmd.py " + cmd;
                    Invoker.invokeCommandsByZT(invokeCommands);
                    String resultFilePath = reportFolderPath + sep + "report.json";
                    readInferResultFile(mutantPath, resultFilePath);
                    if(!mutantWrapper.isBuggy()) {
                        entry.getValue().add(mutantWrapper);
                    }
                }
            }
        }
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
