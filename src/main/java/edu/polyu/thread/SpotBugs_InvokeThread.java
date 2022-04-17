package edu.polyu.thread;

import edu.polyu.util.AbstractIntegrationTest;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcher;
import edu.umd.cs.findbugs.test.matcher.BugInstanceMatcherBuilder;
import org.junit.Test;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Invoker.compileJavaSourceFile;
import static edu.polyu.util.Invoker.invokeCommands;
import static edu.polyu.util.Invoker.invokeCommandsByZT;
import static edu.polyu.util.Util.BASE_SEED_PATH;
import static edu.polyu.util.Util.SpotBugsClassFolder;
import static edu.polyu.util.Util.SpotBugsPath;
import static edu.polyu.util.Util.SpotBugsResultFolder;
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
            File classFolder = new File(SpotBugsClassFolder.getAbsolutePath()  + File.separator + seedFileName);
            if(!classFolder.exists()) {
                classFolder.mkdirs();
            }
            compileJavaSourceFile(this.seedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
            String configPath = BASE_SEED_PATH  + File.separator + "SpotBugs_Rule_Config"  + File.separator + this.seedFolderName + ".xml";
            String reportPath = SpotBugsResultFolder.getAbsolutePath()  + File.separator + this.seedFolderName + File.separator + seedFileName + "_Result.xml";
//            String[] invokeCmds = {"/bin/bash", "-c",
            String[] invokeCmds = {"cmd.exe", "/c",
                    SpotBugsPath + " -textui"
//                            + " -include " + configPath
                            + " -xml:withMessages" + " -output " + reportPath + " "
                            + classFolder.getAbsolutePath()  + File.separator + seedFileName + ".class"};
//            invokeCommands(invokeCmds);
            invokeCommandsByZT(invokeCmds);
        }
    }

    @Test
    public void runMaven() {
        performAnalysis("ghIssues/Issue1759.class");
        BugInstanceMatcher matcher = new BugInstanceMatcherBuilder()
                .bugType("BC_IMPOSSIBLE_DOWNCAST_OF_TOARRAY").build();
        assertThat(getBugCollection(), containsExactly(1, matcher));
    }

}
