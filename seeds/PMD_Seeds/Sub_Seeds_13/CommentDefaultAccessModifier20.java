
package net.sourceforge.pmd.lang.java.rule.codestyle.commentdefaultaccessmodifier;

import android.support.annotation.VisibleForTesting;

public class CommentDefaultAccessModifier {
    @VisibleForTesting void method() {}

    @OnlyForTesting void method2() {}
}
        