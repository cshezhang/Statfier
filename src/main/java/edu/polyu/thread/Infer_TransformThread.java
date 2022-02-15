package edu.polyu.thread;

import edu.polyu.analysis.ASTWrapper;
import edu.polyu.report.Infer_Report;
import edu.polyu.report.Infer_Violation;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.util.Invoker.invokeCommands;
import static edu.polyu.util.Util.GUIDED_RANDOM_TESTING;
import static edu.polyu.util.Util.InferClassFolder;
import static edu.polyu.util.Util.InferPath;
import static edu.polyu.util.Util.InferResultFolder;
import static edu.polyu.util.Util.MAIN_EXECUTION;
import static edu.polyu.util.Util.SEARCH_DEPTH;
import static edu.polyu.util.Util.file2bugs;
import static edu.polyu.util.Util.file2line;
import static edu.polyu.util.Util.readInferResultFile;
import static edu.polyu.util.Util.sep;


/**
 * Description: Infer Transformation Thread
 * Author: Vanguard
 * Date: 2022/2/4 1:23 下午
 */
public class Infer_TransformThread implements Runnable {

    private int currentDepth;
    private ArrayDeque<ASTWrapper> initWrappers;

    // initWrappers contains different seedFolderPaths and seedFolderNames, so we can get them from wrappers
    public Infer_TransformThread(List<ASTWrapper> initWrappers) {
        this.currentDepth = 0;
        this.initWrappers = new ArrayDeque<>() {
            {
                addAll(initWrappers);
            }
        };
    }

    @Override
    public void run() {
        // initWrapper: -> iter1 mutants -> transform -> compile -> detect -> iter2 mutants...
        for (ASTWrapper initWrapper : this.initWrappers) {
            ArrayDeque<ASTWrapper> tmpWrappers = new ArrayDeque<>(64);
            tmpWrappers.add(initWrapper);
            for (int iter = 1; iter <= SEARCH_DEPTH; iter++) {
                while (!tmpWrappers.isEmpty()) {
                    ASTWrapper wrapper = tmpWrappers.pollFirst();
                    if (wrapper.depth == currentDepth) {
                        if (!wrapper.isBuggy()) {
                            List<ASTWrapper> mutants;
                            if (GUIDED_RANDOM_TESTING) {
                                mutants = wrapper.guidedRandomTransformation();
                            } else {
                                if (MAIN_EXECUTION) {
                                    mutants = wrapper.mainTransform();
                                } else {
                                    mutants = wrapper.pureRandomTransformation();
                                }
                            }
                            tmpWrappers.addAll(mutants);
                        }
                    } else {
                        tmpWrappers.addFirst(wrapper);
                        currentDepth += 1;
                        break;
                    }
                }
                List<Infer_Report> reports = new ArrayList<>();
                for (ASTWrapper tmpWrapper : tmpWrappers) {
                    String seedFilePath = tmpWrapper.getFilePath();
                    String seedFolderPath = tmpWrapper.getFolderPath();
                    String[] tokens = seedFilePath.split(sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // Filename is used to specify class folder name
                    File classFolder = new File(InferClassFolder.getAbsolutePath() + File.separator + seedFileName);
                    if (!classFolder.exists()) {
                        classFolder.mkdirs();
                    }
                    String java_path = seedFolderPath + File.separator + seedFileNameWithSuffix;
                    String reportPath = InferResultFolder.getAbsolutePath() + File.separator + seedFileName + "_Result.xml";
                    // infer --pmd-xml xml_path run -- javac java_path
                    String[] invokeCmds = {"/bin/bash", "-c", InferPath + " --pmd-xml " + reportPath + " run -- javac " + java_path};
                    invokeCommands(invokeCmds);
                    String report_path = InferResultFolder.getAbsolutePath() + File.separator + seedFileName + "_Result.xml";
                    reports.addAll(readInferResultFile(tmpWrapper.getFolderPath(), report_path));
                }
                for (Infer_Report report : reports) {
                    if (!file2line.containsKey(report.getFilename())) {
                        file2line.put(report.getFilename(), new HashSet<>());
                        file2bugs.put(report.getFilename(), new HashMap<>());
                    }
                    for (Infer_Violation violation : report.getViolations()) {
                        file2line.get(report.getFilename()).add(violation.getBeginLine());
                        HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFilename());
                        if (!bug2cnt.containsKey(violation.getBugType())) {
                            bug2cnt.put(violation.getBugType(), new HashSet<>());
                        }
                        bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                    }
                }
                List<ASTWrapper> validWrappers = new ArrayList<>();
                while (!tmpWrappers.isEmpty()) {
                    ASTWrapper head = tmpWrappers.pollFirst();
                    if (!head.isBuggy()) {
                        validWrappers.add(head);
                    }
                }
                tmpWrappers.addAll(validWrappers);
            }
        }
    }

}
