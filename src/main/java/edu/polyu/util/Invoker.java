package edu.polyu.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.polyu.report.CheckStyle_Report;
import edu.polyu.report.Infer_Report;
import edu.polyu.report.PMD_Report;
import edu.polyu.thread.CheckStyle_InvokeThread;
import edu.polyu.thread.Infer_InvokeThread;
import edu.polyu.thread.PMD_InvokeThread;
import edu.polyu.thread.SonarQube_InvokeThread;
import edu.polyu.thread.SpotBugs_InvokeThread;
import org.zeroturnaround.exec.ProcessExecutor;

import static edu.polyu.util.Utility.CheckStyleResultFolder;
import static edu.polyu.util.Utility.INFER_MUTATION;
import static edu.polyu.util.Utility.InferResultFolder;
import static edu.polyu.util.Utility.JAVAC_PATH;
import static edu.polyu.util.Utility.PMDResultFolder;
import static edu.polyu.util.Utility.PMD_MUTATION;
import static edu.polyu.util.Utility.Path2Last;
import static edu.polyu.util.Utility.SINGLE_TESTING;
import static edu.polyu.util.Utility.SONARQUBE_LOGIN;
import static edu.polyu.util.Utility.SONARQUBE_PROJECT_KEY;
import static edu.polyu.util.Utility.SPOTBUGS_MUTATION;
import static edu.polyu.util.Utility.SonarQubeResultFolder;
import static edu.polyu.util.Utility.SonarScannerPath;
import static edu.polyu.util.Utility.SpotBugsResultFolder;
import static edu.polyu.util.Utility.THREAD_COUNT;
import static edu.polyu.util.Utility.getFilenamesFromFolder;
import static edu.polyu.util.Utility.getProperty;
import static edu.polyu.util.Utility.inferJarStr;
import static edu.polyu.util.Utility.initThreadPool;
import static edu.polyu.util.Utility.readCheckStyleResultFile;
import static edu.polyu.util.Utility.readInferResultFile;
import static edu.polyu.util.Utility.readPMDResultFile;
import static edu.polyu.util.Utility.readSonarQubeResultFile;
import static edu.polyu.util.Utility.readSpotBugsResultFile;
import static edu.polyu.util.Utility.sep;
import static edu.polyu.util.Utility.spotBugsJarStr;
import static edu.polyu.util.Utility.subSeedFolderNameList;
import static edu.polyu.util.Utility.waitThreadPoolEnding;
import static edu.polyu.util.Utility.writeFileByLine;


/*
 * @Description: This class is used for different invocation functions.
 * @Author: Vanguard
 * @Date: 2021-10-27 14:20:55
 */
public class Invoker {

    public static List<String> failedCommands = new ArrayList<>();

    public static String invokeCommandsByZT(String[] cmdArgs, String returnFormat) {
        StringBuilder argStr = new StringBuilder();
        for (String arg : cmdArgs) {
            argStr.append(arg + " ");
        }
        try {
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            String out = new ProcessExecutor().command(cmdArgs).readOutput(true).redirectError(errorStream).execute().outputUTF8();
            if(returnFormat.equalsIgnoreCase("json")) {
                return out;
            } else {
                return null;
            }
        } catch (Exception e) {
            System.err.println(argStr);
            e.printStackTrace();
            return null;
        }
    }

