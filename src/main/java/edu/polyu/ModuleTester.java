package edu.polyu;

import net.sourceforge.pmd.PMDConfiguration;
import net.sourceforge.pmd.PmdAnalysis;
import org.junit.Test;
import edu.polyu.analysis.TypeWrapper;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class ModuleTester {

    @Test
    public void testASTPrinter() {
        TypeWrapper wrapper = new TypeWrapper("./TestCase/Case1.java", "");
        wrapper.printBasicInfo();
    }

    @Test
    public void testPMD() {
        String projectPath = System.getProperty("user.dir");
        PMDConfiguration config = new PMDConfiguration();
        config.setInputFilePath(Paths.get(projectPath + "/reports/TestCase1.java"));
        List<String> rulePaths = new ArrayList<>() {{add("category/java/bestpractices.xml/UnusedAssignment");}};
        config.setRuleSets(rulePaths);
        config.setIgnoreIncrementalAnalysis(true);
        config.setReportFormat("json");
        config.setReportFile(Paths.get(projectPath + "/reports/test.json"));
        try (PmdAnalysis pmd = PmdAnalysis.create(config)) {
            pmd.performAnalysis();
        }
    }


}

