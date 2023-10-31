package edu.polyu;

import edu.polyu.util.Invoker;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import edu.polyu.analysis.TypeWrapper;
import edu.polyu.util.Utility;

import java.io.File;
import java.util.List;

import static edu.polyu.util.Utility.CLASS_FOLDER;
import static edu.polyu.util.Utility.DEBUG;
import static edu.polyu.util.Utility.FINDSECBUGS_PATH;
import static edu.polyu.util.Utility.FINDSECBUGS_SEED_PATH;
import static edu.polyu.util.Utility.REPORT_FOLDER;
import static edu.polyu.util.Utility.SEED_PATH;
import static edu.polyu.util.Utility.findSecBugsJarList;
import static edu.polyu.util.Utility.findSecBugsJarStr;
import static edu.polyu.util.Utility.getDirectFilenamesFromFolder;
import static edu.polyu.util.Utility.readFileByLine;
import static edu.polyu.util.Utility.sep;

/**
 * Description:
 * Author: RainyD4y
 * Date: 2022/11/15 22:14
 */
public class MeasureData {

    public static boolean isValidLine(String temp) {
        String line = temp.trim();
        if(line == null || line.isEmpty()) {
            return false;
        }
        if(line.startsWith("//") || line.startsWith("/*") || line.startsWith("*") || line.startsWith("*/")) {
            return false;
        }
        return true;
    }

    public static void evaluateProgramElements(String seedFolderPath) {
        List<String> seedPaths = Utility.getFilenamesFromFolder(seedFolderPath, true);
        long sumField = 0, sumMethod = 0, sumClass = 0, sumLines = 0;
        int maxCntField = Integer.MIN_VALUE, maxCntMethod = Integer.MIN_VALUE, maxCntClass = Integer.MIN_VALUE, maxLines = Integer.MIN_VALUE;
        int minCntField = Integer.MAX_VALUE, minCntMethod = Integer.MAX_VALUE, minCntClass = Integer.MAX_VALUE, minLines = Integer.MAX_VALUE;
        String maxpath = "";
        for(String seedPath : seedPaths) {
            List<String> lines = readFileByLine(seedPath);
            int cntLines = 0;
            for(int i = 0; i < lines.size(); i++) {
                if(isValidLine(lines.get(i))) {
                    cntLines++;
                }
            }
            sumLines += cntLines;
            int cntMethod = 0;
            int cntField = 0;
            TypeWrapper wrapper = new TypeWrapper(seedPath, "null");
            List<AbstractTypeDeclaration> types = wrapper.getCompilationUnit().types();
            int cntClass = types.size();
            for(AbstractTypeDeclaration type : types) {
                List<ASTNode> components = type.bodyDeclarations();
                for(ASTNode component : components) {
                    if(component instanceof FieldDeclaration) {
                        cntField++;
                    }
                    if(component instanceof MethodDeclaration) {
                        cntMethod++;
                    }
                }
            }
            sumField += cntField;
            sumMethod += cntMethod;
            sumClass += cntClass;
            maxCntField = Integer.max(maxCntField, cntField);
            maxCntMethod = Integer.max(maxCntMethod, cntMethod);
            maxCntClass = Integer.max(maxCntClass, cntClass);
            if(maxLines < cntLines) {
                maxpath = seedPath;
            }
            if(cntLines == 0) {
                System.out.println(seedPath);
            }
            maxLines = Integer.max(maxLines, cntLines);
            minCntField = Integer.min(minCntField, cntField);
            minCntMethod = Integer.min(minCntMethod, cntMethod);
            minCntClass = Integer.min(minCntClass, cntClass);
            minLines = Integer.min(minLines, cntLines);
        }
        double avgLines = sumLines / (double) seedPaths.size();
        double avgField = sumField / (double) seedPaths.size();
        double avgMethod = sumMethod / (double) seedPaths.size();
        double avgClass = sumClass / (double) seedPaths.size();
        System.out.println("Max Line Field Method Class: " + maxLines + " " + maxCntField + " " + maxCntMethod + " " + maxCntClass);
        System.out.println("Min Line Field Method Class: " + minLines + " " + minCntField + " " + minCntMethod + " " + minCntClass);
        System.out.println("Avg Line Field Method Class: " + avgLines + " " + avgField + " " + avgMethod + " " + avgClass);
        System.out.println("Path: " + maxpath);
    }

    // Evaluate the successful compilation ratio of FindSecBugs
    public static void evaluateCompilationSuccessRatio() {
        int succ = 0, fail = 0;
        findSecBugsJarStr.append(".:");
        for (int i = findSecBugsJarList.size() - 1; i >= 1; i--) {
            findSecBugsJarStr.append(findSecBugsJarList.get(i) + ":");
        }
        findSecBugsJarStr.append(findSecBugsJarList.get(0));
        System.out.println("FindSecBugs Depdency: " + findSecBugsJarStr);
        List<String> subSeedFolderNameList = getDirectFilenamesFromFolder(FINDSECBUGS_SEED_PATH, false);
        for(int i = 0; i < subSeedFolderNameList.size(); i++) {
            System.out.println("Process: " + i);
            String subSeedFolderName = subSeedFolderNameList.get(i);
            String subSeedFolderPath = FINDSECBUGS_SEED_PATH + sep + subSeedFolderName;
            List<String> seedFileNamesWithSuffix = Utility.getFilenamesFromFolder(subSeedFolderPath, false);
            for(String seedFileNameWithSuffix : seedFileNamesWithSuffix) {
                String seedFileName = seedFileNameWithSuffix.substring(0, seedFileNameWithSuffix.length() - 5);
                // seedFileName is used to specify class folder name
                File classFolder = new File(CLASS_FOLDER.getAbsolutePath()  + sep + seedFileName);
                if(!classFolder.exists()) {
                    classFolder.mkdirs();
                }
                boolean isCompiled = Invoker.compileJavaSourceFile(subSeedFolderPath, seedFileNameWithSuffix, classFolder.getAbsolutePath());
                if(isCompiled) {
                    succ++;
                } else {
                    fail++;
                }
//                String reportPath = REPORT_FOLDER.getAbsolutePath()  + sep + subSeedFolderName + sep + seedFileName + "_Result.xml";
//                if(DEBUG) {
//                    System.out.println("Report: " + reportPath);
//                }
//                String[] invokeCommands = new String[3];
//                invokeCommands[0] = "/bin/bash";
//                invokeCommands[1] = "-c";
//                invokeCommands[2] = FINDSECBUGS_PATH + " -xml -output " + reportPath + " " + classFolder.getAbsolutePath();
//                Invoker.invokeCommandsByZT(invokeCommands);
            }
        }
        System.out.println("Succ: " + succ + " Fail: " + fail);
    }

    public static void main(String[] args) {
//        evaluateProgramElements("/Users/austin/projects/Statfier/seeds");
        evaluateCompilationSuccessRatio();
    }

}
