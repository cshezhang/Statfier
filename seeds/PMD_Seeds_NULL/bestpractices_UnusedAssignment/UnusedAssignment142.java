
            class Test {
                private int getC(int c) { return c + 1; }
                private int use(int cc) { return cc + 2; }
                private String readUTF(int index, final int utfLen, final char[] buf) {
                    int endIndex = index + utfLen;
                    int c;
                    char cc = 0;
                    while (index < endIndex) {
                        c = getC(index++);
                        switch (st) {
                        case 0:
                            if (c < 0xE0 && c > 0xBF) cc = 1;
                            else cc = 0;
                            break;
                        case 1:
                            use(cc); break;
                        case 2:
                            cc = use(cc); break;
                        }
                    }
                    return "";
                }
            }
            