
public class OnlyOneReturn {
    void foo() {
        Object o = new Object() {
            void method(int i) {
                switch (i) {
                    case 1: return;
                    case 2: return;
                }
                return;
            }
        };
    }
}
        