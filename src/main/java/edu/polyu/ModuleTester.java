package edu.polyu;

import edu.polyu.analysis.TypeWrapper;
import edu.polyu.util.Utility;
import org.junit.Test;
import org.sonar.wsclient.Host;
import org.sonar.wsclient.Sonar;
import org.sonar.wsclient.connectors.HttpClient4Connector;
import org.sonar.wsclient.services.ManualMeasure;
import org.sonar.wsclient.services.ManualMeasureCreateQuery;
import org.sonar.wsclient.services.ManualMeasureQuery;

import java.util.List;

public class ModuleTester {


    @Test
    public void testSonarQube() {
        String url = "http://localhost:9000";
        String login = "admin";
        String password = "admin";
        Sonar sonar = new Sonar(new HttpClient4Connector(new Host(url, login, password)));

        String projectKey = "sqp_bf8f12a9f55a9393445b65f67bf99cc2c9381b50";
        String manualMetricKey = "burned_budget";

        sonar.create(ManualMeasureCreateQuery.create(projectKey, manualMetricKey).setValue(50.0));

        for (ManualMeasure manualMeasure : sonar.findAll(ManualMeasureQuery.create(projectKey))) {
            System.out.println("Manual measure on project: " + manualMeasure);
        }
    }

    @Test
    public void testDiffIteration() {
        String path1 = "C:\\Users\\austin\\evaluation\\PMD_Large\\results";
        String path2 = "C:\\Users\\austin\\evaluation\\PMD_Large_Iter2\\results";
        List<String> names1 = Utility.getFilenamesFromFolder(path1, false);
        List<String> names2 = Utility.getFilenamesFromFolder(path2, false);
        for(String name : names2) {
            if(!names1.contains(name)) {
                System.out.println(name);
            }
        }
    }

    @Test
    public void testAST() {
        System.out.println(System.getProperty("user.dir"));
    }

}
