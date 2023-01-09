
            public class Test {
                public T addS(List<T> args) {
                    T res = zeroS();//<res
                    for (T arg : args) {
                        res = res.add(arg);
                    }

                    return res;
                }
            }
        