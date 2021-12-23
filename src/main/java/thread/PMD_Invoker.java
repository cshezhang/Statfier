package thread;

import net.sourceforge.pmd.PMD;

import static edu.polyu.Util.PMDResultsFolder;
import static edu.polyu.Util.sep;

public class PMD_Invoker implements Runnable {

    private String seedFolderPath;
    private String seedFolderName;

    public PMD_Invoker(String seedFolderPath, String seedFolderName) {
        this.seedFolderPath = seedFolderPath;
        this.seedFolderName = seedFolderName;
    }

    // seedFolderPath can be java source file or a folder contains source files
    @Override
    public void run() {
        String[] pmdArgs = {
                "--no-cache",
                "-d", seedFolderPath,
                "-R", "./allRules.xml",
                "-f", "json",
                "-r", PMDResultsFolder.getAbsolutePath() + sep + seedFolderName + "_Result.json"
//                "-cache", "./PMD_Cache.bin"
        };
        PMD.runPmd(pmdArgs);
    }

}
