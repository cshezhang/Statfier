
/**
 *
 * UnusedPrivateMethodWithEnum.java
 *
 * Copyright 2014 Expedia, Inc. All rights reserved.
 * EXPEDIA PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

import net.sourceforge.pmd.lang.java.rule.bestpractices.unusedprivatemethod.ClassWithPublicEnum;
import net.sourceforge.pmd.lang.java.rule.bestpractices.unusedprivatemethod.ClassWithPublicEnum.PublicEnum;

/**
 * TODO svenz Describe UnusedPrivateMethodWithEnum
 *
 * @author <a href="mailto:svenz@expedia.com">Sven Zethelius</a>
 *
 */
public class UnusedPrivateMethodWithEnum {
        public void doPublic() {
                ClassWithPublicEnum.PublicEnum value = ClassWithPublicEnum.PublicEnum.values()[0];
                doPrivateWithShortEnum1(value);
                doPrivateWithShortEnum2(value);
        }

        private void doPrivateWithShortEnum1(PublicEnum type) {
                // do something
        }
        private void doPrivateWithShortEnum2(ClassWithPublicEnum.PublicEnum type) {
                // do something
        }
}
        