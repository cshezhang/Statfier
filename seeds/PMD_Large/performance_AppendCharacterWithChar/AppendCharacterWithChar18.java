
            public class NoCompiledClass {
                public String toString() {
                    StringBuilder sb = new StringBuilder();
                    int start = sb.indexOf(" ");
                    if (start == -1) {
                        sb.append("Bar");
                        sb.append(" "); // warning expected: Avoid appending characters as strings in StringBuffer.append.
                    }
                    return sb.toString();
                }
            }
            