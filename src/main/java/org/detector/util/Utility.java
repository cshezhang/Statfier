package org.detector.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BreakStatement;
import org.eclipse.jdt.core.dom.DoStatement;
import org.eclipse.jdt.core.dom.EmptyStatement;
import org.eclipse.jdt.core.dom.EnhancedForStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.ForStatement;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.ThrowStatement;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.TypeDeclarationStatement;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.json.JSONObject;
import org.detector.analysis.TypeWrapper;

import org.detector.report.CheckStyle_Report;
import org.detector.report.CheckStyle_Violation;
import org.detector.report.Infer_Report;
import org.detector.report.Infer_Violation;
import org.detector.report.PMD_Report;
import org.detector.report.PMD_Violation;
import org.detector.report.Report;
import org.detector.report.SonarQube_Report;
import org.detector.report.SonarQube_Violation;
import org.detector.report.SpotBugs_Report;
import org.detector.report.SpotBugs_Violation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.security.SecureRandom;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.detector.analysis.TypeWrapper.getClassOfNode;
import static org.detector.util.Invoker.invokeCommandsByZTWithOutput;

/**
 * @Description: Utility Class for SAMutator
 * @Author: Vanguard
 * @Date: 2021-08-11 11:06
 */
public class Utility {

    public static Properties properties;

