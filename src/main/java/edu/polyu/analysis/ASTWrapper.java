package edu.polyu.analysis;

import edu.polyu.transform.Transform;
import edu.polyu.util.TriTuple;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.polyu.util.Util.*;
import static edu.polyu.util.EditDistance.calculateStringSimilarity;

/**
 * Description: Mutator Scheduler
 * Author: Vanguard
 * Date: 2021-08-10 16:10
 */
public class ASTWrapper {

    public int depth;
    public AST ast;
    public ASTRewrite astRewrite;

    public String filename;
    private String initSeed;
    private int violations;
    private int parViolations;
    private Document document;
    private ASTParser parser;
    private CompilationUnit cu;
    private String filePath; // This variable saves the absolute path.
    private String folderPath;
    private String folderName; // For PMD and CheckStyle, folderName equals to rule name
    private String parentPath;
    private String mutantFolder;
    private List<String> transSeq;
    private List<TypeDeclaration> types;
//    private List<FieldDeclaration> allFieldDeclarations;
//    private List<Statement> allNodes;
    private List<ASTNode> allNodes;
    private HashMap<String, List<ASTNode>> method2statements;
//    private List<Statement> candidateStatements;
    private List<ASTNode> candidateNodes;
//    private List<FieldDeclaration> candidateFieldDeclarations;
    private List<String> mutantContents;

