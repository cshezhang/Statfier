
            package net.sourceforge.pmd.lang.java.rule.errorprone.closeresource;
            import lombok.Cleanup;
            import lombok.val;

            public class Mwe2757 {
                private static SomeClass getCreator() {
                    @Cleanup val context = new FakeContext(); // FakeContext is is the package
                    return context.getBean(SomeClass.class);
                }

                public Mwe2757() {

                }
                static class SomeClass {}
            }
            