package MutatorTestingCases;

/**
 * Description:
 * Author: Austin Zhang
 * Date: 2021/12/20 4:12 下午
 */
public class Case13 {

    public enum EnumWithAnonClass {
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

}
