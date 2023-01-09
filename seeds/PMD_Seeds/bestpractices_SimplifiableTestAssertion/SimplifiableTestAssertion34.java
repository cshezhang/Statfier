
            package com.groupon.mostest.refractor;

            import static org.testng.Assert.assertEquals;

            import org.testng.annotations.Test;

            public class TestWithAssertEquals {

                @Test
                public void testMethodWithBooleanParam() {
                    assertEquals(methodWithBooleanParam(true), "a String value", "they should be equal!");
                }

                public String methodWithBooleanParam(boolean param) {
                    return "a String value";
                }
            }
            