    public ASTWrapper(String filePath, String folderName) {
        this.depth = 0;
        this.filePath = filePath;
        this.initSeed = filePath;
        File targetFile = new File(filePath);
        try {
            this.document = new Document(FileUtils.readFileToString(targetFile, "UTF-8"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.folderPath = targetFile.getParentFile().getAbsolutePath();
        this.folderName = folderName;
        this.filename = targetFile.getName().substring(0, targetFile.getName().length() - 5); // remove .java suffix
        this.parentPath = null;
        this.parViolations = 0;
        if (PMD_MUTATION) {
            this.mutantFolder = userdir + File.separator + "mutants" + File.separator + "iter" + (this.depth + 1) + File.separator + folderName;
        } else {
            this.mutantFolder = userdir + File.separator + "mutants" + File.separator + "iter" + (this.depth + 1);
        }
        this.transSeq = new ArrayList<>();
        this.parser = ASTParser.newParser(AST.JLS3);
        this.parser.setCompilerOptions(compilerOptions);
        this.parser.setSource(document.get().toCharArray());
        this.cu = (CompilationUnit) parser.createAST(null);
        this.ast = cu.getAST();
        this.astRewrite = ASTRewrite.create(ast);
        this.cu.recordModifications();
        this.types = new ArrayList<>();
        for (ASTNode node : (List<ASTNode>) this.cu.types()) { // Attention: AbstractTypeDeclaration
            if (node instanceof TypeDeclaration) {
                this.types.add((TypeDeclaration) node);
            }
        }
//        this.allStatements = new ArrayList<>();
        this.allNodes = new ArrayList<>();
        this.method2statements = new HashMap<>();
        for (TypeDeclaration clazz : this.types) {
            List<ASTNode> components = clazz.bodyDeclarations();
            for (int i = 0; i < components.size(); i++) {
                ASTNode component = components.get(i);
                if (component instanceof FieldDeclaration) {
                    allNodes.add(component);
                }
                if (component instanceof Initializer) {
                    Block block = ((Initializer) component).getBody();
                    if (block != null) {
                        allNodes.addAll(getAllStatements((block.statements())));
                    }
                }
                if (component instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) component;
                    List<ASTNode> statements;
                    Block block = method.getBody();
                    if (block == null) {
                        statements = new ArrayList<>();
                    } else {
                        statements = getAllStatements(block.statements());
                        this.allNodes.addAll(statements);
//                        this.allStatements.addAll(statements);
                    }
                    this.method2statements.put(clazz.getName().toString() + ":" + createMethodSignature(method), statements);
                }
            }
        }
        this.candidateNodes = null;
//        this.candidateFieldDeclarations = new ArrayList<>();
        this.mutantContents = new ArrayList<>();
    }

    // Other cases need invoke this constructor, filename is defined in mutate function
    public ASTWrapper(String filename, String filePath, String content, ASTWrapper parentWrapper) {
        this.depth = parentWrapper.depth + 1;
        this.filePath = filePath;
        this.initSeed = parentWrapper.initSeed;
        this.folderName = parentWrapper.folderName; // PMD needs this to specify bug type
        this.filename = filename;
        if (PMD_MUTATION) {
            this.mutantFolder = userdir + File.separator + "mutants" + File.separator + "iter" + (this.depth + 1) + File.separator + folderName;
        } else {
            this.mutantFolder = userdir + File.separator + "mutants" + File.separator + "iter" + (this.depth + 1);
        }
        this.parViolations = parentWrapper.violations;
        this.parentPath = parentWrapper.filePath;
        this.transSeq = new ArrayList<>();
        this.document = new Document(content);
        File targetFile = new File(filePath);
        this.folderPath = targetFile.getParentFile().getAbsolutePath();
        this.parser = ASTParser.newParser(AST.JLS3);
        this.parser.setCompilerOptions(compilerOptions);
        this.parser.setSource(document.get().toCharArray());
        this.cu = (CompilationUnit) parser.createAST(null);
        this.ast = this.cu.getAST();
        this.astRewrite = ASTRewrite.create(ast);
        this.cu.recordModifications();
        this.types = this.cu.types();
//        this.allFieldDeclarations = new ArrayList<>();
//        this.allStatements = new ArrayList<>();
        this.allNodes = new ArrayList<>();
        this.method2statements = new HashMap<>();
        for (TypeDeclaration clazz : this.types) {
            List<ASTNode> components = clazz.bodyDeclarations();
            for (int i = 0; i < components.size(); i++) {
                ASTNode component = components.get(i);
                allNodes.add(component);
//                if (component instanceof FieldDeclaration) {
//                    allNodes.add(component);
//                }
                if (component instanceof Initializer) {
                    Block block = ((Initializer) component).getBody();
                    if (block != null) {
                        allNodes.addAll(getAllStatements((block.statements())));
                    }
                }
                if (component instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) component;
                    List<ASTNode> nodes;
                    Block block = method.getBody();
                    if (block == null) {
                        nodes = new ArrayList<>();
                    } else {
                        nodes = getAllStatements(block.statements());
//                        this.allStatements.addAll(statements);
                        this.allNodes.addAll(nodes);
                    }
                    this.method2statements.put(clazz.getName().toString() + ":" + createMethodSignature(method), nodes);
                }
            }
        }
        this.candidateNodes = null;
//        this.candidateFieldDeclarations = new ArrayList<>();
        this.mutantContents = new ArrayList<>();
    }

    public void updateAST(String source) {
        this.document = new Document(source);
        this.parser = ASTParser.newParser(AST.JLS3);
        this.parser.setCompilerOptions(compilerOptions);
        this.parser.setSource(document.get().toCharArray());
        this.cu = (CompilationUnit) parser.createAST(null);
        this.ast = cu.getAST();
        this.astRewrite = ASTRewrite.create(this.ast);
        this.cu.recordModifications();
        this.types = cu.types();
        this.allNodes = new ArrayList<>();
//        this.allStatements = new ArrayList<>();
        this.method2statements = new HashMap<>();
        for (TypeDeclaration clazz : this.types) {
            List<ASTNode> components = clazz.bodyDeclarations();
            for (int i = 0; i < components.size(); i++) {
                ASTNode component = components.get(i);
                if (component instanceof FieldDeclaration) {
                    allNodes.add(component);
//                    allFieldDeclarations.add((FieldDeclaration) component);
                }
                if (component instanceof Initializer) {
                    Block block = ((Initializer) component).getBody();
                    if (block != null) {
//                        allStatements.addAll(getAllStatements((block.statements())));
                        allNodes.addAll(getAllStatements((block.statements())));
                    }
                }
                if (component instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) component;
                    List<ASTNode> statements;
                    Block block = method.getBody();
                    if (block == null) {
                        statements = new ArrayList<>();
                    } else {
                        statements = getAllStatements(block.statements());
//                        this.allStatements.addAll(statements);
                        this.allNodes.addAll(statements);
                    }
                    this.method2statements.put(clazz.getName().toString() + ":" + createMethodSignature(method), statements);
                }
            }
        }
//        this.candidateStatements = null;
        this.candidateNodes = null;
//        this.candidateFieldDeclarations = new ArrayList<>();
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

    // This method can be invoked only if the source code file has generated.
    public void writeToJavaFile() {
        try {
            File file = new File(this.filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(this.filePath);
            fileWriter.write(this.document.get());
            fileWriter.close();
        } catch (IOException e) {
            System.err.println("Fail to Write to Java File!");
            e.printStackTrace();
        }
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
            TypeDeclaration[] types = clazz.getTypes();
            for (TypeDeclaration type : types) {
                System.out.println(type);
            }
            MethodDeclaration[] methods = clazz.getMethods();
            for (MethodDeclaration method : methods) {
                System.out.println("----------Method Name: " + method.getName() + "----------");
                Block block = method.getBody();
                if (block == null) {
                    continue;
                }
                List<Statement> statements = block.statements();
                for (int i = 0; i < statements.size(); i++) {
                    Statement statement = (Statement) block.statements().get(i);
                    System.out.println(statement.toString());
                    List<ASTNode> nodes = getChildrenNodes(statement);
                    for (ASTNode node : nodes) {
                        System.out.println(node + "  " + node.getClass() + "  " + String.format("0x%x", System.identityHashCode(node)));
                    }
                    System.out.println("-----------------");
                }
            }
        }
    }

    /*
      This function can get all related statemnts of its ASTWrapper by taint analysis.
    */
    public List<ASTNode> getCandidateNodes() {
        HashSet<Integer> validLines = file2line.get(this.filePath);
        List<ASTNode> resNodes = new ArrayList<>();
        if (validLines == null) {
            return resNodes;
        }
        validLines.remove(-1);
        if (validLines != null && validLines.size() > 0) {
            for (TypeDeclaration clazz : this.types) {
                List<ASTNode> nodes = clazz.bodyDeclarations();
                for (ASTNode node : nodes) {
                    int currentLine = this.cu.getLineNumber(node.getStartPosition());
                    if(validLines.contains(currentLine)) {
                        resNodes.add(node);
                    }
//                    if (node instanceof FieldDeclaration) {
//                        int currentLine = this.cu.getLineNumber(node.getStartPosition());
//                        if (validLines.contains(currentLine)) {
//                            this.candidateFieldDeclarations.add((FieldDeclaration) node);
//                        }
//                    }
//                    List<Statement> statements = new ArrayList<>();
//                    if (node instanceof Initializer) {
//                        Initializer initializer = (Initializer) node;
//                        if (initializer.getBody() != null) {
//                            statements.addAll(getAllStatements(initializer.getBody().statements()));
//                        }
//                    }
//                    if (node instanceof MethodDeclaration) {
//                        MethodDeclaration method = (MethodDeclaration) node;
//                        statements.add(node);
//                    }
//                    for (int i = 0; i < statements.size(); i++) {
//                        Statement statement = statements.get(i);
//                        ArrayList<Statement> st = new ArrayList<>();
//                        st.add(statement);
//                        int currentLine = this.cu.getLineNumber(statement.getStartPosition());
//                        if (validLines.contains(currentLine)) {
//                            candidateStatements.add(statement);
//                        }
//                    }
                }
            }
            this.violations = validLines.size();
        } else {
            this.violations = 0;
        }
        if (resNodes.isEmpty()) {
            return resNodes;
        }
        // Perform intra-procedural def-use analysis to get all candidate statements in this function
        // We may construct a better data flow analysis here
        HashSet<String> sources = new HashSet<>();
        Expression rightExpression = null;
        for (ASTNode node : resNodes) {
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
            MethodDeclaration method = getDirectMethodOfStatement(resNodes.get(0));
            if (method != null) {
                Block block = method.getBody();
                if (block != null) {
                    List<Statement> subStatements = getSubStatements(block.statements());
                    for (Statement statement : subStatements) {
                        if (statement instanceof ExpressionStatement
                                && ((ExpressionStatement) statement).getExpression() instanceof Assignment) {
                            Assignment assignment = (Assignment) ((ExpressionStatement) statement).getExpression();
                            if (assignment.getLeftHandSide() instanceof SimpleName) {
                                if (sources.contains(((SimpleName) assignment.getLeftHandSide()).getIdentifier())) {
                                    resNodes.add(statement);
                                }
                            }
                        }
                        if (statement instanceof VariableDeclarationStatement) {
                            VariableDeclarationFragment vd =
                                    (VariableDeclarationFragment)
                                            ((VariableDeclarationStatement) statement).fragments().get(0);
                            String varName = vd.getName().getIdentifier();
                            if (sources.contains(varName)) {
                                resNodes.add(statement);
                            }
                        }
                    }
                }
            }
        }
        return resNodes;
    }

    public boolean isBuggy() {
        boolean buggy = false;
        if (this.depth != 0 && this.violations < this.parViolations) { // Checking depth is to mutate initial seeds
            HashMap<String, HashSet<Integer>> mutant_bug2lines = file2bugs.get(this.filePath);
            HashMap<String, HashSet<Integer>> source_bug2lines = file2bugs.get(this.parentPath);
            // Two if statements below are used to avoid 1 bug in parent and 0 bug in child, vice versa.
            if (mutant_bug2lines == null && source_bug2lines == null) {
                System.err.println("What the fuck? Both reports don't have bugs?");
                System.exit(-1);
            }
            if (mutant_bug2lines == null) {
                mutant_bug2lines = new HashMap<>();
            }
            if (source_bug2lines == null) {
                source_bug2lines = new HashMap<>();
            }
            ArrayList<Map.Entry<String, HashSet<Integer>>> potentialFPs = new ArrayList<>();
            ArrayList<Map.Entry<String, HashSet<Integer>>> potentialFNs = new ArrayList<>();
            for (Map.Entry<String, HashSet<Integer>> entry : mutant_bug2lines.entrySet()) {
                if (!source_bug2lines.containsKey(entry.getKey())) {
                    potentialFPs.add(entry); // Because mutant has, but source does not have.
                } else {
                    HashSet<Integer> source_bugs = source_bug2lines.get(entry.getKey());
                    HashSet<Integer> mutant_bugs = mutant_bug2lines.get(entry.getKey());
                    if (source_bugs.size() == mutant_bugs.size()) {
                        continue;
                    }
                    if (source_bugs.size() > mutant_bugs.size()) {
                        potentialFNs.add(entry);
                    } else {
                        potentialFPs.add(entry);
                    }
                }
            }
            for (Map.Entry<String, HashSet<Integer>> entry : source_bug2lines.entrySet()) {
                if (!mutant_bug2lines.containsKey(entry.getKey())) {
                    potentialFNs.add(entry); // Because parent has, but child does not have.
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
                paths.add(new TriTuple(this.initSeed, this.filePath, "FP"));
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
                paths.add(new TriTuple(this.initSeed, this.filePath, "FN"));
            }
        }
        return buggy;
    }

    public boolean isDuplicatedMutant(String newContent) {
        for (int i = 0; i < this.mutantContents.size(); i++) {
            String oldContent = mutantContents.get(i);
            float simi = calculateStringSimilarity(oldContent, newContent);
            if (simi >= 1) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ASTWrapper> pureTransformation() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        try {
            if (this.candidateNodes == null) {
                this.candidateNodes = this.allNodes;
            }
            if (this.depth >= SEARCH_DEPTH) {
                return newWrappers;
            }
            int statementSize = this.candidateNodes.size();
            if (statementSize <= 0) {
                return newWrappers;
            }
            List<Transform> transforms = Transform.getTransforms();
            for (ASTNode oldNode : candidateNodes) {
                for (Transform transform : transforms) {
                    int counter = transform.check(oldNode);
                    for (int i = 0; i < counter; i++) {
                        String filename = "mutant_" + mutantCounter.getAndAdd(1);
                        String mutantPath = mutantFolder + File.separator + filename + ".java";
                        String content = this.document.get();
                        ASTWrapper newWrapper = new ASTWrapper(filename, mutantPath, content, this);
                        ASTNode newNode = newWrapper.searchNode(oldNode);
                        if (newNode == null) {
                            continue;
                            // Here, we directly return the newWrappers and regards this situation as failed mutation
                            // Not exception, because we select statements randomly
                        }
                        boolean hasMutated = transform.run(
                                i,
                                newWrapper.ast,
                                newWrapper.astRewrite,
                                getFirstBrotherOfStatement(newNode),
                                newNode
                        );
                        if (hasMutated) {
                            newWrapper.transSeq.add(transform.getIndex());
                            if (COMPILE) {
                                newWrapper.rewriteJavaCode();
                                newWrapper.resetClassName();
                                newWrapper.removePackageDefinition();
                            }
                            newWrapper.rewriteJavaCode();
                            String newContent = newWrapper.document.get();
//                            if (!this.isDuplicatedMutant(newContent)) {
                            newWrappers.add(newWrapper);
                            mutantContents.add(newWrapper.document.get());
                            newWrapper.writeToJavaFile();
//                            }
                        } else {
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

    public ArrayList<ASTWrapper> pureRandomTransformation() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        int randomCount = 0;
        try {
            if (this.candidateNodes == null) {
                this.candidateNodes = this.allNodes;
            }
            if (this.depth >= SEARCH_DEPTH) {
                return newWrappers;
            }
            int statementSize = this.candidateNodes.size();
            if (statementSize <= 0) {
                return newWrappers;
            }
            while (true) {
                if (++randomCount > statementSize * Transform.getTransforms().size()) {
                    break;
                }
                ASTNode oldNode = candidateNodes.get(random.nextInt(statementSize));
                Transform mutator = Transform.getTransformRandomly();
                int counter = mutator.check(oldNode);
                for (int i = 0; i < counter; i++) {
                    if (counter <= 0) {
                        continue;
                    }
                    String filename = "mutant_" + mutantCounter.getAndAdd(1);
                    String mutantPath = mutantFolder + File.separator + filename + ".java";
                    String content = this.document.get();
                    ASTWrapper newWrapper = new ASTWrapper(filename, mutantPath, content, this);
                    ASTNode newNode = newWrapper.searchNode(oldNode);
                    if (newNode == null) {
                        continue;
                        // Here, we directly return the newWrappers and regards this situation as failed mutation
                        // Not exception, because we select statements randomly
                    }
                    boolean hasMutated = mutator.run(
                            i,
                            newWrapper.ast,
                            newWrapper.astRewrite,
                            getFirstBrotherOfStatement(newNode),
                            newNode
                    );
                    if (hasMutated) {
                        newWrapper.transSeq.add(mutator.getIndex());
                        if (COMPILE) {
                            newWrapper.rewriteJavaCode();
                            newWrapper.resetClassName();
                            newWrapper.removePackageDefinition();
                        }
                        newWrapper.rewriteJavaCode();
                        String newContent = newWrapper.document.get();
                        if (!this.isDuplicatedMutant(newContent)) {
                            newWrappers.add(newWrapper);
                            mutantContents.add(newWrapper.document.get());
                            newWrapper.writeToJavaFile();
                        }
                    } else {
                        Files.deleteIfExists(Paths.get(mutantPath));
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newWrappers;
    }

    //    public static AtomicInteger sumMutation = new AtomicInteger(0);
    public ArrayList<ASTWrapper> guidedRandomTransformation() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        int guidedRandomCount = 0;
        try {
            if (this.candidateNodes == null) {
                this.candidateNodes = this.getCandidateNodes();
            }
            int candidateNodesize = this.candidateNodes.size();
            if (candidateNodesize <= 0) {
                invalidSeed.addAndGet(1);
                return newWrappers;
            } else {
                validSeed.addAndGet(1);
            }
            while (true) {
                if (guidedRandomCount > candidateNodesize * Transform.getTransforms().size()) {
                    break;
                }
//                Statement oldStatement = this.candidateNodes.get(random.nextInt(candidateNodesize));
                for (ASTNode candidateNode : this.candidateNodes) {
                    Transform mutator = Transform.getTransformRandomly();
                    guidedRandomCount++;
                    int counter = mutator.check(candidateNode);
//                    if(counter > 0) {
//                        sumMutation.addAndGet(1);
//                    }
                    for (int i = 0; i < counter; i++) {
                        String filename = "mutant_" + mutantCounter.getAndAdd(1);
                        String mutantPath = mutantFolder + File.separator + filename + ".java";
                        String content = this.document.get();
                        ASTWrapper newWrapper = new ASTWrapper(filename, mutantPath, content, this);
                        int oldLineNumber = this.cu.getLineNumber(candidateNode.getStartPosition());
                        ASTNode newNode = newWrapper.searchNodeByLinenumber(candidateNode, oldLineNumber);
                        if (newNode == null) {
                            System.out.println(candidateNode);
                            String s0 = this.document.get();
                            System.out.println(this.document.get().hashCode());
                            String s1 = newWrapper.document.get();
                            System.out.println(newWrapper.document.get().hashCode());
                            newWrapper.searchNodeByLinenumber(candidateNode, oldLineNumber);
                            System.out.println(candidateNode);
                            System.err.println("Old and new ASTWrapper are not matched!");
                            System.exit(-1);
                        }
                        boolean hasMutated = mutator.run(
                                i,
                                newWrapper.ast,
                                newWrapper.astRewrite,
                                getFirstBrotherOfStatement(newNode),
                                newNode);
                        if (hasMutated) {
                            succMutation.addAndGet(1);
                            newWrapper.transSeq.add(mutator.getIndex());
                            if (COMPILE) {
                                newWrapper.rewriteJavaCode();
                                newWrapper.resetClassName();
                                newWrapper.removePackageDefinition();
                            }
                            newWrapper.rewriteJavaCode();
                            String newContent = newWrapper.document.get();
//                        if (!this.isDuplicatedMutant(newContent)) {
                            newWrappers.add(newWrapper);
                            mutantContents.add(newWrapper.document.get());
                            newWrapper.writeToJavaFile();
//                        }
                        } else {
                            failMutation.addAndGet(1);
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

    public static AtomicInteger succMutation = new AtomicInteger(0);
    public static AtomicInteger failMutation = new AtomicInteger(0);
    public static AtomicInteger invalidSeed = new AtomicInteger(0);
    public static AtomicInteger validSeed = new AtomicInteger(0);

    public ArrayList<ASTWrapper> mainTransform() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        try {
            if (this.candidateNodes == null) {
                this.candidateNodes = this.getCandidateNodes();
            }
            if (this.depth == 0) {
                if (this.candidateNodes.size() == 0) {
                    invalidSeed.addAndGet(1);
                } else {
                    validSeed.addAndGet(1);
                }
            }
            for(ASTNode candidateNode : candidateNodes) {
                for(Transform transform : Transform.getTransforms()) {
                    int counter = transform.check(candidateNode);
                    for(int index = 0; index < counter; index++) {
                        String mutantFilename = "mutant_" + mutantCounter.getAndAdd(1);
                        String mutantPath = mutantFolder + File.separator + mutantFilename + ".java";
                        String content = this.document.get();
//                        if (SINGLE_TESTING) {
//                            System.out.println("This: " + this.filePath + "\nParent: " + this.parentPath);
//                            System.out.println(filename + " is generated from: " + mutator.getClass());
//                        }
                        // Here, codes from newWrapper and oldWrapper should be identical
                        ASTWrapper newWrapper = new ASTWrapper(mutantFilename, mutantPath, content, this);
                        int oldLineNumber = this.cu.getLineNumber(candidateNode.getStartPosition());
                        ASTNode newCandidate = newWrapper.searchNodeByLinenumber(candidateNode, oldLineNumber);
                        if (newCandidate == null) {
                            System.out.println(candidateNode);
                            String s0 = this.document.get();
                            System.out.println(this.document.get().hashCode());
                            String s1 = newWrapper.document.get();
                            System.out.println(newWrapper.document.get().hashCode());
                            newWrapper.searchNodeByLinenumber(candidateNode, oldLineNumber);
                            System.err.println("Old and new ASTWrapper are not matched!");
                            System.exit(-1);
                        }
                        boolean hasMutated = transform.run(index, newWrapper.ast, newWrapper.astRewrite, getFirstBrotherOfStatement(newCandidate), newCandidate);
                        if (hasMutated) {
                            succMutation.addAndGet(1);
                            newWrapper.transSeq.add(transform.getIndex());
                            if (COMPILE) {
                                // 1: rewrite for transformation, 2: rewrite for class name and pkg definition
                                newWrapper.rewriteJavaCode();
                                newWrapper.resetClassName();
                                newWrapper.removePackageDefinition();
                            }
                            newWrapper.rewriteJavaCode();
                            String newContent = newWrapper.document.get();
//                        if(!this.isDuplicatedMutant(newContent)) {
                            newWrappers.add(newWrapper);
                            mutantContents.add(newWrapper.document.get());
                            newWrapper.writeToJavaFile();
//                        }
                        } else {
                            failMutation.addAndGet(1);
                            Files.deleteIfExists(Paths.get(mutantPath));
                        }
                    }
                }
            }

            // Below code is original version, only consider statement
//            for (Statement oldStatement : this.candidateNodes) { // Actually, in this line, old statement is candidate statement
//                for (Mutator mutator : Mutator.getMutators()) {
//                    int counter = mutator.check(oldStatement);
//                    for (int i = 0; i < counter; i++) {
//                        String mutantFilename = "mutant_" + mutantCounter.getAndAdd(1);
//                        String mutantPath = mutantFolder + File.separator + mutantFilename + ".java";
//                        String content = this.document.get();
////                        if (SINGLE_TESTING) {
////                            System.out.println("This: " + this.filePath + "\nParent: " + this.parentPath);
////                            System.out.println(filename + " is generated from: " + mutator.getClass());
////                        }
//                        // Here, codes from newWrapper and oldWrapper should be identical because newWrapper is copied from oldWrapper
//                        ASTWrapper newWrapper = new ASTWrapper(mutantFilename, mutantPath, content, this);
//                        int oldLineNumber = this.cu.getLineNumber(oldStatement.getStartPosition());
//                        Statement newStatement = newWrapper.searchStatementByLinenumber(oldStatement, oldLineNumber);
//                        if (newStatement == null) {
//                            System.out.println(oldStatement);
//                            String s0 = this.document.get();
//                            System.out.println(this.document.get().hashCode());
//                            String s1 = newWrapper.document.get();
//                            System.out.println(newWrapper.document.get().hashCode());
//                            newWrapper.searchStatementByLinenumber(oldStatement, oldLineNumber);
//                            newWrapper.searchStatement(oldStatement);
//                            System.out.println(oldStatement);
//                            System.err.println("Old and new ASTWrapper are not matched!");
//                            System.exit(-1);
//                        }
//                        boolean hasMutated =
//                                mutator.run(
//                                        i,
//                                        newWrapper.ast,
//                                        newWrapper.astRewrite,
//                                        getFirstBrotherOfStatement(newStatement),
//                                        newStatement);
//                        if (hasMutated) {
//                            succMutation.addAndGet(1);
//                            newWrapper.transSeq.add(mutator.getIndex());
//                            if (COMPILE) {
//                                // 1: rewrite for transformation, 2: rewrite for class name and pkg definition
//                                newWrapper.rewriteJavaCode();
//                                newWrapper.resetClassName();
//                                newWrapper.removePackageDefinition();
//                            }
//                            newWrapper.rewriteJavaCode();
////                            String newContent = newWrapper.document.get();
////                            if(!this.isDuplicatedMutant(newContent)) {
//                            newWrappers.add(newWrapper);
//                            mutantContents.add(newWrapper.document.get());
//                            newWrapper.writeToJavaFile();
////                            }
//                        } else {
//                            failMutation.addAndGet(1);
//                            Files.deleteIfExists(Paths.get(mutantPath));
//                        }
//                    }
//                }
//            }
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
            }
        }
    }

    // This function is invoked by newWrapper
    public ASTNode searchNodeByLinenumber(ASTNode oldNode, int oldLineNumber) {
        if (oldNode == null) {
            System.err.println("AST Node to be searched is NULL!");
            System.exit(-1);
        }
        for (int i = 0; i < this.allNodes.size(); i++) {
            ASTNode newNode = this.allNodes.get(i);
            if (compareNode(newNode, oldNode)) {
                int newLineNumber = this.cu.getLineNumber(newNode.getStartPosition());
                if (newLineNumber == oldLineNumber) {
                    return newNode;
                }
            }
        }
        return null;
    }

    /*
    Search oldStatement (Source) in new ASTWrapper
     */
    public ASTNode searchNode(ASTNode oldNode) {
        if (oldNode == null) {
            System.err.println("Statement to be searched is NULL!");
            System.exit(-1);
        }
        MethodDeclaration oldMethod = getDirectMethodOfStatement(oldNode);
        if (oldMethod != null) {
            TypeDeclaration oldClazz = (TypeDeclaration) oldMethod.getParent();
            String key = oldClazz.getName().toString() + ":" + createMethodSignature(oldMethod);
            if (this.method2statements.containsKey(key)) {
                List<ASTNode> newNodes = this.method2statements.get(key);
                for (int i = 0; i < newNodes.size(); i++) {
                    ASTNode newNode = newNodes.get(i);
                    if (compareNode(newNode, oldNode)) {
                        return newNode;
                    }
                }
            }
        } else {
            for (int i = 0; i < this.allNodes.size(); i++) {
                ASTNode newNode = allNodes.get(i);
                if (compareNode(oldNode, newNode)) {
                    return newNode;
                }
            }
        }
        return null;
    }

    public boolean compareNode(ASTNode node1, ASTNode node2) {
        if (node1.toString().equals(node2.toString())) {
            return true;
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
        return false;
    }

    public String getFolderPath() {
        return this.folderPath;
    }

    public String getFolderName() {
        return this.folderName;
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
}
