package edu.polyu.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.polyu.report.CheckStyle_Report;
import edu.polyu.report.Infer_Report;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.thread.CheckStyle_InvokeThread;
import edu.polyu.thread.Infer_InvokeThread;
import edu.polyu.thread.PMD_InvokeThread;
import edu.polyu.thread.SpotBugs_InvokeThread;
import org.zeroturnaround.exec.ProcessExecutor;

import static edu.polyu.util.Util.CheckStyleResultFolder;
import static edu.polyu.util.Util.INFER_MUTATION;
import static edu.polyu.util.Util.InferResultFolder;
import static edu.polyu.util.Util.JAVAC_PATH;
import static edu.polyu.util.Util.PMDResultFolder;
import static edu.polyu.util.Util.PMD_MUTATION;
import static edu.polyu.util.Util.SINGLE_TESTING;
import static edu.polyu.util.Util.SPOTBUGS_MUTATION;
import static edu.polyu.util.Util.SpotBugsPath;
import static edu.polyu.util.Util.SpotBugsResultFolder;
import static edu.polyu.util.Util.THREAD_COUNT;
import static edu.polyu.util.Util.getFilenamesFromFolder;
import static edu.polyu.util.Util.getProperty;
import static edu.polyu.util.Util.inferJarStr;
import static edu.polyu.util.Util.listAveragePartition;
import static edu.polyu.util.Util.readCheckStyleResultFile;
import static edu.polyu.util.Util.readInferResultFile;
import static edu.polyu.util.Util.readPMDResultFile;
import static edu.polyu.util.Util.readSpotBugsResultFile;
import static edu.polyu.util.Util.sep;
import static edu.polyu.util.Util.spotBugsJarStr;
import static edu.polyu.util.Util.subSeedFolderNameList;


/*
 * @Description: This class is used for different invocation functions.
 * @Author: Vanguard
 * @Date: 2021-10-27 14:20:55
 */
public class Invoker {

    public static void invokeCommandsByZT(String[] cmdArgs) {
        StringBuilder argStr = new StringBuilder();
        for(String arg : cmdArgs) {
            argStr.append(arg + " ");
        }
        if(SINGLE_TESTING) {
            System.out.println("Invoke Command: " + argStr);
        }
        try {
            int exitValue = new ProcessExecutor().command(cmdArgs).execute().getExitValue();
            if(PMD_MUTATION && exitValue != 4 && exitValue != 0) {
                System.err.println("Execute PMD Error!");
                System.err.println(argStr);
            }
            if(SPOTBUGS_MUTATION && exitValue != 0) {
                System.err.println("Execute SpotBugs Error!");
                System.err.println(argStr);
                System.exit(-1);
            }
        } catch (Exception e) {
            System.err.println(argStr);
            e.printStackTrace();
        }
    }

    public static void invokeCommands(String[] cmdArgs) {
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
                System.err.println("Fail to Invoke Commands: " + args);
                System.err.println("Log: " + builder);
                System.exit(-1);
            }
            process.getOutputStream().close();
        } catch (IOException e) {
            System.err.println("Fail to Invoke Commands: " + args);
            e.printStackTrace();
        } catch (InterruptedException e) {
            System.err.println("Fail to Invoke Commands: " + args);
            e.printStackTrace();
        }
    }

    // folderPath is purely folder path and doesn't contain java file name.
    public static void compileJavaSourceFile(String srcFolderPath, String fileName, String classFileFolder) {
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
        invokeCommandsByZT(cmd_list.toArray(new String[cmd_list.size()]));
    }

    private static ExecutorService threadPool;

    private static void initThreadPool() {
        if(Boolean.parseBoolean(getProperty("FIXED_THREADPOOL"))) {
            threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        } else {
            if (Boolean.parseBoolean(getProperty("CACHED_THREADPOOL"))) {
                threadPool = Executors.newCachedThreadPool();
            } else {
                threadPool = Executors.newSingleThreadExecutor();
            }
        }
    }

    private static void waitThreadPoolEnding() {
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // seedFolderName is seed folder name (last token of folderPath), like: SpotBugs_Seeds, iter1, iter2...
    // Generated class files are saved in SpotBugsClassFolder
    public static List<SpotBugs_Report> invokeSpotBugs(String seedFolderPath) { // seedFolderPath is the java source code folder (seed path), like: /path/to/SingleTesting
        if(seedFolderPath.endsWith(sep)) {
            seedFolderPath = seedFolderPath.substring(0, seedFolderPath.length() - 1);
        }
        if(SINGLE_TESTING) {
            System.out.println("Invoke SpotBugs Path: " + seedFolderPath);
        }
        initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            String seedFolderName = subSeedFolderNameList.get(i);
            String subSeedFolderPath = seedFolderPath + File.separator + seedFolderName;
            List<String> seedFileNames = getFilenamesFromFolder(subSeedFolderPath, false);
            threadPool.submit(new SpotBugs_InvokeThread(subSeedFolderPath, seedFolderName, seedFileNames));
        }
        waitThreadPoolEnding();
        // Here we want to invoke SpotBugs one time and get all analysis results
        // But it seems we cannot process identical class in different folders, this can lead to many FPs or FNs
        List<SpotBugs_Report> reports = new ArrayList<>();
        for(String subSeedFolderName : subSeedFolderNameList) {
            List<String> reportPaths = getFilenamesFromFolder(SpotBugsResultFolder.getAbsolutePath() + File.separator + subSeedFolderName, true);
            for(String reportPath : reportPaths) {
                reports.addAll(readSpotBugsResultFile(seedFolderPath, reportPath));
            }
        }
        return reports;
    }

    public static List<Infer_Report> invokeInfer(int iterDepth, String seedFolderPath) {
        initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new Infer_InvokeThread(iterDepth, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding();
        List<Infer_Report> reports = new ArrayList<>();
        System.out.println("Infer Result Folder: " + InferResultFolder.getAbsolutePath());
        List<String> reportPaths = getFilenamesFromFolder(InferResultFolder.getAbsolutePath(), true);
        if(SINGLE_TESTING) {
            for(String reportPath : reportPaths) {
                System.out.println("Infer Report Path: " + reportPath);
            }
        }
        for(int i = 0; i < reportPaths.size(); i++) {
            reports.addAll(readInferResultFile(iterDepth, reportPaths.get(i)));
        }
        return reports;
    }

    public static List<CheckStyle_Report> invokeCheckStyle(int iterDepth, String seedFolderPath) {
        initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new CheckStyle_InvokeThread(iterDepth, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding();
        List<CheckStyle_Report> reports = new ArrayList<>();
        List<String> reportPaths = getFilenamesFromFolder(CheckStyleResultFolder.getAbsolutePath(), true);
        for(int i = 0; i < reportPaths.size(); i++) {
            reports.addAll(readCheckStyleResultFile(reportPaths.get(i)));
        }
        return reports;
    }

    public static List<PMD_Report> invokePMD(int iterDepth, String seedFolderPath) {
        initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new PMD_InvokeThread(iterDepth, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding();
        List<PMD_Report> reports = new ArrayList<>();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            reports.addAll(readPMDResultFile(PMDResultFolder.getAbsolutePath()  + File.separator + "iter" + iterDepth + "_" + subSeedFolderNameList.get(i) + "_Result.json"));
        }
        return reports;
    }

}
