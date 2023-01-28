package org.detector.analysis;

import org.detector.transform.Transform;
import org.detector.util.Invoker;
import org.detector.util.TriTuple;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.AnnotationTypeDeclaration;
import org.eclipse.jdt.core.dom.Assignment;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.BooleanLiteral;
import org.eclipse.jdt.core.dom.CharacterLiteral;
import org.eclipse.jdt.core.dom.ClassInstanceCreation;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.EnumDeclaration;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.ExpressionStatement;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.IfStatement;
import org.eclipse.jdt.core.dom.ImportDeclaration;
import org.eclipse.jdt.core.dom.Initializer;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.NumberLiteral;
import org.eclipse.jdt.core.dom.PackageDeclaration;
import org.eclipse.jdt.core.dom.PrimitiveType;
import org.eclipse.jdt.core.dom.SimpleName;
import org.eclipse.jdt.core.dom.SingleVariableDeclaration;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.StringLiteral;
import org.eclipse.jdt.core.dom.StructuralPropertyDescriptor;
import org.eclipse.jdt.core.dom.TryStatement;
import org.eclipse.jdt.core.dom.Type;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
import org.eclipse.jdt.core.dom.VariableDeclarationStatement;
import org.eclipse.jdt.core.dom.WhileStatement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import static org.detector.util.Utility.CHECKSTYLE_MUTATION;
import static org.detector.util.Utility.GOOGLE_FORMAT_PATH;
import static org.detector.util.Utility.PMD_MUTATION;
import static org.detector.util.Utility.SONARQUBE_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_MUTATION;
import static org.detector.util.Utility.random;
import static org.detector.util.Utility.failedT;
import static org.detector.util.Utility.file2bugs;
import static org.detector.util.Utility.file2row;
import static org.detector.util.Utility.Path2Last;
import static org.detector.util.Utility.compareNode;
import static org.detector.util.Utility.compactIssues;
import static org.detector.util.Utility.mutantCounter;
import static org.detector.util.Utility.EVALUATION_PATH;
import static org.detector.util.Utility.startTimeStamp;
import static org.detector.util.Utility.successfulT;
import static org.detector.util.Utility.isInvalidModifier;

/**
 * Description: ASTWrapper is 1-to-1 a mutant or seed, it also contains some methods to perform different transformation schedule.
 * Author: RainyD4y
 * Date: 2021-08-10 16:10
 */
public class TypeWrapper {

    public int depth;
    private AST ast;
    private ASTRewrite astRewrite;
    private CompilationUnit cu;

    private String filename;
    private String initSeedPath;
    private int violations;
    public int expectedNumbers;
    private int parViolations;
    private Document document;
    private ASTParser parser;
    private String filePath; // This variable saves the absolute path.
    private String folderPath;
    private String folderName;
    private String parentPath;
    private String mutantFolder;
    private List<ASTNode> nodeIndex;
    private List<String> transSeq;
    private List<ASTNode> transNodes;
    private List<TypeDeclaration> types;
    private List<ASTNode> priorNodes;
    private List<ASTNode> allNodes;
    private HashMap<String, List<ASTNode>> method2statements;
    private HashMap<String, HashSet<String>> method2identifiers;
    private List<ASTNode> candidateNodes;

    public static HashMap<String, String> mutant2seed = new HashMap<>();
    public static HashMap<String, String> mutant2seq = new HashMap<>();

    public static Map compilerOptions = JavaCore.getOptions();
    static {
        compilerOptions.put(JavaCore.COMPILER_COMPLIANCE, JavaCore.VERSION_11);
        compilerOptions.put(JavaCore.COMPILER_CODEGEN_TARGET_PLATFORM, JavaCore.VERSION_11);
        compilerOptions.put(JavaCore.COMPILER_SOURCE, JavaCore.VERSION_11);
    }

