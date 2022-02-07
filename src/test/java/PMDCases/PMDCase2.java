package PMDCases;

/**
 * Description:
 * Author: Vanguard
 * Date: 2021/10/7 4:32 下午
 */
public enum PMDCase2 {

        Foo {
            // missing
            public String toString() {
                return super.toString();
            }
            // missing
            public String getSomething() {
                return null;
            }
        };

        public Object getSomething() {
            return null;
        }

}
