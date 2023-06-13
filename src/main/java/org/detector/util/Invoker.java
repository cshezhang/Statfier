package org.detector.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import net.sourceforge.pmd.PMD;
import org.detector.report.SonarQube_Report;
import org.json.JSONObject;
import org.detector.thread.CheckStyle_InvokeThread;
import org.detector.thread.Infer_InvokeThread;
import org.detector.thread.PMD_InvokeThread;
import org.detector.thread.SpotBugs_InvokeThread;
import org.zeroturnaround.exec.ProcessExecutor;

import static org.detector.report.SpotBugs_Report.readSpotBugsResultFile;
import static org.detector.util.Utility.INFER_MUTATION;
import static org.detector.util.Utility.JAVAC_PATH;
import static org.detector.util.Utility.PMD_MUTATION;
import static org.detector.util.Utility.Path2Last;
import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.SONARQUBE_MUTATION;
import static org.detector.util.Utility.SONAR_SCANNER_PATH;
import static org.detector.util.Utility.SPOTBUGS_MUTATION;
import static org.detector.util.Utility.SonarQubeRuleNames;
import static org.detector.util.Utility.THREAD_COUNT;
import static org.detector.util.Utility.getFilenamesFromFolder;
import static org.detector.util.Utility.inferJarStr;
import static org.detector.util.Utility.initThreadPool;
import static org.detector.util.Utility.readCheckStyleResultFile;
import static org.detector.util.Utility.readInferResultFile;
import static org.detector.util.Utility.readPMDResultFile;
import static org.detector.util.Utility.REPORT_FOLDER;
import static org.detector.util.Utility.sep;
import static org.detector.util.Utility.spotBugsJarStr;
import static org.detector.util.Utility.subSeedFolderNameList;
import static org.detector.util.Utility.waitTaskEnd;
import static org.detector.util.Utility.waitThreadPoolEnding;
import static org.detector.util.Utility.writeLinesToFile;


/**
 * @Description: This class is used for different invocation functions.
 * @Author: RainyD4y
 * @Date: 2021-10-27 14:20:55
 */
public class Invoker {

    public static List<String> failedCommands = new ArrayList<>();

