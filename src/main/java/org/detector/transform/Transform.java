package org.detector.transform;

import org.detector.analysis.TypeWrapper;
import org.eclipse.jdt.core.dom.ASTNode;
import org.detector.analysis.SelectionAlgorithm;
import org.detector.util.Utility;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.detector.util.Utility.COMPILE;
import static org.detector.util.Utility.DEBUG;
import static org.detector.util.Utility.DIV_SELECTION;
import static org.detector.util.Utility.GUIDED_LOCATION;
import static org.detector.util.Utility.INFER_MUTATION;
import static org.detector.util.Utility.NO_SELECTION;
import static org.detector.util.Utility.PMD_MUTATION;
import static org.detector.util.Utility.RANDOM_LOCATION;
import static org.detector.util.Utility.RANDOM_SELECTION;
import static org.detector.util.Utility.SONARQUBE_MUTATION;

/*
 * @Description: The General Class for Mutants.
 * @Author: Vanguard
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
//            transforms.add(CFWrapperWithIfFalse.getInstance());
            transforms.add(CFWrapperWithIfTrue.getInstance());
            transforms.add(CFWrapperWithWhileTrue.getInstance());
//            transforms.add(CompoundExpression1.getInstance());
//            transforms.add(CompoundExpression2.getInstance());
            transforms.add(CompoundExpression3.getInstance());
            transforms.add(CompoundExpression4.getInstance());
            transforms.add(EnumClassWrapper.getInstance());
            transforms.add(NestedClassWrapper.getInstance());
            transforms.add(TransferLocalVarToGlobal.getInstance());
            transforms.add(TransferLocalVarToStaticGlobal.getInstance());
            transforms.add(AddStaticAssignment.getInstance());
            transforms.add(AddStaticModifier.getInstance());
        } else {
            transforms.add(LoopConversion1.getInstance());
            transforms.add(LoopConversion2.getInstance());
            transforms.add(AddArgAssignment.getInstance());
            transforms.add(AddBrackets.getInstance());
            transforms.add(AddControlBranch.getInstance());
            transforms.add(AddGlobalAssignment.getInstance());
            transforms.add(AddLocalAssignment.getInstance());
            if(INFER_MUTATION || SONARQUBE_MUTATION) {
                transforms.add(AddMethodCallToLiteral.getInstance());
            }
            transforms.add(AnonymousClassWrapper.getInstance());
            transforms.add(CFWrapperWithDoWhile.getInstance());
            transforms.add(CFWrapperWithForTrue.getInstance());
            transforms.add(CFWrapperWithIfTrue.getInstance());
            transforms.add(CFWrapperWithWhileTrue.getInstance());
            transforms.add(EnumClassWrapper.getInstance());
            transforms.add(NestedClassWrapper.getInstance());
//            transforms.add(TransferLocalVarToGlobal.getInstance());
//            transforms.add(TransferLocalVarToStaticGlobal.getInstance());
            transforms.add(AddStaticAssignment.getInstance());
            transforms.add(AddStaticModifier.getInstance());
            if(!PMD_MUTATION) {
                transforms.add(CFWrapperWithIfFalse.getInstance());
                transforms.add(CompoundExpression1.getInstance());
                transforms.add(CompoundExpression2.getInstance());
                transforms.add(CompoundExpression3.getInstance());
                transforms.add(CompoundExpression4.getInstance());
                transforms.add(AddRedundantLiteral.getInstance());
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

    public static void singleLevelExplorer(ArrayDeque<TypeWrapper> wrappers, int currentDepth) {  // Current depth means the depth of variants in wrappers, not the iteration level
        while (!wrappers.isEmpty()) {
            TypeWrapper wrapper = wrappers.pollFirst(); // remove TypeWrapper in currentDepth level
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
                wrappers.addFirst(wrapper);
                break;
            }
        }
    }

}
