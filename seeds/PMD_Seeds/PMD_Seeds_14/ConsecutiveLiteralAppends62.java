
            public class NoCompiledClass {
                public String toString() {
                    StringBuffer sb = new StringBuffer();
                    sb.append("test "); // warning expected: StringBuffer (or StringBuilder).append is called 3 consecutive times with literals. Use a single append with a single combined String.
                    sb.append("test2 ").append("test3 ");
                    return sb.toString();
                }
            }
            