    public static String invokeCommandsByZTWithOutput(String[] cmdArgs) {
        StringBuilder argStr = new StringBuilder();
        String output = "";
        for (String arg : cmdArgs) {
            argStr.append(arg + " ");
        }
        try {
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            output = new ProcessExecutor().command(cmdArgs).redirectError(errorStream).readOutput(true).execute().outputUTF8();
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
        return output;
    }

    public static boolean invokeCommandsByZT(String[] cmdArgs) {
        StringBuilder argStr = new StringBuilder();
        for (String arg : cmdArgs) {
            argStr.append(arg + " ");
        }
        try {
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            int exitValue = new ProcessExecutor().command(cmdArgs).redirectError(errorStream).execute().getExitValue();
            // CheckStyle Return value is the number of bugs, hence regard it.
            if(PMD_MUTATION && exitValue != 4 && exitValue != 0) {
                System.err.println("Execute PMD Error!");
                System.err.println(argStr);
                return false;
            }
            if(SPOTBUGS_MUTATION && exitValue != 0) {
                failedCommands.add(argStr.toString());
                return false;
            }
            if(INFER_MUTATION && exitValue != 0) {
                System.err.println("Invoke Infer Error Value: " + exitValue);
                System.err.println("Execute Infer Error!");
                System.err.println(argStr);
                System.err.println("Error Msg: " + new String(errorStream.toByteArray()));
                System.exit(-1);
                return false;
            }
            if(SONARQUBE_MUTATION && exitValue != 0) {
                System.err.println("Invoke SonarQube Error and Return Value: " + exitValue);
                System.err.println(argStr);
                System.err.println("Error Msg: " + new String(errorStream.toByteArray()));
                System.exit(-1);
                return false;
            }
        } catch (Exception e) {
            System.err.println(argStr);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean invokeCommands(String[] cmdArgs) {
        StringBuilder args = new StringBuilder();
        for(int i = 0; i < cmdArgs.length; i++) {
            args.append(cmdArgs[i] + " ");
        }
        InputStream in;
        try {
            Process process = Runtime.getRuntime().exec(cmdArgs);
            in = process.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, "UTF-8"));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = read.readLine()) != null) {
                System.out.println(line);
                builder.append(line);
            }
            int exitValue = process.waitFor();
            if(exitValue != 4 && exitValue != 0) {
                if (SPOTBUGS_MUTATION) {
                    failedCommands.add(args.toString());
                } else {
                    System.err.println("Fail to Invoke Commands1: " + args);
                    System.err.println("Log: " + builder);
                    System.err.println("ExitValue: " + exitValue);
                    System.exit(-1);
                }
                return false;
            }
            process.getOutputStream().close();
        } catch (IOException e) {
            System.err.println("Fail to Invoke Commands2: " + args);
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            System.err.println("Fail to Invoke Commands:3 " + args);
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // folderPath is purely folder path and doesn't contain java file name.
    public static boolean compileJavaSourceFile(String srcFolderPath, String fileName, String classFileFolder) {
        if(DEBUG) {
            System.out.println("Compiling: " + fileName);
        }
        if(!fileName.endsWith(".java")) {
            System.err.println("File: " + fileName + " is not ended by .java");
            System.exit(-1);
        }
        fileName = fileName.substring(0, fileName.length() - 5);
        List<String> cmd_list = new ArrayList<>();
        cmd_list.add(JAVAC_PATH);
        cmd_list.add("-d");
        cmd_list.add(classFileFolder);  // Generated class files are saved in this folder.
        cmd_list.add("-cp");
        if (SPOTBUGS_MUTATION) {
            cmd_list.add(spotBugsJarStr.toString());
        }
        if(INFER_MUTATION) {
            cmd_list.add(inferJarStr.toString());
        }
        cmd_list.add(srcFolderPath  + File.separator + fileName + ".java");
        boolean tag = invokeCommandsByZT(cmd_list.toArray(new String[cmd_list.size()]));
        return tag;
    }

    // seedFolderName is seed folder name (last token of folderPath), like: SpotBugs_Seeds, iter1, iter2...
    // Generated class files are saved in CLASS_FOLDER
    public static void invokeSpotBugs(String seedFolderPath) { // seedFolderPath is the java source code folder (seed path), like: /path/to/SingleTesting
        if(seedFolderPath.endsWith(sep)) {
            seedFolderPath = seedFolderPath.substring(0, seedFolderPath.length() - 1);
        }
        if(DEBUG) {
            System.out.println("Invoke SpotBugs Path: " + seedFolderPath);
        }
        ExecutorService threadPool = initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            String subSeedFolderName = subSeedFolderNameList.get(i);
            String subSeedFolderPath = seedFolderPath + File.separator + subSeedFolderName;
            List<String> seedFileNames = getFilenamesFromFolder(subSeedFolderPath, false);
            threadPool.submit(new SpotBugs_InvokeThread(subSeedFolderPath, subSeedFolderName, seedFileNames));
        }
        waitThreadPoolEnding(threadPool);
        // Here we want to invoke SpotBugs one time and get all analysis results
        // But it seems we cannot process identical class in different folders, this can lead to many FPs or FNs
        for(String subSeedFolderName : subSeedFolderNameList) {
            List<String> reportPaths = getFilenamesFromFolder(REPORT_FOLDER.getAbsolutePath() + File.separator + subSeedFolderName, true);
            for(String reportPath : reportPaths) {
                readSpotBugsResultFile(seedFolderPath + File.separator + subSeedFolderName, reportPath);
            }
        }
    }

