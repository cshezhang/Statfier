package edu.polyu.thread;

import edu.polyu.analysis.SelectionAlgorithm;
import edu.polyu.analysis.TypeWrapper;
import edu.polyu.util.Utility;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

public class SonarQubeTransformThread implements Runnable {

    private int currentDepth;
    private ArrayDeque<TypeWrapper> wrappers;

    public SonarQubeTransformThread(List<TypeWrapper> initWrappers) {
        this.currentDepth = 0;
        this.wrappers = new ArrayDeque<>() {
            {
                addAll(initWrappers);
            }
        };
    }

    @Override
    public void run() {
        for (int i = 1; i <= Utility.SEARCH_DEPTH; i++) {
            while (!wrappers.isEmpty()) {
                TypeWrapper wrapper = wrappers.pollFirst();
                if (wrapper.depth == currentDepth) {
                    if (!wrapper.isBuggy()) { // Insert to queue only wrapper is not buggy
                        List<TypeWrapper> mutants = new ArrayList<>();
                        if (Utility.GUIDED_LOCATION) {
                            mutants = wrapper.TransformByGuidedLocation();
                        } else if (Utility.RANDOM_LOCATION) {
                            mutants = wrapper.TransformByRandomLocation();
                        }
                        if(Utility.NO_SELECTION) {
                            wrappers.addAll(mutants);
                        }
                        if(Utility.RANDOM_SELECTION) {
                            wrappers.addAll(SelectionAlgorithm.Random_Selection(mutants));
                        }
                        if(Utility.DIV_SELECTION) {
                            wrappers.addAll(SelectionAlgorithm.Div_Selection(mutants));
                        }
                    }
                } else {
                    wrappers.addFirst(wrapper); // The last wrapper in current depth
                    currentDepth += 1;
                    break;
                }
            }
        }
    }

}
