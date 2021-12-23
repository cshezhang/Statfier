package edu.polyu;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import edu.polyu.report.PMD_Report;
import edu.polyu.report.SpotBugs_Report;
import net.sourceforge.pmd.PMD;
import org.junit.Test;
import thread.PMD_Invoker;
import thread.SpotBugs_Invoker;

import static edu.polyu.Util.PMDResultsFolder;
import static edu.polyu.Util.SINGLE_TESTING;
import static edu.polyu.Util.SpotBugsResultsFolder;
import static edu.polyu.Util.getFilenamesFromFolder;
import static edu.polyu.Util.getProperty;
import static edu.polyu.Util.jarStr;
import static edu.polyu.Util.listAveragePartition;
import static edu.polyu.Util.readPMDResultFile;
import static edu.polyu.Util.readSpotBugsResultFile;
import static edu.polyu.Util.sep;
import static edu.polyu.Util.threadCount;
import static edu.polyu.Util.threadPool;

/*
 * @Description: This class is used for different invocation functions.
 * @Author: Vanguard
 * @Date: 2021-10-27 14:20:55
 */
public class Invoker {

    public static void invokeCommands(String[] cmdArgs) {
        StringBuilder args = new StringBuilder();
        for(int i = 0; i < cmdArgs.length; i++) {
            args.append(cmdArgs[i] + " ");
        }
        InputStream in;
        try {
            Process process = Runtime.getRuntime().exec(cmdArgs);
            in = process.getInputStream();
            BufferedReader read = new BufferedReader(new InputStreamReader(in, "GBK"));
            String line;
            StringBuilder builder = new StringBuilder();
            while((line = read.readLine()) != null) {
                builder.append(line);
            }
            int exitValue = process.waitFor();
            if(exitValue != 0) {
                System.err.println("Fail to Invoke Commands: " + args);
                System.err.println("Log: " + builder);
                int a = 1 / 0;
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
    public static void compileJavaSourceFile(String folderPath, String fileName, String classFileFolder) {
        if(!fileName.endsWith(".java")) {
            System.err.println("File: " + fileName + " is not ended by .java");
            System.exit(0);
        }
        fileName = fileName.substring(0, fileName.length() - 5);
        List<String> cmd_list = new ArrayList<>();
        cmd_list.add("javac");
        cmd_list.add("-d");
        cmd_list.add(classFileFolder);  // Generated class files are saved in this folder.
        cmd_list.add("-cp");
        cmd_list.add(jarStr.toString());  //
        cmd_list.add(folderPath + sep + fileName + ".java");
        invokeCommands(cmd_list.toArray(new String[cmd_list.size()]));
    }

    public static void initThreadPool() {
        if(Boolean.parseBoolean(getProperty("SINGLE_THREADPOOL"))) {
            threadPool = Executors.newSingleThreadExecutor();
        }
        if(Boolean.parseBoolean(getProperty("FIXED_THREADPOOL"))) {
            threadPool = Executors.newFixedThreadPool(threadCount);
        }
        if(Boolean.parseBoolean(getProperty("CACHED_THREADPOOL"))) {
            threadPool = Executors.newCachedThreadPool();
        }
        if(threadPool == null) {
            System.err.println("Thread Pool cannot be inited!");
            System.exit(-1);
        }
    }

    public static void waiitThreadPoolEnding() {
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // seedFolderPath is the java source code folder (seed path), like: SpotBugs_Seeds, iter1, iter2...
    // seedFolderName is seed folder name (last token of folderPath), like: SpotBugs_Seeds, iter1, iter2...
    // Generated class files are saved in SpotBugsClassFolder
    public static List<SpotBugs_Report> invokeSpotBugs(String seedFolderPath, String seedFolderName) {
        if(seedFolderPath.endsWith(sep)) {
            seedFolderPath = seedFolderPath.substring(0, seedFolderPath.length() - 1);
        }
        if(SINGLE_TESTING) {
            System.out.println("Invoke SpotBugs Path: " + seedFolderPath + "  seedFolderName: " + seedFolderName);
            System.out.println("Invoke Target Path: " + seedFolderPath);
            System.out.println("Output Name: " + seedFolderName);
        }
        List<String> seedFileNamesWithSuffix = getFilenamesFromFolder(seedFolderPath, false); // Filenames with suffix
        initThreadPool();
        List<List<String>> lists = listAveragePartition(seedFileNamesWithSuffix, threadCount);
        long startExecutionTime = System.currentTimeMillis();
        for(int i = 0; i < lists.size(); i++) {
            threadPool.submit(new SpotBugs_Invoker(seedFolderPath, seedFolderName, lists.get(i)));
        }
        waiitThreadPoolEnding();
        // Here we want to invoke SpotBugs one time and get all analysis results
        // But it seems we cannot process identical class in different folders, this can lead to many FPs or FNs
        long executionTime = System.currentTimeMillis() - startExecutionTime;
        List<SpotBugs_Report> reports = new ArrayList<>();
        for(int i = 0; i < seedFileNamesWithSuffix.size(); i++) {
            String seedFilenameWithSuffix = seedFileNamesWithSuffix.get(i);
            String seedFilename = seedFilenameWithSuffix.substring(0, seedFilenameWithSuffix.length() - 5);
            String report_path = SpotBugsResultsFolder.getAbsolutePath() + sep + seedFilename + "_Result.xml";
            reports.addAll(readSpotBugsResultFile(seedFolderPath, report_path));
        }
        System.out.println(String.format(
                "This Iteration Invocation and Compiling Time is: " + String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(executionTime),
                TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))
                )));
        return reports;
    }

    public static List<PMD_Report> invokePMD(String seedFolderPath, String seedFolderName) {
        long startExecutionTime = System.currentTimeMillis();
        initThreadPool();
        for(int i = 0; i < 20; i++) {
            threadPool.submit(new PMD_Invoker(seedFolderPath + sep + "PMD_Seeds_" + i, seedFolderName + "_"  + i));
        }
        waiitThreadPoolEnding();
        long executionTime = System.currentTimeMillis() - startExecutionTime;
        List<PMD_Report> reports = new ArrayList<>();
        for(int i = 0; i < 20; i++) {
            readPMDResultFile(PMDResultsFolder.getAbsolutePath() + sep + seedFolderName + "_" + i + "_Result.json");
        }
        System.out.println(String.format(
                "This Iteration Invocation and Compiling Time is: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(executionTime),
                        TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))
                )));
        return reports;
    }


    @Test
    public void testInvokePMD() {
        String targetPath = "/home/huaien/projects/SAMutator/seeds/PMD_Seeds";
        String[] pmdArgs = {
                "-d", targetPath,
                "-R", "./allRules.xml",
                "-f", "html",
                "-r", "./PMD_Result.html",
                "-cache", "./PMD_Cache.bin"
        };
        PMD.main(pmdArgs);
    }

}
