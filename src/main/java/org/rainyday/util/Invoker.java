package org.rainyday.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;

import org.rainyday.thread.CheckStyle_InvokeThread;
import org.rainyday.thread.Infer_InvokeThread;
import org.rainyday.thread.PMD_InvokeThread;
import org.rainyday.thread.SpotBugs_InvokeThread;
import org.zeroturnaround.exec.ProcessExecutor;

import static org.rainyday.util.Utility.INFER_MUTATION;
import static org.rainyday.util.Utility.JAVAC_PATH;
import static org.rainyday.util.Utility.PMD_MUTATION;
import static org.rainyday.util.Utility.Path2Last;
import static org.rainyday.util.Utility.DEBUG;
import static org.rainyday.util.Utility.SONARQUBE_LOGIN;
import static org.rainyday.util.Utility.SONARQUBE_MUTATION;
import static org.rainyday.util.Utility.SONARQUBE_PROJECT_KEY;
import static org.rainyday.util.Utility.SONAR_SCANNER_PATH;
import static org.rainyday.util.Utility.SPOTBUGS_MUTATION;
import static org.rainyday.util.Utility.getFilenamesFromFolder;
import static org.rainyday.util.Utility.inferJarStr;
import static org.rainyday.util.Utility.initThreadPool;
import static org.rainyday.util.Utility.readCheckStyleResultFile;
import static org.rainyday.util.Utility.readInferResultFile;
import static org.rainyday.util.Utility.readPMDResultFile;
import static org.rainyday.util.Utility.readSonarQubeResultFile;
import static org.rainyday.util.Utility.readSpotBugsResultFile;
import static org.rainyday.util.Utility.reportFolder;
import static org.rainyday.util.Utility.sep;
import static org.rainyday.util.Utility.spotBugsJarStr;
import static org.rainyday.util.Utility.subSeedFolderNameList;
import static org.rainyday.util.Utility.waitThreadPoolEnding;
import static org.rainyday.util.Utility.writeLinesToFile;


/**
 * @Description: This class is used for different invocation functions.
 * @Author: Vanguard
 * @Date: 2021-10-27 14:20:55
 */
public class Invoker {

    public static List<String> failedCommands = new ArrayList<>();

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
            System.exit(0);
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
    // Generated class files are saved in classFolder
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
            List<String> reportPaths = getFilenamesFromFolder(reportFolder.getAbsolutePath() + File.separator + subSeedFolderName, true);
            for(String reportPath : reportPaths) {
                readSpotBugsResultFile(seedFolderPath + File.separator + subSeedFolderName, reportPath);
            }
        }
    }

    // Add setting and dummy-binaries folder for SonarQube seed folder
    public static void writeSettingFile(String seedFolderPath, String settingFilePath) {
        List<String> contents = new ArrayList<>();
        contents.add("sonar.projectKey=" + SONARQUBE_PROJECT_KEY);
        contents.add("sonar.projectName=" + SONARQUBE_PROJECT_KEY);
        contents.add("sonar.projectVersion=1.0");
        contents.add("sonar.login=" + SONARQUBE_LOGIN);
        contents.add("sonar.sourceEncoding=UTF-8");
        contents.add("sonar.scm.disabled=true");
        contents.add("sonar.cpd.exclusions=**/*");
        contents.add("sonar.sources=" + seedFolderPath);
        contents.add("sonar.java.source=11");
        File dummyFolder = new File(seedFolderPath + File.separator + "dummy-binaries");
        if(!dummyFolder.exists()) {
            dummyFolder.mkdir();
        }
        File dummyFile = new File(seedFolderPath + File.separator + "dummy-binaries" + File.separator + "dummy.txt");
        if(!dummyFile.exists()) {
            try {
                dummyFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        contents.add("sonar.java.binaries=" + dummyFolder.getAbsolutePath());
        contents.add("sonar.java.test.binaries=" + dummyFolder.getAbsolutePath());
        writeLinesToFile(settingFilePath, contents);
    }

    public static void invokeSonarQube(String seedFolderPath) {
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            String subSeedFolderName = subSeedFolderNameList.get(i);
            String subSeedFolderPath = seedFolderPath + File.separator + subSeedFolderName;
            if(DEBUG) {
                System.out.println("Seed path: " + subSeedFolderPath);
            }
            String settingPath = subSeedFolderPath + File.separator + "settings";
            writeSettingFile(subSeedFolderPath, settingPath);
            String[] invokeCommands = new String[3];
            if(OSUtil.isWindows()) {
                invokeCommands[0] = "cmd.exe";
                invokeCommands[1] = "/c";
            } else {
                invokeCommands[0] = "/bin/bash";
                invokeCommands[1] = "-c";
            }
            System.out.println("Setting file path: " + settingPath);
            invokeCommands[2] = SONAR_SCANNER_PATH + " -Dproject.settings=" + settingPath;
            invokeCommandsByZT(invokeCommands); // invoke SonarQube to detect target folder
            try {
                Thread.currentThread().sleep(3000);
            } catch (InterruptedException e) {
                System.err.println("Interrupt waiting SQ!");
            }
            String reportFolderPath = reportFolder.getAbsolutePath() + File.separator + "iter0_" + subSeedFolderName;
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
            CNES_Commands.add(reportFolderPath);
            invokeCommandsByZT(CNES_Commands.toArray(new String[CNES_Commands.size()]));  // invoke CNES to export analysis report
            String reportPath = reportFolderPath + File.separator + "CNES_ReportName";
            readSonarQubeResultFile(reportPath);
        }
    }

    public static void invokeInfer(String seedFolderPath) {
        ExecutorService threadPool = initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new Infer_InvokeThread(0, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding(threadPool);
        System.out.println("Infer Result Folder: " + reportFolder.getAbsolutePath());
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        for(String seedPath : seedPaths) {
            String reportPath = reportFolder.getAbsolutePath() + File.separator + "iter0_" + Path2Last(seedPath) + File.separator + "report.json";
            readInferResultFile(seedPath, reportPath);
        }
    }

    public static void invokeCheckStyle(String seedFolderPath) {
        ExecutorService threadPool = initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new CheckStyle_InvokeThread(0, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding(threadPool);
        List<String> reportPaths = getFilenamesFromFolder(reportFolder.getAbsolutePath(), true);
        for(int i = 0; i < reportPaths.size(); i++) {
            readCheckStyleResultFile(reportPaths.get(i));
        }
    }

    public static void invokePMD(String seedFolderPath) {
        ExecutorService threadPool = initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new PMD_InvokeThread(0, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding(threadPool);
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            readPMDResultFile(reportFolder.getAbsolutePath()  + File.separator + "iter0_" + subSeedFolderNameList.get(i) + "_Result.json");
        }
    }

}
