package edu.polyu.thread;

import edu.polyu.util.AbstractIntegrationTest;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;
import org.junit.Test;

import static edu.polyu.Invoker.compileJavaSourceFile;
import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class TestSpotBugs extends AbstractIntegrationTest {
    @Test
    public void test() {
        compileJavaSourceFile("./spotbugsTestCases", "Test.java", "./spotbugsTestCases");
        performAnalysis("./spotbugsTestCases/Test.class");
        BugInstanceMatcher matcher = new BugInstanceMatcherBuilder()
                .bugType("BC_IMPOSSIBLE_DOWNCAST_OF_TOARRAY").build();
        System.out.println(getBugCollection());
//        assertThat(getBugCollection(), containsExactly(1, matcher));
    }
}
