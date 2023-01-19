package org.detector;

import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;
import org.detector.util.Invoker;
import org.detector.util.Utility;

import java.util.List;

import static org.detector.util.Utility.GOOGLE_FORMAT_PATH;
import static org.detector.util.Utility.PMD_MUTATION;
import static org.detector.util.Utility.TOOL_PATH;
import static org.detector.util.Utility.sep;

/**
 * Description:
 * Author: Vanguard
 * Date: 2023/1/19 10:17
 */
public class CodeFormatter {

    public static void main(String[] args) {
//        String casePath = "./TestCase/";
        String SEED_PATH = "/Users/austin/projects/SAMutator/seeds/";
        List<String> paths = Utility.getFilenamesFromFolder(SEED_PATH, true);
        int success = 0, failure = 0;
        for(String path : paths) {
            System.out.println("Processing: " + path);
            List<String> lines = Utility.readFileByLine(path);
            StringBuilder builder = new StringBuilder();
            for(String line : lines) {
                builder.append(line);
            }
            String[] invokeCommands = new String[5];
            invokeCommands[0] = "java";
            invokeCommands[1] = "-jar";
            invokeCommands[2] = GOOGLE_FORMAT_PATH;
            invokeCommands[3] = "--replace";
            invokeCommands[4] = path;
            String formattedSource = Invoker.invokeCommandsByZTWithOutput(invokeCommands);
            if(formattedSource != "") {
                success++;
                Utility.writeLinesToFile(path, formattedSource);
            } else {
                failure++;
            }
        }
        System.out.println(success + " " + failure);
    }

}
