package edu.polyu.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.jdi.Field;
import edu.polyu.analysis.ASTWrapper;
import edu.polyu.analysis.LoopStatement;

import edu.polyu.report.CheckStyle_Report;
import edu.polyu.report.CheckStyle_Violation;
import edu.polyu.report.Infer_Report;
import edu.polyu.report.Infer_Violation;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;
import edu.polyu.report.Report;
import edu.polyu.report.SonarQube_Report;
import edu.polyu.report.SonarQube_Violation;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.report.SpotBugs_Violation;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTMatcher;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BodyDeclaration;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;

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
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @Description: Utility Class for SAMutator
 * @Author: Vanguard
 * @Date: 2021-08-11 11:06
 */
public class Util {

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

    public static final ASTMatcher matcher = new ASTMatcher();
    public static final boolean NO_SELECTION = Boolean.parseBoolean(getProperty("NO_SELECTION"));
    public static final boolean RANDOM_SELECTION = Boolean.parseBoolean(getProperty("RANDOM_SELECTION"));
    public static final boolean TS_SELECTION = Boolean.parseBoolean(getProperty("TS_SELECTION"));
    public static final int THREAD_COUNT = Integer.parseInt(getProperty("THREAD_COUNT"));
    public static final int SEARCH_DEPTH = Integer.parseInt(getProperty("SEARCH_DEPTH"));
    //    public final static long MAX_EXECUTION_TIME = Long.parseLong(getProperty("EXEC_TIME")) * 60 * 1000;;
    public static String userdir = getProperty("USERDIR");
    public static String JAVAC_PATH = getProperty("JAVAC_PATH");

    public final static boolean SINGLE_TESTING = Boolean.parseBoolean(getProperty("SINGLE_TESTING"));
    public final static boolean RANDOM_LOCATION = Boolean.parseBoolean(getProperty("RANDOM_LOCATION"));
    public final static boolean GUIDED_LOCATION = Boolean.parseBoolean(getProperty("GUIDED_LOCATION"));

    // The following Bool variables are used to select static analysis tools.
    public final static boolean PMD_MUTATION = Boolean.parseBoolean(getProperty("PMD_MUTATION"));
    public final static boolean SPOTBUGS_MUTATION = Boolean.parseBoolean(getProperty("SPOTBUGS_MUTATION"));
    public final static boolean INFER_MUTATION = Boolean.parseBoolean(getProperty("INFER_MUTATION"));
    public final static boolean CHECKSTYLE_MUTATION = Boolean.parseBoolean(getProperty("CHECKSTYLE_MUTATION"));
    public final static boolean SONARQUBE_MUTATION = Boolean.parseBoolean(getProperty("SONARQUBE_MUTATION"));
    public final static boolean COMPILE = (SPOTBUGS_MUTATION || INFER_MUTATION) ? true : false;

    public final static String toolPath = getProperty("TOOL_PATH");

    public static String sourceSeedPath = null;
    public static long startTimeStamp = System.currentTimeMillis();
    public static AtomicInteger mutantCounter = new AtomicInteger(0);

    public static final String sep = "/|\\\\";

    public static final SecureRandom random = new SecureRandom();
    public static final long RANDOM_SEED1 = 1649250511;
    public static final long RANDOM_SEED2 = 815954400;
    public static final long RANDOM_SEED3 = 1131573600;
    public static final long RANDOM_SEED4 = 1447106400;
    public static final long RANDOM_SEED5 = 1762725600;
    public static int newVarCounter = 0;

    // seeds, these variables
    public final static String BASE_SEED_PATH = getProperty("SEED_PATH");
    public final static String AST_TESTING_PATH = "." + File.separator + "src" + File.separator + "test" + File.separator + "java" + File.separator + "ASTTestingCases";
    public final static String SINGLE_TESTING_PATH = BASE_SEED_PATH + File.separator + "SingleTesting";
    //    public final static String PMD_SEED_PATH = BASE_SEED_PATH  + File.separator + "PMD_Ground_Truth";
    public final static String PMD_SEED_PATH = BASE_SEED_PATH + File.separator + "PMD_Seeds";
    public final static String SPOTBUGS_SEED_PATH = BASE_SEED_PATH + File.separator + "SpotBugs_Seeds";
    public final static String SONARQUBE_SEED_PATH = BASE_SEED_PATH + File.separator + "SonarQube_Seeds1";
    public final static String INFER_SEED_PATH = BASE_SEED_PATH + File.separator + "Infer_Seeds";
    public final static String CHECKSTYLE_SEED_PATH = BASE_SEED_PATH + File.separator + "CheckStyle_Seeds";
    public final static String CheckStyleConfigPath = BASE_SEED_PATH + File.separator + "CheckStyle_Configs";

