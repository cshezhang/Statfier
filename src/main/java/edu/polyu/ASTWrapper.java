package edu.polyu;

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

import static edu.polyu.Mutator.getMutatorSize;
import static edu.polyu.Util.*;
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
    private List<FieldDeclaration> allFieldDeclarations;
    private List<Statement> allStatements;
    private HashMap<String, List<Statement>> method2statements;
    private List<Statement> candidateStatements;
    private List<FieldDeclaration> candidateFieldDeclarations;
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
        if(PMD_MUTATION) {
            this.mutantFolder = userdir + sep + "mutants" + sep + "iter" + (this.depth + 1) + sep + folderName;
        } else {
            this.mutantFolder = userdir + sep + "mutants" + sep + "iter" + (this.depth + 1);
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
        for(ASTNode node : (List<ASTNode>) this.cu.types()) { // Attention: AbstractTypeDeclaration
            if(node instanceof TypeDeclaration) {
                this.types.add((TypeDeclaration) node);
            }
        }
        this.allFieldDeclarations = new ArrayList<>();
        this.allStatements = new ArrayList<>();
        this.method2statements = new HashMap<>();
        for (TypeDeclaration clazz : this.types) {
            List<ASTNode> components = clazz.bodyDeclarations();
            for(int i = 0; i < components.size(); i++) {
                ASTNode component = components.get(i);
                if(component instanceof FieldDeclaration) {
                    allFieldDeclarations.add((FieldDeclaration) component);
                }
                if(component instanceof Initializer) {
                    Block block = ((Initializer) component).getBody();
                    if(block != null) {
                        allStatements.addAll(getAllStatements((block.statements())));
                    }
                }
                if(component instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) component;
                    List<Statement> statements;
                    Block block = method.getBody();
                    if(block == null ) {
                        statements = new ArrayList<>();
                    } else {
                        statements = getAllStatements(block.statements());
                        this.allStatements.addAll(statements);
                    }
                    this.method2statements.put(clazz.getName().toString() + ":" + createMethodSignature(method), statements);
                }
            }
        }
        this.candidateStatements = null;
        this.candidateFieldDeclarations = new ArrayList<>();
        this.mutantContents = new ArrayList<>();
    }

    // Other cases need invoke this constructor, filename is defined in mutate function
    public ASTWrapper(String filename, String filePath, String content, ASTWrapper parentWrapper) {
        this.depth = parentWrapper.depth + 1;
        this.filePath = filePath;
        this.initSeed = parentWrapper.initSeed;
        this.folderName = parentWrapper.folderName; // PMD needs this to specify bug type
        this.filename = filename;
        if(PMD_MUTATION) {
            this.mutantFolder = userdir + sep + "mutants" + sep + "iter" + (this.depth + 1) + sep + folderName;
        } else {
            this.mutantFolder =  userdir + sep + "mutants" + sep + "iter" + (this.depth + 1);
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
        this.allFieldDeclarations = new ArrayList<>();
        this.allStatements = new ArrayList<>();
        this.method2statements = new HashMap<>();
        for (TypeDeclaration clazz : this.types) {
            List<ASTNode> components = clazz.bodyDeclarations();
            for(int i = 0; i < components.size(); i++) {
                ASTNode component = components.get(i);
                if(component instanceof FieldDeclaration) {
                    allFieldDeclarations.add((FieldDeclaration) component);
                }
                if(component instanceof Initializer) {
                    Block block = ((Initializer) component).getBody();
                    if(block != null) {
                        allStatements.addAll(getAllStatements((block.statements())));
                    }
                }
                if(component instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) component;
                    List<Statement> statements;
                    Block block = method.getBody();
                    if(block == null ) {
                        statements = new ArrayList<>();
                    } else {
                        statements = getAllStatements(block.statements());
                        this.allStatements.addAll(statements);
                    }
                    this.method2statements.put(clazz.getName().toString() + ":" + createMethodSignature(method), statements);
                }
            }
        }
        this.candidateStatements = null;
        this.candidateFieldDeclarations = new ArrayList<>();
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
        this.allStatements = new ArrayList<>();
        this.method2statements = new HashMap<>();
        for (TypeDeclaration clazz : this.types) {
            List<ASTNode> components = clazz.bodyDeclarations();
            for(int i = 0; i < components.size(); i++) {
                ASTNode component = components.get(i);
                if(component instanceof FieldDeclaration) {
                    allFieldDeclarations.add((FieldDeclaration) component);
                }
                if(component instanceof Initializer) {
                    Block block = ((Initializer) component).getBody();
                    if(block != null) {
                        allStatements.addAll(getAllStatements((block.statements())));
                    }
                }
                if(component instanceof MethodDeclaration) {
                    MethodDeclaration method = (MethodDeclaration) component;
                    List<Statement> statements;
                    Block block = method.getBody();
                    if(block == null ) {
                        statements = new ArrayList<>();
                    } else {
                        statements = getAllStatements(block.statements());
                        this.allStatements.addAll(statements);
                    }
                    this.method2statements.put(clazz.getName().toString() + ":" + createMethodSignature(method), statements);
                }
            }
        }
        this.candidateStatements = null;
        this.candidateFieldDeclarations = new ArrayList<>();
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
            for(ASTNode node : super_nodes) {
                System.out.println(node);
            }
            TypeDeclaration[] types = clazz.getTypes();
            for(TypeDeclaration type : types) {
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
    public List<Statement> getCandidateStatements() {
        HashSet<Integer> validLines = file2line.get(this.filePath);
        ArrayList<Statement> candidateStatements = new ArrayList<>();
        if (validLines == null) {
            return candidateStatements;
        }
        validLines.remove(-1);
        if (validLines != null && validLines.size() > 0) {
            for (TypeDeclaration clazz : this.types) {
                List<ASTNode> nodes = clazz.bodyDeclarations();
                for(ASTNode node : nodes) {
                    if(node instanceof FieldDeclaration) {
                        int currentLine = this.cu.getLineNumber(node.getStartPosition());
                        if(validLines.contains(currentLine)) {
                            this.candidateFieldDeclarations.add((FieldDeclaration) node);
                        }
                    }
                    List<Statement> statements = new ArrayList<>();
                    if(node instanceof Initializer) {
                        Initializer initializer = (Initializer) node;
                        if(initializer.getBody() != null) {
                            statements.addAll(getAllStatements(initializer.getBody().statements()));
                        }
                    }
                    if(node instanceof MethodDeclaration) {
                        MethodDeclaration method = (MethodDeclaration) node;
                        if(method.getBody() != null) {
                            statements.addAll(getAllStatements(method.getBody().statements()));
                        }
                    }
                    for (int i = 0; i < statements.size(); i++) {
                        Statement statement = statements.get(i);
                        ArrayList<Statement> st = new ArrayList<>();
                        st.add(statement);
                        int currentLine = this.cu.getLineNumber(statement.getStartPosition());
                        if (validLines.contains(currentLine)) {
                            candidateStatements.add(statement);
                        }
                    }
                }
            }
            this.violations = validLines.size();
        } else {
            this.violations = 0;
        }
        if (candidateStatements.isEmpty()) {
            return candidateStatements;
        }
        // Perform intra-procedural def-use analysis to get all candidate statements in this function
        // We may construct a better data flow analysis here
        HashSet<String> sources = new HashSet<>();
        Expression rightExpression = null;
        for (Statement statement : candidateStatements) {
            if (statement instanceof VariableDeclarationStatement) {
                rightExpression =
                        ((VariableDeclarationFragment)
                                ((VariableDeclarationStatement) statement).fragments().get(0))
                                .getInitializer();
            }
            if (statement instanceof ExpressionStatement
                    && ((ExpressionStatement) statement).getExpression() instanceof Assignment) {
                rightExpression =
                        ((Assignment) ((ExpressionStatement) statement).getExpression()).getRightHandSide();
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
            MethodDeclaration method = getDirectMethodOfStatement(candidateStatements.get(0));
            if(method != null) {
                Block block = method.getBody();
                if (block != null) {
                    List<Statement> subStatements = getSubStatements(block.statements());
                    for (Statement statement : subStatements) {
                        if (statement instanceof ExpressionStatement
                                && ((ExpressionStatement) statement).getExpression() instanceof Assignment) {
                            Assignment assignment = (Assignment) ((ExpressionStatement) statement).getExpression();
                            if (assignment.getLeftHandSide() instanceof SimpleName) {
                                if (sources.contains(((SimpleName) assignment.getLeftHandSide()).getIdentifier())) {
                                    candidateStatements.add(statement);
                                }
                            }
                        }
                        if (statement instanceof VariableDeclarationStatement) {
                            VariableDeclarationFragment vd =
                                    (VariableDeclarationFragment)
                                            ((VariableDeclarationStatement) statement).fragments().get(0);
                            String varName = vd.getName().getIdentifier();
                            if (sources.contains(varName)) {
                                candidateStatements.add(statement);
                            }
                        }
                    }
                }
            }
        }
        return candidateStatements;
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
                buggy =true;
                String bugType = potentialFPs.get(i).getKey();
                if (!compactIssues.containsKey(bugType)) {
                    HashMap<String, ArrayList<TriTuple>> seq2paths = new HashMap<>();
                    compactIssues.put(bugType, seq2paths);
                }
                HashMap<String, ArrayList<TriTuple>> seq2paths = compactIssues.get(bugType);
                String seqKey = this.transSeq.toString();
                if (!seq2paths.containsKey(seqKey)) {
                    ArrayList<TriTuple> paths = new ArrayList<>();
                    seq2paths.put(seqKey, paths);
                }
                ArrayList<TriTuple> paths = seq2paths.get(seqKey);
                paths.add(new TriTuple(this.initSeed, this.filePath, "FP"));
            }
            for (int i = 0; i < potentialFNs.size(); i++) {
                buggy = true;
                String bugType = potentialFNs.get(i).getKey();
                if (!compactIssues.containsKey(bugType)) {
                    HashMap<String, ArrayList<TriTuple>> seq2paths = new HashMap<>();
                    compactIssues.put(bugType, seq2paths);
                }
                HashMap<String, ArrayList<TriTuple>> seq2paths = compactIssues.get(bugType);
                String seqKey = this.transSeq.toString();
                if (!seq2paths.containsKey(seqKey)) {
                    ArrayList<TriTuple> paths = new ArrayList<>();
                    seq2paths.put(seqKey, paths);
                }
                ArrayList<TriTuple> paths = seq2paths.get(seqKey);
                paths.add(new TriTuple(this.initSeed, this.filePath, "FN"));
            }
        }
        return buggy;
    }

    public boolean isDuplicatedMutant(String newContent) {
        for(int i = 0; i < this.mutantContents.size(); i++) {
            String oldContent = mutantContents.get(i);
            float simi = calculateStringSimilarity(oldContent, newContent);
            if(simi >= 1) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<ASTWrapper> pureTransformation() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        try {
            if (this.candidateStatements == null) {
                this.candidateStatements = this.allStatements;
            }
            if(this.depth >= SEARCH_DEPTH) {
                return newWrappers;
            }
            int statementSize = this.candidateStatements.size();
            if (statementSize <= 0) {
                return newWrappers;
            }
            ArrayList<Mutator> mutators = Mutator.getMutators();
            for(Statement oldStatement : candidateStatements) {
                for (Mutator mutator : mutators) {
                    int counter = mutator.check(oldStatement);
                    for (int i = 0; i < counter; i++) {
                        String filename = "mutant_" + mutantCounter.getAndAdd(1);
                        String mutantPath = mutantFolder + sep + filename + ".java";
                        String content = this.document.get();
                        ASTWrapper newWrapper = new ASTWrapper(filename, mutantPath, content, this);
                        Statement newStatement = newWrapper.searchStatement(oldStatement);
                        if (newStatement == null) {
                            continue;
                            // Here, we directly return the newWrappers and regards this situation as failed mutation
                            // Not exception, because we select statements randomly
                        }
                        boolean hasMutated = mutator.run(
                                i,
                                newWrapper.ast,
                                newWrapper.astRewrite,
                                getFirstBrotherOfStatement(newStatement),
                                newStatement
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
            if (this.candidateStatements == null) {
                this.candidateStatements = this.allStatements;
            }
            if(this.depth >= SEARCH_DEPTH) {
                return newWrappers;
            }
            int statementSize = this.candidateStatements.size();
            if (statementSize <= 0) {
                return newWrappers;
            }
            while (true) {
                if (++randomCount > statementSize * getMutatorSize()) {
                    break;
                }
                Statement oldStatement = candidateStatements.get(random.nextInt(statementSize));
                Mutator mutator = Mutator.getMutatorRandomly();
                int counter = mutator.check(oldStatement);
                for(int i = 0; i < counter; i++) {
                    if (counter <= 0) {
                        continue;
                    }
                    String filename = "mutant_" + mutantCounter.getAndAdd(1);
                    String mutantPath = mutantFolder + sep + filename + ".java";
                    String content = this.document.get();
                    ASTWrapper newWrapper = new ASTWrapper(filename, mutantPath, content, this);
                    Statement newStatement = newWrapper.searchStatement(oldStatement);
                    if (newStatement == null) {
                        continue;
                        // Here, we directly return the newWrappers and regards this situation as failed mutation
                        // Not exception, because we select statements randomly
                    }
                    boolean hasMutated = mutator.run(
                            i,
                            newWrapper.ast,
                            newWrapper.astRewrite,
                            getFirstBrotherOfStatement(newStatement),
                            newStatement
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
            if (this.candidateStatements == null) {
                this.candidateStatements = this.getCandidateStatements();
            }
            int candidateStatementSize = this.candidateStatements.size();
            if (candidateStatementSize <= 0) {
                invalidSeed.addAndGet(1);
                return newWrappers;
            } else {
                validSeed.addAndGet(1);
            }
            while (true) {
                if (guidedRandomCount > candidateStatementSize * getMutatorSize()) {
                    break;
                }
//                Statement oldStatement = this.candidateStatements.get(random.nextInt(candidateStatementSize));
                for(Statement oldStatement : this.candidateStatements) {
                    Mutator mutator = Mutator.getMutatorRandomly();
                    guidedRandomCount++;
                    int counter = mutator.check(oldStatement);
//                    if(counter > 0) {
//                        sumMutation.addAndGet(1);
//                    }
                    for (int i = 0; i < counter; i++) {
                        String filename = "mutant_" + mutantCounter.getAndAdd(1);
                        String mutantPath = mutantFolder + sep + filename + ".java";
                        String content = this.document.get();
                        ASTWrapper newWrapper = new ASTWrapper(filename, mutantPath, content, this);
                        int oldLineNumber = this.cu.getLineNumber(oldStatement.getStartPosition());
                        Statement newStatement = newWrapper.searchStatementByLinenumber(oldStatement, oldLineNumber);
                        if (newStatement == null) {
                            System.out.println(oldStatement);
                            String s0 = this.document.get();
                            System.out.println(this.document.get().hashCode());
                            String s1 = newWrapper.document.get();
                            System.out.println(newWrapper.document.get().hashCode());
                            newWrapper.searchStatementByLinenumber(oldStatement, oldLineNumber);
                            System.out.println(oldStatement);
                            System.err.println("Old and new ASTWrapper are not matched!");
                            System.exit(-1);
                        }
                        boolean hasMutated = mutator.run(
                                i,
                                newWrapper.ast,
                                newWrapper.astRewrite,
                                getFirstBrotherOfStatement(newStatement),
                                newStatement);
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
            if (this.candidateStatements == null) {
                this.candidateStatements = this.getCandidateStatements();
            }
            if(this.depth == 0) {
                if(this.candidateStatements.size() == 0 && this.candidateFieldDeclarations.size() == 0) {
                    invalidSeed.addAndGet(1);
                } else {
                    validSeed.addAndGet(1);
                }
            }
//            for(FieldDeclaration oldFieldDeclaration : this.candidateFieldDeclarations) {
//                for(FieldMutator transform : FieldMutator.getTransforms()) {
//                    String mutantFilename = "mutant_" + mutantCounter.getAndAdd(1);
//                    String mutantPath = mutantFolder + sep + mutantFilename + ".java";
//                    String content = this.document.get();
////                        if (SINGLE_TESTING) {
////                            System.out.println("This: " + this.filePath + "\nParent: " + this.parentPath);
////                            System.out.println(filename + " is generated from: " + mutator.getClass());
////                        }
//                    // Here, codes from newWrapper and oldWrapper should be identical
//                    ASTWrapper newWrapper = new ASTWrapper(mutantFilename, mutantPath, content, this);
//                    int oldLineNumber = this.cu.getLineNumber(oldFieldDeclaration.getStartPosition());
//                    FieldDeclaration newFieldDeclaration = newWrapper.searchFieldDeclarationByLinenumber(oldFieldDeclaration, oldLineNumber);
//                    if (newFieldDeclaration == null) {
//                        System.out.println(oldFieldDeclaration);
//                        String s0 = this.document.get();
//                        System.out.println(this.document.get().hashCode());
//                        String s1 = newWrapper.document.get();
//                        System.out.println(newWrapper.document.get().hashCode());
//                        newWrapper.searchFieldDeclarationByLinenumber(oldFieldDeclaration, oldLineNumber);
//                        System.out.println(oldFieldDeclaration);
//                        System.err.println("Old and new ASTWrapper are not matched!");
//                        System.exit(-1);
//                    }
//                    boolean hasMutated =
//                            transform.run(
//                                    newWrapper.ast,
//                                    newWrapper.astRewrite,
//                                    newFieldDeclaration);
//                    if (hasMutated) {
//                        succMutation.addAndGet(1);
//                        newWrapper.transSeq.add(transform.getIndex());
//                        if (COMPILE) {
//                            // 1: rewrite for transformation, 2: rewrite for class name and pkg definition
//                            newWrapper.rewriteJavaCode();
//                            newWrapper.resetClassName();
//                            newWrapper.removePackageDefinition();
//                        }
//                        newWrapper.rewriteJavaCode();
//                        String newContent = newWrapper.document.get();
////                        if(!this.isDuplicatedMutant(newContent)) {
//                            newWrappers.add(newWrapper);
//                            mutantContents.add(newWrapper.document.get());
//                            newWrapper.writeToJavaFile();
////                        }
//                    } else {
//                        failMutation.addAndGet(1);
//                        Files.deleteIfExists(Paths.get(mutantPath));
//                    }
//                }
//            }
            for (Statement oldStatement : this.candidateStatements) {
                for (Mutator mutator : Mutator.getMutators()) {
                    int counter = mutator.check(oldStatement);
                    for(int i = 0; i < counter; i++) {
                        String mutantFilename = "mutant_" + mutantCounter.getAndAdd(1);
                        String mutantPath = mutantFolder + sep + mutantFilename + ".java";
                        String content = this.document.get();
//                        if (SINGLE_TESTING) {
//                            System.out.println("This: " + this.filePath + "\nParent: " + this.parentPath);
//                            System.out.println(filename + " is generated from: " + mutator.getClass());
//                        }
                        // Here, codes from newWrapper and oldWrapper should be identical
                        ASTWrapper newWrapper = new ASTWrapper(mutantFilename, mutantPath, content, this);
                        int oldLineNumber = this.cu.getLineNumber(oldStatement.getStartPosition());
                        Statement newStatement = newWrapper.searchStatementByLinenumber(oldStatement, oldLineNumber);
                        if (newStatement == null) {
                            System.out.println(oldStatement);
                            String s0 = this.document.get();
                            System.out.println(this.document.get().hashCode());
                            String s1 = newWrapper.document.get();
                            System.out.println(newWrapper.document.get().hashCode());
                            newWrapper.searchStatementByLinenumber(oldStatement, oldLineNumber);
                            newWrapper.searchStatement(oldStatement);
                            System.out.println(oldStatement);
                            System.err.println("Old and new ASTWrapper are not matched!");
                            System.exit(-1);
                        }
                        boolean hasMutated =
                                mutator.run(
                                        i,
                                        newWrapper.ast,
                                        newWrapper.astRewrite,
                                        getFirstBrotherOfStatement(newStatement),
                                        newStatement);
                        if (hasMutated) {
                            succMutation.addAndGet(1);
                            newWrapper.transSeq.add(mutator.getIndex());
                            if (COMPILE) {
                                // 1: rewrite for transformation, 2: rewrite for class name and pkg definition
                                newWrapper.rewriteJavaCode();
                                newWrapper.resetClassName();
                                newWrapper.removePackageDefinition();
                            }
                            newWrapper.rewriteJavaCode();
                            String newContent = newWrapper.document.get();
//                            if(!this.isDuplicatedMutant(newContent)) {
                                newWrappers.add(newWrapper);
                                mutantContents.add(newWrapper.document.get());
                                newWrapper.writeToJavaFile();
//                            }
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

    public FieldDeclaration searchFieldDeclarationByLinenumber(FieldDeclaration oldFieldDeclaration, int oldLineNumber) {
        if (oldFieldDeclaration == null) {
            System.err.println("FieldDeclaration to be searched is NULL!");
            System.exit(-1);
        }
        for (int i = 0; i < this.allFieldDeclarations.size(); i++) {
            FieldDeclaration newFieldDeclaration = this.allFieldDeclarations.get(i);
            if (matcher.match(newFieldDeclaration, oldFieldDeclaration)) {
                int newLineNumber = this.cu.getLineNumber(newFieldDeclaration.getStartPosition());
                if (newLineNumber == oldLineNumber) {
                    return newFieldDeclaration;
                }
            }
        }
        return null;
    }

    // This function is invoked by newWrapper
    public Statement searchStatementByLinenumber(Statement oldStatement, int oldLineNumber) {
        if (oldStatement == null) {
            System.err.println("Statement to be searched is NULL!");
            System.exit(-1);
        }
        for (int i = 0; i < this.allStatements.size(); i++) {
            Statement newStatement = this.allStatements.get(i);
            if (compareStatement(newStatement, oldStatement)) {
                int newLineNumber = this.cu.getLineNumber(newStatement.getStartPosition());
                if (newLineNumber == oldLineNumber) {
                    return newStatement;
                }
            }
        }
        return null;
    }

    /*
    Search oldStatement (Source) in new ASTWrapper
     */
    public Statement searchStatement(Statement oldStatement) {
        if (oldStatement == null) {
            System.err.println("Statement to be searched is NULL!");
            System.exit(-1);
        }
        MethodDeclaration oldMethod = getDirectMethodOfStatement(oldStatement);
        if(oldMethod != null) {
            TypeDeclaration oldClazz = (TypeDeclaration) oldMethod.getParent();
            String key = oldClazz.getName().toString() + ":" + createMethodSignature(oldMethod);
            if (this.method2statements.containsKey(key)) {
                List<Statement> newStatements = this.method2statements.get(key);
                for (int i = 0; i < newStatements.size(); i++) {
                    Statement newStatement = newStatements.get(i);
                    if (compareStatement(newStatement, oldStatement)) {
                        return newStatement;
                    }
                }
            }
        } else {
            for(int i = 0; i < this.allStatements.size(); i++) {
                Statement newStatement = allStatements.get(i);
                if(compareStatement(oldStatement, newStatement)) {
                    return newStatement;
                }
            }
        }
        return null;
    }

    public boolean compareStatement(Statement statement1, Statement statement2) {
        if(statement1.toString().equals(statement2.toString())) {
            return true;
        }
        if(statement1 instanceof IfStatement) {
            return matcher.match((IfStatement) statement1, statement2);
        }
        if(statement1 instanceof SwitchStatement) {
            return matcher.match((SwitchStatement) statement1, statement2);
        }
        if(statement1 instanceof WhileStatement) {
            return matcher.match((WhileStatement) statement1, statement2);
        }
        if(statement1 instanceof DoStatement) {
            return matcher.match((DoStatement) statement1, statement2);
        }
        if(statement1 instanceof ForStatement) {
            return matcher.match((ForStatement) statement1, statement2);
        }
        if(statement1 instanceof TryStatement) {
            return matcher.match((TryStatement) statement1, statement2);
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
