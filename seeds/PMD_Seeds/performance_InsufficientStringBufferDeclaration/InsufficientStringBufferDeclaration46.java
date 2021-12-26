
            package net.sourceforge.pmd.lang.java.types.testdata;

            public class DummyCompiledClass {
                public String toString() {

                    StringBuffer sb = new StringBuffer();
                    sb.append("test ");
                    sb.append("test2 ").append("test3 ");
                    appendToSpringBuffer(sb, "test4");
                }
            }
        