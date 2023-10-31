package edu.polyu;

import org.apache.commons.io.FileUtils;
import edu.polyu.analysis.TypeWrapper;
import edu.polyu.util.Invoker;
import edu.polyu.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.FINDSECBUGS_SEED_PATH;
import static edu.polyu.util.Utility.GOOGLE_FORMAT_PATH;
import static edu.polyu.util.Utility.PROJECT_PATH;
import static edu.polyu.util.Utility.sep;

/**
 * Description: Format the original source code with google-java-format.
 * Author: RainyD4y
 * Date: 2023/1/19 10:17
 */
public class CodeFormatter {

    public static void formatCode(String inputPath) {
        List<String> paths = Utility.getFilenamesFromFolder(inputPath, true);
        int success = 0, failure = 0;
        for (String path : paths) {
            if (DEBUG) {
                System.out.println("Processing: " + path);
            }
            List<String> lines = Utility.readFileByLine(path);
            StringBuilder builder = new StringBuilder();
            for (String line : lines) {
                builder.append(line);
            }
            String[] invokeCommands = new String[5];
            invokeCommands[0] = "java";
            invokeCommands[1] = "-jar";
            invokeCommands[2] = GOOGLE_FORMAT_PATH;
            invokeCommands[3] = "--replace";
            invokeCommands[4] = path;
            String formattedSource = Invoker.invokeCommandsByZTWithOutput(invokeCommands);
            if (formattedSource.equals("") || (formattedSource.length() == 0 && builder.length() > 0)) {
                System.out.println("Failure Path: " + path);
                failure++;
            } else {
                success++;
                Utility.writeLinesToFile(path, formattedSource);
            }
        }
        System.out.println("Success Number: " + success + "  Failure Number: " + failure);
    }

    public static void checkParser() {
        try {
            TypeWrapper wrapper = new TypeWrapper("./TestCase/Case1.java", "TestCase");
            String code = wrapper.getCode();
            String content = FileUtils.readFileToString(new File("./TestCase/Case1.java"), "UTF-8");
            System.out.println(code.hashCode());
            System.out.println(content.hashCode());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        formatCode(FINDSECBUGS_SEED_PATH);
//        checkParser();
    }

}
