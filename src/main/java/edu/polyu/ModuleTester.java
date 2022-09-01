package edu.polyu;

import edu.polyu.util.Utility;
import org.junit.Test;
import java.util.List;

public class ModuleTester {

    int a1 = 0;

    public void test(int a2) {
        int a3 = 0;
        System.out.println(a1);
        System.out.println(a2);
        System.out.println(a3);
    }

    @Test
    public void testSonarQube() {
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

