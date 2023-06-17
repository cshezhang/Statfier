package org.detector;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import net.sourceforge.pmd.PMD;
import org.detector.util.Invoker;
import org.detector.util.Pair;
import org.detector.util.Utility;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static org.detector.report.CheckStyle_Report.readCheckStyleResultFile;
import static org.detector.report.Infer_Report.readSingleInferResultFile;
import static org.detector.report.PMD_Report.readSinglePMDResultFile;
import static org.detector.report.SonarQube_Report.readSingleSonarQubeResultFile;
import static org.detector.report.SpotBugs_Report.readSingleSpotBugsResultFile;
import static org.detector.util.Invoker.createSonarQubeProject;
import static org.detector.util.Invoker.deleteSonarQubeProject;
import static org.detector.util.Invoker.invokeCommandsByZT;
import static org.detector.util.Invoker.invokeCommandsByZTWithOutput;
import static org.detector.util.Utility.CHECKSTYLE_CONFIG_PATH;
import static org.detector.util.Utility.CHECKSTYLE_MUTATION;
import static org.detector.util.Utility.CHECKSTYLE_PATH;
import static org.detector.util.Utility.EVALUATION_PATH;
import static org.detector.util.Utility.INFER_MUTATION;
import static org.detector.util.Utility.PMD_MUTATION;
import static org.detector.util.Utility.SEED_PATH;
import static org.detector.util.Utility.SONARQUBE_MUTATION;
import static org.detector.util.Utility.SONARQUBE_PROJECT_KEY;
import static org.detector.util.Utility.SONAR_SCANNER_PATH;
import static org.detector.util.Utility.SPOTBUGS_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_PATH;
import static org.detector.util.Utility.CLASS_FOLDER;
import static org.detector.util.Utility.file2bugs;
import static org.detector.util.Utility.getDirectFilenamesFromFolder;
import static org.detector.util.Utility.inferJarStr;
import static org.detector.util.Utility.MUTANT_FOLDER;
import static org.detector.util.Utility.reg_sep;
import static org.detector.util.Utility.REPORT_FOLDER;
import static org.detector.util.Utility.sep;
import static org.detector.util.Utility.waitTaskEnd;
import static org.detector.util.Utility.writeLinesToFile;

public class ComparisonEvaluation {

    public static int killedMutant = 0;
    public static Map<String, List<Pair>> bug2pairs = new HashMap<>();

    public static void invokeUniversalMutator(String seedPath, String outputPath) {
        String[] invokeCommands = new String[3];
        invokeCommands[0] = "/bin/bash";
        invokeCommands[1] = "-c";
        invokeCommands[2] = "mutate --mutantDir " + outputPath + " " + seedPath;
        invokeCommandsByZT(invokeCommands);
    }

    public static void invokePMD(String seedPath, String MUTANT_FOLDERPath) {
        String[] tokens = seedPath.split(reg_sep);
        String seedFileName = Utility.Path2Last(seedPath);
        String seedFolderName = tokens[tokens.length - 2];
        String category = seedFolderName.split("_")[0];
        String bugType = seedFolderName.split("_")[1];
        List<String> mutantPaths = getDirectFilenamesFromFolder(MUTANT_FOLDERPath, true);
        String seedReportPath = REPORT_FOLDER.getAbsolutePath()  + File.separator + seedFileName + ".json";
        String[] seedConfig = {
                "-d", seedPath,
                "-R", "category/java/" + category + ".xml/" + bugType,
                "-f", "json",
                "-r", seedReportPath,
                "--no-cache"
        };
        PMD.runPmd(seedConfig);
        readSinglePMDResultFile(seedReportPath, seedPath);
        Map<String, List<Integer>> source_bug2lines = file2bugs.get(seedPath);
        int seedSum = 0;
        for(List<Integer> entry : source_bug2lines.values()) {
            seedSum += entry.size();
        }
        for(String mutantPath : mutantPaths) {
            String mutantFileName = Utility.Path2Last(mutantPath);
            String mutantReportPath = REPORT_FOLDER.getAbsolutePath() + File.separator + mutantFileName + ".json";
            String[] mutantConfig = {
                    "-d", mutantPath,
                    "-R", "category/java/" + category + ".xml/" + bugType,
                    "-f", "json",
                    "-r", mutantReportPath,
                    "--no-cache"
            };
            PMD.runPmd(mutantConfig);
            readSinglePMDResultFile(mutantReportPath, mutantPath);
            int mutantSum = 0;
            if(!file2bugs.containsKey(mutantPath)) {
                System.out.println("Error Mutant: " + mutantPath);
                System.exit(-1);
            }
            Map<String, List<Integer>> mutant_bug2lines = file2bugs.get(mutantPath);
            for(List<Integer> lines : mutant_bug2lines.values()) {
                mutantSum += lines.size();
            }
            if(mutantSum > seedSum) {
                killedMutant++;
                if(!bug2pairs.containsKey(bugType)) {
                    bug2pairs.put(bugType, new ArrayList<>());
                }
                bug2pairs.get(bugType).add(new Pair(seedPath, mutantPath));
            }
        }
    }

