package edu.polyu.transform;

import edu.polyu.analysis.ASTWrapper;
import org.eclipse.jdt.core.dom.ASTNode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static edu.polyu.util.Util.SINGLE_TESTING;
import static edu.polyu.util.Util.random;

/*
 * @Description: The General Class for Mutants.
 * @Author: Vanguard
 * @Date: 2021-10-14 09:25:07
 */
public abstract class Transform {

    private static List<Transform> transforms;
    public final static HashMap<String, Transform> name2transform;

    public abstract List<ASTNode> check(ASTWrapper wrapper, ASTNode node);
    public abstract boolean run(ASTNode targetNode, ASTWrapper wrapper, ASTNode broNode, ASTNode srcNode);

    public String getIndex() {
        return this.getClass().getSimpleName();
    }

    /*
    Add Final to Argument -> Method Invocation
    Add Assignment -> Method Invocation (Only consider this condition)
    Add Redundant Literal -> Assignment
    Add Control Branch -> Expression Statement
    Add Brackets -> Assignment
    Add Argument Assignment -> MethodInvocation, Assignment, and ClassInstance
     */
    static {
        transforms = new ArrayList<>();
        name2transform = new HashMap<>();
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
//            transforms.add(CFWrapperWithForFalse.getInstance());
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
//                transforms.add(CFWrapperWithForFalse.getInstance());
            transforms.add(CFWrapperWithForTrue.getInstance());
//                transforms.add(CFWrapperWithIfFlase.getInstance());
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
        for(Transform transform : transforms) {
            name2transform.put(transform.getIndex(), transform);
        }
    }

    public static Transform getTransformRandomly() {
        return transforms.get(random.nextInt(transforms.size()));
    }

    public static List<Transform> getTransforms() {
        return transforms;
    }

}
