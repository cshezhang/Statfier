
            class DT1 {
                public static final String Q = "q";
                public static final String T = "t";

                public static int convert(String type) {
                    if (type.equals(Q)) { // 6
                        return 1;
                    } else if (type.equals(T)) { // 8
                        return 2;
                    } else {
                        return 3;
                    }
                }
                public static int convert2(String type) {
                    if (Q.equals(type)) { // 15
                        return 1;
                    } else if (type.equals(T)) { // 17
                        return 2;
                    } else {
                        return 3;
                    }
                }
                public static int convert3(String type) {
                    if (type.equals("q")) { // 24
                        return 1;
                    } else if (type.equals("t")) { // 26
                        return 2;
                    } else {
                        return 3;
                    }
                }
            }
        