    public static List<String> failedParse = new ArrayList<>();
    public TypeWrapper(String filePath, String folderName) {
        this.depth = 0;
        this.expectedNumbers = 0;
        this.filePath = filePath;
        this.initSeedPath = filePath;
        File targetFile = new File(filePath);
        try {
            String content = FileUtils.readFileToString(targetFile, "UTF-8");
            this.document = new Document(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.folderPath = targetFile.getParentFile().getAbsolutePath();
        this.folderName = folderName; // folderName -> subSeedFolderName
        this.filename = targetFile.getName().substring(0, targetFile.getName().length() - 5); // remove .java suffix
        this.parentPath = "initSeed";
        this.mutantFolder = EVALUATION_PATH + File.separator + "mutants" + File.separator + "iter" + (this.depth + 1) + File.separator + folderName;
        this.nodeIndex = new ArrayList<>();
        this.transSeq = new ArrayList<>();
        this.transNodes = new ArrayList<>();
        this.parse2nodes();
    }

    // Other cases need invoke this constructor, filename is defined in mutate function
    public TypeWrapper(String filename, String filepath, String content, TypeWrapper parentWrapper) {
        this.depth = parentWrapper.depth + 1;
        this.expectedNumbers = 0;
        this.filePath = filepath;
        this.initSeedPath = parentWrapper.initSeedPath;
        this.folderName = parentWrapper.folderName; // PMD needs this to specify bug type
        this.filename = filename;
        this.document = new Document(content);
        this.mutantFolder = EVALUATION_PATH + File.separator + "mutants" + File.separator + "iter" + (this.depth + 1) + File.separator + folderName;
        this.parViolations = parentWrapper.violations;
        this.parentPath = parentWrapper.filePath;
        this.nodeIndex = new ArrayList<>();
        this.transSeq = new ArrayList<>();
        this.transNodes = new ArrayList<>();
        File targetFile = new File(filePath);
        this.folderPath = targetFile.getParentFile().getAbsolutePath();
        this.parse2nodes();
    }

    public void updateAST(String source) {
        this.document = new Document(source);
        this.parse2nodes();
    }

    public void rewriteJavaCode() {
        TextEdit edits = this.astRewrite.rewriteAST(this.document, null);
        try {
            edits.apply(this.document);
        } catch (Exception e) {
            System.err.println("Fail to Rewrite Java Document!");
            e.printStackTrace();
        }
        String newCode = this.document.get();
        updateAST(newCode);
    }

    private void parse2nodes() {
        this.parser = ASTParser.newParser(AST.getJLSLatest());
        this.parser.setCompilerOptions(compilerOptions);
        this.parser.setSource(document.get().toCharArray());
        this.cu = (CompilationUnit) parser.createAST(null);
        this.ast = cu.getAST();
        this.astRewrite = ASTRewrite.create(this.ast);
        this.cu.recordModifications();
        this.types = new ArrayList<>();
        for (ASTNode node : (List<ASTNode>) this.cu.types()) {
            if (node instanceof TypeDeclaration) {
                this.types.add((TypeDeclaration) node);
            }
        }
        this.allNodes = new ArrayList<>();
        this.method2statements = new HashMap<>();
        this.method2identifiers = new HashMap<>();
        int initializerCount = 0;
        for (TypeDeclaration type : this.types) {
            this.allNodes.add(type);
            List<ASTNode> components = type.bodyDeclarations();
            for (int i = 0; i < components.size(); i++) {
                ASTNode component = components.get(i);
                this.allNodes.add(component);
                // The following parts are used to get sub nodes by analyze Initializer and MethodDeclaration
                if (component instanceof Initializer) {
                    Block block = ((Initializer) component).getBody();
                    HashSet<String> ids;
                    List<ASTNode> statements;
                    if (block != null || block.statements().size() > 0) {
                        ids = getIdentifiers(block);
                        statements = getAllStatements(block.statements());
                        this.allNodes.addAll(statements);
                    } else {
                        ids = new HashSet<>();
                        statements = new ArrayList<>();
                    }
                    this.method2identifiers.put(type.getName().toString() + ":Initializer" + initializerCount, ids);
                    this.method2statements.put(type.getName().toString() + ":Initializer" + initializerCount++, statements);
                }
                if (component instanceof MethodDeclaration) {
                    HashSet<String> ids;
                    MethodDeclaration method = (MethodDeclaration) component;
                    List<ASTNode> statements;
                    Block block = method.getBody();
                    if (block != null && block.statements().size() > 0) {
                        statements = getAllStatements(block.statements());
                        this.allNodes.addAll(statements);
                        ids = getIdentifiers(((MethodDeclaration) component).getBody());
                    } else {
                        statements = new ArrayList<>();
                        ids = new HashSet<>();
                    }
                    this.method2identifiers.put(type.getName().toString() + ":" + createMethodSignature(method), ids);
                    this.method2statements.put(type.getName().toString() + ":" + createMethodSignature(method), statements);
                }
            }
        }
        List<ASTNode> validNodes = new ArrayList<>();
        if(priorNodes != null && priorNodes.size() > 0) {
            for(ASTNode priorNode : priorNodes) {
                for(ASTNode node : this.allNodes) {
                    if(compareNode(priorNode, node)) {
                        validNodes.add(node);
                    }
                }
            }
        }
        this.priorNodes = new ArrayList<>(validNodes);
        this.candidateNodes = null;
    }

    // This method can be invoked only if the source code file has generated.
    public boolean writeToJavaFile() {
        String code = this.getCode();
        try {
            File file = new File(this.filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(this.filePath);
            fileWriter.write(code);
            fileWriter.close();
            if(SPOTBUGS_MUTATION || CHECKSTYLE_MUTATION) {
                String[] invokeCommands = new String[5];
                invokeCommands[0] = "java";
                invokeCommands[1] = "-jar";
                invokeCommands[2] = GOOGLE_FORMAT_PATH;
                invokeCommands[3] = "--replace";
                invokeCommands[4] = this.filePath;
                boolean isFormatted = Invoker.invokeCommandsByZT(invokeCommands);
                if (!isFormatted) {
                    FileUtils.delete(file);
                    return false;
                }
            }
        } catch (IOException e) {
            System.err.println("Fail to Write to Java File!");
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public void printBasicInfo() {
        PackageDeclaration packageDeclaration = this.cu.getPackage();
        if (packageDeclaration != null) {
            System.out.println("Package Declaration: " + packageDeclaration);
        }
        for (TypeDeclaration clazz : this.types) {
            System.out.println("----------Type(Class) Name: " + clazz.getName() + "----------");
            List<ASTNode> super_nodes = clazz.bodyDeclarations();
            for (ASTNode node : super_nodes) {
                System.out.println(node);
            }
            TypeDeclaration[] subTypes = clazz.getTypes();
            for (TypeDeclaration subType : subTypes) {
                System.out.println(subType);
            }
            List<ASTNode> components = clazz.bodyDeclarations();
            for(ASTNode node : components) {
                System.out.println(node);
            }
            FieldDeclaration[] fields = clazz.getFields();
            for(FieldDeclaration field : fields) {
                System.out.println(field);
            }
            MethodDeclaration[] methods = clazz.getMethods();
            for (MethodDeclaration method : methods) {
                int line1 = this.cu.getLineNumber(method.getStartPosition());
                int line2 = this.cu.getLineNumber(method.getStartPosition() + method.getLength());
                System.out.println(line1);
                System.out.println(line2);
                List<ASTNode> subNodes = getChildrenNodes(method);
                System.out.println(subNodes);
                System.out.println("----------Method Name: " + method.getName() + "----------");
                Block block = method.getBody();
                if (block == null || block.statements().size() == 0) {
                    continue;
                }
                List<Statement> statements = block.statements();
                for (int i = 0; i < statements.size(); i++) {
                    Statement statement = (Statement) block.statements().get(i);
                    List<ASTNode> subSubNodes = getChildrenNodes(statement);
                    List<ASTNode> nodes = getChildrenNodes(statement);
                    for (ASTNode node : nodes) {
                        System.out.println(node + "  " + node.getClass() + "  " + String.format("0x%x", System.identityHashCode(node)));
                    }
                    System.out.println("-----------------");
                    System.out.println(subSubNodes);
                }
            }
        }
    }

    /*
      This function can get all related statemnts of its ASTWrapper by taint analysis.
    */
    public List<ASTNode> getCandidateNodes() {
        List<Integer> validLines = file2row.get(this.filePath);
        List<ASTNode> resNodes = new ArrayList<>();
        if (validLines == null) { // no warning in this file
            return resNodes;
        }
//        validLines.remove(-1);
        if (validLines != null && validLines.size() > 0) {
            for (ASTNode node : this.allNodes) {
                int row = this.cu.getLineNumber(node.getStartPosition());
                if (validLines.contains(row)) {
                    resNodes.add(node);
                }
            }
            this.violations = validLines.size();
        } else {
            this.violations = 0;
        }
        for(ASTNode priorNode : priorNodes) {
            if(priorNode instanceof IfStatement) {
                resNodes.add(((IfStatement) priorNode).getExpression());
            }
            if(priorNode instanceof WhileStatement) {
                resNodes.add(((WhileStatement) priorNode).getExpression());
            }
            if(priorNode instanceof FieldDeclaration) {
                resNodes.add(((VariableDeclarationFragment)((FieldDeclaration) priorNode).fragments().get(0)).getInitializer());
            }
        }
        if (resNodes.isEmpty()) {
            return resNodes;
        }
        List<ASTNode> nodes2add = new ArrayList<>();
        for(ASTNode node : resNodes) {
            Block block = getDirectBlockOfStatement(node);
            if(block != null) {
                ASTNode outNode = block.getParent();
                if(outNode instanceof IfStatement && isLiteral(((IfStatement) outNode).getExpression())) {
                    // Here, we need IfStatement expreesion generated by transform, e.g., if (false).
                    nodes2add.add(((IfStatement) outNode).getExpression());
                }
            }
        }
        resNodes.addAll(nodes2add);
        // Perform intra-procedural def-use analysis to get all candidate statements in this function
        // We may construct a better data flow analysis here
        HashSet<String> sources = new HashSet<>();
        List<ASTNode> methodNodes = new ArrayList<>();
        Expression rightExpression = null;
        for (ASTNode node : resNodes) {
            if(node instanceof FieldDeclaration) {
                String fieldName = ((VariableDeclarationFragment) ((FieldDeclaration) node).fragments().get(0)).getName().getFullyQualifiedName();
                for(Map.Entry<String, List<ASTNode>> entry : this.method2statements.entrySet()) {
                    List<ASTNode> statements = entry.getValue();
                    for(ASTNode statement : statements) {
                        if(statement instanceof VariableDeclarationStatement) {
                            break;
                        }
                        if(statement instanceof ExpressionStatement && ((ExpressionStatement) statement).getExpression() instanceof Assignment) {
                            for (ASTNode subNode : getChildrenNodes(statement)) {
                                if (subNode instanceof SimpleName && ((SimpleName) subNode).getIdentifier().equals(fieldName)) {
                                    methodNodes.add(statement);
                                    break;
                                }
                            }
                        }
                    }
                }
            }
            if (node instanceof VariableDeclarationStatement) {
                rightExpression =
                        ((VariableDeclarationFragment)
                                ((VariableDeclarationStatement) node).fragments().get(0))
                                .getInitializer();
            }
            if (node instanceof ExpressionStatement
                    && ((ExpressionStatement) node).getExpression() instanceof Assignment) {
                rightExpression =
                        ((Assignment) ((ExpressionStatement) node).getExpression()).getRightHandSide();
            }
        }
        if (rightExpression != null) {
            if (rightExpression instanceof SimpleName) {
                sources.add(((SimpleName) rightExpression).getIdentifier());
            }
            if (rightExpression instanceof ClassInstanceCreation) {
                List<Expression> arguments = ((ClassInstanceCreation) rightExpression).arguments();
                // Improvement: We may consider arguments of MethodInvocation's arguments, e.g.,
                // str.get(str1.get());
                for (Expression argument : arguments) {
                    if (argument instanceof MethodInvocation) {
                        Expression argExpr = ((MethodInvocation) argument).getExpression();
                        if (argExpr instanceof SimpleName) {
                            sources.add(((SimpleName) argExpr).getIdentifier());
                        }
                    }
                }
            }
            MethodDeclaration method = getDirectMethodOfNode(resNodes.get(0));
            if (method != null) {
                Block block = method.getBody();
                if (block != null) {
                    List<Statement> subStatements = getSubStatements(block.statements());
                    for (Statement statement : subStatements) {
                        if (statement instanceof ExpressionStatement && ((ExpressionStatement) statement).getExpression() instanceof Assignment) {
                            Assignment assignment = (Assignment) ((ExpressionStatement) statement).getExpression();
                            if (assignment.getLeftHandSide() instanceof SimpleName) {
                                if (sources.contains(((SimpleName) assignment.getLeftHandSide()).getIdentifier())) {
                                    resNodes.add(statement);
                                }
                            }
                        }
                        if (statement instanceof VariableDeclarationStatement) {
                            VariableDeclarationFragment vd = (VariableDeclarationFragment) ((VariableDeclarationStatement) statement).fragments().get(0);
                            String varName = vd.getName().getIdentifier();
                            if (sources.contains(varName)) {
                                resNodes.add(statement);
                            }
                        }
                    }
                }
            }
        }
        for (int i = resNodes.size() - 1; i >= 0; i--) {
            ASTNode resNode = resNodes.get(i);
            if (resNode instanceof TypeDeclaration || resNode instanceof Initializer || resNode instanceof EnumDeclaration
                    || resNode instanceof AnnotationTypeDeclaration) {
                resNodes.remove(i);
            }
        }
        resNodes.addAll(methodNodes);
        return resNodes;
    }

    public static Set<String> existedBugs = new HashSet<>();
    public boolean isBuggy() {
        boolean buggy = false;
        if(this.parentPath == "initSeed") {
            return false;
        }
        if (this.depth != 0 && this.violations != this.parViolations) { // Checking depth is to mutate initial seeds
            // bug type -> line numbers
            Map<String, List<Integer>> mutant_bug2lines = file2bugs.get(this.filePath);
            Map<String, List<Integer>> source_bug2lines = file2bugs.get(this.parentPath);
            // Two if statements below are used to avoid 1 bug in parent and 0 bug in child, vice versa.
            if (mutant_bug2lines == null && source_bug2lines == null) {
                System.err.println("Both reports don't have bugs!");
                System.exit(-1);
            }
            if (mutant_bug2lines == null) {
                if(PMD_MUTATION || SONARQUBE_MUTATION) {
                    mutant_bug2lines = new HashMap<>();
                } else {
                    return false;
                }
            }
            if (source_bug2lines == null) {
                if(PMD_MUTATION || SONARQUBE_MUTATION) {
                    source_bug2lines = new HashMap<>();
                } else {
                    return false;
                }
            }
            List<Map.Entry<String, List<Integer>>> potentialFPs = new ArrayList<>();
            List<Map.Entry<String, List<Integer>>> potentialFNs = new ArrayList<>();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Map.Entry<String, List<Integer>> entry : mutant_bug2lines.entrySet()) {
                if (!source_bug2lines.containsKey(entry.getKey())) { // check bug type
                    potentialFPs.add(entry); // Because mutant has, but source does not have.
                    String sd = sdf.format(new Date(Long.parseLong(String.valueOf(System.currentTimeMillis()))));
                    long execTime = System.currentTimeMillis() - startTimeStamp;
                    long minutes = (execTime / 1000) / 60;
                    long seconds = (execTime / 1000) % 60;
                    if(!existedBugs.contains(entry.getKey())) {
                        existedBugs.add(entry.getKey());
                        System.out.println(existedBugs.size() + " bug(s) is found at " + sd + ", " + String.format("%d min(s) %d sec(s) since execution.", minutes, seconds) );
                        System.out.println("Bug type: " + entry.getKey());
                    }
                } else {
                    List<Integer> source_bugs = source_bug2lines.get(entry.getKey());
                    List<Integer> mutant_bugs = mutant_bug2lines.get(entry.getKey());
                    if (source_bugs.size() == mutant_bugs.size()) {
                        continue;
                    }
                    if (source_bugs.size() > mutant_bugs.size()) {
                        potentialFNs.add(entry);
                        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(System.currentTimeMillis()))));
                        long execTime = System.currentTimeMillis() - startTimeStamp;
                        long minutes = (execTime / 1000) / 60;
                        long seconds = (execTime / 1000) % 60;
                        if(!existedBugs.contains(entry.getKey())) {
                            existedBugs.add(entry.getKey());
                            System.out.println(existedBugs.size() + " bug(s) is found at " + sd + ", " + String.format("%d min(s) %d sec(s) since execution.", minutes, seconds) );
                            System.out.println("Bug type: " + entry.getKey());
                        }
                    } else {
                        if (this.transSeq.get(this.transSeq.size() - 1).equals("AddControlBranch")) {
                            if (source_bugs.size() + this.expectedNumbers < mutant_bugs.size()) {
                                potentialFPs.add(entry);
                                String sd = sdf.format(new Date(Long.parseLong(String.valueOf(System.currentTimeMillis()))));
                                long execTime = System.currentTimeMillis() - startTimeStamp;
                                long minutes = (execTime / 1000) / 60;
                                long seconds = (execTime / 1000) % 60;
                                if(!existedBugs.contains(entry.getKey())) {
                                    existedBugs.add(entry.getKey());
                                    System.out.println(existedBugs.size() + " bug(s) is found at " + sd + ", " + String.format("%d min(s) %d sec(s) since execution.", minutes, seconds) );
                                    System.out.println("Bug type: " + entry.getKey());
                                }
                            }
                        } else {
                            potentialFPs.add(entry);
                            String sd = sdf.format(new Date(Long.parseLong(String.valueOf(System.currentTimeMillis()))));
                            long execTime = System.currentTimeMillis() - startTimeStamp;
                            long minutes = (execTime / 1000) / 60;
                            long seconds = (execTime / 1000) % 60;
                            if(!existedBugs.contains(entry.getKey())) {
                                existedBugs.add(entry.getKey());
                                System.out.println(existedBugs.size() + " bug(s) is found at " + sd + ", " + String.format("%d min(s) %d sec(s) since execution.", minutes, seconds) );
                                System.out.println("Bug type: " + entry.getKey());
                            }
                        }
                    }
                }
            }
            for (Map.Entry<String, List<Integer>> entry : source_bug2lines.entrySet()) {
                if (!mutant_bug2lines.containsKey(entry.getKey())) {  // check bug type
                    potentialFNs.add(entry); // Because parent has, but child does not have.
                    String sd = sdf.format(new Date(Long.parseLong(String.valueOf(System.currentTimeMillis()))));
                    long execTime = System.currentTimeMillis() - startTimeStamp;
                    long minutes = (execTime / 1000) / 60;
                    long seconds = (execTime / 1000) % 60;
                    if(!existedBugs.add(entry.getKey())) {
                        existedBugs.add(entry.getKey());
                        System.out.println(existedBugs.size() + " bug(s) is found at " + sd + ", " + String.format("%d min(s) %d sec(s) since execution.", minutes, seconds) );
                        System.out.println("Bug type: " + entry.getKey());
                    }
                }
            }
            for (int i = 0; i < potentialFPs.size(); i++) {
                buggy = true;
                String bugType = potentialFPs.get(i).getKey();
                if (!compactIssues.containsKey(bugType)) {
                    HashMap<String, List<TriTuple>> seq2paths = new HashMap<>();
                    compactIssues.put(bugType, seq2paths);
                }
                HashMap<String, List<TriTuple>> seq2paths = compactIssues.get(bugType);
                String seqKey = this.transSeq.toString();
                if (!seq2paths.containsKey(seqKey)) {
                    ArrayList<TriTuple> paths = new ArrayList<>();
                    seq2paths.put(seqKey, paths);
                }
                List<TriTuple> paths = seq2paths.get(seqKey);
                paths.add(new TriTuple(this.initSeedPath, this.filePath, "FP"));
            }
            for (int i = 0; i < potentialFNs.size(); i++) {
                buggy = true;
                String bugType = potentialFNs.get(i).getKey();
                if (!compactIssues.containsKey(bugType)) {
                    HashMap<String, List<TriTuple>> seq2paths = new HashMap<>();
                    compactIssues.put(bugType, seq2paths);
                }
                HashMap<String, List<TriTuple>> seq2paths = compactIssues.get(bugType);
                String seqKey = this.transSeq.toString();
                if (!seq2paths.containsKey(seqKey)) {
                    ArrayList<TriTuple> paths = new ArrayList<>();
                    seq2paths.put(seqKey, paths);
                }
                List<TriTuple> paths = seq2paths.get(seqKey);
                paths.add(new TriTuple(this.initSeedPath, this.filePath, "FN"));
            }
        }
        return buggy;
    }

    public static int transformedSeed = 0;
    // 只是random location，没有random transformation
    public List<TypeWrapper> TransformByRandomLocation() {
        List<TypeWrapper> newWrappers = new ArrayList<>();
        transformedSeed++;
        if (this.candidateNodes == null) {
            this.candidateNodes = this.allNodes;
        }
        int cnt;
        if(file2row.containsKey(this.filePath)) {
            cnt = file2row.get(this.filePath).size();
        } else {
            cnt = 1;
        }
        Set<ASTNode> visited = new HashSet<>();
        int randomCount = 0;
        while (true) {
            if(this.candidateNodes.isEmpty()) {
                break;
            }
            ASTNode candidateNode = this.candidateNodes.get(random.nextInt(this.candidateNodes.size()));
            if(!visited.contains(candidateNode)) {
                visited.add(candidateNode);
                this.candidateNodes.remove(candidateNode);
            } else {
                if(visited.size() >= this.candidateNodes.size()) {
                    break;
                } else {
                    continue;
                }
            }
            if (++randomCount > cnt) {
                break;
            }
            TypeDeclaration type = getClassOfNode(candidateNode);
            if(isInvalidModifier(type)) {
                continue;
            }
            for(Transform transform : Transform.getTransforms()) {
                List<ASTNode> targetNodes = transform.check(this, candidateNode);
                for (ASTNode targetNode : targetNodes) {
                    String mutantFilename = "mutant_" + mutantCounter.getAndAdd(1);
                    String mutantPath = mutantFolder + File.separator + mutantFilename + ".java";
                    String content = this.document.get();
                    TypeWrapper newMutant = new TypeWrapper(mutantFilename, mutantPath, content, this);
                    int oldLineNumber1 = this.cu.getLineNumber(targetNode.getStartPosition());
                    int oldColNumber1 = this.cu.getColumnNumber(targetNode.getStartPosition());
                    ASTNode newTargetNode = newMutant.searchNodeByPosition(targetNode, oldLineNumber1, oldColNumber1);
                    if (newTargetNode == null) {
                        continue;
                    }
                    int oldLineNumber2 = this.cu.getLineNumber(candidateNode.getStartPosition());
                    int oldColNumber2 = this.cu.getColumnNumber(candidateNode.getStartPosition());
                    ASTNode newSrcNode = newMutant.searchNodeByPosition(candidateNode, oldLineNumber2, oldColNumber2);
                    if (newSrcNode == null) {
                        continue;
                    }
                    boolean hasMutated = transform.run(newTargetNode, newMutant, getFirstBrotherOfStatement(newSrcNode), newSrcNode);
                    if (hasMutated) {
                        successfulT++;
                        newMutant.nodeIndex.add(targetNode); // Add transformation type, it will be used in mutant selection
                        newMutant.transSeq.add(transform.getIndex());
                        newMutant.transNodes.add(newSrcNode);
                        newWrappers.add(newMutant);
                    } else {
                        failedT++;
                        try {
                            Files.deleteIfExists(Paths.get(mutantPath));
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        return newWrappers;
    }

    public List<TypeWrapper> TransformByGuidedLocation() {
        List<TypeWrapper> newWrappers = new ArrayList<>();
        try {
            if (this.candidateNodes == null) {
                this.candidateNodes = this.getCandidateNodes();
            }
            if(this.candidateNodes.size() == 0) {
                return newWrappers;
            }
            transformedSeed++;
            for (ASTNode candidateNode : candidateNodes) {
                if(isInvalidModifier(candidateNode)) {
                   continue;
                }
                for (Transform transform : Transform.getTransforms()) {
                    List<ASTNode> targetNodes = transform.check(this, candidateNode);
                    for (ASTNode targetNode : targetNodes) {
                        String mutantFilename = "mutant_" + mutantCounter.getAndAdd(1);
                        String mutantPath = mutantFolder + File.separator + mutantFilename + ".java";
                        String content = this.document.get();
                        TypeWrapper newMutant = new TypeWrapper(mutantFilename, mutantPath, content, this);
                        // Node to be transformed
                        int oldLineNumber1 = this.cu.getLineNumber(targetNode.getStartPosition());
                        int oldColNumber1 = this.cu.getColumnNumber(targetNode.getStartPosition());
                        ASTNode newTargetNode = newMutant.searchNodeByPosition(targetNode, oldLineNumber1, oldColNumber1);
                        if (newTargetNode == null) {
                            newMutant.searchNodeByPosition(targetNode, oldLineNumber1, oldColNumber1);
                            System.out.println(newMutant.getFilePath());
                            System.err.println("Old and new ASTWrapper are not matched!");
                            System.exit(-1);
                        }
                        // source node to extract from report
                        int oldRowNumber2 = this.cu.getLineNumber(candidateNode.getStartPosition());
                        int oldColNumber2 = this.cu.getColumnNumber(candidateNode.getStartPosition());
                        ASTNode newSrcNode = newMutant.searchNodeByPosition(candidateNode, oldRowNumber2, oldColNumber2);
                        if (newSrcNode == null) {
                            newMutant.searchNodeByPosition(candidateNode, oldRowNumber2, oldColNumber2);
                            System.out.println(newMutant.getFilePath());
                            System.err.println("Old and new ASTWrapper are not matched!");
                            System.exit(-1);
                        }
                        boolean hasMutated = transform.run(newTargetNode, newMutant, getFirstBrotherOfStatement(newSrcNode), newSrcNode);
                        if (hasMutated) {
                            successfulT++;
                            newMutant.nodeIndex.add(targetNode); // Add transformation type, it will be used in mutant selection
                            newMutant.transSeq.add(transform.getIndex());
                            newMutant.transNodes.add(newSrcNode);
                            newWrappers.add(newMutant);
                        } else {
                            failedT++;
                            Files.deleteIfExists(Paths.get(mutantPath));
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newWrappers;
    }

    public void removePackageDefinition() {
        PackageDeclaration pd = this.cu.getPackage();
        if (pd != null) {
            this.astRewrite.remove(pd, null);
        }
    }

    /*
    The goal of resetClassName is to make mutants can be compiled successfully.
    Hence, the srcName can be confirmed by the filename.
     */
    public void resetClassName() {
        String srcName = Path2Last(this.parentPath);
        TypeDeclaration clazz = null;
        // srcName represents parent class name.
        for (int i = 0; i < types.size(); i++) {
            if (types.get(i).getName().getIdentifier().equals(srcName)) {
                clazz = types.get(i);
            }
        }
        if (clazz == null) {
            System.err.println("Severe Error! No Parent Main Class is found in: " + this.filePath);
            System.out.println("Src Path: " + this.initSeedPath);
            System.exit(-1);
        }
        // This is used to change names of constructor.
        for (int i = 0; i < clazz.getMethods().length; i++) {
            MethodDeclaration method = clazz.getMethods()[i];
            if (method.getName().getIdentifier().equals(srcName)) {
                this.astRewrite.replace(method.getName(), this.ast.newSimpleName(this.filename), null);
            }
        }
        // This is used to change the name of main class.
        this.astRewrite.replace(clazz.getName(), this.ast.newSimpleName(this.filename), null);
        for (TypeDeclaration td : this.types) {
            List<ASTNode> nodes = getChildrenNodes(td);
            for (int i = 0; i < nodes.size(); i++) {
                ASTNode node = nodes.get(i);
                if (node instanceof SimpleName && ((SimpleName) node).getIdentifier().equals(srcName)) {
                    this.astRewrite.replace(node, this.ast.newSimpleName(this.filename), null);
                }
//                if(node instanceof SimpleType) && ((SimpleType) node).getName())
            }
        }
    }

    // This function is invoked by newWrapper
    public ASTNode searchNodeByPosition(ASTNode oldNode, int oldRowNumber, int oldColNumber) {
        if (oldNode == null) {
            System.err.println("AST Node to be searched is NULL!");
            System.exit(-1);
        }
        for (int i = 0; i < this.allNodes.size(); i++) { // 1: Statement-level search
            ASTNode newStatement = this.allNodes.get(i);
            int newLineNumber = this.cu.getLineNumber(newStatement.getStartPosition());
            int newColNumber = this.cu.getColumnNumber(newStatement.getStartPosition());
            if (newLineNumber == oldRowNumber && newColNumber == oldColNumber) {
                if (compareNode(newStatement, oldNode)) {
                    return newStatement;
                }
            }
        }
        for (int i = 0; i < this.allNodes.size(); i++) {  // 2: Fine-grained ASTNode-level search
            ASTNode newStatement = this.allNodes.get(i);
            List<ASTNode> newNodes = getChildrenNodes(newStatement);
            for (ASTNode newNode : newNodes) {
                if (compareNode(newNode, oldNode)) {
                    int newRowNumber = this.cu.getLineNumber(newNode.getStartPosition());
                    int newColNumber = this.cu.getColumnNumber(newNode.getStartPosition());
                    if (newRowNumber == oldRowNumber && newColNumber == oldColNumber) {
                        return newNode;
                    }
                }
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return this.filePath;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public String getFolderName() {
        return this.folderName;
    }

    public String getParentPath() {
        return this.parentPath;
    }

    public int getViolations() {
        return this.violations;
    }

    public String getFileName() {
        return this.filename;
    }

    public String getFilePath() {
        return this.filePath;
    }

    public String getCode() {
        return this.document.get();
    }

    public Document getDocument() {
        return this.document;
    }

    public List<String> getTransSeq() {
        return this.transSeq;
    }

    public List<ASTNode> getTransNodes() {
        return this.transNodes;
    }

    public int getDepth() {
        return depth;
    }

    public AST getAst() {
        return ast;
    }

    public ASTRewrite getAstRewrite() {
        return astRewrite;
    }

    public CompilationUnit getCompilationUnit() {
        return cu;
    }

    public String getInitSeedPath() {
        return this.initSeedPath;
    }

    public HashMap<String, HashSet<String>> getMethod2identifiers() {
        return method2identifiers;
    }

    public List<ASTNode> getPriorNodes() {
        return priorNodes;
    }


    public static List<ASTNode> getChildrenNodes(List<ASTNode> roots) {
        List<ASTNode> nodes = new ArrayList<>();
        for(ASTNode node : roots) {
            nodes.addAll(getChildrenNodes(node));
        }
        return nodes;
    }

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

    public static boolean isLiteral(ASTNode astNode) {
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

    public static List<Statement> getSubStatements(Statement srcStatement) {
        List<Statement> results = new ArrayList<>();
        ArrayDeque<Statement> que = new ArrayDeque<>();
        que.add(srcStatement);
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
        if(sourceStatements == null || sourceStatements.size() == 0) {
            return results;
        }
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

    public static ASTNode getDirectBrotherOfStatement(ASTNode statement) {
        ASTNode parent = statement.getParent();
        while (!(parent instanceof Statement)) {
            parent = parent.getParent();
            if (parent == null || parent.equals(parent.getParent())) {
                System.err.println("Error in Finding Brother Statement!");
                System.exit(-1);
            }
        }
        return parent;
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
        return currentStatement;
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

    public static boolean checkClassProperty(TypeDeclaration clazz) {
        if(clazz == null || clazz.isInterface()) {
            return false;
        }
        if(clazz.getSuperclassType() != null && clazz.getSuperclassType().toString().contains("TestCase")) {
            return false;
        }
        if(clazz.superInterfaceTypes() != null) {
            for(ASTNode node : (List<ASTNode>) clazz.superInterfaceTypes()) {
                if(node.toString().contains("Serializable")) {
                    return false;
                }
            }
        }
        if(clazz.getParent() instanceof CompilationUnit) {
            CompilationUnit cu = (CompilationUnit) clazz.getParent();
            List<ImportDeclaration> imports = cu.imports();
            for(ImportDeclaration im : imports) {
                String name = im.getName().getFullyQualifiedName();
                if(name.contains("org.junit.jupiter") || name.contains("org.junit")) {
                    return false;
                }
            }
        }
        return true;
    }

    public static MethodDeclaration getDirectMethodOfNode(ASTNode node) {
        if(node == null || node instanceof FieldDeclaration) {
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

    public static TypeDeclaration getClassOfNode(ASTNode node) {
        ASTNode parent = node;
        while (parent != null && !(parent instanceof TypeDeclaration)) {
            parent = parent.getParent();
            if (parent == null || parent.equals(parent.getParent())) {
                System.err.println("Error in Finding Type!");
                System.exit(-1);
            }
        }
        return (TypeDeclaration) parent;
    }

    public static Type checkLiteralType(AST ast, Expression literalExpression) {
        if (literalExpression instanceof NumberLiteral) {
            String token = ((NumberLiteral) literalExpression).getToken();
            if (token.contains(".")) {
                return ast.newPrimitiveType(PrimitiveType.DOUBLE);
            } else {
                if(token.contains("L")) {
                    return ast.newPrimitiveType(PrimitiveType.LONG);
                } else {
                    return ast.newPrimitiveType(PrimitiveType.INT);
                }
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
