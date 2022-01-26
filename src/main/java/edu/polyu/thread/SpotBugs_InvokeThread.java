package edu.polyu.thread;

import edu.polyu.util.AbstractIntegrationTest;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;

import java.io.File;
import java.util.List;

import static edu.polyu.Invoker.compileJavaSourceFile;
import static edu.polyu.Util.BASE_SEED_PATH;
import static edu.polyu.Util.SpotBugsClassFolder;
import static edu.polyu.Util.SpotBugsPath;
import static edu.polyu.Util.SpotBugsResultFolder;
import static edu.polyu.Util.sep;
import static edu.umd.cs.findbugs.test.CountMatcher.containsExactly;
import static org.hamcrest.MatcherAssert.assertThat;

public class SpotBugs_InvokeThread extends AbstractIntegrationTest implements Runnable {

    private String seedFolderPath;
    private String seedFolderName;
    private List<String> seedFileNamesWithSuffix;

    public SpotBugs_InvokeThread(String seedFolderPath, String seedFolderName, List<String> seedFileNamesWithSuffix) {
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
        this.seedFileNamesWithSuffix = seedFileNamesWithSuffix;  // only name, not path
    }

    @Override
    public void run() {
        for(int i = 0; i < this.seedFileNamesWithSuffix.size(); i++) {
            String seedFileNameWithSuffix = this.seedFileNamesWithSuffix.get(i);
            String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
            // Why needs a name here? -> To specify class folder name
            File classFolder = new File(SpotBugsClassFolder.getAbsolutePath() + sep + seedFileName);
            if(!classFolder.exists()) {
                classFolder.mkdirs();
            }
            compileJavaSourceFile(this.seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
            String configPath = BASE_SEED_PATH + sep + "SpotBugs_Rule_Config" + sep + this.seedFolderName + ".xml";
            String reportPath = SpotBugsResultFolder.getAbsolutePath() + sep + seedFileName + "_Result.xml";
            String[] invokeCmds = {"/bin/bash", "-c",
                    SpotBugsPath + " -textui" + " -include " + configPath + " -xml:withMessages" + " -output " + reportPath + " "
                            + classFolder.getAbsolutePath() + sep + seedFileName + ".class"};
//            invokeCommands(invokeCmds);
            StringBuilder builder = new StringBuilder();
            for(String arg : invokeCmds) {
                builder.append(arg);
            }
            System.out.println(builder);
        }
    }

    public void runMaven() {
        performAnalysis("ghIssues/Issue1759.class");
        BugInstanceMatcher matcher = new BugInstanceMatcherBuilder()
                .bugType("BC_IMPOSSIBLE_DOWNCAST_OF_TOARRAY").build();
        assertThat(getBugCollection(), containsExactly(1, matcher));
    }

}
