
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

import net.sourceforge.pmd.lang.java.ast.ASTCompilationUnit;
import net.sourceforge.pmd.lang.java.ast.ASTPackageDeclaration;


public class GenericWithOverloadsImpl implements GenericInterfaceWithOverloads<String, Integer> {

    // a bridge method is generated for each of these


    // missing
    public String visit(ASTCompilationUnit node, String data) {
        return null;
    }


    // missing
    public String visit(ASTPackageDeclaration node, String data) {
        return null;
    }
}
        