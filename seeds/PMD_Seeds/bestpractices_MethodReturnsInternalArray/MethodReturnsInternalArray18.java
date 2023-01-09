
            public class MyClass {
                private String[] foobar1 = new String[0];
                private String[] foobar2 = {};
                private static String[] FOO_BAR_3 = new String[0];
                private static String[] FOO_BAR_4 = {};
                public final String[] call1() { return foobar1; }
                public final String[] call2() { return foobar2; }
                public final String[] call3() { return FOO_BAR_3; }
                public final String[] call4() { return FOO_BAR_4; }
            }
            