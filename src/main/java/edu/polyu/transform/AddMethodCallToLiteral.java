package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
import edu.polyu.report.PMD_Report;
import edu.polyu.report.PMD_Violation;
import edu.polyu.report.Report;
import edu.polyu.report.Violation;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.Block;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jdt.core.dom.Expression;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.eclipse.jdt.core.dom.MethodInvocation;
import org.eclipse.jdt.core.dom.Modifier;
import org.eclipse.jdt.core.dom.ReturnStatement;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.SwitchCase;
import org.eclipse.jdt.core.dom.SwitchStatement;
import org.eclipse.jdt.core.dom.TypeDeclaration;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;
import org.eclipse.jdt.core.dom.rewrite.ListRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.checkExpressionLiteral;
import static edu.polyu.util.Util.checkLiteralType;
import static edu.polyu.util.Util.file2report;
import static edu.polyu.util.Util.getChildrenNodes;
import static edu.polyu.util.Util.getDirectMethodOfStatement;
import static edu.polyu.util.Util.random;


/**
 * Description:
 * Author: Vanguard
 * Date: 2021-12-29 16:10
 */
public class AddMethodCallToLiteral extends Transform {

    private static int literalCounter = 0;

    private static final AddMethodCallToLiteral addMethodCallToLiteral = new AddMethodCallToLiteral();
    private AddMethodCallToLiteral() {}

    public static AddMethodCallToLiteral getInstance() {
        return addMethodCallToLiteral;
    }

    @Override
    public boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode broStatement, ASTNode srcNode) {
        AST ast = wrapper.getAst();
        ASTRewrite astRewrite = wrapper.getAstRewrite();
        MethodDeclaration newMethod = ast.newMethodDeclaration();
        String newMethodName = "getLiteral" + literalCounter++;
        newMethod.setReturnType2(checkLiteralType(ast, (Expression) targetNode));
        newMethod.setName(ast.newSimpleName(newMethodName));
        ReturnStatement returnStatement = ast.newReturnStatement();
        Block newBlock = ast.newBlock();
        newBlock.statements().add(returnStatement);
        newMethod.setBody(newBlock);
        returnStatement.setExpression((Expression) ASTNode.copySubtree(ast, targetNode));
        MethodDeclaration oldMethod = getDirectMethodOfStatement(srcNode);
        if(oldMethod == null) {
            return false;  // It means that this statement is not located in a method, may stay in a initializer of class
        }
        boolean hasStatic = false;
        for(ASTNode modifier : (List<ASTNode>) oldMethod.modifiers()) {
            if(modifier instanceof Modifier) {
                if(((Modifier) modifier).getKeyword().toString().equals("static")) {
                    hasStatic = true;
                }
            }
        }
        if(hasStatic) {
            newMethod.modifiers().add(ast.newModifier(Modifier.ModifierKeyword.STATIC_KEYWORD));
        }
        TypeDeclaration clazz = (TypeDeclaration) oldMethod.getParent();
        ListRewrite listRewrite = astRewrite.getListRewrite(clazz, TypeDeclaration.BODY_DECLARATIONS_PROPERTY);
        listRewrite.insertFirst(newMethod, null);
        MethodInvocation newMethodInvocation = ast.newMethodInvocation();
        newMethodInvocation.setName(ast.newSimpleName(newMethodName));
        astRewrite.replace(targetNode, newMethodInvocation, null);
        return true;
    }

    @Override
    public List<ASTNode> check(ASTWrapper wrapper, ASTNode node) {
        List<ASTNode> nodes = new ArrayList<>();
        if (node instanceof FieldDeclaration || node instanceof MethodDeclaration) {
            return nodes;
        }
        List<ASTNode> subNodes = getChildrenNodes(node);
        for (ASTNode subNode : subNodes) {
            if (checkExpressionLiteral(subNode)) {
                nodes.add(subNode);
            }
        }
        if (node instanceof SwitchStatement) {
            for (Statement statement : (List<Statement>) ((SwitchStatement) node).statements()) {
                if(statement instanceof SwitchCase) {
                    Expression e = ((SwitchCase) statement).getExpression();
                    for(int i = nodes.size() - 1; i >= 0; i--) {
                        ASTNode subNode = nodes.get(i);
                        if(subNode == e) {
                            nodes.remove(subNode);
                        }
                    }
                }
            }
        }
        if (nodes.size() == 0) {
            return nodes;
        }
        CompilationUnit cu = wrapper.getCompilationUnit();
        List<ASTNode> resNodes = new ArrayList<>();
        List<ASTNode> candidateNodes = new ArrayList<>();
        // Locate by column
        for (ASTNode targetNode : nodes) {
            int col = cu.getColumnNumber(targetNode.getStartPosition()), row = cu.getLineNumber(targetNode.getStartPosition());
            Report report = file2report.get(wrapper.getFilePath());
            if (report instanceof PMD_Report) {
                PMD_Report pmd_report = (PMD_Report) report;
                List<PMD_Violation> violations = pmd_report.getViolations();
                for (PMD_Violation violation : violations) {
                    if (violation.getBeginLine() == row) {
                        candidateNodes.add(targetNode);
                        if(col >= violation.getBeginCol() - 1 && col <= violation.getEndCol() + 1) {
                            resNodes.add(targetNode);
                        }
                    }
                }
            }
        }
        if (resNodes.size() == 0) { // Cannot locate by column, randomly select one node
            if (candidateNodes.size() == 0) {
                resNodes.add(nodes.get(random.nextInt(nodes.size())));
            } else {
                if (candidateNodes.size() > 2) {
                    resNodes.add(candidateNodes.get(0));
                    resNodes.add(candidateNodes.get(1));
                } else {
                    resNodes.add(candidateNodes.get(0));
                }
            }
        }
        return resNodes;
    }

}