    public static void invokeSpotBugs(String seedPath, String mutantFolderPath) {
        File seedFile = new File(seedPath);
        String folderName = seedFile.getParentFile().getName();
        String seedFileName = seedFile.getName().substring(0, seedFile.getName().lastIndexOf('.'));
        String seedReportPath = REPORT_FOLDER.getAbsolutePath() + sep + folderName + sep + seedFileName + "_Result.xml";
        File seedClassFolder = new File(CLASS_FOLDER + sep + folderName + sep + seedFileName);
        if(!seedClassFolder.exists()) {
            seedClassFolder.mkdir();
        }
        boolean isCompiled = Invoker.compileJavaSourceFile(seedFile.getParentFile().getAbsolutePath(), seedFile.getName(), seedClassFolder.getAbsolutePath());
        if(!isCompiled) {
            System.out.println("Failed Compilation: " + seedPath);
            return;
        }
        String[] commands = new String[3];
        commands[0] = "/bin/bash";
        commands[1] = "-c";
        commands[2] = SPOTBUGS_PATH + " -textui"
                + " -xml:withMessages" + " -output " + seedReportPath + " "
                + seedClassFolder.getAbsolutePath();
        invokeCommandsByZT(commands);
        readSingleSpotBugsResultFile(seedFile, seedReportPath);
        if(!file2bugs.containsKey(seedPath)) {
            System.out.println("Error Seed in SpotBugs: " + seedPath);
            return;
        }
        Map<String, List<Integer>> source_bug2lines = file2bugs.get(seedPath);
        int seedSum = 0;
        for(List<Integer> entry : source_bug2lines.values()) {
            seedSum += entry.size();
        }
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {
            File mutantFile = new File(mutantPath);
            String mutantFileName = mutantFile.getName().substring(0, mutantFile.getName().lastIndexOf('.')); // no suffix
            String mutantReportPath = REPORT_FOLDER.getAbsolutePath() + sep + folderName + sep + mutantFileName + "_Result.xml";
            File mutantClassFolder = new File(CLASS_FOLDER.getAbsolutePath() + sep + folderName + sep + mutantFileName);
            if(!mutantClassFolder.exists()) {
                mutantClassFolder.mkdir();
            }
            isCompiled = Invoker.compileJavaSourceFile(mutantFile.getParentFile().getAbsolutePath(), mutantFile.getName(), mutantClassFolder.getAbsolutePath());
            if(!isCompiled) {
                continue;
            }
            commands[0] = "/bin/bash";
            commands[1] = "-c";
            commands[2] = SPOTBUGS_PATH + " -textui"
                    + " -xml:withMessages" + " -output " + mutantReportPath + " "
                    + mutantClassFolder.getAbsolutePath();
            invokeCommandsByZT(commands);
            readSingleSpotBugsResultFile(mutantFile, mutantReportPath);
            int mutantSum = 0;
            if(!file2bugs.containsKey(mutantPath)) {
                System.out.println("Error Mutant in SpotBugs: " + mutantPath);
                continue;
            }
            Map<String, List<Integer>> mutant_bug2lines = file2bugs.get(mutantPath);
            for(List<Integer> lines : mutant_bug2lines.values()) {
                mutantSum += lines.size();
            }
            if(mutantSum > seedSum) {
                killedMutant++;
                for(Map.Entry<String, List<Integer>> entry : mutant_bug2lines.entrySet()) {
                    if(source_bug2lines.containsKey(entry.getKey())) {
                        if(entry.getValue().size() > source_bug2lines.get(entry.getKey()).size()) {
                            String bugType = entry.getKey();
                            if(!bug2pairs.containsKey(bugType)) {
                                bug2pairs.put(bugType, new ArrayList<>());
                            }
                            bug2pairs.get(bugType).add(new Pair(seedPath, mutantPath));
                        }
                    }
                }
            }
        }
    }