    // Add setting and dummy-binaries folder for SonarQube seed folder
    public static void writeSettingFile(String seedFolderPath, String settingFilePath, String projectName) {
        List<String> contents = new ArrayList<>();
        contents.add("sonar.projectKey=" + projectName);
        contents.add("sonar.projectName=" + projectName);
        contents.add("sonar.projectVersion=1.0");
        contents.add("sonar.login=admin");
        contents.add("sonar.password=123456");
        contents.add("sonar.sourceEncoding=UTF-8");
        contents.add("sonar.scm.disabled=true");
        contents.add("sonar.cpd.exclusions=**/*");
        contents.add("sonar.sources=" + seedFolderPath);
        contents.add("sonar.java.source=11");
        File dummyFolder = new File(seedFolderPath + sep + "dummy-binaries");
        if(!dummyFolder.exists()) {
            dummyFolder.mkdir();
        }
        File dummyFile = new File(seedFolderPath + sep + "dummy-binaries" + sep + "dummy.txt");
        if(!dummyFile.exists()) {
            try {
                dummyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        contents.add("sonar.java.binaries=" + dummyFolder.getAbsolutePath());
        contents.add("sonar.java.test.binaries=" + dummyFolder.getAbsolutePath());
        File settingFile = new File(settingFilePath);
        if(!settingFile.exists()) {
            writeLinesToFile(settingFilePath, contents);
        }
    }

    public static void deleteSonarQubeProject(String projectName) {
        String[] curlPostCommands = new String[6];
        curlPostCommands[0] = "curl";
        curlPostCommands[1] = "-u";
        curlPostCommands[2] = "admin:123456";
        curlPostCommands[3] = "-X";
        curlPostCommands[4] = "POST";
        curlPostCommands[5] = "http://localhost:9000/api/projects/delete?project=" + projectName;
        invokeCommandsByZT(curlPostCommands);
    }

    public static boolean createSonarQubeProject(String projectName) {
        String[] curlPostCommands = new String[6];
        curlPostCommands[0] = "curl";
        curlPostCommands[1] = "-u";
        curlPostCommands[2] = "admin:123456";
        curlPostCommands[3] = "-X";
        curlPostCommands[4] = "POST";
        curlPostCommands[5] = "http://localhost:9000/api/projects/create?name=" + projectName + "&project=" + projectName;
        return invokeCommandsByZT(curlPostCommands);
    }

    // First level process
    public static void invokeSonarQube(String seedFolderPath) {
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            String subSeedFolderName = subSeedFolderNameList.get(i);
            String subSeedFolderPath = seedFolderPath + File.separator + subSeedFolderName;
            if(DEBUG) {
                System.out.println("Seed path: " + subSeedFolderPath);
            }
            String newProjectName = "iter0_" + subSeedFolderName;
            deleteSonarQubeProject(newProjectName);
            boolean isCreated = createSonarQubeProject(newProjectName);
            if(!isCreated) {
                System.err.println("NewProjectName: " + newProjectName + " is not created!");
                continue;
            }
            String settingPath = subSeedFolderPath + sep + "settings";
            writeSettingFile(subSeedFolderPath, settingPath, newProjectName);
            String[] invokeCommands = new String[3];
            if(OSUtil.isWindows()) {
                invokeCommands[0] = "cmd.exe";
                invokeCommands[1] = "/c";
            } else {
                invokeCommands[0] = "/bin/bash";
                invokeCommands[1] = "-c";
            }
            invokeCommands[2] = SONAR_SCANNER_PATH + " -Dproject.settings=" + settingPath;
            boolean hasExec = invokeCommandsByZT(invokeCommands); // invoke SonarQube to detect target folder
            if(hasExec) {
                waitTaskEnd(newProjectName);
            } else {
                return;
            }
            List<String> ruleNames = new ArrayList<>(SonarQubeRuleNames);
            String[] curlCommands = new String[4];
            for (String ruleName : ruleNames) {
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
    }

    public static void invokeInfer(String seedFolderPath) {
        ExecutorService threadPool = initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new Infer_InvokeThread(0, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding(threadPool);
        System.out.println("Infer Result Folder: " + REPORT_FOLDER.getAbsolutePath());
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        for(String seedPath : seedPaths) {
            String reportPath = REPORT_FOLDER.getAbsolutePath() + File.separator + "iter0_" + Path2Last(seedPath) + File.separator + "report.json";
            readInferResultFile(seedPath, reportPath);
        }
    }

    public static void invokeCheckStyle(String seedFolderPath) {
        ExecutorService threadPool = initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new CheckStyle_InvokeThread(0, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding(threadPool);
        List<String> reportPaths = getFilenamesFromFolder(REPORT_FOLDER.getAbsolutePath(), true);
        for(int i = 0; i < reportPaths.size(); i++) {
            readCheckStyleResultFile(reportPaths.get(i));
        }
    }

    public static void invokePMD(String seedFolderPath) {
        if(THREAD_COUNT > 1) {
            ExecutorService threadPool = initThreadPool();
            for (int i = 0; i < subSeedFolderNameList.size(); i++) {
                threadPool.submit(new PMD_InvokeThread(0, seedFolderPath, subSeedFolderNameList.get(i)));
            }
            waitThreadPoolEnding(threadPool);
            for (int i = 0; i < subSeedFolderNameList.size(); i++) {
                readPMDResultFile(REPORT_FOLDER.getAbsolutePath() + File.separator + "iter0_" + subSeedFolderNameList.get(i) + "_Result.json");
            }
        } else {
            for(int i = 0; i < subSeedFolderNameList.size(); i++) {
                String seedFolderName = subSeedFolderNameList.get(i);
                String[] tokens = seedFolderName.split("_");
                String ruleCategory = tokens[0];
                String ruleType = tokens[1];
                String[] pmdConfig = {
                        "-d", seedFolderPath  + File.separator + seedFolderName,
                        "-R", "category/java/" + ruleCategory + ".xml/" + ruleType,
                        "-f", "json",
                        "-r", REPORT_FOLDER.getAbsolutePath()  + File.separator + "iter" + 0 + "_" + seedFolderName + "_Result.json"
//                        "--no-cache"
                };
                PMD.runPmd(pmdConfig);
            }
            for (int i = 0; i < subSeedFolderNameList.size(); i++) {
                readPMDResultFile(REPORT_FOLDER.getAbsolutePath() + File.separator + "iter0_" + subSeedFolderNameList.get(i) + "_Result.json");
            }
        }
    }

}
