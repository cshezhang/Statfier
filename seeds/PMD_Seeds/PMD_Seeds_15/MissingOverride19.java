
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

import net.sourceforge.pmd.lang.ast.Node;
import net.sourceforge.pmd.lang.java.ast.ASTType;
import net.sourceforge.pmd.lang.java.ast.AbstractJavaTypeNode;
import net.sourceforge.pmd.lang.java.ast.JavaNode;

public abstract class HierarchyWithSeveralBridges<T extends Node> {

    abstract void foo(T node);

    public static abstract class SubclassOne<T extends JavaNode> extends HierarchyWithSeveralBridges<T> {

        // this one could be resolved
        // @Override
        // abstract void foo(T node);

    }

    public static abstract class SubclassTwo<T extends AbstractJavaTypeNode> extends SubclassOne<T> {

    }


    public static class Concrete extends SubclassTwo<ASTType> {

        // bridges: foo(AbstractJavaTypeNode), foo(JavaNode), foo(Node)

        // missing
        void foo(ASTType node) {

        }
    }
}
        