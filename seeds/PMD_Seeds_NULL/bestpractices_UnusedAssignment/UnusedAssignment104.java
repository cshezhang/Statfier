
            class Foo {

                static String replaceBackslash(String str) {
                    int i = 0, len = str.length();
                    char c;
                    StringBuffer b = new StringBuffer();
                    for (i = 0; i < len; i++)
                        if ((c = str.charAt(i)) == '\\')
                            b.append("\\\\");
                        else
                            b.append(c);

                    return b.toString();
                }
            }
        