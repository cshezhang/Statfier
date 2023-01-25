package org.detector.report;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.file2bugs;
import static org.detector.util.Utility.file2row;

/**
 * @Description: 
 * @Author: RainyD4y
 * @Date: 2021-10-18 11:21:22
 */
public class SpotBugs_Report implements Report {

    private String filepath;
    private List<SpotBugs_Violation> violations;
    
    public SpotBugs_Report(String filepath) {
        this.filepath = filepath;
        this.violations = new ArrayList<>();
    }

    public void addViolation(SpotBugs_Violation newViolation) {
        this.violations.add(newViolation);
    }

    public String getFilepath() {
        return this.filepath;
    }

    public List<SpotBugs_Violation> getViolations() {
        return this.violations;
    }

    @Override
    public String toString() {
        StringBuilder str = new StringBuilder();
        str.append("SpotBugs Report: " + this.filepath + "\n");
        for(int i = 0; i < violations.size(); i++) {
            str.append(violations.get(i) + "\n");
        }
        return str.toString();
    }

    // Variable seedFolderPath contains sub seed folder name
    public static void readSpotBugsResultFile(String seedFolderPath, String reportPath) {
        if (DEBUG) {
            System.out.println("SpotBugs Detection Result FileName: " + reportPath);
        }
        File reportFile = new File(reportPath);
        if(!reportFile.exists() || reportFile.length() == 0) {
            return;
        }
        HashMap<String, SpotBugs_Report> filepath2report = new HashMap<>();
        SAXReader saxReader = new SAXReader();
        try {
            Document report = saxReader.read(reportFile);
            Element root = report.getRootElement();
            List<Element> bugInstances = root.elements("BugInstance");
            for (Element bugInstance : bugInstances) {
                List<Element> sourceLines = bugInstance.elements("SourceLine");
                for (Element sourceLine : sourceLines) {
                    SpotBugs_Violation violation = new SpotBugs_Violation(seedFolderPath, sourceLine, bugInstance.attribute("type").getText());
                    String filepath = violation.getFilepath();
                    if (filepath2report.containsKey(filepath)) {
                        filepath2report.get(filepath).addViolation(violation);
                    } else {
                        SpotBugs_Report spotBugs_report = new SpotBugs_Report(filepath);
                        spotBugs_report.addViolation(violation);
                        filepath2report.put(filepath, spotBugs_report);
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        for (SpotBugs_Report report : filepath2report.values()) {
            if (!file2row.containsKey(report.getFilepath())) {
                file2row.put(report.getFilepath(), new ArrayList<>());
                file2bugs.put(report.getFilepath(), new HashMap<>());
            }
            for (SpotBugs_Violation violation : report.getViolations()) {
                file2row.get(report.getFilepath()).add(violation.getBeginLine());
                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                if (!bug2cnt.containsKey(violation.getBugType())) {
                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
                }
                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
            }
        }
    }

}

