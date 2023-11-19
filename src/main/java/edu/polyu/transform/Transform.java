package edu.polyu.transform;

import edu.polyu.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.ASTNode;
import edu.polyu.analysis.SelectionAlgorithm;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static edu.polyu.util.Utility.CHECKSTYLE_MUTATION;
import static edu.polyu.util.Utility.COMPILE;
import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.DIV_SELECTION;
import static edu.polyu.util.Utility.GUIDED_LOCATION;
import static edu.polyu.util.Utility.INFER_MUTATION;
import static edu.polyu.util.Utility.NO_SELECTION;
import static edu.polyu.util.Utility.PMD_MUTATION;
import static edu.polyu.util.Utility.RANDOM_LOCATION;
import static edu.polyu.util.Utility.RANDOM_SELECTION;
import static edu.polyu.util.Utility.SONARQUBE_MUTATION;

/**
 * @Description: The General Class for Mutants.
 * @Author: RainyD4y
 * @Date: 2021-10-14 09:25:07
 */
public abstract class Transform {

    private static List<Transform> transforms;
    public final static HashMap<String, Transform> name2transform;

    public abstract List<ASTNode> check(TypeWrapper wrapper, ASTNode node);

    public abstract boolean run(ASTNode targetNode, TypeWrapper wrapper, ASTNode broNode, ASTNode srcNode);

    public String getIndex() {
        return this.getClass().getSimpleName();
    }

    static {
        transforms = new ArrayList<>();
        name2transform = new HashMap<>();
        if (DEBUG) {
            transforms.add(LoopConversion1.getInstance());
            transforms.add(LoopConversion2.getInstance());
            transforms.add(AnonymousClassWrapper.getInstance());
            transforms.add(CFWrapperWithDoWhile.getInstance());
            transforms.add(CFWrapperWithForTrue1.getInstance());
            transforms.add(CFWrapperWithForTrue2.getInstance());
            transforms.add(CFWrapperWithIfTrue.getInstance());
            transforms.add(CFWrapperWithWhileTrue.getInstance());
            transforms.add(EnumClassWrapper.getInstance());
            transforms.add(NestedClassWrapper.getInstance());
            if(!PMD_MUTATION && !CHECKSTYLE_MUTATION) {
                transforms.add(CFWrapperWithIfFalse.getInstance());
                transforms.add(CompoundExpression1.getInstance());
                transforms.add(CompoundExpression2.getInstance());
                transforms.add(CompoundExpression3.getInstance());
                transforms.add(CompoundExpression4.getInstance());
                transforms.add(AddRedundantLiteral.getInstance());
            }
            if(!CHECKSTYLE_MUTATION) {
                transforms.add(AddBrackets.getInstance());
                transforms.add(AddArgAssignment.getInstance());
//                transforms.add(AddControlBranch.getInstance());
                transforms.add(AddStaticModifier.getInstance());
                transforms.add(AddLocalAssignment.getInstance());
                transforms.add(AddStaticAssignment.getInstance());
                transforms.add(AddGlobalAssignment.getInstance());
            }
        } else {
            transforms.add(LoopConversion1.getInstance());
            transforms.add(LoopConversion2.getInstance());
            if(INFER_MUTATION || SONARQUBE_MUTATION) {
                // related to inter-procedural analysis
                transforms.add(AddMethodCallToLiteral.getInstance());
                transforms.add(TransferLocalVarToGlobal.getInstance());
                transforms.add(TransferLocalVarToStaticGlobal.getInstance());
            }
            transforms.add(AnonymousClassWrapper.getInstance());
            transforms.add(CFWrapperWithDoWhile.getInstance());
            transforms.add(CFWrapperWithForTrue1.getInstance());
            transforms.add(CFWrapperWithForTrue2.getInstance());
            transforms.add(CFWrapperWithIfTrue.getInstance());
            transforms.add(CFWrapperWithWhileTrue.getInstance());
            transforms.add(EnumClassWrapper.getInstance());
            transforms.add(NestedClassWrapper.getInstance());
            if(!PMD_MUTATION && !CHECKSTYLE_MUTATION) {
                transforms.add(CFWrapperWithIfFalse.getInstance());
                transforms.add(CompoundExpression1.getInstance());
                transforms.add(CompoundExpression2.getInstance());
                transforms.add(CompoundExpression3.getInstance());
                transforms.add(CompoundExpression4.getInstance());
                transforms.add(AddRedundantLiteral.getInstance());
            }
            if(!CHECKSTYLE_MUTATION) {
                transforms.add(AddBrackets.getInstance());
                transforms.add(AddArgAssignment.getInstance());
//                transforms.add(AddControlBranch.getInstance());
                transforms.add(AddStaticModifier.getInstance());
                transforms.add(AddLocalAssignment.getInstance());
                transforms.add(AddStaticAssignment.getInstance());
                transforms.add(AddGlobalAssignment.getInstance());
            }
        }
        for (Transform transform : transforms) {
            name2transform.put(transform.getIndex(), transform);
        }
    }

