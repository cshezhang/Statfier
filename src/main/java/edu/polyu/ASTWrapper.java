package edu.polyu;

import edu.polyu.report.PMD_Report;
import org.apache.commons.io.FileUtils;
import org.eclipse.jdt.core.dom.*;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jface.text.Document;
import org.eclipse.text.edits.TextEdit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static edu.polyu.Mutator.mutator_size;
import static edu.polyu.Util.Path2Last;
import static edu.polyu.Util.SEARCH_DEPTH;
import static edu.polyu.Util.SINGLE_TESTING;
import static edu.polyu.Util.all_PMD_Reports;
import static edu.polyu.Util.checkExecutionTime;
import static edu.polyu.Util.compactIssues;
import static edu.polyu.Util.compilerOptions;
import static edu.polyu.Util.createMethodSignature;
import static edu.polyu.Util.file2bugs;
import static edu.polyu.Util.file2line;
import static edu.polyu.Util.mutantCounter;
import static edu.polyu.Util.getAllStatements;
import static edu.polyu.Util.getChildrenNodes;
import static edu.polyu.Util.getDirectMethodOfStatement;
import static edu.polyu.Util.getFirstBrotherOfStatement;
import static edu.polyu.Util.getSubStatements;
import static edu.polyu.Util.getTypeOfStatement;
import static edu.polyu.Util.random;
import static edu.polyu.Util.sep;
import static edu.polyu.Util.userdir;

/**
 * Description: Mutator Scheduler Author: Huaien Zhang Date: 2021-08-10 16:10
 */
public class ASTWrapper {

    public int depth;
    public AST ast;
    public ASTRewrite astRewrite;

    public String filename;
    // public HashMap<String, Integer> bug2line;
    private int violations;
    private int parViolations;
    private Document document;
    private ASTParser parser;
    private CompilationUnit cu;
    private String filePath; // This variable saves the absolute path.
    private String folderPath;
    private String folderName;
    private String parentPath;
    private String mutantFolder;
    private List<Integer> transSeq;
    private List<TypeDeclaration> types;
    private List<Statement> allStatements;
    private HashMap<String, List<Statement>> method2statements;
    private List<Statement> candidateStatements;

    public ASTWrapper(String filePath, String folderName) {
        this.depth = 0;
        this.filePath = filePath;
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
        this.mutantFolder = userdir + sep + "mutants" + sep + "iter" + (this.depth + 1) + sep + folderName;
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
        this.allStatements = new ArrayList<>();
        this.method2statements = new HashMap<>();
        for (TypeDeclaration clazz : this.types) {
            for (MethodDeclaration method : clazz.getMethods()) {
                List<Statement> statements = getAllStatements(method);
                this.allStatements.addAll(statements);
                this.method2statements.put(
                        clazz.getName().toString() + ":" + createMethodSignature(method), statements);
            }
        }
        this.candidateStatements = null;
    }

    // Other cases need invoke this constructor, filename is defined in mutate function
    public ASTWrapper(String filename, String filePath, String content, ASTWrapper parentWrapper) {
        this.depth = parentWrapper.depth + 1;
        this.filePath = filePath;
        this.folderName = parentWrapper.folderName;
        this.filename = filename;
        this.mutantFolder = userdir + sep + "mutants" + sep + "iter" + this.depth + sep + folderName;
        this.parViolations = parentWrapper.violations;
        this.parentPath = parentWrapper.filePath;
        this.transSeq = new ArrayList<>();
        this.document = new Document(content);
        File targetFile = new File(filePath);
        this.folderPath = targetFile.getParentFile().getAbsolutePath();
        this.parser = ASTParser.newParser(AST.JLS3);
        this.parser.setSource(document.get().toCharArray());
        this.cu = (CompilationUnit) parser.createAST(null);
        this.ast = cu.getAST();
        this.astRewrite = ASTRewrite.create(ast);
        this.cu.recordModifications();
        this.types = this.cu.types();
        this.allStatements = new ArrayList<>();
        this.method2statements = new HashMap<>();
        for (TypeDeclaration clazz : this.types) {
            for (MethodDeclaration method : clazz.getMethods()) {
                List<Statement> statements = getAllStatements(method);
                this.allStatements.addAll(statements);
                this.method2statements.put(
                        clazz.getName().toString() + ":" + createMethodSignature(method), statements);
            }
        }
        this.candidateStatements = null;
    }

