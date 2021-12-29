//package edu.polyu.mutators;
//
//import edu.polyu.Mutator;
//import edu.polyu.Util;
//import org.eclipse.jdt.core.dom.AST;
//import org.eclipse.jdt.core.dom.ASTNode;
//import org.eclipse.jdt.core.dom.Assignment;
//import org.eclipse.jdt.core.dom.Block;
//import org.eclipse.jdt.core.dom.Expression;
//import org.eclipse.jdt.core.dom.ExpressionStatement;
//import org.eclipse.jdt.core.dom.FieldDeclaration;
//import org.eclipse.jdt.core.dom.Initializer;
//import org.eclipse.jdt.core.dom.Modifier;
//import org.eclipse.jdt.core.dom.SimpleName;
//import org.eclipse.jdt.core.dom.Statement;
//import org.eclipse.jdt.core.dom.TypeDeclaration;
//import org.eclipse.jdt.core.dom.VariableDeclarationFragment;
//import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
//import org.eclipse.jdt.core.dom.rewrite.ListRewrite;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import static edu.polyu.Util.getChildrenNodes;
//import static edu.polyu.Util.getTypeOfStatement;
//import static edu.polyu.Util.random;
//
//public class TransferLocalVarToStaticInit extends Mutator {
//
//    private static int varCounter;
//    private static TransferLocalVarToStaticInit instance = new TransferLocalVarToStaticInit();
//
//    private TransferLocalVarToStaticInit() {
//        varCounter = 0;
//    }
//
//    public static TransferLocalVarToStaticInit getInstance() {
//        return instance;
//    }
//
//    @Override
//    public int getIndex() {
//        return 12;
//    }
//
//    @Override
//    public boolean transform(AST ast, ASTRewrite astRewrite, Statement brotherStatement, Statement sourceStatement) {
//        List<ASTNode> subNodes = getChildrenNodes(sourceStatement);
//        List<ASTNode> literalNodes = new ArrayList<>();
//        for(int i = 0; i < subNodes.size(); i++) {
//            ASTNode node = subNodes.get(i);
//            if(Util.checkExpressionLiteral(node)) {
//                literalNodes.add(node);
//            }
//        }
//        int randomIndex = random.nextInt(literalNodes.size());
//        Expression targetLiteral = (Expression) literalNodes.get(randomIndex);
//        TypeDeclaration clazz = getTypeOfStatement(sourceStatement);
//        List<ASTNode> bodyNodes = clazz.bodyDeclarations();
//        String newVarName = "t2si" + varCounter++;
//        SimpleName newVar = ast.newSimpleName(newVarName);
//        VariableDeclarationFragment newVdFragment = ast.newVariableDeclarationFragment();
//        newVdFragment.setName(newVar);
//        FieldDeclaration fieldDeclaration = ast.newFieldDeclaration(newVdFragment);
//        fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
//        fieldDeclaration.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.FINAL_KEYWORD));
//        Assignment assignment = ast.newAssignment();
//        assignment.setLeftHandSide(newVar);
//        assignment.setRightHandSide(targetLiteral);
//        ExpressionStatement newAssignment = ast.newExpressionStatement(assignment);
//        Initializer initializer = ast.newInitializer();
//        Block block = ast.newBlock();
//        block.statements().add(newAssignment);
//        initializer.setBody(block);
//        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
//        listRewrite.insertFirst(block, null);
//        listRewrite.insertFirst(fieldDeclaration, null);
//        astRewrite.replace(targetLiteral, newVar, null);
//        return true;
//    }
//
//    @Override
//    public boolean check(Statement statement) {
//        List<ASTNode> nodes = getChildrenNodes(statement);
//        for(int i = 0; i < nodes.size(); i++) {
//            if(Util.checkExpressionLiteral(nodes.get(i))) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//}