    public static void invokeCheckStyle(String seedPath, String mutantFolderPath) {
        File seedFile = new File(seedPath);
        String folderName = seedFile.getParentFile().getName();
        String seedFileName = seedFile.getName().substring(0, seedFile.getName().lastIndexOf('.'));
        String seedReportPath = REPORT_FOLDER.getAbsolutePath() + sep + folderName + sep + seedFileName + "_Result.xml";
        String[] commands = new String[3];
        commands[0] = "/bin/bash";
        commands[1] = "-c";
        commands[2] = "java -jar " + CHECKSTYLE_PATH + " -f" + " plain" + " -o " + seedReportPath + " -c " + CHECKSTYLE_CONFIG_PATH +  " " + seedPath;
        if(!invokeCommandsByZT(commands)) {
            return;
        }
        readCheckStyleResultFile(seedReportPath);
        if(!file2bugs.containsKey(seedPath)) {
            System.out.println("Error Seed in CheckStyle: " + seedPath);
            invokeCommandsByZT(commands);
            readCheckStyleResultFile(seedReportPath);
            return;
        }
        Map<String, List<Integer>> source_bug2lines = file2bugs.get(seedPath);
        int seedSum = 0;
        for(List<Integer> entry : source_bug2lines.values()) {
            seedSum += entry.size();
        }
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {
            File mutantFile = new File(mutantPath);
            String mutantFileName = mutantFile.getName().substring(0, mutantFile.getName().lastIndexOf('.')); // no suffix
            String mutantReportPath = REPORT_FOLDER.getAbsolutePath() + sep + folderName + sep + mutantFileName + "_Result.xml";
            commands[0] = "/bin/bash";
            commands[1] = "-c";
            commands[2] = "java -jar " + CHECKSTYLE_PATH + " -f" + " plain" + " -o " + mutantReportPath + " -c " + CHECKSTYLE_CONFIG_PATH +  " " + seedPath;
            if(!invokeCommandsByZT(commands)) {
                continue;
            }
            readCheckStyleResultFile(mutantReportPath);
            int mutantSum = 0;
            if(!file2bugs.containsKey(mutantPath)) {
                System.out.println("Error Mutant in CheckStyle: " + mutantPath);
                invokeCommandsByZT(commands);
                readCheckStyleResultFile(mutantReportPath);
                continue;
            }
            Map<String, List<Integer>> mutant_bug2lines = file2bugs.get(mutantPath);
            for(List<Integer> lines : mutant_bug2lines.values()) {
                mutantSum += lines.size();
            }
            if(mutantSum > seedSum) {
                killedMutant++;
                for(Map.Entry<String, List<Integer>> entry : mutant_bug2lines.entrySet()) {
                    if(source_bug2lines.containsKey(entry.getKey())) {
                        if(entry.getValue().size() > source_bug2lines.get(entry.getKey()).size()) {
                            String bugType = entry.getKey();
                            if(!bug2pairs.containsKey(bugType)) {
                                bug2pairs.put(bugType, new ArrayList<>());
                            }
                            bug2pairs.get(bugType).add(new Pair(seedPath, mutantPath));
                        }
                    }
                }
            }
        }
    }

