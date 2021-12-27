/*
 * @Description: The General Class for Mutants.
 * @Author: Austin ZHANG
 * @Date: 2021-10-14 09:25:07
 */
package edu.polyu;

import edu.polyu.mutators.*;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.Statement;
import org.eclipse.jdt.core.dom.rewrite.ASTRewrite;

import java.util.ArrayList;

import static edu.polyu.Util.SINGLE_TESTING;
import static edu.polyu.Util.random;

public abstract class Mutator {

    private static ArrayList<Mutator> mutators;

    public abstract boolean transform(AST ast, ASTRewrite astRewrite, Statement brother, Statement sourceStatement);
    public abstract boolean check(Statement statement);
    public abstract int getIndex();

    /*
    Add Final to Argument -> Method Invocation
    Add Assignment -> Method Invocation (目前只考虑了这种情况)
    Add Redundant Literal -> Assignment
    Add Control Branch -> Expression Statement
    Add Brackets -> Assignment
    Add Argument Assignment -> MethodInvocation, Assignment, and ClassInstance
     */
    static {
        mutators = new ArrayList<>();
        if(SINGLE_TESTING) {
//            mutators.add(AddArgAssignment.getInstance());
//            mutators.add(AddAssignment.getInstance());
//            mutators.add(AddBrackets.getInstance());
//            mutators.add(AddControlBranch.getInstance());
//            mutators.add(AddRedundantLiteral.getInstance());
//            mutators.add(AnonymousClassWrapper.getInstance());
//            mutators.add(EnumClassWrapper.getInstance());
//            mutators.add(NestedClassWrapper.getInstance());
            mutators.add(AddMethodCall.getInstance());
//            mutators.add(TransferLocalVarToGlobal.getInstance());
        } else {
            mutators.add(AddArgAssignment.getInstance());
            mutators.add(AddAssignment.getInstance());
            mutators.add(AddBrackets.getInstance());
            mutators.add(AddControlBranch.getInstance());
            mutators.add(AddRedundantLiteral.getInstance());
            mutators.add(AnonymousClassWrapper.getInstance());
            mutators.add(EnumClassWrapper.getInstance());
            mutators.add(NestedClassWrapper.getInstance());
            mutators.add(AddMethodCall.getInstance());
            mutators.add(TransferLocalVarToGlobal.getInstance());
        }
    }

    public static Mutator getMutatorRandomly() {
        return mutators.get(random.nextInt(mutators.size()));
    }

    public static ArrayList<Mutator> getMutators() {
        return mutators;
    }

    public static int getMutatorSize() {
        return mutators.size();
    }

}
