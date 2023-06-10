package org.detector;

import net.sourceforge.pmd.PMD;
import org.detector.util.Invoker;
import org.detector.util.Utility;

import java.io.File;
import java.util.List;

import static org.detector.util.Utility.CHECKSTYLE_MUTATION;
import static org.detector.util.Utility.INFER_MUTATION;
import static org.detector.util.Utility.PMD_MUTATION;
import static org.detector.util.Utility.PMD_SEED_PATH;
import static org.detector.util.Utility.SONARQUBE_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_PATH;
import static org.detector.util.Utility.classFolder;
import static org.detector.util.Utility.file2bugs;
import static org.detector.util.Utility.getDirectFilenamesFromFolder;
import static org.detector.util.Utility.mutantFolder;
import static org.detector.util.Utility.readPMDResultFile;
import static org.detector.util.Utility.reg_sep;
import static org.detector.util.Utility.reportFolder;

public class ComparisonEvaluation {

    public static void invokeUniversalMutator(String seedPath, String outputPath) {
        String[] invokeCommands = new String[5];
        invokeCommands[0] = "/bin/sh";
        invokeCommands[1] = "-c";
        invokeCommands[2] = "mutate --mutantDir " + outputPath + " " + seedPath;
        Invoker.invokeCommandsByZT(invokeCommands);
    }

    public static void invokeMuJava(String seedPath, String output) {

    }

    public static void invokePMD(String seedPath, String mutantFolderPath) {
        String[] tokens = seedPath.split(reg_sep);
        String seedFolderName = tokens[tokens.length - 2];
        String category = seedFolderName.split("_")[0];
        String bugType = seedFolderName.split("_")[1];
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        String srcResultFilePath = "";
        String[] srcPmdConfig = {
                "-d", seedPath,
                "-R", "category/java/" + category + ".xml/" + bugType,
                "-f", "json",
                "-r", srcResultFilePath
        };
        PMD.runPmd(srcPmdConfig);
        readPMDResultFile(srcResultFilePath);
        for(String mutantPath : mutantPaths) {
            String dstResultFilePath = reportFolder.getAbsolutePath() + File.separator + seedFolderName + "_Result.json";
            String[] dstPmdConfig = {
                    "-d", mutantPath,
                    "-R", "category/java/" + category + ".xml/" + bugType,
                    "-f", "json",
                    "-r", dstResultFilePath
            };
            PMD.runPmd(dstPmdConfig);
            readPMDResultFile(dstResultFilePath);
        }
    }

    public static void invokeSpotBugs(String seedPath, String mutantFolderPath) {
        String seedReportPath = "";
        File seedClassFolder = new File("");
        String[] seedInvokeCommands = new String[3];
        seedInvokeCommands[0] = "/bib/sh";
        seedInvokeCommands[1] = "-c";
        seedInvokeCommands[2] = SPOTBUGS_PATH + " -textui"
                + " -xml:withMessages" + " -output " + seedReportPath + " "
                + seedClassFolder.getAbsolutePath();
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {
            File mutantFile = new File(mutantPath);
            String mutantReportPath = "";
            File mutantClassFolder = new File(classFolder.getAbsolutePath() + File.separator + mutantFile.getName());
            Invoker.compileJavaSourceFile(mutantFile.getParentFile().getAbsolutePath(), mutantFile.getName(), mutantClassFolder.getAbsolutePath());
            String[] invokeCommands = new String[3];
            invokeCommands[0] = "/bin/sh";
            invokeCommands[1] = "-c";
            invokeCommands[2] = SPOTBUGS_PATH + " -textui"
                    + " -xml:withMessages" + " -output " + mutantReportPath + " "
                    + mutantClassFolder.getAbsolutePath();
            Invoker.invokeCommandsByZT(invokeCommands);
        }
    }

    public static void invokeCheckStyle(String seedPath, String mutantFolderPath) {
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {

        }
    }

    public static void invokeInfer(String seedPath, String mutantFolderPath) {
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {

        }
    }

    public static void invokeSonarQube(String seedPath, String mutantFolderPath) {
        List<String> mutantPaths = getDirectFilenamesFromFolder(mutantFolderPath, true);
        for(String mutantPath : mutantPaths) {

        }
    }

    public static void main(String[] args) {
        List<String> seedPaths = Utility.getFilenamesFromFolder(PMD_SEED_PATH, true);
        for(String seedPath : seedPaths) {
            String mutantFolderPath = mutantFolder.getAbsolutePath() + File.separator;
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
    }

}