    public void updateAST(String source) {
        this.document = new Document(source);
        this.parser = ASTParser.newParser(AST.JLS3);
        this.parser.setSource(document.get().toCharArray());
        this.cu = (CompilationUnit) parser.createAST(null);
        this.ast = cu.getAST();
        this.astRewrite = ASTRewrite.create(this.ast);
        this.cu.recordModifications();
        this.types = cu.types();
        this.allStatements = new ArrayList<>();
        for (TypeDeclaration clazz : this.types) {
            for (MethodDeclaration method : clazz.getMethods()) {
                List<Statement> statements = getAllStatements(method);
                this.allStatements.addAll(statements);
                this.method2statements.put(
                        clazz.getName().toString() + ":" + createMethodSignature(method), statements);
            }
        }
        this.candidateStatements = null;
    }

    public void rewriteJavaCode() {
        TextEdit edits = this.astRewrite.rewriteAST(this.document, null);
        try {
            edits.apply(this.document);
        } catch (Exception e) {
            System.err.println("Fail to Rewrite Java Document!");
            e.printStackTrace();
        }
        updateAST(this.document.get());
    }

    // This method can be invoked only if the source code file has generated.
    public void writeToJavaFile() {
        try {
            File file = new File(this.filePath);
            if (!file.exists()) {
                file.createNewFile();
            }
            FileWriter fileWriter = new FileWriter(this.filePath);
            String s = this.document.get();
            fileWriter.write(this.document.get());
            fileWriter.close();
        } catch (IOException e) {
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
            for (MethodDeclaration method : clazz.getMethods()) {
                System.out.println("----------Method Name: " + method.getName() + "----------");
                Block block = method.getBody();
                if (block == null) {
                    continue;
                }
                for (int i = 0; i < block.statements().size(); i++) {
                    Statement statement = (Statement) block.statements().get(i);
                    System.out.println(statement.toString());
                    List<ASTNode> nodes = getChildrenNodes(statement);
                    for (ASTNode node : nodes) {
                        System.out.println(
                                node + "  " + node.getClass() + "  " + String.format("0x%x", System.identityHashCode(node)));
                    }
                    System.out.println("-----------------");
                }
            }
        }
    }

    /*
      This function can get all related statemnts of its ASTWrapper by taint analysis.
    */
    public ArrayList<Statement> getCandidateStatement() {
        //        System.out.println("Find Candidate Statements in File: " + filename);
        List<List<PMD_Report>> pmd_reports = all_PMD_Reports;
        HashSet<Integer> validLines = file2line.get(this.filePath);
        ArrayList<Statement> candidateStatements = new ArrayList<>();
        if (validLines == null) {
            return candidateStatements;
        }
        validLines.remove(-1);
        //        if(validLines != null || validLines.size() == 0) {
        if (validLines != null && validLines.size() > 0) {
            for (TypeDeclaration clazz : this.types) {
                List<ASTNode> nodes = clazz.bodyDeclarations();
                for(ASTNode node : nodes) {
                    if(node instanceof MethodDeclaration) {
                        MethodDeclaration method = (MethodDeclaration) node;
                        List<Statement> statements = getAllStatements(method);
                        for (Statement statement : statements) {
                            ArrayList<Statement> st = new ArrayList<>();
                            st.add(statement);
                            int currentLine = this.cu.getLineNumber(statement.getStartPosition());
                            if (validLines.contains(currentLine)) {
                                candidateStatements.addAll(getSubStatements(null, st));
                            }
                        }
                    }
                    if(node instanceof FieldDeclaration) {
                        
                    }
                }
            }
            this.violations = validLines.size();
        } else {
            this.violations = 0;
        }
        if (candidateStatements.isEmpty()) {
            //            System.out.println("No violations in this file, it needs more checks!");
            return candidateStatements;
        }
        // Perform intra-procedural def-use analysis to get all candidate statements in this function
        // 这里感觉可以重构一个更好的过程内数据流分析
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
            List<Statement> subStatements =
                    getSubStatements(getDirectMethodOfStatement(candidateStatements.get(0)));
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
        return candidateStatements;
    }