    // mutants
    public final static File mutantFolder = new File(userdir + File.separator + "mutants");
    public final static File resultFolder = new File(userdir + File.separator + "results");

    // results
//    public final static File resultFolder = new File(userdir  + File.separator + "results");
    public final static File PMDResultFolder = new File(userdir + File.separator + "PMD_Results");
    public final static File InferResultFolder = new File(userdir + File.separator + "Infer_Results");
    public final static File InferClassFolder = new File(userdir + File.separator + "Infer_Classes");
    public final static File SpotBugsResultFolder = new File(userdir + File.separator + "SpotBugs_Results");
    public final static File SpotBugsClassFolder = new File(userdir + File.separator + "SpotBugs_Classes");
    public final static File CheckStyleResultFolder = new File(userdir + File.separator + "CheckStyle_results");

    // tools
    public final static String SpotBugsPath = toolPath + File.separator + "SpotBugs" + File.separator + "bin" + File.separator + "spotbugs";
    public final static String InferPath = "/home/vanguard/bin/Infer/bin/infer";
    //    public final static String InferPath = "~" + File.separator + "bin"  + File.separator + "Infer"  + File.separator + "bin"  + File.separator + "infer";
//    public final static String InferPath = "infer";
    public final static String CheckStylePath = toolPath + File.separator + "checkstyle.jar";
    public static List<String> spotBugsJarList = getFilenamesFromFolder(toolPath + File.separator + "SpotBugs_Dependency", true);
    public static List<String> inferJarList = getFilenamesFromFolder(toolPath + File.separator + "Infer_Dependency", true);
    public static List<String> subSeedFolderNameList;
    public static StringBuilder spotBugsJarStr = new StringBuilder(); // This is used to save dependency jar files for SpotBugs
    public static StringBuilder inferJarStr = new StringBuilder();

    public static HashMap<String, HashSet<Integer>> file2row = new HashMap<>(); // filename -> set: buggy line numbers
    public static HashMap<String, HashSet<Integer>> file2col = new HashMap<>(); // filename -> set: buggy line numbers
    public static HashMap<String, Report> file2report = new HashMap<>();
    public static HashMap<String, HashMap<String, HashSet<Integer>>> file2bugs = new HashMap<>(); // filename -> (bug type -> lines)

