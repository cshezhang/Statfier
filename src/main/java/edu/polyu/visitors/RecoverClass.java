/*
 * @Description: This is used to recover SimpleName about the Class.
 * @Author: Austin ZHANG
 * @Date: 2021-11-09 15:01:05
 */
package edu.polyu.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

public class RecoverClass extends ASTVisitor {

    @Override
    public boolean visit(TypeDeclaration clazz) {
        return true;
    }

}

