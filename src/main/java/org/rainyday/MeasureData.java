package org.rainyday;

import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.AbstractTypeDeclaration;
import org.eclipse.jdt.core.dom.FieldDeclaration;
import org.eclipse.jdt.core.dom.MethodDeclaration;
import org.rainyday.analysis.TypeWrapper;
import org.rainyday.util.Utility;

import java.util.List;

import static org.rainyday.util.Utility.BASE_SEED_PATH;
import static org.rainyday.util.Utility.readFileByLine;

/**
 * Description:
 * Author: Vanguard
 * Date: 2022/11/15 22:14
 */
public class MeasureData {


    public static void evaluateProgramElements(String seedFolderPath) {
        List<String> seedPaths = Utility.getFilenamesFromFolder(seedFolderPath, true);
        long sumField = 0, sumMethod = 0, sumClass = 0, sumLines = 0;
        int maxCntField = Integer.MIN_VALUE, maxCntMethod = Integer.MIN_VALUE, maxCntClass = Integer.MIN_VALUE, maxLines = Integer.MIN_VALUE;
        int minCntField = Integer.MAX_VALUE, minCntMethod = Integer.MAX_VALUE, minCntClass = Integer.MAX_VALUE, minLines = Integer.MAX_VALUE;
        for(String seedPath : seedPaths) {
            List<String> lines = readFileByLine(seedPath);
            int cntLines = lines.size();
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
    }

    public static void main(String[] args) {
        evaluateProgramElements(BASE_SEED_PATH);
    }

}
