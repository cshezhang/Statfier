package org.rainyday;

import org.rainyday.util.Schedule;
import org.rainyday.util.Utility;

import static org.rainyday.util.Utility.sourceSeedPath;

/**
 * Description: Main Process for automatic testing
 * Author: Vanguard
 * Date: 2021-08-08 16:20
 */
public class AutomaticTester {

    public static void main(String[] args) {
        long time1 = System.currentTimeMillis();
        Utility.initEnv();
        Schedule schedule = Schedule.getInstance();
        if (Utility.PMD_MUTATION) {
            schedule.executePMDTransform(sourceSeedPath);
        }
        if (Utility.SPOTBUGS_MUTATION) {
            schedule.executeSpotBugsTransform(sourceSeedPath);
        }
        if (Utility.CHECKSTYLE_MUTATION) {
            schedule.executeCheckStyleTransform(sourceSeedPath);
        }
        if (Utility.INFER_MUTATION) {
            schedule.executeInferTransform(sourceSeedPath);
        }
        if (Utility.SONARQUBE_MUTATION) {
            schedule.executeSonarQubeTransform(sourceSeedPath);
        }
        long OVERALL_EXEC_TIME = System.currentTimeMillis() - time1;
        long minutes = (OVERALL_EXEC_TIME / 1000) / 60;
        long seconds = (OVERALL_EXEC_TIME / 1000) % 60;
        System.out.format("%d min(s) %d sec(s).", minutes, seconds);
    }

}