    public static boolean invokeCommandsByZT(String[] cmdArgs) {
        StringBuilder argStr = new StringBuilder();
        for (String arg : cmdArgs) {
            argStr.append(arg + " ");
        }
        try {
            ByteArrayOutputStream errorStream = new ByteArrayOutputStream();
            int exitValue = new ProcessExecutor().command(cmdArgs).redirectError(errorStream).execute().getExitValue();
//            System.out.println("Error: " + new String(errorStream.toByteArray()));
            // CheckStyle Return value is the number of bugs.
            if(PMD_MUTATION && exitValue != 4 && exitValue != 0) {
                System.err.println("Execute PMD Error!");
                System.err.println(argStr);
                return false;
            }
            if(SPOTBUGS_MUTATION && exitValue != 0) {
//                System.err.println("Execute SpotBugs Error!");
//                System.err.println(argStr);
                failedCommands.add(argStr.toString());
                return false;
            }
            if(INFER_MUTATION && exitValue != 0) {
                System.err.println("Exit Value: " + exitValue);
                System.err.println("Execute Infer Error!");
                System.err.println(argStr);
                System.err.println("Error: " + new String(errorStream.toByteArray()));
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
        if(SINGLE_TESTING) {
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
    // Generated class files are saved in SpotBugsClassFolder
    public static void invokeSpotBugs(String seedFolderPath) { // seedFolderPath is the java source code folder (seed path), like: /path/to/SingleTesting
        if(seedFolderPath.endsWith(sep)) {
            seedFolderPath = seedFolderPath.substring(0, seedFolderPath.length() - 1);
        }
        if(SINGLE_TESTING) {
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
            List<String> reportPaths = getFilenamesFromFolder(SpotBugsResultFolder.getAbsolutePath() + File.separator + subSeedFolderName, true);
            for(String reportPath : reportPaths) {
                readSpotBugsResultFile(seedFolderPath + File.separator + subSeedFolderName, reportPath);
            }
        }
    }

    // Add setting and dummy-binaries folder for SonarQube seed folder
    public static void writeSettingFile(String seedFolderPath, String settingFilePath) {
        List<String> contents = new ArrayList<>();
        contents.add("sonar.projectKey=Statfier\nsonar.projectName=Statfier\nsonar.projectVersion=1.0");
        contents.add("sonar.login=" + SONARQUBE_PROJECT_KEY);
        contents.add("sonar.sourceEncoding=UTF-8");
        contents.add("sonar.scm.disabled=true");
        contents.add("sonar.cpd.exclusions=**/*");
        contents.add("sonar.sources=" + seedFolderPath);
        contents.add("sonar.java.source=17");
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
        writeFileByLine(settingFilePath, contents);
    }

    public static void invokeSonarQube(String seedFolderPath) {
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            String subSeedFolderPath = seedFolderPath + File.separator + subSeedFolderNameList.get(i);
            if(SINGLE_TESTING) {
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
            invokeCommands[2] = SonarScannerPath + " -Dproject.settings=" + settingPath;
            invokeCommandsByZT(invokeCommands);
            String[] curlCommands = new String[4];
            curlCommands[0] = "curl";
            curlCommands[1] = "-u";
            curlCommands[2] = "sqp_b5cd7ba6cd143a589260158df861fcf43a20f5b9:";
            curlCommands[3] = "http://localhost:9000/api/issues/search?componentKeys=Statfier&facets=types&facetMode=count";
            String jsonContent = invokeCommandsByZT(curlCommands, "json");
            String reportPath = SonarQubeResultFolder.getAbsolutePath() + File.separator + "iter0_" + subSeedFolderNameList.get(i) + ".json";
            writeFileByLine(reportPath, jsonContent);
            readSonarQubeResultFile(jsonContent);
        }
    }

    public static void invokeInfer(String seedFolderPath) {
        ExecutorService threadPool = initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new Infer_InvokeThread(0, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding(threadPool);
        System.out.println("Infer Result Folder: " + InferResultFolder.getAbsolutePath());
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        for(String seedPath : seedPaths) {
            String reportPath = InferResultFolder.getAbsolutePath() + File.separator + "iter0_" + Path2Last(seedPath) + File.separator + "report.json";
            readInferResultFile(seedPath, reportPath);
        }
    }

    public static void invokeCheckStyle(String seedFolderPath) {
        ExecutorService threadPool = initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new CheckStyle_InvokeThread(0, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding(threadPool);
        List<String> reportPaths = getFilenamesFromFolder(CheckStyleResultFolder.getAbsolutePath(), true);
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
            readPMDResultFile(PMDResultFolder.getAbsolutePath()  + File.separator + "iter0_" + subSeedFolderNameList.get(i) + "_Result.json");
        }
    }

}
