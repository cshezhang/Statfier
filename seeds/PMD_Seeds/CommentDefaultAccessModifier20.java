
package iter0;

import android.support.annotation.VisibleForTesting;

public class CommentDefaultAccessModifier {
    @VisibleForTesting void method() {}

    @OnlyForTesting void method2() {}
}
        