    public boolean diffAnalysis() {
        if (this.depth != 0
                && this.violations < this.parViolations) { // Checking depth is to mutate initial seeds
            //            System.out.println("--------Diff Begin--------");
            //            System.out.println("Warning: Diff happened!" + "\nMutant: " + this.filePath +
            // "\nSource: " + this.parentPath);
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
            //            System.out.print("Potential FP: ");
            for (int i = 0; i < potentialFPs.size(); i++) {
                String bugType = potentialFPs.get(i).getKey();
                HashSet<Integer> lines = potentialFPs.get(i).getValue();
                //                System.out.println("[" + bugType + ", (" + lines + ")]");
                if (!compactIssues.containsKey(bugType)) {
                    HashMap<String, ArrayList<String>> seq2paths = new HashMap<>();
                    compactIssues.put(bugType, seq2paths);
                }
                HashMap<String, ArrayList<String>> seq2paths = compactIssues.get(bugType);
                String seqKey = this.transSeq.toString();
                if (!seq2paths.containsKey(seqKey)) {
                    ArrayList<String> paths = new ArrayList<>();
                    seq2paths.put(seqKey, paths);
                }
                ArrayList<String> paths = seq2paths.get(seqKey);
                paths.add(this.filePath);
            }
            //            System.out.print("\nPotential FN: ");
            for (int i = 0; i < potentialFNs.size(); i++) {
                String bugType = potentialFNs.get(i).getKey();
                HashSet<Integer> lines = potentialFNs.get(i).getValue();
                //                System.out.println("[" + bugType + ", (" + lines + ")]");
                if (!compactIssues.containsKey(bugType)) {
                    HashMap<String, ArrayList<String>> seq2paths = new HashMap<>();
                    compactIssues.put(bugType, seq2paths);
                }
                HashMap<String, ArrayList<String>> seq2paths = compactIssues.get(bugType);
                String seqKey = this.transSeq.toString();
                if (!seq2paths.containsKey(seqKey)) {
                    ArrayList<String> paths = new ArrayList<>();
                    seq2paths.put(seqKey, paths);
                }
                ArrayList<String> paths = seq2paths.get(seqKey);
                paths.add(this.filePath);
            }
            //            System.out.println("--------Diff End--------");
            return true;
        } else {
            return false;
        }
    }

