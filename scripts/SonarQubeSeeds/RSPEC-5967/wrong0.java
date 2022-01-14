
@Test
@RepeatedTest(2) // Noncompliant, this test will be repeated 3 times
void test() { }

@ParameterizedTest
@Test
@MethodSource("methodSource")
void test2(int argument) { } // Noncompliant, this test will fail with ParameterResolutionException
