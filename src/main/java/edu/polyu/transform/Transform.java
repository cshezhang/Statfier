package edu.polyu.transform;

import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Util.PURE_TESTING;
import static edu.polyu.util.Util.SINGLE_TESTING;
import static edu.polyu.util.Util.random;

/*
 * @Description: The General Class for Mutants.
 * @Author: Vanguard
 * @Date: 2021-10-14 09:25:07
 */
public abstract class Transform {

    private static ArrayList<Transform> transforms;

    public abstract boolean run(int index, AST ast, ASTRewrite astRewrite, ASTNode brotherStatement, ASTNode oldStatement);
//    public abstract List<ASTNode> getCandidateNodes(Statement statement);
    public abstract int check(ASTNode statement);
    public String getIndex() {
        return this.getClass().getSimpleName();
    }

    /*
    Add Final to Argument -> Method Invocation
    Add Assignment -> Method Invocation (目前只考虑了这种情况)
    Add Redundant Literal -> Assignment
    Add Control Branch -> Expression Statement
    Add Brackets -> Assignment
    Add Argument Assignment -> MethodInvocation, Assignment, and ClassInstance
     */
    static {
        transforms = new ArrayList<>();
        if(PURE_TESTING) {
            transforms.add(AddArgAssignment.getInstance());
            transforms.add(AddBrackets.getInstance());
            transforms.add(AddControlBranch.getInstance());
            transforms.add(AddGlobalAssignment.getInstance());
            transforms.add(AddLocalAssignment.getInstance());
            transforms.add(AddMethodCallToLiteral.getInstance());
            transforms.add(AddRedundantLiteral.getInstance());
            transforms.add(AnonymousClassWrapper.getInstance());
            transforms.add(CFWrapperWithDoWhile.getInstance());
            transforms.add(CFWrapperWithForFalse.getInstance());
            transforms.add(CFWrapperWithForTrue.getInstance());
            transforms.add(CFWrapperWithIfFlase.getInstance());
            transforms.add(CFWrapperWithIfTrue.getInstance());
            transforms.add(CFWrapperWithWhileTrue.getInstance());
            transforms.add(CompoundExpression1.getInstance());
            transforms.add(CompoundExpression2.getInstance());
            transforms.add(EnumClassWrapper.getInstance());
            transforms.add(NestedClassWrapper.getInstance());
            transforms.add(TransferLocalVarToGlobal.getInstance());
            transforms.add(TransferLocalVarToStaticGlobal.getInstance());

            transforms.add(AddStaticAssignment.getInstance());
            transforms.add(AddStaticModifier.getInstance());
        } else {
            if (SINGLE_TESTING) {
                transforms.add(AddArgAssignment.getInstance());
                transforms.add(AddBrackets.getInstance());
                transforms.add(AddControlBranch.getInstance());
                transforms.add(AddGlobalAssignment.getInstance());
                transforms.add(AddLocalAssignment.getInstance());
                transforms.add(AddMethodCallToLiteral.getInstance());
                transforms.add(AddRedundantLiteral.getInstance());
                transforms.add(AnonymousClassWrapper.getInstance());
                transforms.add(CFWrapperWithDoWhile.getInstance());
                transforms.add(CFWrapperWithForFalse.getInstance());
                transforms.add(CFWrapperWithForTrue.getInstance());
                transforms.add(CFWrapperWithIfFlase.getInstance());
                transforms.add(CFWrapperWithIfTrue.getInstance());
                transforms.add(CFWrapperWithWhileTrue.getInstance());
                transforms.add(CompoundExpression1.getInstance());
                transforms.add(CompoundExpression2.getInstance());
                transforms.add(EnumClassWrapper.getInstance());
                transforms.add(NestedClassWrapper.getInstance());
                transforms.add(TransferLocalVarToGlobal.getInstance());
                transforms.add(TransferLocalVarToStaticGlobal.getInstance());

                transforms.add(AddStaticAssignment.getInstance());
                transforms.add(AddStaticModifier.getInstance());
            } else {
                transforms.add(AddArgAssignment.getInstance());
                transforms.add(AddBrackets.getInstance());
                transforms.add(AddControlBranch.getInstance());
                transforms.add(AddGlobalAssignment.getInstance());
                transforms.add(AddLocalAssignment.getInstance());
                transforms.add(AddMethodCallToLiteral.getInstance());
                transforms.add(AddRedundantLiteral.getInstance());
                transforms.add(AnonymousClassWrapper.getInstance());
                transforms.add(CFWrapperWithDoWhile.getInstance());
                transforms.add(CFWrapperWithForFalse.getInstance());
                transforms.add(CFWrapperWithForTrue.getInstance());
                transforms.add(CFWrapperWithIfFlase.getInstance());
                transforms.add(CFWrapperWithIfTrue.getInstance());
                transforms.add(CFWrapperWithWhileTrue.getInstance());
                transforms.add(CompoundExpression1.getInstance());
                transforms.add(CompoundExpression2.getInstance());
                transforms.add(EnumClassWrapper.getInstance());
                transforms.add(NestedClassWrapper.getInstance());
                transforms.add(TransferLocalVarToGlobal.getInstance());
                transforms.add(TransferLocalVarToStaticGlobal.getInstance());

                transforms.add(AddStaticAssignment.getInstance());
                transforms.add(AddStaticModifier.getInstance());
            }
        }
    }

    public static Transform getTransformRandomly() {
        return transforms.get(random.nextInt(transforms.size()));
    }

    public static List<Transform> getTransforms() {
        return transforms;
    }

}
