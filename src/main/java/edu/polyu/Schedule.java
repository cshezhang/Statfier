package edu.polyu;

import static edu.polyu.Invoker.invokePMD;
import static edu.polyu.Invoker.invokeSpotBugs;
import static edu.polyu.Util.PMD_MUTATION;
import static edu.polyu.Util.Path2Last;
import static edu.polyu.Util.SEARCH_DEPTH;
import static edu.polyu.Util.SINGLE_TESTING;
import static edu.polyu.Util.SPOTBUGS_MUTATION;
import static edu.polyu.Util.file2bugs;
import static edu.polyu.Util.file2line;
import static edu.polyu.Util.getFilenamesFromFolder;
import static edu.polyu.Util.sep;
import static edu.polyu.Util.userdir;

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
 * Author: Vanguard
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
            ASTWrapper mutator = new ASTWrapper(filePath, "sub");
            mutator.printBasicInfo();
        }
    }

    public void schedulePureTesting(List<ASTWrapper> srcWrappers) {
        int current_depth = -1;
        ArrayDeque<ASTWrapper> que = new ArrayDeque<>();
        que.addAll(srcWrappers);
        while(!que.isEmpty()) {
            ASTWrapper head = que.pollFirst();
            if(current_depth != head.depth) {
                current_depth = head.depth;
                if(current_depth != 0) {
                    System.out.println(head.getFolderPath());
                    // No need to invocate locateMutationCode, just randomly select mutants
                }
                if (current_depth >= SEARCH_DEPTH) {
                    break;
                }
            }
            ArrayList<ASTWrapper> newWrappers = head.pureTransformation();
            que.addAll(newWrappers);
        }
    }

    public void schedulePureRandomTesting(List<ASTWrapper> srcWrappers) {
        int current_depth = -1;
        ArrayDeque<ASTWrapper> que = new ArrayDeque<>();
        que.addAll(srcWrappers);
        while(!que.isEmpty()) {
            ASTWrapper head = que.pollFirst();
            if(current_depth != head.depth) {
                current_depth = head.depth;
                if(current_depth != 0) {
                    System.out.println(head.getFolderPath());
                    // No need to invocate locateMutationCode, just randomly select mutants
                }
                if (current_depth >= SEARCH_DEPTH) {
                    break;
                }
            }
            ArrayList<ASTWrapper> newWrappers = head.pureRandomTransformation();
            que.addAll(newWrappers);
        }
    }

    public void scheduleGuidedRandomTesting(List<ASTWrapper> srcWarppers) {
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
                    String currentIterFolder = userdir + sep + "mutants" + sep + "iter" + current_depth;
                    tester.locateMutationCode(head.depth, currentIterFolder);
                }
                System.gc();
            }
            ArrayList<ASTWrapper> newWrappers = head.guidedRandomTransformation();
            que.addAll(newWrappers);
        }
    }

    public void scheduleBFS(List<ASTWrapper> srcWrappers) {
        int current_depth = -1;
        ArrayDeque<ASTWrapper> que = new ArrayDeque<>();
        que.addAll(srcWrappers);
        while(!que.isEmpty()) {
            ASTWrapper head = que.pollFirst();
            if(current_depth != head.depth) {
                current_depth = head.depth;
                if(current_depth != 0) {
                    if(SINGLE_TESTING) {
                        System.out.println("Current Depth: " + head.depth);
                    }
                    String currentIterFolder = userdir + sep + "mutants" + sep + "iter" + current_depth;
                    tester.locateMutationCode(head.depth, currentIterFolder);
                }
                if (current_depth >= SEARCH_DEPTH) {
                    break;
                }
                System.gc();
            }
            ArrayList<ASTWrapper> newWrappers = head.mainTransformation();
            que.addAll(newWrappers);
        }
    }

    public void pureTesting(String targetPath) {
        if (targetPath.endsWith(".java")) {
            System.err.println("You should give a folder for Seed Init!");
            System.exit(-1);
        }
        List<String> seedPaths = getFilenamesFromFolder(targetPath, true);
        System.out.println("Pure Random Testing Initial Seed Count: " + seedPaths.size());
        ArrayList<ASTWrapper> srcWrappers = new ArrayList<>();
        for(int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            String[] tokens = seedPath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if(SINGLE_TESTING) {
                System.out.println("Seed Path: " + seedPath);
            }
            ASTWrapper initSeedWrapper = new ASTWrapper(seedPath, seedFolderName);
            srcWrappers.add(initSeedWrapper);
        }
        System.out.println("Initial Wrappers Size: " + srcWrappers.size());
        System.gc();
        schedulePureTesting(srcWrappers);
    }

    public void pureRandomTesting(String targetPath) {
        if (targetPath.endsWith(".java")) {
            System.err.println("You should give a folder for Seed Init!");
            System.exit(-1);
        }
        List<String> seedPaths = getFilenamesFromFolder(targetPath, true);
        System.out.println("Pure Random Testing Initial Seed Count: " + seedPaths.size());
        ArrayList<ASTWrapper> srcWrappers = new ArrayList<>();
        for(int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            String[] tokens = seedPath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if(SINGLE_TESTING) {
                System.out.println("Seed Path: " + seedPath);
            }
            ASTWrapper initSeedWrapper = new ASTWrapper(seedPath, seedFolderName);
            srcWrappers.add(initSeedWrapper);
        }
        System.out.println("Initial Wrappers Size: " + srcWrappers.size());
        System.gc();
        schedulePureRandomTesting(srcWrappers);
    }

    public void guidedRandomTesting(String seedFolderPath) {
        if (seedFolderPath.endsWith(".java")) {
            System.err.println("You should give a folder!");
            System.exit(-1);
        }
        locateMutationCode(0, seedFolderPath); // init analysis for seed files
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("Guided Random Testing Initial Seed Count: " + seedPaths.size());
        ArrayList<ASTWrapper> srcWrappers = new ArrayList<>();
        for(int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            String[] tokens = seedPath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            // System.out.println("Seed Path: " + seedPath);
            ASTWrapper astWrapper = new ASTWrapper(seedPath, seedFolderName);
            srcWrappers.add(astWrapper);
        }
        System.out.println("Initial Wrappers Size: " + srcWrappers.size());
        System.gc();
        scheduleGuidedRandomTesting(srcWrappers);
    }

    public void executeMutation(String seedFolderPath) {
        locateMutationCode(0, seedFolderPath);
        List<String> seedPaths = getFilenamesFromFolder(seedFolderPath, true);
        System.out.println("All Initial Seed Count: " + seedPaths.size());
        ArrayList<ASTWrapper> srcWrappers = new ArrayList<>();
        for (int index = 0; index < seedPaths.size(); index++) {
            String seedPath = seedPaths.get(index);
            String[] tokens = seedPath.split(sep);
            String seedFolderName = tokens[tokens.length - 2];
            if (!file2line.containsKey(seedPath)) {
                continue;
            }
            ASTWrapper astWrapper = new ASTWrapper(seedPath, seedFolderName);
            srcWrappers.add(astWrapper);
        }
        System.out.println("Initial Wrappers Size: " + srcWrappers.size());
        System.gc();
        scheduleBFS(srcWrappers);
    }

    // This function only can invoke static analysis tool and cannot include other parts.
    public void locateMutationCode(int iterDepth, String seedFolderPath) {
        String seedFolderName = Path2Last(seedFolderPath);
        System.out.println("Invoke Analyzer for " + seedFolderPath + " and Analysis Output Folder is: " + seedFolderName + ", Depth=" + iterDepth);
        if(PMD_MUTATION) {
            List<PMD_Report> reports = invokePMD(iterDepth, seedFolderPath);
//            all_PMD_Reports.add(reports);
            for (PMD_Report report : reports) {
                if(!file2line.containsKey(report.getFilename())) {
                    file2line.put(report.getFilename(), new HashSet<>());
                    file2bugs.put(report.getFilename(), new HashMap<>());
                }
                for (PMD_Violation violation : report.getViolations()) {
                    file2line.get(report.getFilename()).add(violation.beginLine);
                    HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilename());
                    if(!bug2cnt.containsKey(violation.getBugType())) {
                        bug2cnt.put(violation.getBugType(), new HashSet<>());
                    }
                    bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                }
            }
            if(SINGLE_TESTING) {
                System.out.println("Iteration Level: " + iterDepth + ", File Size: " + file2bugs.keySet().size());
            }
            return;
        }
        if(SPOTBUGS_MUTATION) {
            List<SpotBugs_Report> reports = invokeSpotBugs(seedFolderPath, seedFolderName);
//            all_SpotBugs_Reports.add(reports);
            for(SpotBugs_Report report : reports) {
                if(!file2line.containsKey(report.getFilename())) {
                    file2line.put(report.getFilename(), new HashSet<>());
                    file2bugs.put(report.getFilename(), new HashMap<>());
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
