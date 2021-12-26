package edu.polyu;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import edu.polyu.analysis.LoopStatement;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;
import edu.polyu.report.SpotBugs_Report;
import edu.polyu.report.SpotBugs_Violation;

import org.apache.commons.io.FileUtils;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.SecureRandom;
import java.util.*;
import java.util.concurrent.ExecutorService;

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

    public static int THREAD_COUNT;
    public static final int SEARCH_DEPTH = Integer.parseInt(getProperty("SEARCH_DEPTH"));
    public final static long MAX_EXECUTION_TIME = Long.parseLong(getProperty("EXEC_TIME")) * 60 * 1000;;
    public static String userdir = getProperty("USERDIR");

    public final static boolean AST_TESTING = Boolean.parseBoolean(getProperty("AST_TESTING"));
    public final static boolean SINGLE_TESTING = Boolean.parseBoolean(getProperty("SINGLE_TESTING"));
    public final static boolean PURE_RANDOM_TESTING = Boolean.parseBoolean(getProperty("PURE_RANDOM_TESTING"));
    public final static boolean GUIDED_RANDOM_TESTING = Boolean.parseBoolean(getProperty("GUIDED_RANDOM_TESTING"));
    public final static boolean MAIN_EXECUTION = Boolean.parseBoolean(getProperty("MAIN_EXECUTION"));

    // The following Bool variables are used to select static analysis tools.
    public final static boolean PMD_MUTATION = Boolean.parseBoolean(getProperty("PMD_MUTATION"));
    public final static boolean SPOTBUGS_MUTATION = Boolean.parseBoolean(getProperty("SPOTBUGS_MUTATION"));
    public final static boolean CHECKSTYLE_MUTATION = false;
    public final static boolean ERRORPRONE_MUTATION = false;
    public final static boolean SONARQUBE_MUTATION = false;

    public final static String toolPath = getProperty("TOOL_PATH");

    public static ExecutorService threadPool = null;
    public static String sourceSeedPath = null;
    public static int subSeedIndex;
    public static long startTimeStamp = System.currentTimeMillis();
    public static long compileTime = 0;
    public static int mutantCounter = 0;

    public final static String sep = File.separator;
    public static final SecureRandom random = new SecureRandom();
    public static int newVarCounter = 0;

    // seeds, these variables
    public final static String BASE_SEED_PATH = getProperty("SEED_PATH");
    public final static String AST_TESTING_PATH = "/home/huaien/projects/SAMutator" + sep + "src" + sep + "test" + sep + "java" + sep + "ASTTestingCases";
    public final static String SINGLE_TESTING_PATH = BASE_SEED_PATH + sep + "SingleTesting";
    public final static String PMD_SEED_PATH = BASE_SEED_PATH + sep + "PMD_Seeds";
    public final static String SPOTBUGS_SEED_PATH = BASE_SEED_PATH + sep + "SpotBugs_Seeds";
    public final static String SONARQUBE_SEED_PATH = BASE_SEED_PATH + sep + "SonarQube_Seeds";
    public final static String ERRORPRONE_SEED_PATH = BASE_SEED_PATH + sep + "Errorprone_Seeds";
    public final static String CHECKSTYLE_SEED_PATH = BASE_SEED_PATH + sep + "Checkstyle_Seeds";

    // mutants
    public final static File mutantFolder = new File(userdir + sep + "mutants");

    // results
    public final static File resultFolder = new File(userdir + sep + "results");
    public final static File SpotBugsResultsFolder = new File(userdir + sep + "results" + sep + "SpotBugs_Results");
    public final static File PMDResultsFolder = new File(userdir + sep + "results" + sep + "PMD_Results");
    public final static File SpotBugsClassFolder = new File(userdir + sep + "results" + sep + "SpotBugs_Classes");

    // tools
    public final static String SpotBugsPath = toolPath + sep + "SpotBugs" + sep + "bin" + sep + "spotbugs";
    public static List<String> jarList = getFilenamesFromFolder(toolPath + sep + "libs", true);
    public static List<String> subSeedFolderNameList;
    public static StringBuilder jarStr = new StringBuilder();

    public static HashMap<String, HashSet<Integer>> file2line = new HashMap<>();
    public static HashMap<String, HashMap<String, HashSet<Integer>>> file2bugs = new HashMap<>(); // filename -> (bug type -> lines)

    // file2bugs is used to compare parent's and child's violation reports.
    public static HashMap<String, Integer> file2hash = new HashMap<>();

    // (rule -> (transSeq -> Mutant_List))
    public static HashMap<String, HashMap<String, ArrayList<String>>> compactIssues = new HashMap<>();
    public static List<List<SpotBugs_Report>> all_SpotBugs_Reports = new ArrayList<>();
    public static List<List<PMD_Report>> all_PMD_Reports = new ArrayList<>();
    public static Map compilerOptions = JavaCore.getOptions();

    public static void initEnv() {
        try {
            FileUtils.deleteDirectory(new File(userdir + sep + "mutants"));
            FileUtils.deleteDirectory(new File(userdir + sep + "results"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(!mutantFolder.exists()) {
            mutantFolder.mkdir();
        }
        if(SINGLE_TESTING) {
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
            if (ERRORPRONE_MUTATION) {
                sourceSeedPath = ERRORPRONE_SEED_PATH;
            }
            if (CHECKSTYLE_MUTATION) {
                sourceSeedPath = CHECKSTYLE_SEED_PATH;
            }
        }
        subSeedFolderNameList = getDirectFilenamesFromFolder(sourceSeedPath, false);
        subSeedIndex = subSeedFolderNameList.size();
        THREAD_COUNT = subSeedIndex;
        for(int i = 1; i <= 8; i++) {
            File iter = new File(mutantFolder.getAbsolutePath() + sep + "iter" + i);
            iter.mkdir();
            for(int j = 0; j < subSeedIndex; j++) {
                String subFolderName = subSeedFolderNameList.get(j);
                File subSeedFolder = new File(iter.getAbsolutePath() + sep + subFolderName);
                subSeedFolder.mkdir();
            }
        }
        if(!resultFolder.exists()) {
            resultFolder.mkdir();
        }
        if(!SpotBugsClassFolder.exists() && SPOTBUGS_MUTATION) {
            SpotBugsClassFolder.mkdir();
        }
        if(!SpotBugsResultsFolder.exists() && SPOTBUGS_MUTATION) {
            SpotBugsResultsFolder.mkdir();
        }
        if(!PMDResultsFolder.exists() && PMD_MUTATION) {
            PMDResultsFolder.mkdir();
        }
        for(int i = jarList.size() - 1; i >= 1; i--) {
            jarStr.append(jarList.get(i) + ":");
        }
        jarStr.append(jarList.get(0));
        compilerOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_1_5);
        compilerOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_1_5);
        compilerOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_1_5);
    }

    public static <T> List<List<T>> listAveragePartition(List<T> source, int n) {
        List<List<T>> result = new ArrayList<List<T>>();
        int remaider = source.size() % n;
        int number = source.size() / n;
        int offset = 0;
        for (int i = 0; i < n; i++) {
            List<T> value = null;
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
        for(ASTNode parameter : parameters) {
            if(parameter instanceof SingleVariableDeclaration) {
                SingleVariableDeclaration svd = (SingleVariableDeclaration) parameter;
                signature.append(":" + svd.getType().toString());
            } else {
                System.err.println("What a Fucked Parameter: " + parameter);
                System.exit(-1);
            }
        }
        return signature.toString();
    }

    public static void checkExecutionTime() {
//        long executionTime = System.currentTimeMillis() - startTimeStamp - compileTime;
//        if (executionTime >= MAX_EXECUTION_TIME) {
//            System.out.println("Execution time is: " + (executionTime / 1000.0) / 60.0 + " mins");
//            System.out.println("Compile time is: " + (compileTime / 1000.0) / 60.0 + " mins");
//            System.out.println("Whole execution time is: " + ((executionTime + compileTime) / 1000.0) / 60.0 + " mins");
//            System.exit(0);
//        }
    }

    public static List<ASTNode> getChildrenNodes(ASTNode root) {
        ArrayList<ASTNode> nodes = new ArrayList<>();
        ArrayDeque<ASTNode> que = new ArrayDeque<>();
        que.add(root);
        while(!que.isEmpty()) {
            ASTNode head = que.pollFirst();
            List<StructuralPropertyDescriptor> children = (List<StructuralPropertyDescriptor>) head.structuralPropertiesForType();
            for(StructuralPropertyDescriptor descriptor : children) {
                Object child = head.getStructuralProperty(descriptor);
                if(child == null) {
                    continue;
                }
                if(child instanceof ASTNode) {
                    nodes.add((ASTNode)child);
                    que.addLast((ASTNode)child);
                }
                if(child instanceof List) {
                    List<ASTNode> newChildren = (List<ASTNode>)child;
                    nodes.addAll(newChildren);
                    for(ASTNode node : newChildren) {
                        que.addLast(node);
                    }
                }
            }
        }
        return nodes;
    }

    public static boolean checkExpressionLiteral(ASTNode astNode) {
        if(astNode instanceof StringLiteral || astNode instanceof NumberLiteral
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
        if(thenStatement != null) {
            if(thenStatement instanceof Block) {
                results.addAll(((Block) thenStatement).statements());
            } else {
                results.add(thenStatement);
            }
        }
        if(elseStatement != null) {
            if(elseStatement instanceof Block) {
                results.addAll((List<Statement>) ((Block)elseStatement).statements());
            } else {
                results.add(elseStatement);
            }
        }
        return results;
    }

    public static HashMap<MethodDeclaration, List<Statement>> method2substatements = new HashMap<>(); // This is used to save all sub-statements

    public static List<Statement> getSubStatements(MethodDeclaration method) {
        if(method2substatements.containsKey(method)) {
            return method2substatements.get(method);
        }
        List<Statement> results = new ArrayList<>();
        if(method.getBody() == null) {
            method2substatements.put(method, results);
            return results;
        }
        results = getSubStatements(method, method.getBody().statements());
        return results;
    }

    /*
        This function can get all sub-statements of specific statement(s).
        Sub-statement is defined as statement without containing block
     */
    public static List<Statement> getSubStatements(MethodDeclaration method, List<Statement> sourceStatements) {
        if(method != null && method2substatements.containsKey(method)) {
            return method2substatements.get(method);
        }
        List<Statement> results = new ArrayList<>();
        ArrayDeque<Statement> que = new ArrayDeque<>();
        que.addAll(sourceStatements);
        while(!que.isEmpty()) {
            Statement head = que.pollFirst();
            if(head instanceof IfStatement) {
                que.addAll(getIfSubStatements((IfStatement) head));
                continue;
            }
            if(head instanceof TryStatement) {
                que.addAll(((TryStatement) head).getBody().statements());
                continue;
            }
            if(LoopStatement.isLoopStatement(head)) {
                LoopStatement loopStatement = new LoopStatement(head);
                Statement body = loopStatement.getBody();
                if(body instanceof Block) {
                    que.addAll((List<Statement>)((Block) body).statements());
                } else {
                    que.add(body);
                }
                continue;
            }
            results.add(head);
        }
        if(method != null) {
            method2substatements.put(method, results);
        }
        return results;
    }

    public static HashMap<MethodDeclaration, List<Statement>> method2statements = new HashMap<>(); // This is used to save all statements

    public static List<Statement> getAllStatements(MethodDeclaration method) {
        List<Statement> results = new ArrayList<>();
        if(method.getBody() == null) {
            method2statements.put(method, results);
            return results;
        }
        results = getAllStatements(method, method.getBody().statements());
        return results;
    }

    public static List<Statement> getAllStatements(MethodDeclaration method, List<Statement> sourceStatements) {
        if(method != null && method2statements.containsKey(method)) {
            return method2statements.get(method);
        }
        List<Statement> results = new ArrayList<>();
        ArrayDeque<Statement> que = new ArrayDeque<>();
        que.addAll(sourceStatements);
        while(!que.isEmpty()) {
            Statement head = que.pollFirst();
            results.add(head);
            if(head instanceof IfStatement) {
                que.addAll(getIfSubStatements((IfStatement) head));
                continue;
            }
            if(head instanceof TryStatement) {
                que.addAll(((TryStatement) head).getBody().statements());
                continue;
            }
            if(LoopStatement.isLoopStatement(head)) {
                LoopStatement loopStatement = new LoopStatement(head);
                Statement body = loopStatement.getBody();
                if(body instanceof Block) {
                    que.addAll((List<Statement>)((Block) body).statements());
                } else {
                    que.add(body);
                }
                continue;
            }
        }
        if(method != null) {
            method2statements.put(method, results);
        }
        return results;
    }

    public static List<PMD_Report> readPMDResultFile(final String jsonPath) {
        List<PMD_Report> pmd_reports = new ArrayList<>();
        ObjectMapper mapper = new ObjectMapper();
        File jsonFile = new File(jsonPath);
        try {
            JsonNode rootNode = mapper.readTree(jsonFile);
            JsonNode reportNodes = rootNode.get("files");
            for(int i = 0; i < reportNodes.size(); i++) {
                JsonNode reportNode = reportNodes.get(i);
                PMD_Report newReport = new PMD_Report(reportNode.get("filename").asText());
                JsonNode violationNodes = reportNode.get("violations");
//                System.out.println("-----------: " + newReport.getFilename() + "      " + violationNodes.size());
                for(int j = 0; j < violationNodes.size(); j++) {
                    JsonNode violationNode = violationNodes.get(j);
                    PMD_Violation violation = new PMD_Violation(violationNode);
                    newReport.addViolation(violation);
                }
                pmd_reports.add(newReport);
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return pmd_reports;
    }

    public static List<SpotBugs_Report> readSpotBugsResultFile(String seedPath, String reportPath) {
        if(SINGLE_TESTING) {
            System.out.println("SpotBugs Detection Resutl FileName: " + reportPath);
        }
        HashMap<String, SpotBugs_Report> name2report = new HashMap<>();
        List<SpotBugs_Report> results = new ArrayList<>();
        SAXReader saxReader = new SAXReader();
        try {
            Document report = saxReader.read(new File(reportPath));
            Element root = report.getRootElement();
            List<Element> bugInstances = root.elements("BugInstance");
            int nostart_cnt = 0;
            for(Element bugInstance : bugInstances) {
                List<Element> sourceLines = bugInstance.elements("SourceLine");
                for(Element sourceLine : sourceLines) {
                    SpotBugs_Violation violation = new SpotBugs_Violation(seedPath, sourceLine, bugInstance.attribute("type").getText());
                    String filename = violation.getFilename();
                    if(name2report.containsKey(filename)) {
                        name2report.get(filename).addViolation(violation);
                    } else {
                        SpotBugs_Report spotBugs_report = new SpotBugs_Report(filename);
                        spotBugs_report.addViolation(violation);
                        name2report.put(filename, spotBugs_report);
                    }
                }
            }
        } catch (DocumentException e) {
            e.printStackTrace();
        }
        for(SpotBugs_Report report : name2report.values()) {
            results.add(report);
        }
        return results;
    }

    // Get the last token in the path, not include postfix, e.g., .java
    public static String Path2Last(String path) {
        String[] tokens = path.split(sep);
        String target = tokens[tokens.length - 1];
        if(target.contains(".")) {
            return target.substring(0, target.length() - 5);
        } else {
            return target;
        }
    }

    // get Direct file list of tarege folder path, mainly used to count sub_seed folders
    public static List<String> getDirectFilenamesFromFolder(String path, boolean getAbsolutePath) {
        LinkedList<String> fileList = new LinkedList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for(File file : files) {
            fileList.add(file.getAbsolutePath());
        }
        if(getAbsolutePath) {
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
        if(SINGLE_TESTING) {
            System.out.println("Searching Folder: " + path);
        }
        LinkedList<String> fileList = new LinkedList<>();
        File dir = new File(path);
        File[] files = dir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                fileList.addAll(getFilenamesFromFolder(file.getAbsolutePath(), getAbsolutePath));
            } else {
                fileList.add(file.getAbsolutePath());
            }
        }
        if(getAbsolutePath) {
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

    public static Statement getFirstBrotherOfStatement(Statement statement) {
        if(statement == null) {
            int a = 10;
        }
        ASTNode parent = statement.getParent();
        ASTNode currentStatement = statement;
        if(parent == null) {
            int a = 10;
        }
        while(!(parent instanceof Block)) {
            parent = parent.getParent();
            currentStatement = currentStatement.getParent();
        }
        if(!(currentStatement instanceof Statement)) {
            System.err.println("Error: Current Statement cannot be casted to Statement!");
        }
        return (Statement) currentStatement;
    }

    public static Block getDirectBlockOfStatement(Statement statement) {
        ASTNode parent = statement.getParent();
        while(!(parent instanceof Block)) {
            parent = parent.getParent();
        }
        return (Block) parent;
    }

    public static MethodDeclaration getDirectMethodOfStatement(Statement statement) {
        ASTNode parent = statement.getParent();
        while(!(parent instanceof MethodDeclaration)) {
            parent = parent.getParent();
        }
        return (MethodDeclaration) parent;
    }

    public static TypeDeclaration getTypeOfStatement(Statement statement) {
        ASTNode parent = statement.getParent();
        while(!(parent instanceof TypeDeclaration)) {
            parent = parent.getParent();
        }
        return (TypeDeclaration) parent;
    }

    public static String getProperty(String name) {
        if(properties.containsKey(name)) {
            return properties.getProperty(name);
        } else {
            System.err.println(name + " is not existed!");
            System.exit(-1);
            return null;
        }
    }

}
