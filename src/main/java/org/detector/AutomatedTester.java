package org.detector;

import org.detector.util.Schedule;

import java.lang.management.ManagementFactory;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.detector.util.Utility.initEnv;
import static org.detector.util.Schedule.writeEvaluationResult;
import static org.detector.util.Utility.CHECKSTYLE_MUTATION;
import static org.detector.util.Utility.INFER_MUTATION;
import static org.detector.util.Utility.PMD_MUTATION;
import static org.detector.util.Utility.SONARQUBE_MUTATION;
import static org.detector.util.Utility.SPOTBUGS_MUTATION;
import static org.detector.util.Utility.sourceSeedPath;

/**
 * Description: Main entry for automated testing
 * Author: RainyD4y
 * Date: 2021-08-08 16:20
 */
public class AutomatedTester {

    public static void main(String[] args) {
        long startTimeStamp = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String sd = sdf.format(new Date(Long.parseLong(String.valueOf(startTimeStamp))));
        System.out.println("Start Time: " + sd);
        String name = ManagementFactory.getRuntimeMXBean().getName();
        String pid = name.split("@")[0];
        System.out.println("Java Program PID: " + pid);
        initEnv();
        Schedule schedule = Schedule.getInstance();
        if (PMD_MUTATION) {
            schedule.executePMDTransform(sourceSeedPath);
        }
        if (SPOTBUGS_MUTATION) {
            schedule.executeSpotBugsTransform(sourceSeedPath);
        }
        if (CHECKSTYLE_MUTATION) {
            schedule.executeCheckStyleTransform(sourceSeedPath);
        }
        if (INFER_MUTATION) {
            schedule.executeInferTransform(sourceSeedPath);
        }
        if (SONARQUBE_MUTATION) {
            schedule.executeSonarQubeTransform(sourceSeedPath);
        }
        writeEvaluationResult();
        long endTimeStamp = System.currentTimeMillis();
        long execTime = endTimeStamp - startTimeStamp;
        long minutes = (execTime / 1000) / 60;
        long seconds = (execTime / 1000) % 60;
        System.out.format("Overall execution time: %d min(s) %d sec(s).\n", minutes, seconds);
        sd = sdf.format(new Date(Long.parseLong(String.valueOf(endTimeStamp))));
        System.out.println("End Time: " + sd);
    }

}

