package org.detector.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.sourceforge.pmd.PMD;
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
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.io.FileUtils;

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
 * @Author: RainyD4y
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

    public final static String SONARQUBE_PROJECT_KEY = getProperty("SONARQUBE_PROJECT_KEY");
    public final static String SONARQUBE_LOGIN = getProperty("SONARQUBE_LOGIN");

    public final static String toolPath = getProperty("TOOL_PATH");

    public static final long startTimeStamp = System.currentTimeMillis();

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
    public static String SEED_PATH = null;

    // mutants and results
    public final static File MUTANT_FOLDER = new File(EVALUATION_PATH + sep + "mutants");
    public final static File REPORT_FOLDER = new File(EVALUATION_PATH + sep + "reports");
    public final static File CLASS_FOLDER = new File(EVALUATION_PATH + sep + "classes");
    public final static File RESULT_FOLDER = new File(EVALUATION_PATH + sep + "results");

    // tools
    public final static String TOOL_PATH = getProperty("TOOL_PATH");
    public final static String GOOGLE_FORMAT_PATH = TOOL_PATH + sep + "GoogleFormatter.jar";
    public final static String SPOTBUGS_PATH = getProperty("SPOTBUGS_PATH");
    public final static String CHECKSTYLE_PATH = getProperty("CHECKSTYLE_PATH");
    public final static String CHECKSTYLE_CONFIG_PATH = toolPath + sep + "CheckStyle_Configs";
    public final static String INFER_PATH = getProperty("INFER_PATH");
    public final static String SONAR_SCANNER_PATH = getProperty("SONAR_SCANNER_PATH");
    public static List<String> spotBugsJarList = getFilenamesFromFolder(toolPath + sep + "SpotBugs_Dependency", true);
    public static List<String> inferJarList = getFilenamesFromFolder(toolPath + sep + "Infer_Dependency", true);
    public static List<String> subSeedFolderNameList;
    public static StringBuilder spotBugsJarStr = new StringBuilder(); // This is used to save dependency jar files for SpotBugs
    public static StringBuilder inferJarStr = new StringBuilder();

    public static HashMap<String, List<Integer>> file2row = new HashMap<>(); // String: filename -> List: row numbers of different bugs
    public static HashMap<String, Report> file2report = new HashMap<>();
    public static HashMap<String, HashMap<String, List<Integer>>> file2bugs = new HashMap<>(); // filename -> (bug type -> lines)

    // (rule -> (transSeq -> Mutant_List))
    public static ConcurrentHashMap<String, HashMap<String, List<TriTuple>>> compactIssues = new ConcurrentHashMap<>();
    public static List<String> failedReportPaths = new ArrayList<>();
    public static List<String> failedExecuteSpotBugs = new ArrayList<>();
    public static List<String> failedCheckStyleExecution = new ArrayList<>();
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
            SEED_PATH = PMD_SEED_PATH;
        }
        if (SPOTBUGS_MUTATION) {
            SEED_PATH = SPOTBUGS_SEED_PATH;
        }
        if (SONARQUBE_MUTATION) {
            SEED_PATH = SONARQUBE_SEED_PATH;
        }
        if (CHECKSTYLE_MUTATION) {
            SEED_PATH = CHECKSTYLE_SEED_PATH;
        }
        if (INFER_MUTATION) {
            SEED_PATH = INFER_SEED_PATH;
        }
        if(SEED_PATH == null) {
            System.err.println("SEED_PATH is not initialized correctly!");
            System.exit(-1);
        }
        List<String> seedPaths = getFilenamesFromFolder(SEED_PATH, true);
        for(String seedPath : seedPaths) {
            if(seedPath.endsWith(".class")) {
                System.out.println("Delete Class in Seed Folder: " + seedPath);
                try {
                    FileUtils.delete(new File(seedPath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
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
            System.out.println("Making Dir: " + ud.getAbsolutePath());
            ud.mkdir();
            if (!ud.exists()) {
                System.err.println("Fail to create EVALUATION_PATH!\n");
                System.exit(-1);
            }
            if (!REPORT_FOLDER.mkdir()) {
                System.err.println("Fail to create result folder!\n");
                System.exit(-1);
            }
            if (!MUTANT_FOLDER.mkdir()) {
                System.err.println("Fail to create mutant folder!\n");
                System.exit(-1);
            }
            if (COMPILE) {
                if (!CLASS_FOLDER.mkdir()) {
                    System.err.println("Fail to create class folder!\n");
                    System.exit(-1);
                }
            }
            if (!RESULT_FOLDER.mkdir()) {
                System.err.println("Fail to create result folder!\n");
                System.exit(-1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // subSeedFolder, like security_hardcodedCryptoKey
        subSeedFolderNameList = getDirectFilenamesFromFolder(SEED_PATH, false);
        // Generate mutant folder from iter1 -> iter8
        for (String subSeedFolderName : subSeedFolderNameList) {
            File subSeedFolder = new File(REPORT_FOLDER.getAbsolutePath() + sep + subSeedFolderName);
            subSeedFolder.mkdir();
            subSeedFolder = new File(CLASS_FOLDER.getAbsolutePath() + sep + subSeedFolderName);
            subSeedFolder.mkdir();
        }
        for (int i = 1; i <= 8; i++) {
            File iter = new File(MUTANT_FOLDER.getAbsolutePath() + sep + "iter" + i);
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

    // Get the last token in the path, not include postfix, e.g., .java
    public static String Path2Last(String path) {
        String[] tokens = path.split(reg_sep);
        String target = tokens[tokens.length - 1];
        if (target.contains(".")) {
            return target.substring(0, target.lastIndexOf('.'));
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
