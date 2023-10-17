package org.detector.casecheck;

import net.sourceforge.pmd.PMD;
import net.sourceforge.pmd.PMDConfiguration;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.detector.util.Utility.PMD_SEED_PATH;
import static org.detector.util.Utility.calculatePMDResultFile;
import static org.detector.util.Utility.EVALUATION_PATH;
import static org.detector.util.Utility.getFilePathsFromFolder;

public class PMDCaseChecker {

    private static final String pmdCSVPath = "."  + File.separator + "scripts"  + File.separator + "PMDSeedData.csv";

    static class DiffRecord {

        String filename;
        String category;
        String bugType;
        String description;
        int detect;
        int expect;
        String xml_path;

        public DiffRecord(String filename, String category, String bugType, String description, int detect, int expect, String xml_path) {
            this.filename = filename;
            this.category = category;
            this.bugType = bugType;
            this.description = description;
            this.detect = detect;
            this.expect = expect;
            this.xml_path = xml_path;
        }

        @Override
        public String toString() {
            return this.filename + " " + this.category + " " + this.filename + " " + this.detect + " " + this.expect;
        }
    }

    public static void readCSVFile() {
        try {
            ArrayList<DiffRecord> diffRecords = new ArrayList<>();
            String[] HEADERS = {"filename", "cate", "rules", "description", "problem_cnt", "FP", "XML_Path"};
            Reader in = new FileReader(pmdCSVPath);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT
                    .withHeader(HEADERS)
                    .withFirstRecordAsHeader()
                    .parse(in);
            for(CSVRecord record : records) {
                String filename = record.get("filename");
                String category = record.get("cate");
                String bugType = record.get("rules");
                String description = record.get("description");
                int expect = Integer.parseInt(record.get("problem_cnt"));
                boolean isFP = Boolean.parseBoolean(record.get("FP"));
                String xml_path = record.get("XML_Path");
                int detectedBugs = Integer.parseInt(invokePMD(PMD_SEED_PATH  + File.separator + category + "_" + bugType, category + "_" + bugType, filename));
                if(isFP && detectedBugs > 0) {
                    continue;
                }
                if (detectedBugs > expect && expect != 0) {
                    DiffRecord newRecord = new DiffRecord(PMD_SEED_PATH  + File.separator + category + "_" + bugType  + File.separator + filename, category, bugType, description, detectedBugs, expect, (String)xml_path);
                    diffRecords.add(newRecord);
                }
            }
            final String[] FILE_HEADER = {"Filepath", "Category", "Rule", "Description", "Detect", "Expect", "XML_PATH"};
            CSVFormat format = CSVFormat.DEFAULT.withHeader(FILE_HEADER);
            String resultPath = EVALUATION_PATH  + File.separator + "CaseCheck_allRules.log";
            System.out.println("Result Path: " + resultPath);
            // Write to CSV file
            CSVPrinter printer = null;
            try {
                Writer out = new FileWriter(resultPath);
                printer = new CSVPrinter(out, format);
                for (DiffRecord record : diffRecords) {
                    List<String> tmp = new ArrayList<>();
                    tmp.add(record.filename);
                    tmp.add(record.category);
                    tmp.add(record.bugType);
                    tmp.add("\"" + record.description + "\"");
                    tmp.add(String.format("%d", record.detect));
                    tmp.add(String.format("%d", record.expect));
                    tmp.add(record.xml_path);
                    printer.printRecord(tmp);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                printer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String invokePMD(String seedFolderPath, String seedFolderName, String fileNameWithSuffix) {
        String[] tokens = seedFolderName.split("_");
        String ruleCategory = tokens[0];
        String ruleType = tokens[1];
        String resultFileName = fileNameWithSuffix.substring(0, fileNameWithSuffix.length() - 5) + "_Result.json";
        String resultFilePath =  EVALUATION_PATH  + File.separator + "PMD_Case_Check"  + File.separator + resultFileName;
        PMDConfiguration pmdConfig = new PMDConfiguration();
        pmdConfig.setInputPathList(getFilePathsFromFolder(seedFolderPath  + File.separator + fileNameWithSuffix));
        pmdConfig.setRuleSets(new ArrayList<>() {
            {
                add("category/java/" + ruleCategory + ".xml/" + ruleType);
            }
        });
        pmdConfig.setReportFormat("json");
        pmdConfig.setReportFile(Paths.get(resultFilePath));
        pmdConfig.setIgnoreIncrementalAnalysis(true);
        PMD.runPmd(pmdConfig);
        int bugCounter = calculatePMDResultFile(resultFilePath);
        return String.format("%d", bugCounter);
    }

    public static void main(String[] args) {
        readCSVFile();
    }

}
