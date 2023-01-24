package org.detector;

import org.apache.commons.io.FileUtils;
import org.detector.analysis.TypeWrapper;
import org.detector.util.Invoker;
import org.detector.util.Utility;

import java.io.File;
import java.io.IOException;
import java.util.List;

import static org.detector.util.Utility.GOOGLE_FORMAT_PATH;
import static org.detector.util.Utility.readFile;
import static org.detector.util.Utility.readFileByLine;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2023/1/19 10:17
 */
public class CodeFormatter {

    public static void formatCode() {
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
//        formatCode();
        checkParser();
    }

}
