package edu.polyu.report;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.file2bugs;
import static edu.polyu.util.Utility.file2report;
import static edu.polyu.util.Utility.file2row;

/**
 * @Description:
 * @Author: RainyD4y
 * @Date: 2023-10-30 15:07:30
 */
public class FindSecBugsReport extends Report {

    public FindSecBugsReport(String filePath) {
        super(filePath);
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("FindSecBugs Report: " + this.filePath + "\n");
        for(int i = 0; i < violations.size(); i++) {
            str.append(violations.get(i) + "\n");
        }
        return str.toString();
    }

    // Variable seedFolderPath contains sub seed folder name
    // seedFolderPath is the absolute path
    public static void readFindSecBugsResultFile(String seedFolderPath, String reportPath) {
        if (DEBUG) {
            System.out.println("FindSecBugs Detection Result FileName: " + reportPath);
        }
        File reportFile = new File(reportPath);
        if(!reportFile.exists() || reportFile.length() == 0) {
            return;
        }
        HashMap<String, Report> path2report = new HashMap<>();
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(reportFile);
            Element root = document.getRootElement();
            List<Element> bugInstances = root.elements("BugInstance");
            for (Element bugInstance : bugInstances) {
                List<Element> sourceLines = bugInstance.elements("SourceLine");
                for (Element sourceLine : sourceLines) {
                    Violation violation = new FindSecBugsViolation(sourceLine, bugInstance.attribute("type").getText());
                    String filePath = seedFolderPath  + File.separator + sourceLine.attribute("sourcefile").getText();
                    if (path2report.containsKey(filePath)) {
                        path2report.get(filePath).addViolation(violation);
                    } else {
                        Report report = new FindSecBugsReport(filePath);
                        report.addViolation(violation);
                        path2report.put(filePath, report);
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        for (Report report : path2report.values()) {
            if (!file2row.containsKey(report.getFilePath())) {
                file2row.put(report.getFilePath(), new ArrayList<>());
                file2bugs.put(report.getFilePath(), new HashMap<>());
            }
            for (Violation violation : report.getViolations()) {
                file2row.get(report.getFilePath()).add(violation.getBeginLine());
                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilePath());
                if (!bug2cnt.containsKey(violation.getBugType())) {
                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
                }
                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
            }
        }
    }

    public static void readSingleFindSecBugsResultFile(File seedFile, String reportPath) {
        if (DEBUG) {
            System.out.println("FindSecBugs Detection Result FileName: " + reportPath);
        }
        File reportFile = new File(reportPath);
        if(!reportFile.exists() || reportFile.length() == 0) {
            return;
        }
        String seedPath = seedFile.getAbsolutePath();
        String seedFolderPath = seedFile.getParent();
        if (file2report.containsKey(seedPath)) {
            System.out.println("Repeat process: " + seedPath);
            System.out.println("Report Path: " + reportPath);
            System.exit(-1);
        }
        file2row.put(seedPath, new ArrayList<>());
        file2bugs.put(seedPath, new HashMap<>());
        Report report = new FindSecBugsReport(seedPath);
        file2report.put(seedPath, report);
        SAXReader saxReader = new SAXReader();
        try {
            Document document = saxReader.read(reportFile);
            Element root = document.getRootElement();
            List<Element> bugInstances = root.elements("BugInstance");
            for (Element bugInstance : bugInstances) {
                List<Element> sourceLines = bugInstance.elements("SourceLine");
                for (Element sourceLine : sourceLines) {
                    Violation violation = new FindSecBugsViolation(sourceLine, bugInstance.attribute("type").getText());
                    String filePath = seedFolderPath  + File.separator + sourceLine.attribute("sourcefile").getText();
                    if(!filePath.equals(seedPath)) {
                        System.out.println("Seed Path: " + seedPath);
                        System.out.println("File Path: " + filePath);
                        System.out.println();
                    }
                    report.addViolation(violation);
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        for (Violation violation : report.getViolations()) {
            file2row.get(seedPath).add(violation.getBeginLine());
            HashMap<String, List<Integer>> bug2cnt = file2bugs.get(seedPath);
            if (!bug2cnt.containsKey(violation.getBugType())) {
                bug2cnt.put(violation.getBugType(), new ArrayList<>());
            }
            bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
        }
    }

}

