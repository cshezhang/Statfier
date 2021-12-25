
public class OuterClass {
    public void method1() {}
    public void method2() {}
    public void method3() {}
    public void method4() {}
    public void method5() {}
    public void method6() {}
    public void method7() {}

    public static class InnerClass extends OuterClass {
        @Override
        public void method1() {}
        @Override
        public void method2() {}
        @Override
        public void method3() {}
        @Override
        public void method4() {}
        @Override
        public void method5() {}
        @Override
        public void method6() {}
        @Override
        public void method7() {}
    }
}
        