    public static List<Transform> getTransforms() {
        return transforms;
    }

    public static AtomicInteger cnt1 = new AtomicInteger(0);
    public static AtomicInteger cnt2 = new AtomicInteger(0);

    public static void singleLevelExplorer(List<TypeWrapper> wrappers, int currentDepth) {  // Current depth means the depth of variants in wrappers, not the iteration level
        while (!wrappers.isEmpty()) {
            TypeWrapper wrapper = wrappers.get(0); // remove TypeWrapper in currentDepth level
            wrappers.remove(0);
            if (wrapper.depth == currentDepth) {
                if (!wrapper.isBuggy()) {
                    List<TypeWrapper> mutants = new ArrayList<>();
                    if (GUIDED_LOCATION) {
                        mutants = wrapper.TransformByGuidedLocation();
                    } else if (RANDOM_LOCATION) {
                        mutants = wrapper.TransformByRandomLocation();
                    }
                    if (DEBUG) {
                        System.out.println("Src Path: " + wrapper.getFilePath());
                        System.out.println("Mutant Size: " + mutants.size());
                    }
                    cnt1.addAndGet(mutants.size());
                    List<TypeWrapper> reducedMutants = null;
                    if (NO_SELECTION) {
                        reducedMutants = mutants;
                    }
                    if (RANDOM_SELECTION) {
                        reducedMutants = SelectionAlgorithm.Random_Selection(mutants);
                    }
                    if (DIV_SELECTION) {
                        reducedMutants = SelectionAlgorithm.Div_Selection(mutants);
                    }
                    if(DEBUG) {
                        System.out.println("Reduced Mutant Size: " + reducedMutants.size());
                    }
                    cnt2.addAndGet(reducedMutants.size());
                    for (int j = 0; j < reducedMutants.size(); j++) {
                        TypeWrapper newMutant = reducedMutants.get(j);
                        if (COMPILE) {
                            newMutant.rewriteJavaCode();  // 1. Rewrite transformation, don't remove this line, we need rewrite Java code twice
                            newMutant.resetClassName();  // 2. Rewrite class name and pkg definition
                            newMutant.removePackageDefinition();
                        }
                        newMutant.rewriteJavaCode();
                        if (newMutant.writeToJavaFile()) {
                            TypeWrapper.mutant2seed.put(newMutant.getFilePath(), newMutant.getInitSeedPath());
                            TypeWrapper.mutant2seq.put(newMutant.getFilePath(), newMutant.getTransSeq().toString());
                        }
                    }
                    wrappers.addAll(reducedMutants);
                }
            } else {
                wrappers.add(0, wrapper);
                break;
            }
        }
    }

