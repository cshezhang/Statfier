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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.detector.util.Utility.CHECKSTYLE_CONFIG_PATH;
import static org.detector.util.Utility.CHECKSTYLE_MUTATION;
import static org.detector.util.Utility.CHECKSTYLE_PATH;
import static org.detector.util.Utility.INFER_MUTATION;
import static org.detector.util.Utility.PMD_MUTATION;
import static org.detector.util.Utility.PMD_SEED_PATH;
import static org.detector.util.Utility.Path2Last;
import static org.detector.util.Utility.SONARQUBE_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_PATH;
import static org.detector.util.Utility.classFolder;
import static org.detector.util.Utility.file2bugs;
import static org.detector.util.Utility.getDirectFilenamesFromFolder;
import static org.detector.util.Utility.inferJarStr;
import static org.detector.util.Utility.mutantFolder;
import static org.detector.util.Utility.readSinglePMDResultFile;
import static org.detector.util.Utility.reg_sep;
import static org.detector.util.Utility.reportFolder;

public class ComparisonEvaluation {

    public static Map<String, List<String>> seed2mutant = new HashMap<>();

    public static Map<String, List<Pair>> bug2pairs = new HashMap<>();

    public static void invokeUniversalMutator(String seedPath, String outputPath) {
        String[] invokeCommands = new String[3];
        invokeCommands[0] = "/bin/sh";
        invokeCommands[1] = "-c";
        invokeCommands[2] = "mutate --mutantDir " + outputPath + " " + seedPath;
        Invoker.invokeCommandsByZT(invokeCommands);
    }

    public static void invokePMD(String seedPath, String mutantFolderPath) {
        String[] tokens = seedPath.split(reg_sep);
        String seedFileName = Utility.Path2Last(seedPath);
        String seedFolderName = tokens[tokens.length - 2];
        String category = seedFolderName.split("_")[0];
        String bugType = seedFolderName.split("_")[1];
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        String seedReportPath = reportFolder.getAbsolutePath()  + File.separator + seedFileName + ".json";
        String[] seedConfig = {
                "-d", seedPath,
                "-R", "category/java/" + category + ".xml/" + bugType,
                "-f", "json",
                "-r", seedReportPath
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
            String mutantReportPath = reportFolder.getAbsolutePath() + File.separator + mutantFileName + ".json";
            String[] mutantConfig = {
                    "-d", mutantPath,
                    "-R", "category/java/" + category + ".xml/" + bugType,
                    "-f", "json",
                    "-r", mutantReportPath
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
                if(!bug2pairs.containsKey(bugType)) {
                    bug2pairs.put(bugType, new ArrayList<>());
                }
                bug2pairs.get(bugType).add(new Pair(seedPath, mutantPath));
            }
        }
    }

    public static void invokeSpotBugs(String seedPath, String mutantFolderPath) {
        String seedFileName = Path2Last(seedPath);
        String seedReportPath = reportFolder.getAbsolutePath()  + File.separator + seedFileName + "_Result.xml";;
        File seedClassFolder = new File("");
        String[] commands = new String[3];
        commands[0] = "/bib/sh";
        commands[1] = "-c";
        commands[2] = SPOTBUGS_PATH + " -textui"
                + " -xml:withMessages" + " -output " + seedReportPath + " "
                + seedClassFolder.getAbsolutePath();
        Invoker.invokeCommandsByZT(commands);
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {
            File mutantFile = new File(mutantPath);
            String mutantReportPath = "";
            File mutantClassFolder = new File(classFolder.getAbsolutePath() + File.separator + mutantFile.getName());
            Invoker.compileJavaSourceFile(mutantFile.getParentFile().getAbsolutePath(), mutantFile.getName(), mutantClassFolder.getAbsolutePath());
            commands[0] = "/bin/sh";
            commands[1] = "-c";
            commands[2] = SPOTBUGS_PATH + " -textui"
                    + " -xml:withMessages" + " -output " + mutantReportPath + " "
                    + mutantClassFolder.getAbsolutePath();
            Invoker.invokeCommandsByZT(commands);
        }
    }

    public static void invokeCheckStyle(String seedPath, String mutantFolderPath) {
        // Add a map: seed report -> mutant report -> Diff analysis
        String reportPath = "";
        String[] invokeCommands = new String[3];
        invokeCommands[0] = "/bin/bash";
        invokeCommands[1] = "-c";
        invokeCommands[2] = "java -jar " + CHECKSTYLE_PATH + " -f" + " plain" + " -o " + reportPath + " -c " + CHECKSTYLE_CONFIG_PATH +  " " + seedPath;
        Invoker.invokeCommandsByZT(invokeCommands);
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {
            reportPath = "";
            invokeCommands[2] = "java -jar " + CHECKSTYLE_PATH + " -f" + " plain" + " -o " + reportPath + " -c " + CHECKSTYLE_CONFIG_PATH +  " " + mutantPath;
        }
    }

    public static void invokeInfer(String seedPath, String mutantFolderPath) {
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);;
        seed2mutant.put(seedPath, new ArrayList<>());
        String filename = Utility.Path2Last(seedPath);
        String reportFolderPath = reportFolder + File.separator + filename;
        String cmd = "\"" + Utility.INFER_PATH + " run -o " + reportFolderPath + " -- " + Utility.JAVAC_PATH +
                " -d " + classFolder.getAbsolutePath() + File.separator + filename +
                " -cp " + inferJarStr + " " + seedPath + "\"";
        String[] invokeCommands = new String[3];
        invokeCommands[0] = "/bin/bash";
        invokeCommands[1] = "-c";
        invokeCommands[2] = cmd;
        for(String mutantPath : mutantPaths) {
            filename = Utility.Path2Last(seedPath);
            reportFolderPath = reportFolder + File.separator + filename;
            cmd = "\"" + Utility.INFER_PATH + " run -o " + reportFolderPath + " -- " + Utility.JAVAC_PATH +
                    " -d " + classFolder.getAbsolutePath() + File.separator + filename +
                    " -cp " + inferJarStr + " " + seedPath + "\"";
            invokeCommands[0] = "/bin/bash";
            invokeCommands[1] = "-c";
            invokeCommands[2] = cmd;
        }
    }

    public static void invokeSonarQube(String seedPath, String mutantFolderPath) {
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {

        }
    }

    public static void main(String[] args) {
        Utility.initEnv();
        List<String> seedPaths = Utility.getFilenamesFromFolder(PMD_SEED_PATH, true);
        for(String seedPath : seedPaths) {
            String seedFileName = Utility.Path2Last(seedPath);
            File subMutantFolder = new File(mutantFolder.getAbsolutePath() + File.separator + seedFileName);
            if(!subMutantFolder.exists()) {
                subMutantFolder.mkdir();
            }
            String mutantFolderPath = subMutantFolder.getAbsolutePath();
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
            File resultFile = new File(Utility.EVALUATION_PATH + File.separator + "results" + File.separator + entry.getKey() + ".json");
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
    }

}
