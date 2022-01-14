package edu.polyu;

import static edu.polyu.Util.*;

public class MainExecutionTesting {

    public static void main(String[] args) {
        initEnv();
        Schedule tester = Schedule.getInstance();
        if(PMD_MUTATION) {
            tester.executePMDMutation(PMD_SEED_PATH);
        }
        if(SPOTBUGS_MUTATION) {
            tester.executeSpotBugsMutation(SPOTBUGS_SEED_PATH);
        }

    }

}
