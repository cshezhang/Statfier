package edu.polyu;

import static edu.polyu.Util.*;

public class GuidedRandomTesting {

    public static void main(String[] args) {
        initEnv();
        Schedule tester = Schedule.getInstance();
        String targetSeedPath = null;
        if(PMD_MUTATION) {
            targetSeedPath = PMD_SEED_PATH;
        }
        if(SPOTBUGS_MUTATION) {
            targetSeedPath = SPOTBUGS_SEED_PATH;
        }
        tester.guidedRandomTesting(targetSeedPath);
    }

}
