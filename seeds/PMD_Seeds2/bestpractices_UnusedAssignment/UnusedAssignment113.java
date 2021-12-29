
            package pmdtests;

            import java.util.ArrayList;
            import java.util.List;
            import java.util.stream.Collectors;
            import java.util.stream.IntStream;

            public class TestDu {

                private List<String> list = new ArrayList<>();

                public void run() {
                    String str = Thread.currentThread().getName() + " Element : %d";
                    for (int i = 0; i < 10_000; i++) {
                        list.add(String.format(str, i));
                    }
                }

                public void runAgain() {
                    String str = Thread.currentThread().getName() + " Element : %d";
                    for (int i = 0; i < 10_000; i++)
                        list.add(String.format(str, i));
                }

                public void runOnceMore() {
                    String str = Thread.currentThread().getName() + " Element : %d";
                    list =  IntStream.range(0, 10_000)
                                     .mapToObj(i -> String.format(str, i))
                                     .collect(Collectors.toList());
                }
            }
        