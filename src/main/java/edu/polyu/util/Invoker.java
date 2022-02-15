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

import edu.polyu.report.PMD_Report;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.thread.PMD_InvokeThread;
import edu.polyu.thread.SpotBugs_InvokeThread;
import edu.polyu.thread.StreamInfoThread;
import org.zeroturnaround.exec.ProcessExecutor;

import static edu.polyu.util.Util.*;

/*
 * @Description: This class is used for different invocation functions.
 * @Author: Vanguard
 * @Date: 2021-10-27 14:20:55
 */
public class Invoker {

    public static String getConfigXMLFile(String rule) {
        String xmlFilePath = "";
        return xmlFilePath;
    }

//    public static void invokeCommands(String command) {
//        ProcessBuilder pb;
//        Process process = null;
//        BufferedReader br = null;
//        StringBuilder resMsg = null;
//        OutputStream os = null;
//        String cmd1 = "cmd.exe";
//        int exitValue;
//        try {
//            pb = new ProcessBuilder(cmd1);
//            pb.redirectErrorStream(true);
//            process = pb.start();
//            os = process.getOutputStream();
//            os.write(command.getBytes());
//            os.flush();
//            os.close();
//            boolean isNeedResultMsg = true;
//            resMsg = new StringBuilder();
//            // get command result
//            if (isNeedResultMsg) {
//                br = new BufferedReader(new InputStreamReader(process.getInputStream()));
//                String s;
//                while ((s = br.readLine()) != null) {
//                    resMsg.append(s + "\n");
//                }
//                resMsg.deleteCharAt(resMsg.length()-1);
//                exitValue = process.waitFor();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                if (os != null) {
//                    os.close();
//                }
//                if (br != null) {
//                    br.close();
//                }
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//
//            if (process != null) {
//                process.destroy();
//            }
//        }
//        System.out.println(resMsg.toString());
//    }

    public static void invokeCommandsByZT(String[] cmdArgs) {
        try {
            int exitValue = new ProcessExecutor().command(cmdArgs)
                    .execute().getExitValue();
            StringBuilder builder = new StringBuilder();
            for(String arg : cmdArgs) {
                builder.append(arg + " ");
            }
            if(PMD_MUTATION && exitValue != 4 && exitValue != 0) {
                System.err.println("Execute PMD Error!");
                System.err.println(builder);
            }
            if(SPOTBUGS_MUTATION && exitValue != 0) {
                System.err.println("Execute SpotBugs Error!");
                System.err.println(builder);
            }
        } catch (Exception e) {
            StringBuilder args = new StringBuilder();
            for(String str : cmdArgs) {
                args.append(str + " ");
            }
            System.err.println(args);
            e.printStackTrace();
        }
    }

    public static void invokeCommandsWithLog(String[] cmdArgs) {
//        System.out.println(cmdArgs);
        Process process = null;
        try {
            process = Runtime.getRuntime().exec(cmdArgs);
//            bufferedReader = new BufferedReader(new InputStreamReader(new BufferedInputStream(process.getInputStream()), Charset.forName("GB2312")));
            // 开启线程读取错误输出，避免阻塞
            new StreamInfoThread(process.getErrorStream(), "error").start();
            new StreamInfoThread(process.getInputStream(), "output").start();
            int returnValue = process.waitFor();
            if(returnValue != 0 && returnValue != 4) {
                System.err.println("Error!");
                System.exit(-1);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            try {
                if (process != null) {
                    process.getInputStream().close();
                    process.getOutputStream().close();
                    process.getErrorStream().close();
                    process.destroy();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
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
    public static void compileJavaSourceFile(String folderPath, String fileName, String classFileFolder) {
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
        cmd_list.add(folderPath  + File.separator + fileName + ".java");
        invokeCommands(cmd_list.toArray(new String[cmd_list.size()]));
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
        List<List<String>> lists = listAveragePartition(seedFileNamesWithSuffix, THREAD_COUNT);
        long startExecutionTime = System.currentTimeMillis();
        for(int i = 0; i < lists.size(); i++) {
            threadPool.submit(new SpotBugs_InvokeThread(seedFolderPath, seedFolderName, lists.get(i)));
        }
        waitThreadPoolEnding();
        // Here we want to invoke SpotBugs one time and get all analysis results
        // But it seems we cannot process identical class in different folders, this can lead to many FPs or FNs
        long executionTime = System.currentTimeMillis() - startExecutionTime;
        List<SpotBugs_Report> reports = new ArrayList<>();
        for(int i = 0; i < seedFileNamesWithSuffix.size(); i++) {
            String seedFilenameWithSuffix = seedFileNamesWithSuffix.get(i);
            String seedFilename = seedFilenameWithSuffix.substring(0, seedFilenameWithSuffix.length() - 5);
            String report_path = SpotBugsResultFolder.getAbsolutePath()  + File.separator + seedFilename + "_Result.xml";
            reports.addAll(readSpotBugsResultFile(seedFolderPath, report_path));
        }
        System.out.println(String.format(
                "SpotBugs Invocation Time is: " + String.format("%d min, %d sec",
                TimeUnit.MILLISECONDS.toMinutes(executionTime),
                TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))
                )));
        return reports;
    }

    public static List<PMD_Report> invokePMD(int iterDepth, String seedFolderPath) {
        long startExecutionTime = System.currentTimeMillis();
        initThreadPool();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            threadPool.submit(new PMD_InvokeThread(iterDepth, seedFolderPath, subSeedFolderNameList.get(i)));
        }
        waitThreadPoolEnding();
        long executionTime = System.currentTimeMillis() - startExecutionTime;
        List<PMD_Report> reports = new ArrayList<>();
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            reports.addAll(readPMDResultFile(PMDResultFolder.getAbsolutePath()  + File.separator + "iter" + iterDepth + "_" + subSeedFolderNameList.get(i) + "_Result.json"));
        }
        System.out.println(String.format(
                "PMD Invocation Time is: " + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(executionTime),
                        TimeUnit.MILLISECONDS.toSeconds(executionTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(executionTime))
                )));
        return reports;
    }

}
