
org.junit.Assert.assertEquals(0, runner.exitCode(), "Unexpected exit code");
org.assertj.core.api.Assertions.assertThat(runner.exitCode()).isEqualTo(0);
