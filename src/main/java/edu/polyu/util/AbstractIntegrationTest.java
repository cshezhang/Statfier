package edu.polyu.util;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.stream.Collectors;

import edu.umd.cs.findbugs.BugCollection;
import edu.umd.cs.findbugs.BugCollectionBugReporter;
import edu.umd.cs.findbugs.SystemProperties;
import edu.umd.cs.findbugs.internalAnnotations.SlashedClassName;
import edu.umd.cs.findbugs.test.AnalysisRunner;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;

public abstract class AbstractIntegrationTest {

    /**
     * Build path if running command line build
     */
//    private static final String BUILD_CLASSES_CLI = "/target/classes/java/main/";
    private static final String BUILD_CLASSES_CLI = ".";

    /**
     * Build path if running in Eclipse
     */
    private static final String BUILD_CLASSES_ECLIPSE = "/classesEclipse/";

    private BugCollectionBugReporter bugReporter;

    private static File getFindbugsTestCases() {
        final File f = new File(SystemProperties.getProperty("spotbugsTestCases.home", "./spotbugsTestCases"));
        assertTrue("'spotbugsTestCases' directory not found", f.exists());
        assertTrue(f.isDirectory());
        assertTrue(f.canRead());

        return f;
    }

    private static File getFindbugsTestCasesFile(final String path) {
        File f = new File(getFindbugsTestCases(), path);
        if (!f.exists() && path.startsWith(BUILD_CLASSES_CLI)) {
            String replaced = path.replace(BUILD_CLASSES_CLI, BUILD_CLASSES_ECLIPSE);
            replaced = replaced.replace("../java9/", "");
            File f2 = new File(getFindbugsTestCases(), replaced);
            if (f2.exists()) {
                f = f2;
            }
        }
        assertTrue(f.getAbsolutePath() + " not found", f.exists());
        assertTrue(f.getAbsolutePath() + " is not readable", f.canRead());

        return f;
    }

    protected BugCollection getBugCollection() {
        return bugReporter.getBugCollection();
    }

    /**
     * Sets up a FB engine to run on the 'spotbugsTestCases' project. It enables
     * all the available detectors and reports all the bug categories. Uses a
     * low priority threshold.
     */
    protected void performAnalysis(@SlashedClassName final String... analyzeMe) {
        AnalysisRunner runner = new AnalysisRunner();

        final File lib = getFindbugsTestCasesFile("lib");
        for (final File f : lib.listFiles()) {
            final String path = f.getPath();
            if (f.canRead() && path.endsWith(".jar")) {
                runner.addAuxClasspathEntry(f.toPath());
            }
        }

        // TODO : Unwire this once we move bug samples to a proper sourceset
        Path[] paths = Arrays.stream(analyzeMe)
                .map(s -> getFindbugsTestCasesFile(BUILD_CLASSES_CLI + s).toPath())
                .collect(Collectors.toList())
                .toArray(new Path[analyzeMe.length]);
        bugReporter = runner.run(paths);
    }
}