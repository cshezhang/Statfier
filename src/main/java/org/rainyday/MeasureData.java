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
        int maxCntField = Integer.MIN_VALUE, maxCntMethod = Integer.MIN_VALUE;
        int minCntField = Integer.MAX_VALUE, minCntMethod = Integer.MAX_VALUE;
        for(String seedPath : seedPaths) {
            List<String> lines = readFileByLine(seedPath);
            int cntLine = lines.size();
            int cntMethod = 0;
            int cntField = 0;
            TypeWrapper wrapper = new TypeWrapper(seedPath, "null");
            List<AbstractTypeDeclaration> types = wrapper.getCompilationUnit().types();
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
        }
    }

    public static void main(String[] args) {
        evaluateProgramElements(BASE_SEED_PATH);
    }

}
