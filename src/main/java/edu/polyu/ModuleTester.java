package edu.polyu;

import edu.polyu.util.Utility;
import org.junit.Test;
import org.sonarqube.ws.Issues;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;
import java.util.ArrayList;
import java.util.List;

public class ModuleTester {

    int a1 = 0;

    public void test(int a2) {
        int a3 = 0;
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(a3);
    }

//    @Test
//    public void testEqual() {
//        String s1 = "123";
//        String s2 = "1";
//        String s3 = "23";
//        System.out.println(s1.equals(s2 + s3));
//        System.out.println(s1 == s2 + s3);
//    }

    @Test
    public void testSonarQubeInvocation() {
//        sonar-scanner \
//        -Dsonar.projectKey=Statfier \
//        -Dsonar.sources=. \
//        -Dsonar.host.url=http://localhost:9000 \
//        -Dsonar.login=sqp_b5cd7ba6cd143a589260158df861fcf43a20f5b9
        String[] invokeCommands = new String[5];

    }

    @Test
    public void testSonarQube() {
//        HttpConnector httpConnector = HttpConnector.newBuilder().url("http://localhost:9000").credentials("admin", "admin").build();
//        SearchWsRequest issueSearchRequest = new SearchWsRequest();
//        issueSearchRequest.setPageSize(1000);
//        issueSearchRequest.setResolved(false);
//        List<String> bugTypesList = new ArrayList<String>();
//        bugTypesList.add("BUG");
//        issueSearchRequest.setTypes(bugTypesList);
//        WsClient wsClient = WsClientFactories.getDefault().newClient(httpConnector);
//        Issues.SearchWsResponse issuesResponse =  wsClient.issues().search(issueSearchRequest);
//        System.out.println(issuesResponse.getIssuesList());
//
//        System.out.println("DONE");
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