    // (rule -> (transSeq -> Mutant_List))
    public static ConcurrentHashMap<String, HashMap<String, List<TriTuple>>> compactIssues = new ConcurrentHashMap<>();


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
        random.setSeed(RANDOM_SEED5);
        if (SINGLE_TESTING) {
            sourceSeedPath = SINGLE_TESTING_PATH;
        } else {
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
        }
        try {
            File ud = new File(userdir);
            if (ud.exists()) {
                FileUtils.deleteDirectory(new File(userdir));
            }
            ud.mkdir();
            if (!ud.exists()) {
                System.err.println("Fail to create userdir!\n");
                System.exit(-1);
            }
            if (!resultFolder.mkdir()) {
                System.err.println("Fail to create result folder!\n");
                System.exit(-1);
            }
            if (!mutantFolder.mkdir()) {
                System.err.println("Fail to create mutant folder!\n");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // subSeedFolder, like security_hardcodedCryptoKey
        subSeedFolderNameList = getDirectFilenamesFromFolder(sourceSeedPath, false);
        // Generate mutant folder from iter1 -> iter8
        for (int i = 1; i <= 8; i++) {
            File iter = new File(mutantFolder.getAbsolutePath() + File.separator + "iter" + i);
            iter.mkdir();
            for (String subSeedFolderName : subSeedFolderNameList) {
                File subSeedFolder = new File(iter.getAbsolutePath() + File.separator + subSeedFolderName);
                subSeedFolder.mkdir();
            }
        }
        if (PMD_MUTATION && !PMDResultFolder.exists()) {
            PMDResultFolder.mkdir();
        }

        if (SPOTBUGS_MUTATION) {
            if (!SpotBugsClassFolder.exists()) {
                SpotBugsClassFolder.mkdir();
            }
            if (!SpotBugsResultFolder.exists()) {
                SpotBugsResultFolder.mkdir();
                for (String subSeedFolderName : subSeedFolderNameList) {
                    File reportFolder = new File(SpotBugsResultFolder.getAbsolutePath() + File.separator + subSeedFolderName);
                    if (reportFolder.exists()) {
                        System.err.println("Init Error!");
                        System.exit(-1);
                    }
                    reportFolder.mkdir();
                }
            }
        }
        if (CHECKSTYLE_MUTATION && !CheckStyleResultFolder.exists()) {
            CheckStyleResultFolder.mkdir();
        }
        if (INFER_MUTATION) {
            if (!InferClassFolder.exists()) {
                InferClassFolder.mkdir();
            }
            if (!InferResultFolder.exists()) {
                InferResultFolder.mkdir();
            }
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

    public static String createMethodSignature(MethodDeclaration method) {
        StringBuilder signature = new StringBuilder();
        List<ASTNode> parameters = method.parameters();
        signature.append(method.getName().toString());
        for (ASTNode parameter : parameters) {
            if (parameter instanceof SingleVariableDeclaration) {
                SingleVariableDeclaration svd = (SingleVariableDeclaration) parameter;
                signature.append(":" + svd.getType().toString());
            } else {
                System.err.println("What a Fucked Parameter: " + parameter);
                System.exit(-1);
            }
        }
        return signature.toString();
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

    public static List<ASTNode> getChildrenNodes(ASTNode root) {
        ArrayList<ASTNode> nodes = new ArrayList<>();
        if(root == null) {
            return nodes;
        }
        ArrayDeque<ASTNode> que = new ArrayDeque<>();
        que.add(root);
        while (!que.isEmpty()) {
            ASTNode head = que.pollFirst();
            List<StructuralPropertyDescriptor> children = (List<StructuralPropertyDescriptor>) head.structuralPropertiesForType();
            for (StructuralPropertyDescriptor descriptor : children) {
                Object child = head.getStructuralProperty(descriptor);
                if (child == null) {
                    continue;
                }
                if (child instanceof ASTNode) {
                    nodes.add((ASTNode) child);
                    que.addLast((ASTNode) child);
                }
                if (child instanceof List) {
                    List<ASTNode> newChildren = (List<ASTNode>) child;
                    nodes.addAll(newChildren);
                    for (ASTNode node : newChildren) {
                        que.addLast(node);
                    }
                }
            }
        }
        if (nodes.size() == 0) {
            nodes.add(root);
        }
        return nodes;
    }

    public static boolean checkExpressionLiteral(ASTNode astNode) {
        if (astNode instanceof StringLiteral || astNode instanceof NumberLiteral
                || astNode instanceof BooleanLiteral || astNode instanceof CharacterLiteral
            /*|| expression instanceof NullLiteral || expression instanceof TypeLiteral*/) {
            return true;
        }
        return false;
    }

    public static ArrayList<Statement> getIfSubStatements(IfStatement target) {
        ArrayList<Statement> results = new ArrayList<>();
        Statement thenStatement = target.getThenStatement();
        Statement elseStatement = target.getElseStatement();
        if (thenStatement != null) {
            if (thenStatement instanceof Block) {
                results.addAll(((Block) thenStatement).statements());
            } else {
                results.add(thenStatement);
            }
        }
        if (elseStatement != null) {
            if (elseStatement instanceof Block) {
                results.addAll((List<Statement>) ((Block) elseStatement).statements());
            } else {
                results.add(elseStatement);
            }
        }
        return results;
    }

    /*
        This function can get all sub-statements of specific statement(s).
        Sub-statement is defined as statement without containing block
     */
    public static List<Statement> getSubStatements(List<Statement> sourceStatements) {
        List<Statement> results = new ArrayList<>();
        ArrayDeque<Statement> que = new ArrayDeque<>();
        que.addAll(sourceStatements);
        while (!que.isEmpty()) {
            Statement head = que.pollFirst();
            if (head instanceof IfStatement) {
                que.addAll(getIfSubStatements((IfStatement) head));
                continue;
            }
            if (head instanceof TryStatement) {
                que.addAll(((TryStatement) head).getBody().statements());
                continue;
            }
            if (LoopStatement.isLoopStatement(head)) {
                LoopStatement loopStatement = new LoopStatement(head);
                Statement body = loopStatement.getBody();
                if (body instanceof Block) {
                    que.addAll((List<Statement>) ((Block) body).statements());
                } else {
                    que.add(body);
                }
                continue;
            }
            results.add(head);
        }
        return results;
    }

    public static List<ASTNode> getAllNodes(List<ASTNode> srcNodes) {
        List<ASTNode> resNodes = new ArrayList<>();
        ArrayDeque<ASTNode> que = new ArrayDeque<>();
        que.addAll(srcNodes);
        while (!que.isEmpty()) {
            ASTNode head = que.pollFirst();
            resNodes.add(head);
            if (head instanceof IfStatement) {
                que.addAll(getIfSubStatements((IfStatement) head));
                continue;
            }
            if (head instanceof TryStatement) {
                que.addAll(((TryStatement) head).getBody().statements());
                continue;
            }
            if (LoopStatement.isLoopStatement(head)) {
                LoopStatement loopStatement = new LoopStatement(head);
                Statement body = loopStatement.getBody();
                if (body instanceof Block) {
                    que.addAll((List<Statement>) ((Block) body).statements());
                } else {
                    que.add(body);
                }
                continue;
            }
        }
        return resNodes;
    }

    public static List<Statement> getAllStatements(List<Statement> sourceStatements) {
        List<Statement> results = new ArrayList<>();
        ArrayDeque<Statement> que = new ArrayDeque<>();
        que.addAll(sourceStatements);
        while (!que.isEmpty()) {
            Statement head = que.pollFirst();
            results.add(head);
            if (head instanceof IfStatement) {
                que.addAll(getIfSubStatements((IfStatement) head));
                continue;
            }
            if (head instanceof TryStatement) {
                que.addAll(((TryStatement) head).getBody().statements());
                continue;
            }
            if (LoopStatement.isLoopStatement(head)) {
                LoopStatement loopStatement = new LoopStatement(head);
                Statement body = loopStatement.getBody();
                if (body instanceof Block) {
                    que.addAll((List<Statement>) ((Block) body).statements());
                } else {
                    que.add(body);
                }
                continue;
            }
        }
        return results;
    }

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

    public static List<PMD_Report> readPMDResultFile(final String jsonPath) {
        List<PMD_Report> pmd_reports = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(jsonPath);
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
            System.err.println("Exceptional Json Path:" + jsonPath);
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pmd_reports;
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

    public static boolean writeFileByLine(String outputPath, List<String> lines) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(outputPath));
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

    public static List<SonarQube_Report> readSonarQubeResultFile(String reportPath, String seedFolderPath) {
        if (SINGLE_TESTING) {
            System.out.println("SonarQube Detection Resutl FileName: " + reportPath);
        }
        HashMap<String, SonarQube_Report> name2report = new HashMap<>();
        List<SonarQube_Report> results = new ArrayList<>();
//        final String[] FILE_HEADER = {"severity", "updateDate", "comments",	"line", "author", "rule", "project", "effort", "message",
//                "creationDate", "type",	"tags", "component", "flows", "scope", "textRange",	"debt", "key",	"hash", "status"};
        try {
            Reader reader = new FileReader(reportPath);
            CSVParser format = CSVFormat.EXCEL.withFirstRecordAsHeader()
                    .withIgnoreHeaderCase()
                    .withTrim()
                    .withDelimiter('\t')
                    .parse(reader);
            List<CSVRecord> records = format.getRecords();
//            CSVFormat format = CSVFormat.EXCEL.withHeader(FILE_HEADER).withSkipHeaderRecord(true)
//                    .withIgnoreEmptyLines(true)
//                    .withTrim()
//                    .withDelimiter('\t');
//            Reader in = new FileReader(reportPath);
//            Iterable<CSVRecord> records = format.parse(in);
            String lineNumber, bugType, component, flows;
            for (CSVRecord record : records) {
                lineNumber = record.get("line");
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
                String filepath = seedFolderPath + File.separator + file.substring(file.indexOf(":") + 1);
                if (!name2report.containsKey(filepath)) {
                    SonarQube_Report report = new SonarQube_Report(filepath);
                    name2report.put(filepath, report);
                }
                SonarQube_Report report = name2report.get(filepath);
                if (lineNumber.contains(".0")) {
                    lineNumber = lineNumber.substring(0, lineNumber.length() - 2);
                }
                try {
                    SonarQube_Violation violation = new SonarQube_Violation(bugType, Integer.parseInt(lineNumber));
                    report.addViolation(violation);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        results.addAll(name2report.values());
        return results;
    }

    public static List<CheckStyle_Report> readCheckStyleResultFile(String reportPath) { // one report -> one file
        HashMap<String, CheckStyle_Report> name2report = new HashMap<>();
        List<CheckStyle_Report> results = new ArrayList<>();
        try {
            FileInputStream inputStream = new FileInputStream(reportPath);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            List<String> errorInstances = new ArrayList<>();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                if (line.contains("[ERROR]")) {
                    errorInstances.add(line);
                }
            }
            inputStream.close();
            bufferedReader.close();
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
                    System.err.println("End Index Error!");
                    System.exit(-1);
                }
                String content = errorInstance.substring(startIndex, endIndex);
                int index1 = content.indexOf(".java") + ".java".length(), index2 = -1;
                if (content.charAt(index1) != ':') {
                    System.err.println("Index1 Error!");
                    System.exit(-1);
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
                    if (index2 < content.length() - 1) {
                        col = Integer.parseInt(content.substring(index2 + 1, content.length() - 1));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                CheckStyle_Violation violation = new CheckStyle_Violation(filepath);
                violation.setBeginLine(row);
                violation.setBeginColumn(col);
                index1 = errorInstance.lastIndexOf('[');
                String bugType = errorInstance.substring(index1 + 1, errorInstance.length() - 1);
                violation.setBugType(bugType);
                if (name2report.containsKey(filepath)) {
                    name2report.get(filepath).addViolation(violation);
                } else {
                    CheckStyle_Report newReport = new CheckStyle_Report(filepath);
                    results.add(newReport);
                    newReport.addViolation(violation);
                    name2report.put(filepath, newReport);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }

    public static List<String> failedReport = new ArrayList<>();

    // seedFolderPath has iter depth information
    public static List<Infer_Report> readInferResultFile(String seedFilepath, String reportPath) {
        List<Infer_Report> results = new ArrayList<>();
        HashMap<String, Infer_Report> name2report = new HashMap<>();
        ObjectMapper mapper = new ObjectMapper();
        File reportFile = new File(reportPath);
        if (!reportFile.exists()) {
            failedReport.add(reportPath);
            return results;
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
        results.addAll(name2report.values());
        return results;
    }

    // Variable seedFolderPath contains sub seed folder name
    public static List<SpotBugs_Report> readSpotBugsResultFile(String seedFolderPath, String reportPath) {
        if (SINGLE_TESTING) {
            System.out.println("SpotBugs Detection Resutl FileName: " + reportPath);
        }
        HashMap<String, SpotBugs_Report> name2report = new HashMap<>();
        List<SpotBugs_Report> results = new ArrayList<>();
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
                    if (name2report.containsKey(filepath)) {
                        name2report.get(filepath).addViolation(violation);
                    } else {
                        SpotBugs_Report spotBugs_report = new SpotBugs_Report(filepath);
                        spotBugs_report.addViolation(violation);
                        name2report.put(filepath, spotBugs_report);
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        results.addAll(name2report.values());
        return results;
    }

    // Get the last token in the path, not include postfix, e.g., .java
    public static String Path2Last(String path) {
        String[] tokens = path.split(sep);
        String target = tokens[tokens.length - 1];
        if (target.contains(".")) {
            return target.substring(0, target.indexOf('.'));
        } else {
            return target;
        }
    }

    public static HashSet<String> getIdentifiers(Block block) {
        HashSet<String> identifiers = new HashSet<>();
        for (Statement statement : (List<Statement>) block.statements()) {
            List<ASTNode> subNodes = getChildrenNodes(statement);
            for (ASTNode subNode : subNodes) {
                if (subNode instanceof SimpleName) {
                    identifiers.add(((SimpleName) subNode).getIdentifier());
                }
            }
        }
        return identifiers;
    }

    // get Direct file list of tarege folder path, mainly used to count sub_seed folders
    public static List<String> getDirectFilenamesFromFolder(String path, boolean getAbsolutePath) {
        LinkedList<String> fileList = new LinkedList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        if (files == null) {
            System.err.println("GetDirectFile Cannot find: " + path);
        }
        for (File file : files) {
            fileList.add(file.getAbsolutePath());
        }
        if (getAbsolutePath) {
            return fileList;
        } else {
            LinkedList<String> pureNames = new LinkedList<>();
            for (String srcName : fileList) {
                String[] tokens = srcName.split(sep);
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
                String[] tokens = srcName.split(sep);
                pureNames.add(tokens[tokens.length - 1]);
            }
            return pureNames;
        }
    }

    public static ASTNode getStatementOfNode(ASTNode node) {
        if(node == null) {
            return null;
        }
        ASTNode parNode = node;
        while(parNode != null && !(parNode instanceof Statement || parNode instanceof FieldDeclaration)) {
            parNode = parNode.getParent();
        }
        return parNode;
    }

    public static ASTNode getFirstBrotherOfStatement(ASTNode statement) {
        if (!(statement instanceof Statement)) {
            return null;
        }
        ASTNode parent = statement.getParent();
        ASTNode currentStatement = statement;
        while (!(parent instanceof Block)) {
            parent = parent.getParent();
            currentStatement = currentStatement.getParent();
            if (parent == null || parent.equals(parent.getParent())) {
                System.err.println("Error in Finding Brother Statement!");
                System.exit(-1);
            }
        }
        if (!(currentStatement instanceof Statement)) {
            System.err.println("Error: Current Statement cannot be casted to Statement!");
        }
        return (Statement) currentStatement;
    }

    public static Block getDirectBlockOfStatement(ASTNode statement) {
        if (statement instanceof Statement) {
            ASTNode parent = statement.getParent();
            while (!(parent instanceof Block)) {
                parent = parent.getParent();
                if (parent == null || parent.equals(parent.getParent())) {
                    System.err.println("Error in Finding Direct Block!");
                    System.exit(-1);
                }
            }
            return (Block) parent;
        } else {
            return null;
        }
    }

    public static MethodDeclaration getDirectMethodOfStatement(ASTNode node) {
        if(node == null) {
            return null;
        }
        if (node instanceof MethodDeclaration) {
            return (MethodDeclaration) node;
        }
        ASTNode parent = node.getParent();
        while (!(parent instanceof MethodDeclaration)) {
            parent = parent.getParent();
            if (parent == null || parent.equals(parent.getParent())) {
                return null;
            }
        }
        return (MethodDeclaration) parent;
    }

    public static TypeDeclaration getClassOfStatement(ASTNode statement) {
        ASTNode parent = statement.getParent();
        while (parent != null && !(parent instanceof TypeDeclaration)) {
            parent = parent.getParent();
            if (parent == null || parent.equals(parent.getParent())) {
                System.err.println("Error in Finding Type!");
                System.exit(-1);
            }
        }
        return (TypeDeclaration) parent;
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

    public static List<ASTWrapper> randomMutantSampling(List<ASTWrapper> wrappers) {
        HashSet<Integer> selectedIndex = new HashSet<>();
        List<ASTWrapper> filteredWrappers = new ArrayList<>();
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

    public static Type checkLiteralType(AST ast, Expression literalExpression) {
        if (literalExpression instanceof NumberLiteral) {
            String token = ((NumberLiteral) literalExpression).getToken();
            if (token.contains(".")) {
                return ast.newPrimitiveType(PrimitiveType.DOUBLE);
            } else {
                return ast.newPrimitiveType(PrimitiveType.INT);
            }
        }
        if (literalExpression instanceof StringLiteral) {
            return ast.newSimpleType(ast.newSimpleName("String"));
        }
        if (literalExpression instanceof CharacterLiteral) {
            return ast.newPrimitiveType(PrimitiveType.CHAR);
        }
        if (literalExpression instanceof BooleanLiteral) {
            return ast.newPrimitiveType(PrimitiveType.BOOLEAN);
        }
        return ast.newSimpleType(ast.newSimpleName("Object"));
    }

}