    public static void invokeInfer(String seedPath, String mutantFolderPath) {
        File seedFile = new File(seedPath);
        String folderName = seedFile.getParentFile().getName();
        String seedFileName = seedFile.getName().substring(0, seedFile.getName().lastIndexOf('.'));
        File seedReportFolder = new File(REPORT_FOLDER.getAbsolutePath() + sep + seedFileName);
        String seedReportPath = seedReportFolder.getAbsolutePath() + sep + "report.json";
        File seedClassFolder = new File(CLASS_FOLDER + sep + folderName + sep + seedFileName);
        if(!seedClassFolder.exists()) {
            seedClassFolder.mkdir();
        }
        String[] commands = new String[3];
        commands[0] = "/bin/bash";
        commands[1] = "-c";
        commands[2] = "python3 cmd.py " + "\"" + Utility.INFER_PATH + " run -o " + seedReportFolder.getAbsolutePath() + " -- " + Utility.JAVAC_PATH +
                " -d " + CLASS_FOLDER.getAbsolutePath() + sep + seedFileName +
                " -cp " + inferJarStr + " " + seedPath + "\"";
        if(!invokeCommandsByZT(commands)) {
            return;
        }
        readSingleInferResultFile(seedPath, seedReportPath);
        if(!file2bugs.containsKey(seedPath)) {
            return;
        }
        Map<String, List<Integer>> source_bug2lines = file2bugs.get(seedPath);
        int seedSum = 0;
        for(List<Integer> entry : source_bug2lines.values()) {
            seedSum += entry.size();
        }
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {
            File mutantFile = new File(mutantPath);
            String mutantFileName = mutantFile.getName().substring(0, mutantFile.getName().lastIndexOf('.')); // no suffix
            String mutantReportPath = "";
            File mutantClassFolder = new File(CLASS_FOLDER.getAbsolutePath() + sep + folderName + sep + mutantFileName);
            if(!mutantClassFolder.exists()) {
                mutantClassFolder.mkdir();
            }
            commands[0] = "/bin/bash";
            commands[1] = "-c";
            commands[2] = "python3 cmd.py " + "\"" + Utility.INFER_PATH + " run -o " + REPORT_FOLDER.getAbsolutePath() + " -- " + Utility.JAVAC_PATH +
                    " -d " + CLASS_FOLDER.getAbsolutePath() + sep + mutantFileName +
                    " -cp " + inferJarStr + " " + seedPath + "\"";;
            if(!invokeCommandsByZT(commands)) {
                continue;
            }
            readSingleInferResultFile(mutantPath, mutantReportPath);
            if(!file2bugs.containsKey(mutantPath)) {
                continue;
            }
            int mutantSum = 0;
            Map<String, List<Integer>> mutant_bug2lines = file2bugs.get(mutantPath);
            for(List<Integer> lines : mutant_bug2lines.values()) {
                mutantSum += lines.size();
            }
            if(mutantSum > seedSum) {
                killedMutant++;
                for(Map.Entry<String, List<Integer>> entry : mutant_bug2lines.entrySet()) {
                    if(source_bug2lines.containsKey(entry.getKey())) {
                        if(entry.getValue().size() > source_bug2lines.get(entry.getKey()).size()) {
                            String bugType = entry.getKey();
                            if(!bug2pairs.containsKey(bugType)) {
                                bug2pairs.put(bugType, new ArrayList<>());
                            }
                            bug2pairs.get(bugType).add(new Pair(seedPath, mutantPath));
                        }
                    }
                }
            }
        }
    }

