
            import static org.junit.Assert.*;
            import static net.sourceforge.pmd.lang.java.rule.codestyle.UnnecessaryImportTest.*;
            import static org.junit.Assert.assertTrue;
            // this import is needed for disambiguation - as DuplicateImportsTest
            // defines assertTrue with the same signature, too.

            public class DuplicateImports {
                static {
                    assertTrue("", true); // the one from the disambiguation import
                    assertSomething("", true); // from UnnecessaryImportTest.*
                    assertFalse("", true); // from Assert.*
                }
            }
            