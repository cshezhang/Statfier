
@RepeatedTest(2)
void test() { }

@ParameterizedTest
@MethodSource("methodSource")
void test2(int argument) { }
