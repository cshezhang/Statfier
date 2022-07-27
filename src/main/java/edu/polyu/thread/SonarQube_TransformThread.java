package edu.polyu.thread;

import edu.polyu.analysis.TypeWrapper;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.List;

import static edu.polyu.analysis.SelectionAlgorithm.Random_Selection;
import static edu.polyu.analysis.SelectionAlgorithm.Div_Selection;
import static edu.polyu.util.Utility.GUIDED_LOCATION;
import static edu.polyu.util.Utility.NO_SELECTION;
import static edu.polyu.util.Utility.RANDOM_LOCATION;
import static edu.polyu.util.Utility.RANDOM_SELECTION;
import static edu.polyu.util.Utility.SEARCH_DEPTH;
import static edu.polyu.util.Utility.DIV_SELECTION;

public class SonarQube_TransformThread implements Runnable {

    private int currentDepth;
    private ArrayDeque<TypeWrapper> wrappers;

    public SonarQube_TransformThread(List<TypeWrapper> initWrappers) {
        this.currentDepth = 0;
        this.wrappers = new ArrayDeque<>() {
            {
                addAll(initWrappers);
            }
        };
    }

    @Override
    public void run() {
        for (int i = 1; i <= SEARCH_DEPTH; i++) {
            while (!wrappers.isEmpty()) {
                TypeWrapper wrapper = wrappers.pollFirst();
                if (wrapper.depth == currentDepth) {
                    if (!wrapper.isBuggy()) { // Insert to queue only wrapper is not buggy
                        List<TypeWrapper> mutants = new ArrayList<>();
                        if (GUIDED_LOCATION) {
                            mutants = wrapper.TransformByGuidedLocation();
                        } else if (RANDOM_LOCATION) {
                            mutants = wrapper.TransformByRandomLocation();
                        }
                        if(NO_SELECTION) {
                            wrappers.addAll(mutants);
                        }
                        if(RANDOM_SELECTION) {
                            wrappers.addAll(Random_Selection(mutants));
                        }
                        if(DIV_SELECTION) {
                            wrappers.addAll(Div_Selection(mutants));
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