    static {
        properties = new Properties();
        File file = new File("./config.properties");
        try {
            InputStream in = new FileInputStream(file);
            properties.load(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static final boolean NO_SELECTION = Boolean.parseBoolean(getProperty("NO_SELECTION"));
    public static final boolean RANDOM_SELECTION = Boolean.parseBoolean(getProperty("RANDOM_SELECTION"));
    public static final boolean DIV_SELECTION = Boolean.parseBoolean(getProperty("DIV_SELECTION"));
    public static final int THREAD_COUNT = Integer.parseInt(getProperty("THREAD_COUNT"));
    public static final int SEARCH_DEPTH = Integer.parseInt(getProperty("SEARCH_DEPTH"));
    public final static long MAX_EXECUTION_TIME = 60 * 60 * 1000;
    public static String PROJECT_PATH = getProperty("PROJECT_PATH");
    public static String EVALUATION_PATH = getProperty("EVALUATION_PATH");
    public static String JAVAC_PATH = getProperty("JAVAC_PATH");
    public static int SEED_INDEX = Integer.parseInt(getProperty("SEED_INDEX"));

    public final static boolean DEBUG = Boolean.parseBoolean(getProperty("DEBUG"));
    public final static boolean RANDOM_LOCATION = Boolean.parseBoolean(getProperty("RANDOM_LOCATION"));
    public final static boolean GUIDED_LOCATION = Boolean.parseBoolean(getProperty("GUIDED_LOCATION"));

    // The following Bool variables are used to select static analysis tools.
    public final static boolean PMD_MUTATION = Boolean.parseBoolean(getProperty("PMD_MUTATION"));
    public final static boolean SPOTBUGS_MUTATION = Boolean.parseBoolean(getProperty("SPOTBUGS_MUTATION"));
    public final static boolean INFER_MUTATION = Boolean.parseBoolean(getProperty("INFER_MUTATION"));
    public final static boolean CHECKSTYLE_MUTATION = Boolean.parseBoolean(getProperty("CHECKSTYLE_MUTATION"));
    public final static boolean SONARQUBE_MUTATION = Boolean.parseBoolean(getProperty("SONARQUBE_MUTATION"));
    public final static boolean COMPILE = (SPOTBUGS_MUTATION || INFER_MUTATION) ? true : false;

    public final static String SONARQUBE_PROJECT_KEY = getProperty("SONAR_QUBE_PROJECT_KEY");
    public final static String SONARQUBE_LOGIN = getProperty("SONAR_QUBE_LOGIN");

    public final static String toolPath = getProperty("TOOL_PATH");

    public static String sourceSeedPath = null;
    public static long startTimeStamp = System.currentTimeMillis();

    public static final String reg_sep = "/|\\\\";
    public static final String sep = File.separator;

    public static final Date date = new Date();
    public static final SimpleDateFormat sdf = new SimpleDateFormat("YYYY-MM-dd");
    public static final SecureRandom random = new SecureRandom();
    public static final long RANDOM_SEED1 = 1649250511;
    public static final long RANDOM_SEED2 = 815954400;
    public static final long RANDOM_SEED3 = 1131573600;
    public static final long RANDOM_SEED4 = 1447106400;
    public static final long RANDOM_SEED5 = 1762725600;
    public static int failedT = 0;
    public static int successfulT = 0;
    public static AtomicInteger mutantCounter = new AtomicInteger(0);

    // seeds
    public final static String PMD_SEED_PATH = getProperty("PMD_SEED_PATH");
    public final static String SPOTBUGS_SEED_PATH = getProperty("SPOTBUGS_SEED_PATH");
    public final static String INFER_SEED_PATH = getProperty("INFER_SEED_PATH");
    public final static String CHECKSTYLE_SEED_PATH = getProperty("CHECKSTYLE_SEED_PATH");
    public final static String SONARQUBE_SEED_PATH = getProperty("SONARQUBE_SEED_PATH");

    // mutants and results
    public final static File mutantFolder = new File(EVALUATION_PATH + sep + "mutants");
    public final static File reportFolder = new File(EVALUATION_PATH + sep + "reports");
    public final static File classFolder = new File(EVALUATION_PATH + sep + "classes");
    public final static File resultFolder = new File(EVALUATION_PATH + sep + "results");

    // tools
    public final static String SPOTBUGS_PATH = getProperty("SPOTBUGS_PATH");
    public final static String CHECKSTYLE_PATH = getProperty("CHECKSTYLE_PATH");
    public final static String CheckStyleConfigPath = toolPath + sep + "CheckStyle_Configs";
    public final static String INFER_PATH = getProperty("INFER_PATH");
    public final static String SONAR_SCANNER_PATH = getProperty("SONAR_SCANNER_PATH");
    public static List<String> spotBugsJarList = getFilenamesFromFolder(toolPath + sep + "SpotBugs_Dependency", true);
    public static List<String> inferJarList = getFilenamesFromFolder(toolPath + sep + "Infer_Dependency", true);
    public static List<String> subSeedFolderNameList;
    public static StringBuilder spotBugsJarStr = new StringBuilder(); // This is used to save dependency jar files for SpotBugs
    public static StringBuilder inferJarStr = new StringBuilder();

    public static HashMap<String, List<Integer>> file2row = new HashMap<>(); // String: filename -> List: row numbers of different bugs
    //    public static HashMap<String, List<Integer>> file2col = new HashMap<>(); // filename -> List: buggy column numbers
    public static HashMap<String, Report> file2report = new HashMap<>();
    public static HashMap<String, HashMap<String, List<Integer>>> file2bugs = new HashMap<>(); // filename -> (bug type -> lines)
    public static HashMap<String, Boolean> noReport = new HashMap<>();

    // (rule -> (transSeq -> Mutant_List))
    public static ConcurrentHashMap<String, HashMap<String, List<TriTuple>>> compactIssues = new ConcurrentHashMap<>();
    public static List<String> failedReport = new ArrayList<>();
    public static Set<String> SonarQubeRuleNames;

    public static void initEnv() {
        String sp;
        if (OSUtil.isWindows()) {
            sp = ";";
        } else { // Linux, Mac OS
            sp = ":";
        }
        spotBugsJarStr.append("." + sp);
        for (int i = spotBugsJarList.size() - 1; i >= 1; i--) {
            spotBugsJarStr.append(spotBugsJarList.get(i) + sp);
        }
        spotBugsJarStr.append(spotBugsJarList.get(0));
        inferJarStr.append("." + sp);
        for (int i = inferJarList.size() - 1; i >= 1; i--) {
            inferJarStr.append(inferJarList.get(i) + sp);
        }
        inferJarStr.append(inferJarList.get(0));
        if (SEED_INDEX == 1) {
            random.setSeed(RANDOM_SEED1);
        }
        if (SEED_INDEX == 2) {
            random.setSeed(RANDOM_SEED2);
        }
        if (SEED_INDEX == 3) {
            random.setSeed(RANDOM_SEED3);
        }
        if (SEED_INDEX == 4) {
            random.setSeed(RANDOM_SEED4);
        }
        if (SEED_INDEX == 5) {
            random.setSeed(RANDOM_SEED5);
        }
        if (PMD_MUTATION) {
            sourceSeedPath = PMD_SEED_PATH;
        }
        if (SPOTBUGS_MUTATION) {
            sourceSeedPath = SPOTBUGS_SEED_PATH;
        }
        if (SONARQUBE_MUTATION) {
            sourceSeedPath = SONARQUBE_SEED_PATH;
        }
        if (CHECKSTYLE_MUTATION) {
            sourceSeedPath = CHECKSTYLE_SEED_PATH;
        }
        if (INFER_MUTATION) {
            sourceSeedPath = INFER_SEED_PATH;
        }
        try {
            File file = new File(CHECKSTYLE_PATH);
            if(!file.exists()) {
                System.err.println("CheckStyle is not existed!");
                System.exit(-1);
            }
            file = new File(SPOTBUGS_PATH);
            if(!file.exists()) {
                System.err.println("SpotBugs is not existed");
                System.exit(-1);
            }
            File ud = new File(EVALUATION_PATH);
            if (ud.exists()) {
                FileUtils.deleteDirectory(new File(EVALUATION_PATH));
            }
            ud.mkdir();
            if (!ud.exists()) {
                System.err.println("Fail to create EVALUATION_PATH!\n");
                System.exit(-1);
            }
            if (!reportFolder.mkdir()) {
                System.err.println("Fail to create result folder!\n");
                System.exit(-1);
            }
            if (!mutantFolder.mkdir()) {
                System.err.println("Fail to create mutant folder!\n");
                System.exit(-1);
            }
            if (SPOTBUGS_MUTATION || INFER_MUTATION) {
                if (!classFolder.mkdir()) {
                    System.err.println("Fail to create class folder!\n");
                    System.exit(-1);
                }
            }
            if (!resultFolder.mkdir()) {
                System.err.println("Fail to create result folder!\n");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // subSeedFolder, like security_hardcodedCryptoKey
        subSeedFolderNameList = getDirectFilenamesFromFolder(sourceSeedPath, false);
        // Generate mutant folder from iter1 -> iter8
        if(!PMD_MUTATION) {
            for (String subSeedFolderName : subSeedFolderNameList) {
                File subSeedFolder = new File(reportFolder.getAbsolutePath() + sep + subSeedFolderName);
                subSeedFolder.mkdir();
            }
        }
        for (int i = 1; i <= 8; i++) {
            File iter = new File(mutantFolder.getAbsolutePath() + sep + "iter" + i);
            iter.mkdir();
            for (String subSeedFolderName : subSeedFolderNameList) {
                File subSeedFolder = new File(iter.getAbsolutePath() + sep + subSeedFolderName);
                subSeedFolder.mkdir();
            }
        }
        SonarQubeRuleNames = getSonarQubeRuleNames();
    }

    public static Set<String> getSonarQubeRuleNames() {
        String ruleNamePath = PROJECT_PATH + sep + "tools" + sep + "SonarQube_Rules.txt";
        List<String> lines = readFileByLine(ruleNamePath);
        if(lines.size() > 1) {
            System.err.println("Expected line number is ONE!");
            System.exit(-1);
        }
        String[] ruleNames = lines.get(0).split(",");
        Set<String> ruleNameSet = new HashSet<>();
        for(String ruleName : ruleNames) {
            ruleNameSet.add(ruleName);
        }
        return ruleNameSet;
    }

    public static ExecutorService initThreadPool() {
        ExecutorService threadPool;
        if (Boolean.parseBoolean(getProperty("FIXED_THREAD_POOL"))) {
            threadPool = Executors.newFixedThreadPool(THREAD_COUNT);
        } else {
            if (Boolean.parseBoolean(getProperty("CACHED_THREAD_POOL"))) {
                threadPool = Executors.newCachedThreadPool();
            } else {
                threadPool = Executors.newSingleThreadExecutor();
            }
        }
        return threadPool;
    }

    public static void waitThreadPoolEnding(ExecutorService threadPool) {
        threadPool.shutdown();
        try {
            threadPool.awaitTermination(Long.MAX_VALUE, TimeUnit.MINUTES);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static <T> List<List<T>> listAveragePartition(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;
        int number = source.size() / n;
        int offset = 0;
        for (int i = 0; i < n; i++) {
            List<T> value;
            if (remaider > 0) {
                value = source.subList(i * number + offset, (i + 1) * number + offset + 1);
                remaider--;
                offset++;
            } else {
                value = source.subList(i * number + offset, (i + 1) * number + offset);
            }
            result.add(value);
        }
        return result;
    }

//    public static void checkExecutionTime() {
//        long executionTime = System.currentTimeMillis() - startTimeStamp - compileTime;
//        if (executionTime >= MAX_EXECUTION_TIME) {
//            System.out.println("Execution time is: " + (executionTime / 1000.0) / 60.0 + " mins");
//            System.out.println("Compile time is: " + (compileTime / 1000.0) / 60.0 + " mins");
//            System.out.println("Whole execution time is: " + ((executionTime + compileTime) / 1000.0) / 60.0 + " mins");
//            System.exit(0);
//        }
//    }

    public static int calculatePMDResultFile(final String jsonPath) {
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(jsonPath);
        int count = 0;
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode reportNodes = rootNode.get("files");
            for (int i = 0; i < reportNodes.size(); i++) {
                JsonNode reportNode = reportNodes.get(i);
                JsonNode violationNodes = reportNode.get("violations");
                count += violationNodes.size();
            }
        } catch (JsonProcessingException e) {
            System.err.println("Exceptional Json Path:" + jsonPath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return count;
    }

    public static void readPMDResultFile(final String reportPath) {
        List<PMD_Report> pmd_reports = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(reportPath);
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode reportNodes = rootNode.get("files");
            for (int i = 0; i < reportNodes.size(); i++) {
                JsonNode reportNode = reportNodes.get(i);
                PMD_Report newReport = new PMD_Report(reportNode.get("filename").asText());
                JsonNode violationNodes = reportNode.get("violations");
                for (int j = 0; j < violationNodes.size(); j++) {
                    JsonNode violationNode = violationNodes.get(j);
                    PMD_Violation violation = new PMD_Violation(violationNode);
                    newReport.addViolation(violation);
                }
                pmd_reports.add(newReport);
            }
        } catch (JsonProcessingException e) {
            System.err.println("Exceptional Json Path:" + reportPath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (PMD_Report report : pmd_reports) {
            file2report.put(report.getFilepath(), report);
            if (!file2row.containsKey(report.getFilepath())) {
                file2row.put(report.getFilepath(), new ArrayList<>());
                file2bugs.put(report.getFilepath(), new HashMap<>());
            }
            for (PMD_Violation violation : report.getViolations()) {
                file2row.get(report.getFilepath()).add(violation.beginLine);
                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                if (!bug2cnt.containsKey(violation.getBugType())) {
                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
                }
                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
            }
        }
    }

    public static List<String> readFileByLine(String filepath) {
        List<String> lines = new ArrayList<>();
        try {
            FileInputStream fis = new FileInputStream(filepath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fis));
            String str;
            while ((str = br.readLine()) != null) {
                lines.add(str);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }

    public static boolean writeLinesToFile(String outputPath, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(outputPath));
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(content + "\n");
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static boolean writeLinesToFile(String outputPath, List<String> lines) {
        try {
            FileOutputStream fos = new FileOutputStream(outputPath);
            OutputStreamWriter osw = new OutputStreamWriter(fos, "UTF-8");
            BufferedWriter bw = new BufferedWriter(osw);
            for (String line : lines) {
                bw.write(line + "\n");
            }
            bw.close();
            osw.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    // 这个应该是用CNES得出来的，新版已经用不了这个插件了，换成web api的
    public static void readSonarQubeResultFile(String reportPath) {
        if (DEBUG) {
            System.out.println("SonarQube Detection Result FileName: " + reportPath);
        }
        HashMap<String, SonarQube_Report> name2report = new HashMap<>();
//        final String[] FILE_HEADER = {"severity", "updateDate", "comments",	"line", "author", "rule", "project", "effort", "message",
//                "creationDate", "type",	"tags", "component", "flows", "scope", "textRange",	"debt", "key",	"hash", "status"};
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            System.out.println("Report Path: " + reportFile.getAbsoluteFile());
            System.exit(-1);
        }
        Reader reader;
        CSVParser format;
        List<CSVRecord> records = null;
        try {
            reader = new FileReader(reportPath);
            format = CSVFormat.EXCEL.withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .withDelimiter('\t')
                    .parse(reader);
            records = format.getRecords();
        } catch (FileNotFoundException e) {
            System.out.println("Cannot find SonarQube report file by path!");
            System.exit(-1);
        } catch (IOException e) {
            System.err.println("IO error in parsing SonarQube report!");
            System.exit(-1);
        }
        String lineNumber, bugType, component, flows;
        if (records.isEmpty()) {
            System.err.println("Empty Record Path: " + reportPath);
            System.exit(-1);
        }
        CSVRecord testRecord = records.get(0);
        boolean textRange = false;
        for (String item : testRecord.toList()) {
            if (item.equals("textRange")) {
                textRange = true;
            }
        }
        for (CSVRecord record : records) {
            if (textRange) {
                String tuples = record.get("line");
                int startIndex = tuples.indexOf('=');
                int endIndex = tuples.indexOf(',');
                lineNumber = tuples.substring(startIndex + 1, endIndex);
            } else {
                lineNumber = record.get("line");
            }
            if (lineNumber.trim().equals("-")) {
                continue;
            }
            bugType = record.get("rule");
            component = record.get("component");
            flows = record.get("flows");
            String file;
            if (component.contains(".java")) {
                file = component;
            } else {
                if (flows.contains(".java")) {
                    file = flows;
                } else {
                    continue;
                }
            }
            String filepath;
            if (reportPath.contains("iter0")) {
                filepath = PROJECT_PATH + sep + file.substring(file.indexOf(":") + 1);
            } else {
                filepath = EVALUATION_PATH + sep + file.substring(file.indexOf(":") + 1);
            }
            if (!name2report.containsKey(filepath)) {
                SonarQube_Report report = new SonarQube_Report(filepath);
                name2report.put(filepath, report);
            }
            SonarQube_Report report = name2report.get(filepath);
            if (lineNumber.contains(".0")) {
                lineNumber = lineNumber.substring(0, lineNumber.length() - 2);
            }
//            SonarQube_Violation violation = new SonarQube_Violation(bugType, Integer.parseInt(lineNumber));
//            report.addViolation(violation);
        }

        for (SonarQube_Report report : name2report.values()) {
            file2report.put(report.getFilepath(), report);
            if (!file2row.containsKey(report.getFilepath())) {
                file2row.put(report.getFilepath(), new ArrayList<>());
                file2bugs.put(report.getFilepath(), new HashMap<>());
            }
            for (SonarQube_Violation violation : report.getViolations()) {
                file2row.get(report.getFilepath()).add(violation.getBeginLine());
                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                if (!bug2cnt.containsKey(violation.getBugType())) {
                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
                }
                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
            }
        }
    }

    public static void readCheckStyleResultFile(String reportPath) { // one report -> one file
        HashMap<String, CheckStyle_Report> name2report = new HashMap<>();
        File checkFile = new File(reportPath);
        if (!checkFile.exists()) {
            return;
        }
        List<String> errorInstances = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(reportPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("[ERROR]")) {
                    errorInstances.add(line);
                }
            }
            inputStream.close();
            bufferedReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String filepath;
        for (String errorInstance : errorInstances) {
            int startIndex = errorInstance.indexOf(' ') + 1, endIndex = -1;
            for (int i = startIndex + 1; i < errorInstance.length(); i++) {
                if (errorInstance.charAt(i) == ' ') {
                    endIndex = i;
                    break;
                }
            }
            if (endIndex == -1) {
                return;
            }
            String content = errorInstance.substring(startIndex, endIndex);
            int index1 = content.indexOf(".java") + ".java".length(), index2 = -1;
            if (content.charAt(index1) != ':') {
                return;
            }
            for (int i = index1 + 1; i < content.length(); i++) {
                if (content.charAt(i) == ':') {
                    index2 = i;
                    break;
                }
            }
            filepath = content.substring(0, index1);
            int row = 0, col = -1;
            try {
                row = Integer.parseInt(content.substring(index1 + 1, index2));
//                    if (index2 < content.length() - 1) {
//                        col = Integer.parseInt(content.substring(index2 + 1, content.length() - 1));
//                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
            CheckStyle_Violation violation = new CheckStyle_Violation(filepath);
            violation.setBeginLine(row);
            index1 = errorInstance.lastIndexOf('[');
            String bugType = errorInstance.substring(index1 + 1, errorInstance.length() - 1);
            violation.setBugType(bugType);
            if (name2report.containsKey(filepath)) {
                name2report.get(filepath).addViolation(violation);
            } else {
                CheckStyle_Report newReport = new CheckStyle_Report(filepath);
                newReport.addViolation(violation);
                name2report.put(filepath, newReport);
            }
        }
        for (CheckStyle_Report report : name2report.values()) {
            file2report.put(report.getFilepath(), report);
            if (!file2row.containsKey(report.getFilepath())) {
                file2row.put(report.getFilepath(), new ArrayList<>());
                file2bugs.put(report.getFilepath(), new HashMap<>());
            }
            for (CheckStyle_Violation violation : report.getViolations()) {
                file2row.get(report.getFilepath()).add(violation.getBeginLine());
                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                if (!bug2cnt.containsKey(violation.getBugType())) {
                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
                }
                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
            }
        }
    }


    // seedFolderPath has iter depth information
    public static void readInferResultFile(String seedFilepath, String reportPath) {
        HashMap<String, Infer_Report> name2report = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            failedReport.add(reportPath);
            return;
        }
        try {
            JsonNode rootNode = mapper.readTree(reportFile);
            for (int i = 0; i < rootNode.size(); i++) {
                JsonNode violationNode = rootNode.get(i);
                Infer_Violation infer_violation = new Infer_Violation(violationNode);
                if (name2report.containsKey(seedFilepath)) {
                    name2report.get(seedFilepath).addViolation(infer_violation);
                } else {
                    Infer_Report infer_report = new Infer_Report(seedFilepath);
                    infer_report.addViolation(infer_violation);
                    name2report.put(seedFilepath, infer_report);
                }
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Infer_Report report : name2report.values()) {
            file2report.put(report.getFilepath(), report);
            if (!file2row.containsKey(report.getFilepath())) {
                file2row.put(report.getFilepath(), new ArrayList<>());
                file2bugs.put(report.getFilepath(), new HashMap<>());
            }
            for (Infer_Violation violation : report.getViolations()) {
                file2row.get(report.getFilepath()).add(violation.getBeginLine());
                HashMap<String, List<Integer>> bug2cnt = file2bugs.get(report.getFilepath());
                if (!bug2cnt.containsKey(violation.getBugType())) {
                    bug2cnt.put(violation.getBugType(), new ArrayList<>());
                }
                bug2cnt.get(violation.getBugType()).add(violation.getBeginLine());
            }
        }
    }

    // Variable seedFolderPath contains sub seed folder name
    public static void readSpotBugsResultFile(String seedFolderPath, String reportPath) {
        if (DEBUG) {
            System.out.println("SpotBugs Detection Result FileName: " + reportPath);
        }
        HashMap<String, SpotBugs_Report> filepath2report = new HashMap<>();
        SAXReader saxReader = new SAXReader();
        try {
            Document report = saxReader.read(new File(reportPath));
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

    // Get the last token in the path, not include postfix, e.g., .java
    public static String Path2Last(String path) {
        String[] tokens = path.split(reg_sep);
        String target = tokens[tokens.length - 1];
        if (target.contains(".")) {
            return target.substring(0, target.indexOf('.'));
        } else {
            return target;
        }
    }

    // get Direct file list of target folder path, mainly used to count sub_seed folders
    public static List<String> getDirectFilenamesFromFolder(String path, boolean getAbsolutePath) {
        LinkedList<String> fileList = new LinkedList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null) {
            System.err.println("GetDirectFile Cannot find: " + path);
        }
        for (File file : files) {
            if (file.getName().charAt(0) == '.') {
                continue;
            }
            fileList.add(file.getAbsolutePath());
        }
        if (getAbsolutePath) {
            return fileList;
        } else {
            LinkedList<String> pureNames = new LinkedList<>();
            for (String srcName : fileList) {
                String[] tokens = srcName.split(reg_sep);
                pureNames.add(tokens[tokens.length - 1]);
            }
            return pureNames;
        }
    }

    // The list contains absolute paths.
    public static List<String> getFilenamesFromFolder(String path, boolean getAbsolutePath) {
        LinkedList<String> fileList = new LinkedList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null) {
            System.err.println("GetFileName cannot find: " + path);
            System.exit(-1);
        }
        for (File file : files) {
            if (file.getName().charAt(0) == '.') {
                continue;
            }
            if (file.isDirectory()) {
                fileList.addAll(getFilenamesFromFolder(file.getAbsolutePath(), getAbsolutePath));
            } else {
                fileList.add(file.getAbsolutePath());
            }
        }
        if (getAbsolutePath) {
            return fileList;
        } else {
            LinkedList<String> pureNames = new LinkedList<>();
            for (String srcName : fileList) {
                String[] tokens = srcName.split(reg_sep);
                pureNames.add(tokens[tokens.length - 1]);
            }
            return pureNames;
        }
    }

    public static String getProperty(String name) {
        if (properties.containsKey(name)) {
            return properties.getProperty(name);
        } else {
            System.err.println(name + " is not existed!");
            System.exit(-1);
            return null;
        }
    }

    public static List<TypeWrapper> randomMutantSampling(List<TypeWrapper> wrappers) {
        HashSet<Integer> selectedIndex = new HashSet<>();
        List<TypeWrapper> filteredWrappers = new ArrayList<>();
        int targetSize = (int) (wrappers.size() * 0.1 + 0.5);
        while (selectedIndex.size() < targetSize) {
            for (int i = 0; i < wrappers.size(); i++) {
                if (Math.random() < 0.5) {
                    continue;
                }
                if (selectedIndex.contains(i)) {
                    continue;
                } else {
                    selectedIndex.add(i);
                }
            }
        }
        for (Integer index : selectedIndex) {
            filteredWrappers.add(wrappers.get(index));
        }
        return filteredWrappers;
    }

    public static boolean isInvalidModifier(ASTNode node) {
        TypeDeclaration type = getClassOfNode(node);
        if (type == null) {
            return false;
        }
        List<ASTNode> modifiers = (List<ASTNode>) type.modifiers();
        for (ASTNode m_node : modifiers) {
            if(!(m_node instanceof Modifier)) { // Annotation
                return false;
            }
            Modifier modifier = (Modifier) m_node;
            if (modifier.isAbstract() || modifier.isNative()) {
                return true;
            }
        }
        return false;
    }

    public static boolean isInvalidModifier(TypeDeclaration type) {
        if (type == null) {
            return false;
        }
        List<ASTNode> modifiers = (List<ASTNode>) type.modifiers();
        for (ASTNode node : modifiers) {
            if(node instanceof Modifier) {
                Modifier modifier = (Modifier) node;
                if (modifier.isAbstract() || modifier.isNative()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static final ASTMatcher matcher = new ASTMatcher();
    public static boolean compareNode(ASTNode node1, ASTNode node2) {
        if (node1.toString().equals(node2.toString())) {
            return true;
        }
        if (node1 instanceof MethodDeclaration) {
            return matcher.match((MethodDeclaration) node1, node2);
        }
        if (node1 instanceof IfStatement) {
            return matcher.match((IfStatement) node1, node2);
        }
        if (node1 instanceof SwitchStatement) {
            return matcher.match((SwitchStatement) node1, node2);
        }
        if (node1 instanceof WhileStatement) {
            return matcher.match((WhileStatement) node1, node2);
        }
        if (node1 instanceof DoStatement) {
            return matcher.match((DoStatement) node1, node2);
        }
        if (node1 instanceof ForStatement) {
            return matcher.match((ForStatement) node1, node2);
        }
        if (node1 instanceof TryStatement) {
            return matcher.match((TryStatement) node1, node2);
        }
        if (node1 instanceof EnhancedForStatement) {
            return matcher.match((EnhancedForStatement) node1, node2);
        }
        if (node1 instanceof TypeDeclarationStatement) {
            return matcher.match((TypeDeclarationStatement) node1, node2);
        }
        if (node1 instanceof BreakStatement) {
            return matcher.match((BreakStatement) node1, node2);
        }
        if (node1 instanceof EmptyStatement) {
            return matcher.match((EmptyStatement) node1, node2);
        }
        if (node1 instanceof ThrowStatement) {
            return matcher.match((ThrowStatement) node1, node2);
        }
        if (node1 instanceof SwitchStatement) {
            return matcher.match((SwitchStatement) node1, node2);
        }
        if (node1 instanceof Block) {
            return matcher.match((Block) node1, node2);
        }
        return false;
    }

    public static void waitTaskEnd(String projectKey) {
        boolean start = false;
        while(true) {
            String[] curlCommands = new String[4];
            curlCommands[0] = "curl";
            curlCommands[1] = "-u";
            curlCommands[2] = "admin:123456";
            curlCommands[3] = "http://localhost:9000/api/ce/activity_status?component=" + projectKey;
            String output = invokeCommandsByZTWithOutput(curlCommands);
            JSONObject root = new JSONObject(output);
            // {"pending":0,"failing":0,"inProgress":0}
            int pending = root.getInt("pending");
            int failing = root.getInt("failing");
            int inProgress = root.getInt("inProgress");
            if(pending > 0 || inProgress > 0) {
                start = true;
            }
            if(start && pending == 0 && inProgress == 0  && failing == 0) {
                break;
            }
            if(failing > 0) {
                System.err.println("Failed CE!");
                System.exit(-1);
            }
        }
    }

    public static boolean hasStaticModifier(ASTNode node) {
        List<ASTNode> modifiers = null;
        if(node instanceof FieldDeclaration) {
            modifiers = ((FieldDeclaration) node).modifiers();
        }
        if(node instanceof VariableDeclarationStatement) {
            modifiers = ((VariableDeclarationStatement) node).modifiers();
        }
        if(modifiers == null) {
            return false;
        }
        for(ASTNode modifier : modifiers) {
            if(modifier instanceof Modifier && ((Modifier) modifier).getKeyword().equals(Modifier.ModifierKeyword.STATIC_KEYWORD)) {
                return true;
            }
        }
        return false;
    }

}