    public ArrayList<ASTWrapper> pureRandomMutate() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        int randomCount = 0;
        try {
            if (this.candidateStatements == null) {
                this.candidateStatements = this.allStatements;
            }
            if (diffAnalysis()) {
                return newWrappers;
            }
            int statementSize = this.candidateStatements.size();
            if (statementSize <= 0) {
                return newWrappers;
            }
            while (true) {
                if (++randomCount > statementSize * mutator_size) {
                    break;
                }
                Statement oldStatement = candidateStatements.get(random.nextInt(statementSize));
                Mutator mutator = Mutator.getMutatorRandomly();
                if (!mutator.check(oldStatement)) {
                    continue;
                }
                String filename = "mutant_" + mutantCounter++;
                String mutantPath = mutantFolder + sep + filename + ".java";
                String content = this.document.get();
                TypeDeclaration clazz = getTypeOfStatement(oldStatement);
                ASTWrapper newWrapper = new ASTWrapper(filename, mutantPath, content, this);
                Statement newStatement = newWrapper.searchStatement(oldStatement);
                if (newStatement == null) {
                    //                    System.out.println(oldStatement.toString());
                    //                    newWrapper.method2statements.forEach((k, v) -> {
                    //                        System.out.println(k);
                    //                        System.out.println(v);
                    //                    });
                    //                    System.err.println("Old and new ASTWrapper are not matched!");
                    //                    System.exit(-1);
                    continue;
                    // Here, we directly return the newWrappers and regards this situation as failed mutation
                    // Not exception, because we select statements randomly
                }
                boolean hasMutated =
                        mutator.transform(
                                newWrapper.ast,
                                newWrapper.astRewrite,
                                getFirstBrotherOfStatement(newStatement),
                                newStatement);
                if (hasMutated) {
                    newWrapper.rewriteJavaCode();
                    newWrapper.resetClassName();
                    newWrapper.removePackageDefinition();
                    newWrapper.rewriteJavaCode();
                    newWrapper.writeToJavaFile();
                    newWrappers.add(newWrapper);
                } else {
                    Files.deleteIfExists(Paths.get(mutantPath));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newWrappers;
    }

    public ArrayList<ASTWrapper> guidedRandomMutate() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        int guidedRandomCount = 0;
        try {
            if (this.candidateStatements == null) {
                this.candidateStatements = this.getCandidateStatement();
            }
            if (diffAnalysis()) {
                return newWrappers;
            }
            int statementSize = this.candidateStatements.size();
            if (statementSize <= 0) {
                return newWrappers;
            }
            while (true) {
                if (++guidedRandomCount > statementSize * mutator_size) {
                    break;
                }
                Statement oldStatement = this.candidateStatements.get(random.nextInt(statementSize));
                Mutator mutator = Mutator.getMutatorRandomly();
                if (!mutator.check(oldStatement)) {
                    continue;
                }
                String filename = "mutant_" + mutantCounter++;
                String mutantPath = mutantFolder + sep + filename + ".java";
                String content = this.document.get();
                TypeDeclaration clazz = getTypeOfStatement(oldStatement);
                ASTWrapper newWrapper = new ASTWrapper(filename, mutantPath, content, this);
                Statement newStatement = newWrapper.searchStatement(oldStatement);
                if (newStatement == null) {
                    //                    System.out.println(oldStatement.toString());
                    //                    String s0 = this.document.get();
                    //                    System.out.println(this.document.get().hashCode());
                    //                    String s1 = newWrapper.document.get();
                    //                    System.out.println(newWrapper.document.get().hashCode());
                    System.err.println("Old and new ASTWrapper are not matched!");
                    System.exit(-1);
                }
                boolean hasMutated =
                        mutator.transform(
                                newWrapper.ast,
                                newWrapper.astRewrite,
                                getFirstBrotherOfStatement(newStatement),
                                newStatement);
                if (hasMutated) {
                    newWrapper.rewriteJavaCode();
                    newWrapper.resetClassName();
                    newWrapper.removePackageDefinition();
                    newWrapper.rewriteJavaCode();
                    newWrapper.writeToJavaFile();
                    newWrappers.add(newWrapper);
                } else {
                    Files.deleteIfExists(Paths.get(mutantPath));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newWrappers;
    }

    public ArrayList<ASTWrapper> mutateWithNoCompile() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        try {
            if (this.candidateStatements == null) {
                this.candidateStatements = this.getCandidateStatement();
            }
            if (diffAnalysis()) { // If bugs detected, then stop the mutation
                return newWrappers;
            }
            if (this.depth >= SEARCH_DEPTH) {
                return newWrappers;
            }
            for (Statement oldStatement : this.candidateStatements) {
                for (Mutator mutator : Mutator.getMutators()) {
                    if (!mutator.check(oldStatement)) {
                        continue;
                    }
                    String mutantFilename = "mutant_" + mutantCounter++;
                    String mutantPath = mutantFolder + sep + mutantFilename + ".java";
                    String content = this.document.get();
                    if (SINGLE_TESTING) {
                        System.out.println("This: " + this.filePath + "\nParent: " + this.parentPath);
                        System.out.println(filename + " is generated from: " + mutator.getClass());
                    }
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
                        System.out.println(oldStatement);
                        System.err.println("Old and new ASTWrapper are not matched!");
                        System.exit(-1);
                    }
                    boolean hasMutated = mutator.transform(newWrapper.ast, newWrapper.astRewrite, getFirstBrotherOfStatement(newStatement), newStatement);
                    // 先把上边的mutate完成trasnform, 然后再进行类名和包名的替换
                    if (hasMutated) {
                        newWrapper.transSeq.add(mutator.getIndex());
                        newWrapper.rewriteJavaCode();
                        newWrapper.writeToJavaFile();
                        newWrappers.add(newWrapper);
                    } else {
                        Files.deleteIfExists(Paths.get(mutantPath));
                    }
                    checkExecutionTime();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return newWrappers;
    }

    public ArrayList<ASTWrapper> mutateWithCompile() {
        ArrayList<ASTWrapper> newWrappers = new ArrayList<>();
        try {
            if (this.candidateStatements == null) {
                this.candidateStatements = this.getCandidateStatement();
            }
            if (diffAnalysis()) { // If bugs detected, then stop the mutation
                return newWrappers;
            }
            if (this.depth >= SEARCH_DEPTH) {
                return newWrappers;
            }
            for (Statement oldStatement : this.candidateStatements) {
                for (Mutator mutator : Mutator.getMutators()) {
                    if (!mutator.check(oldStatement)) {
                        continue;
                    }
                    String mutantFilename = "mutant_" + mutantCounter++;
                    String mutantPath = mutantFolder + sep + mutantFilename + ".java";
                    String content = this.document.get();
                    if (SINGLE_TESTING) {
                        System.out.println("This: " + this.filePath + "\nParent: " + this.parentPath);
                        System.out.println(filename + " is generated from: " + mutator.getClass());
                    }
                    // 这个时候newWrapper的code应该和原来的一样
                    ASTWrapper newWrapper = new ASTWrapper(mutantFilename, mutantPath, content, this);
                    Statement newStatement = newWrapper.searchStatement(oldStatement);
                    ;
                    if (newStatement == null) {
                        System.out.println(oldStatement.toString());
                        String s0 = this.document.get();
                        System.out.println(this.document.get().hashCode());
                        String s1 = newWrapper.document.get();
                        System.out.println(newWrapper.document.get().hashCode());
                        newWrapper.searchStatement(oldStatement);
                        ;
                        System.out.println(oldStatement);
                        System.err.println("Old and new ASTWrapper are not matched!");
                        System.exit(-1);
                    }
                    boolean hasMutated =
                            mutator.transform(
                                    newWrapper.ast,
                                    newWrapper.astRewrite,
                                    getFirstBrotherOfStatement(newStatement),
                                    newStatement);
                    // 1: rewrite for transformation, 2: rewrite for class name and pkg definition
                    if (hasMutated) {
                        newWrapper.rewriteJavaCode();
                        newWrapper.resetClassName();
                        newWrapper.removePackageDefinition();
                        newWrapper.rewriteJavaCode();
                        newWrapper.writeToJavaFile();
                        newWrappers.add(newWrapper);
                    } else {
                        Files.deleteIfExists(Paths.get(mutantPath));
                    }
                    checkExecutionTime();
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

    // This function is invoked by newWrapper
    public Statement searchStatementByLinenumber(Statement oldStatement, int oldLineNumber) {
        if (oldStatement == null) {
            System.err.println("Statement to be searched is NULL!");
            System.exit(-1);
        }
        Statement newStatement = null;
        for (int i = 0; i < this.allStatements.size(); i++) {
            Statement statement = this.allStatements.get(i);
            if (statement.toString().equals(oldStatement.toString())) {
                int newLineNumber = this.cu.getLineNumber(statement.getStartPosition());
                if (newLineNumber == oldLineNumber) {
                    newStatement = statement;
                    break;
                }
            }
        }
        return newStatement;
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
        TypeDeclaration oldClazz = (TypeDeclaration) oldMethod.getParent();
        String key = oldClazz.getName().toString() + ":" + createMethodSignature(oldMethod);
        if (this.method2statements.containsKey(key)) {
            List<Statement> newStatements = this.method2statements.get(key);
            for (int i = 0; i < newStatements.size(); i++) {
                Statement newStatement = newStatements.get(i);
                String s1 = newStatement.toString();
                String s2 = oldStatement.toString();
                if (s1.equals(s2)) {
                    return newStatement;
                }
            }
        }
        return null;
    }

    public String getFolderPath() {
        return this.folderPath;
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
