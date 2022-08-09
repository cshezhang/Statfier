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

    @Test
    public void testEqual() {
        String s1 = "123";
        String s2 = "1";
        String s3 = "23";
        System.out.println(s1.equals(s2 + s3));
        System.out.println(s1 == s2 + s3);
    }

//    @Test
//    public void testSonarQube() {
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
//    }

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
