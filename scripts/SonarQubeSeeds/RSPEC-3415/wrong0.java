
org.junit.Assert.assertEquals(runner.exitCode(), 0, "Unexpected exit code");  // Noncompliant; Yields error message like: Expected:<-1>. Actual:<0>.
org.assertj.core.api.Assertions.assertThat(0).isEqualTo(runner.exitCode()); // Noncompliant