    public static void invokeSonarQube(String seedPath, String mutantFolderPath) {
        File seedFile = new File(seedPath);
        String folderName = seedFile.getParentFile().getName();
        String seedFileName = seedFile.getName().substring(0, seedFile.getName().lastIndexOf('.'));
        String seedReportPath = REPORT_FOLDER.getAbsolutePath() + sep + folderName + sep + seedFileName + "_Result.json";
        deleteSonarQubeProject(SONARQUBE_PROJECT_KEY);
        createSonarQubeProject(SONARQUBE_PROJECT_KEY);
        String[] invokeCommands = new String[3];
        invokeCommands[0] = "/bin/bash";
        invokeCommands[1] = "-c";
        invokeCommands[2] = SONAR_SCANNER_PATH + " -Dsonar.projectKey=" + SONARQUBE_PROJECT_KEY
                + " -Dsonar.projectBaseDir=" + EVALUATION_PATH + sep + "mutants"
                + " -Dsonar.sources=" + seedFile.getAbsolutePath() + " -Dsonar.host.url=http://localhost:9000"
                + " -Dsonar.login=admin -Dsonar.password=123456";
        if (invokeCommandsByZT(invokeCommands)) {
            waitTaskEnd();
        } else {
            return;
        }
        String[] curlCommands = new String[4];
        curlCommands[0] = "curl";
        curlCommands[1] = "-u";
        curlCommands[2] = "admin:123456";
        curlCommands[3] = "http://localhost:9000/api/issues/search?p=1&ps=500&componentKeys=" + SONARQUBE_PROJECT_KEY;
        String output = invokeCommandsByZTWithOutput(curlCommands);
        readSingleSonarQubeResultFile(seedPath, output);
        writeLinesToFile(seedReportPath, output);
        Map<String, List<Integer>> source_bug2lines = file2bugs.get(seedPath);
        if(!file2bugs.containsKey(seedPath)) {
            readSingleSonarQubeResultFile(seedPath, output);
        }
        int seedSum = 0;
        for(List<Integer> entry : source_bug2lines.values()) {
            seedSum += entry.size();
        }
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {
            File mutantFile = new File(mutantPath);
            String mutantFileName = mutantFile.getName().substring(0, mutantFile.getName().lastIndexOf('.')); // no suffix
            String mutantReportPath = REPORT_FOLDER.getAbsolutePath() + sep + folderName + sep + mutantFileName + "_Result.xml";
            deleteSonarQubeProject(SONARQUBE_PROJECT_KEY);
            createSonarQubeProject(SONARQUBE_PROJECT_KEY);
            invokeCommands[0] = "/bin/bash";
            invokeCommands[1] = "-c";
            invokeCommands[2] = SONAR_SCANNER_PATH + " -Dsonar.projectKey=" + SONARQUBE_PROJECT_KEY
                    + " -Dsonar.projectBaseDir=" + EVALUATION_PATH + sep + "mutants"
                    + " -Dsonar.sources=" + mutantFile.getAbsolutePath() + " -Dsonar.host.url=http://localhost:9000"
                    + " -Dsonar.login=admin -Dsonar.password=123456";
            if (invokeCommandsByZT(invokeCommands)) {
                waitTaskEnd();
            } else {
                continue;
            }
            output = invokeCommandsByZTWithOutput(curlCommands);
            readSingleSonarQubeResultFile(mutantPath, output);
            writeLinesToFile(mutantReportPath, output);
            int mutantSum = 0;
            Map<String, List<Integer>> mutant_bug2lines = file2bugs.get(mutantPath);
            for(List<Integer> lines : mutant_bug2lines.values()) {
                mutantSum += lines.size();
            }
            if(mutantSum > seedSum) {
                killedMutant++;
                for(Map.Entry<String, List<Integer>> entry : mutant_bug2lines.entrySet()) {
                    if(source_bug2lines.containsKey(entry.getKey())) {
                        if(entry.getValue().size() > source_bug2lines.get(entry.getKey()).size()) {
                            String bugType = entry.getKey();
                            if(!bug2pairs.containsKey(bugType)) {
                                bug2pairs.put(bugType, new ArrayList<>());
                            }
                            bug2pairs.get(bugType).add(new Pair(seedPath, mutantPath));
                        }
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        long startTimeStamp = System.currentTimeMillis();
        List<String> output = new ArrayList<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(startTimeStamp))));
        System.out.println("Start Time: " + sd);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        System.out.println("Java Program PID: " + pid);
        Utility.initEnv();
        List<String> seedPaths = Utility.getFilenamesFromFolder(SEED_PATH, true);
        System.out.println("Seed Path: " + SEED_PATH);
        System.out.println("Seed Path Size: " + seedPaths.size());
        output.add("Seed Path: " + SEED_PATH);
        output.add("Seed Path Size: " + seedPaths.size());
        for(int i = 0; i < seedPaths.size(); i++) {
            long timer = System.currentTimeMillis() - Utility.startTimeStamp;
            long minutes = TimeUnit.MILLISECONDS.toMinutes(timer);
            long seconds = TimeUnit.MILLISECONDS.toSeconds(timer) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(timer));
            System.out.println("Current execution time: " + String.format("%d min, %d sec", minutes, seconds));
            if(minutes > 360) {
                System.out.println("Timeout!");
                break;
            }
            String seedPath = seedPaths.get(i);
            System.out.println("Index: " + i + " Seed Path: " + seedPath);
            System.out.println("Killed Mutant: " + killedMutant);
            String seedFileName = Utility.Path2Last(seedPath);
            File mutantFolder = new File(MUTANT_FOLDER.getAbsolutePath() + sep + seedFileName);
            if(!mutantFolder.exists()) {
                mutantFolder.mkdir();
            }
            String mutantFolderPath = mutantFolder.getAbsolutePath();
            invokeUniversalMutator(seedPath, mutantFolderPath);
            if(PMD_MUTATION) {
                invokePMD(seedPath, mutantFolderPath);
            }
            if(SPOTBUGS_MUTATION) {
                invokeSpotBugs(seedPath, mutantFolderPath);
            }
            if(CHECKSTYLE_MUTATION) {
                invokeCheckStyle(seedPath, mutantFolderPath);
            }
            if(INFER_MUTATION) {
                invokeInfer(seedPath, mutantFolderPath);
            }
            if(SONARQUBE_MUTATION) {
                invokeSonarQube(seedPath, mutantFolderPath);
            }
        }
        System.out.println("Found bugs in " + bug2pairs.keySet().size() + " rules.");
        output.add("Found bugs in " + bug2pairs.keySet().size() + " rules.");
        System.out.println("Killed Mutants: " + killedMutant);
        output.add("Killed Mutants: " + killedMutant);
        int sumPair = 0;
        for(Map.Entry<String, List<Pair>> entry : bug2pairs.entrySet()) {
            ObjectMapper mapper = new ObjectMapper();
            ObjectNode root = mapper.createObjectNode();
            root.put("Rule", entry.getKey());
            root.put("PairSize", entry.getValue().size());
            sumPair += entry.getValue().size();
            ArrayNode bugs = mapper.createArrayNode();
            for (Pair pair : entry.getValue()) {
                ObjectNode bug = mapper.createObjectNode();
                bug.put("SeedPath", pair.first);
                bug.put("MutantPath", pair.second);
                bugs.add(bug);
            }
            root.put("Bugs", bugs);
            File resultFile = new File(EVALUATION_PATH + File.separator + "results" + File.separator + entry.getKey() + ".json");
            try {
                if (!resultFile.exists()) {
                    resultFile.createNewFile();
                }
                FileWriter jsonWriter = new FileWriter(resultFile);
                BufferedWriter jsonBufferedWriter = new BufferedWriter(jsonWriter);
                jsonBufferedWriter.write(root.toString());
                jsonBufferedWriter.close();
                jsonWriter.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        System.out.println("Pair Size: " + sumPair);
        output.add("Pair Size: " + sumPair);
        long executionTime = System.currentTimeMillis() - Utility.startTimeStamp;
        System.out.println(
            "Overall Execution Time is: " + String.format("%d min, %d sec",
            TimeUnit.MILLISECONDS.toMinutes(executionTime),
            TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))));
        output.add("Overall Execution Time is: " + String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(executionTime),
                TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))));
        writeLinesToFile(EVALUATION_PATH + sep + "Output.log", output);
    }

}
