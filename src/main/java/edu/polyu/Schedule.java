package edu.polyu;

import static edu.polyu.Invoker.invokePMD;
import static edu.polyu.Invoker.invokeSpotBugs;
import static edu.polyu.Util.*;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.report.SpotBugs_Violation;

/**
 * Description: This file is the main class for our framework
 * Author: Austin
 * Date: 2021/8/25 10:03 上午
 */
public class Schedule {

    // Implement singleton pattern
    private static final Schedule tester = new Schedule();
    private Schedule() {}
    public static Schedule getInstance() {
        return tester;
    }

    public void testAST(String targetFolder) {
        if(targetFolder.contains(".")) {
            System.err.println("You should provide a folder path!");
            System.exit(-1);
        }
        List<String> filePaths = getFilenamesFromFolder(targetFolder, true);
        for(String filePath : filePaths) {
            System.out.println("Testing Java File: " + filePath);
            ASTWrapper mutator = new ASTWrapper(filePath);
            mutator.printBasicInfo();
        }
    }

    public void schedulePureRandomTesting(ArrayList<ASTWrapper> srcWarppers) {
        int current_depth = -1;
        ArrayDeque<ASTWrapper> que = new ArrayDeque<>();
        que.addAll(srcWarppers);
        while(!que.isEmpty()) {
            ASTWrapper head = que.pollFirst();
            if(current_depth != head.depth) {
                current_depth = head.depth;
                if(current_depth != 0) {
                    System.out.println(head.getFolderPath());
//                    tester.locateMutationCode(head.getFolderPath()); // No code location, just randomly select mutants
                }
            }
            ArrayList<ASTWrapper> newWrappers = head.pureRandomMutate();
            que.addAll(newWrappers);
        }
    }

    public void scheduleGuidedRandomTesting(ArrayList<ASTWrapper> srcWarppers) {
        int current_depth = -1;
        ArrayDeque<ASTWrapper> que = new ArrayDeque<>();
        que.addAll(srcWarppers);
        while(!que.isEmpty()) {
            ASTWrapper head = que.pollFirst();
            if(SINGLE_TESTING) {
                System.out.println("Searching file: [" + head.getFilePath() + "] Head Depth: " + head.depth);
            }
            if(current_depth != head.depth) {
                current_depth = head.depth;
                if(current_depth != 0) {
                    tester.locateMutationCode(head.getFolderPath());
                }
            }
            ArrayList<ASTWrapper> newWrappers = head.guidedRandomMutate();
            que.addAll(newWrappers);
        }
    }

    public void scheduleBFS(ArrayList<ASTWrapper> srcWrappers) {
        int current_depth = -1;
        ArrayDeque<ASTWrapper> que = new ArrayDeque<>();
        que.addAll(srcWrappers);
        while(!que.isEmpty()) {
            ASTWrapper head = que.pollFirst();
            int file_hash = head.getDocument().get().hashCode();
            file2hash.put(head.getFilePath(), file_hash);
            // System.out.println("Searching file: [" + head.getFilePath() + "] Head Depth: " + head.depth);
            if(current_depth != head.depth) {
                current_depth = head.depth;
                if(current_depth != 0) {
                    if(SINGLE_TESTING) {
                        System.out.println("Current Depth: " + head.depth);
                    }
                    tester.locateMutationCode(head.getFolderPath());
                }
            }
            ArrayList<ASTWrapper> newWrappers = head.mutate();
            que.addAll(newWrappers);
        }
    }

    public void pureRandomTesting(String targetPath) {
        if (targetPath.endsWith(".java")) {
            System.err.println("You should give a folder!");
            System.exit(-1);
        }
        List<String> seedPaths = getFilenamesFromFolder(targetPath, true);
        System.out.println("Random Testing Initial Seed Count: " + seedPaths.size());
        ArrayList<ASTWrapper> srcWrappers = new ArrayList<>();
        for(int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            // System.out.println("Seed Path: " + seedPath);
            ASTWrapper astWrapper = new ASTWrapper(seedPath);
            srcWrappers.add(astWrapper);
        }
        System.out.println("Initial Wrappers Size: " + srcWrappers.size());
        schedulePureRandomTesting(srcWrappers);
    }

    public void guidedRandomTesting(String seedFolderPath) {
        if (seedFolderPath.endsWith(".java")) {
            System.err.println("You should give a folder!");
            System.exit(-1);
        }
        locateMutationCode(seedFolderPath); // init analysis for seed files
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("Random Testing Initial Seed Count: " + seedPaths.size());
        ArrayList<ASTWrapper> srcWrappers = new ArrayList<>();
        for(int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            // System.out.println("Seed Path: " + seedPath);
            ASTWrapper astWrapper = new ASTWrapper(seedPath);
            srcWrappers.add(astWrapper);
        }
        System.out.println("Initial Wrappers Size: " + srcWrappers.size());
        scheduleGuidedRandomTesting(srcWrappers);
    }

    public void executeMutation(String targetPath) {
        locateMutationCode(targetPath);
        List<String> seedPaths = getFilenamesFromFolder(targetPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
        ArrayList<ASTWrapper> srcWrappers = new ArrayList<>();
        for (int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            if (!file2line.containsKey(seedPath)) {
                continue;
            }
            ASTWrapper astWrapper = new ASTWrapper(seedPath);
            srcWrappers.add(astWrapper);
        }
        System.out.println("Initial Wrappers Size: " + srcWrappers.size());
        scheduleBFS(srcWrappers);
    }

    // This function only can invoke static analysis tool and cannot include other parts.
    public void locateMutationCode(String seedFolderPath) {
        String seedFolderName = Path2Last(seedFolderPath);
        System.out.println(seedFolderPath + " is located and Analysis Output Folder is: " + seedFolderName);
        if(PMD_MUTATION) {       
            invokePMD(seedFolderPath, seedFolderName);
            List<PMD_Report> reports = readPMDResultFile(userdir + sep + "PMD_Results" + sep + seedFolderName + "_Result.json");
            for (PMD_Report report : reports) {
                if(!file2line.containsKey(report.getFilename())) {
                    file2line.put(report.getFilename(), new HashSet<>());
                }
                for (PMD_Violation violation : report.getViolations()) {
                    file2line.get(report.getFilename()).add(violation.beginLine);
                    file2line.get(report.getFilename()).add(violation.endLine);
                }
            }
        }
        if(SPOTBUGS_MUTATION) {
            List<SpotBugs_Report> reports = invokeSpotBugs(seedFolderPath, seedFolderName);
            all_SpotBugs_Reports.add(reports);
            for(SpotBugs_Report report : reports) {
                if(!file2line.containsKey(report.getFilename())) {
                    file2line.put(report.getFilename(), new HashSet<>());
                    file2bugs.put(  report.getFilename(), new HashMap<>());
                }
                for(SpotBugs_Violation violation : report.getViolations()) {
                    file2line.get(report.getFilename()).add(violation.getBeginLine());
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilename());
                    if(!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
            }
        }
    }

}
