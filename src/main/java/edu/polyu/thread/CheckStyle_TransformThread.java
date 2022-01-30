package edu.polyu.thread;

import edu.polyu.ASTWrapper;
import edu.polyu.Invoker;
import edu.polyu.report.CheckStyle_Report;
import edu.polyu.report.CheckStyle_Violation;

import java.io.File;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static edu.polyu.Invoker.compileJavaSourceFile;
import static edu.polyu.Invoker.invokeCommands;
import static edu.polyu.Util.CHECKSTYLE_PATH;
import static edu.polyu.Util.CheckStyleResultFolder;
import static edu.polyu.Util.GUIDED_RANDOM_TESTING;
import static edu.polyu.Util.MAIN_EXECUTION;
import static edu.polyu.Util.SEARCH_DEPTH;
import static edu.polyu.Util.SpotBugsClassFolder;
import static edu.polyu.Util.SpotBugsResultFolder;
import static edu.polyu.Util.file2bugs;
import static edu.polyu.Util.file2line;
import static edu.polyu.Util.randomMutantSampling;
import static edu.polyu.Util.readCheckStyleResultFile;
import static edu.polyu.Util.sep;

public class CheckStyle_TransformThread implements Runnable {

    private int currentDepth;
    private List<ASTWrapper> initWrappers;

    public CheckStyle_TransformThread(List<ASTWrapper> initWrappers) {
        this.currentDepth = 0;
        this.initWrappers = new ArrayList<>() {
            {
                addAll(initWrappers);
            }
        };
    }

    @Override
    public void run() {
        for(int i = 0; i < initWrappers.size(); i++) {
            ASTWrapper seedWrapper = initWrappers.get(i);
            ArrayDeque<ASTWrapper> wrapperQue = new ArrayDeque<>();
            wrapperQue.add(seedWrapper);
            String configFilePath = Invoker.getConfigXMLFile(seedWrapper.getFolderName());
            // java -jar checkstyle-9.2.1-all.jar -c /sun_checks.xml MyClass.java
            for (int depth = 1; depth <= SEARCH_DEPTH; depth++) {
                ASTWrapper wrapper = wrapperQue.pollFirst();
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
                        List<ASTWrapper> filteredMutants = randomMutantSampling(mutants);
                        wrapperQue.addAll(filteredMutants);
                    }
                } else {
                    wrapperQue.addFirst(wrapper);
                    currentDepth += 1;
                    break;
                }
                List<CheckStyle_Report> reports = new ArrayList<>();
                for (ASTWrapper tmpWrapper : wrapperQue) {
                    String seedFilePath = tmpWrapper.getFilePath();
                    String seedFolderPath = tmpWrapper.getFolderPath();
                    String[] tokens = seedFilePath.split(sep);
                    String seedFileNameWithSuffix = tokens[tokens.length - 1];
                    String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                    // Filename is used to specify class folder name
                    File classFolder = new File(SpotBugsClassFolder.getAbsolutePath() + sep + seedFileName);
                    if (!classFolder.exists()) {
                        classFolder.mkdirs();
                    }
                    compileJavaSourceFile(seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
                    String reportPath = CheckStyleResultFolder.getAbsolutePath() + sep + seedFileName + "_Result.xml";
                    String[] args = {"javac", "-jar", CHECKSTYLE_PATH, "-c", configFilePath, "-o", reportPath, seedFilePath};
                    invokeCommands(args);
                    String report_path = SpotBugsResultFolder.getAbsolutePath() + sep + seedFileName + "_Result.xml";
                    reports.addAll(readCheckStyleResultFile(report_path));
                }
                for (CheckStyle_Report report : reports) {
                    if (!file2line.containsKey(report.getFileName())) {
                        file2line.put(report.getFileName(), new HashSet<>());
                        file2bugs.put(report.getFileName(), new HashMap<>());
                    }
                    for (CheckStyle_Violation violation : report.getViolations()) {
                        file2line.get(report.getFileName()).add(violation.getBeginLine());
                        HashMap<String, HashSet<Integer>> bug2cnt = file2bugs.get(report.getFileName());
                        if (!bug2cnt.containsKey(violation.getBugType())) {
                            bug2cnt.put(violation.getBugType(), new HashSet<>());
                        }
                        bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
                    }
                }
                List<ASTWrapper> validWrappers = new ArrayList<>();
                while (!wrapperQue.isEmpty()) {
                    ASTWrapper head = wrapperQue.pollFirst();
                    if (!head.isBuggy()) {
                        validWrappers.add(head);
                    }
                }
                wrapperQue.addAll(validWrappers);
            }
        }
    }

}