    // Return value:
    public static List<TypeWrapper> singleLevelExplorer(List<TypeWrapper> wrappers) {  // Current depth means the depth of variants in wrappers, not the iteration level
        List<TypeWrapper> newWrappers = new ArrayList<>();
        while (!wrappers.isEmpty()) {
            TypeWrapper wrapper = wrappers.get(0); // remove TypeWrapper in currentDepth level
            wrappers.remove(0);
            List<TypeWrapper> mutants = new ArrayList<>();
            if (GUIDED_LOCATION) {
                mutants = wrapper.TransformByGuidedLocation();
            } else if (RANDOM_LOCATION) {
                mutants = wrapper.TransformByRandomLocation();
            }
            if (DEBUG) {
                System.out.println("Src Path: " + wrapper.getFilePath());
                System.out.println("Mutant Size: " + mutants.size());
            }
            cnt1.addAndGet(mutants.size());
            List<TypeWrapper> reducedMutants = null;
            if (NO_SELECTION) {
                reducedMutants = mutants;
            }
            if (RANDOM_SELECTION) {
                reducedMutants = SelectionAlgorithm.Random_Selection(mutants);
            }
            if (DIV_SELECTION) {
                reducedMutants = SelectionAlgorithm.Div_Selection(mutants);
            }
            if(DEBUG) {
                System.out.println("Reduced Mutant Size: " + reducedMutants.size());
            }
            cnt2.addAndGet(reducedMutants.size());
            for (int j = 0; j < reducedMutants.size(); j++) {
                TypeWrapper newMutant = reducedMutants.get(j);
                if (COMPILE) {
                    newMutant.rewriteJavaCode();  // 1. Rewrite transformation, don't remove this line, we need rewrite Java code twice
                    newMutant.resetClassName();  // 2. Rewrite class name and pkg definition
                    newMutant.removePackageDefinition();
                }
                newMutant.rewriteJavaCode();
                if (newMutant.writeToJavaFile()) {
                    TypeWrapper.mutant2seed.put(newMutant.getFilePath(), newMutant.getInitSeedPath());
                    TypeWrapper.mutant2seq.put(newMutant.getFilePath(), newMutant.getTransSeq().toString());
                }
            }
            newWrappers.addAll(reducedMutants);
        }
        return newWrappers;
    }

    public static void singleLevelExplorer(ArrayDeque<TypeWrapper> wrappers, int currentDepth) {  // Current depth means the depth of variants in wrappers, not the iteration level
        while (!wrappers.isEmpty()) {
            TypeWrapper wrapper = wrappers.pollFirst(); // remove TypeWrapper in currentDepth level
            if (wrapper.depth < currentDepth) {
                if (!wrapper.isBuggy()) {
                    List<TypeWrapper> mutants = new ArrayList<>();
                    if (GUIDED_LOCATION) {
                        mutants = wrapper.TransformByGuidedLocation();
                    } else if (RANDOM_LOCATION) {
                        mutants = wrapper.TransformByRandomLocation();
                    }
                    if (DEBUG) {
                        System.out.println("Src Path: " + wrapper.getFilePath());
                        System.out.println("Mutant Size: " + mutants.size());
                    }
                    cnt1.addAndGet(mutants.size());
                    List<TypeWrapper> reducedMutants = null;
                    if (NO_SELECTION) {
                        reducedMutants = mutants;
                    }
                    if (RANDOM_SELECTION) {
                        reducedMutants = SelectionAlgorithm.Random_Selection(mutants);
                    }
                    if (DIV_SELECTION) {
                        reducedMutants = SelectionAlgorithm.Div_Selection(mutants);
                    }
                    if(DEBUG) {
                        System.out.println("Reduced Mutant Size: " + reducedMutants.size());
                    }
                    cnt2.addAndGet(reducedMutants.size());
                    for (int j = 0; j < reducedMutants.size(); j++) {
                        TypeWrapper newMutant = reducedMutants.get(j);
                        if (COMPILE) {
                            newMutant.rewriteJavaCode();  // 1. Rewrite transformation, don't remove this line, we need rewrite Java code twice
                            newMutant.resetClassName();  // 2. Rewrite class name and pkg definition
                            newMutant.removePackageDefinition();
                        }
                        newMutant.rewriteJavaCode();
                        if (newMutant.writeToJavaFile()) {
                            TypeWrapper.mutant2seed.put(newMutant.getFilePath(), newMutant.getInitSeedPath());
                            TypeWrapper.mutant2seq.put(newMutant.getFilePath(), newMutant.getTransSeq().toString());
                        }
                    }
                    wrappers.addAll(reducedMutants);
                }
            } else {
                wrappers.addFirst(wrapper);
                break;
            }
        }
    }

}
