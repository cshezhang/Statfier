package edu.polyu;

import edu.polyu.util.Utility;
import org.junit.Test;
import org.sonarqube.ws.Issues;
import org.sonarqube.ws.client.HttpConnector;
import org.sonarqube.ws.client.WsClient;
import org.sonarqube.ws.client.WsClientFactories;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static edu.polyu.util.Invoker.invokeCommandsByZT;

public class ModuleTester {

    int a1 = 0;

    public void test(int a2) {
        int a3 = 0;
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(a3);
    }

    @Test
    public void testSonarQubeInvocation() {
//        String[] invokeCommands = new String[2];
//        invokeCommands[0] = "sonar-scanner";
//        invokeCommands[1] = "-Dproject.settings=" + "./seeds/SonarQube_Test/settings";
////        invokeCommands[1] = "-Dsonar.projectKey=Statfier";
////        invokeCommands[2] = "-Dsonar.sources=" + SONARQUBE_SEED_PATH;
////        invokeCommands[3] = "-Dsonar.host.url=http://localhost:9000";
////        invokeCommands[4] = "-Dsonar.login=sqp_b5cd7ba6cd143a589260158df861fcf43a20f5b9";
////        invokeCommands[5] = "-Dsonar.sonar.java.binaries=" + SONARQUBE_SEED_PATH + File.separator + "dummy-binaries";
////        invokeCommands[6] = "-Dsonar.sonar.java.test.binaries=" + SONARQUBE_SEED_PATH + File.separator + "dummy-binaries";
//        boolean tag = invokeCommandsByZT(invokeCommands, true);
//        if(tag) {
//            System.out.println("Success to execute SonarQube!");
//        } else {
//            System.out.println("Fail to execute SonarQube!");
//        }
//        String link = "curl -u sqp_b5cd7ba6cd143a589260158df861fcf43a20f5b9: http://localhost:9000/api/issues/search?componentKeys=Statfier&facets=types&facetMode=count";  // Url to access
        String link = "curl http://localhost:9000/api/issues/search?componentKeys=Statfier&facets=types&facetMode=count";  // Url to access
        String[] tokens = new String[4];
        tokens[0] = "curl";
        tokens[1] = "-u";
        tokens[2] = "sqp_b5cd7ba6cd143a589260158df861fcf43a20f5b9:";
        tokens[3] = "http://localhost:9000/api/issues/search?componentKeys=Statfier&facets=types&facetMode=count";
        String jsonContent = invokeCommandsByZT(tokens, "json");
        if(jsonContent == null) {
            System.err.println("Fail to get Json content!");
        }
//        try {
//            URL url = new URL(link);
//            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
//            connection.connect();
//            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
//            String line;
//            StringBuilder stringBuilder = new StringBuilder();
//            while ((line = reader.readLine()) != null) {
//                stringBuilder.append(line);
//            }
//            reader.close();
//            connection.disconnect();
//            System.out.println(stringBuilder);
//        } catch (MalformedURLException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
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

