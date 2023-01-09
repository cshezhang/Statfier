
package de.friedenhagen.pmd_private;

import java.net.URI;

import org.junit.runner.Description;

/**
 * Holder object for the id in the {@link TestLink} annotation. By defining only a private constructor in this abstract
 * class and having both extending classes as inner classes, we assure nobody is able to create other extending classes.
 *
 * @param <T>
 *            of the id, either {@link Long} for internal or {@link String} for external IDs.
 *
 * @author Mirko Friedenhagen
 */
abstract class TestLinkId<T> {

    /** id of the test. */
    private final T id;

    /**
     * Private to make sure we only have {@link ExternalTestLinkId} and {@link InternalTestLinkId} as subclasses.
     *
     * @param id
     *            of the test.
     */
    private TestLinkId(final T id) {
        this.id = id;
    }

    /**
     * @return the id.
     */
    public T getId() {
        return id;
    }

    /**
     * Returns a String representation of the type of the current TestLink ID.
     *
     * @return type of the ID.
     */
    public abstract String getType();

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("%s='%s'", getType(), getId());
    }

    /**
     * An external Testlink ID.
     */
    public static class ExternalTestLinkId extends TestLinkId<String> {

        /**
         * @param id
         *            of the testcase
         */
        public ExternalTestLinkId(String id) {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public String getType() {
            return "external_id";
        }
    }

    /**
     * An internal Testlink ID.
     */
    public static class InternalTestLinkId extends TestLinkId<Long> {

        /**
         * @param id
         *            of the testcase
         */
        public InternalTestLinkId(final Long id) {
            super(id);
        }

        /** {@inheritDoc} */
        @Override
        public String getType() {
            return "id";
        }
    }
}

/**
 * Resolves links to the Testlink instance.
 *
 * @author Mirko Friedenhagen
 */
public class TestLinkUriResolver {

    /** baseUri of the Testlink instance. */
    private final URI baseUri;

    /**
     * The constructor normalizes the URI by adding a trailing slash when it is missing.
     *
     * @param baseUri
     *            without <tt>index.php</tt>, eg <tt>http://testlink.sourceforge.net/demo/</tt>.
     */
    TestLinkUriResolver(URI baseUri) {
        final String asciiUri = baseUri.toASCIIString();
        if (asciiUri.endsWith("/")) {
            this.baseUri = baseUri;
        } else {
            this.baseUri = URI.create(asciiUri + "/");
        }
    }

    /**
     * http://testlink.sourceforge.net/demo/lib/testcases/tcPrint.php?testcase_id=2750.
     *
     * @param internalTestLinkId
     *            id of the testcase
     * @return an URI pointing to the printview of the last version of the testcase description.
     */
    private URI fromTestLinkId(final TestLinkId.InternalTestLinkId internalTestLinkId) {
        return baseUri.resolve(String.format("lib/testcases/tcPrint.php?testcase_id=%s", internalTestLinkId.getId()));
    }

    /**
     * http://testlink.sourceforge.net/demo/lib/testcases/archiveData.php?targetTestCase=SM-1&edit=testcase&allowedit=0.
     *
     * @param externalTestLinkId
     *            id of the testcase
     * @return an URI pointing to the printview of the last version of the testcase description.
     */
    private URI fromTestLinkId(final TestLinkId.ExternalTestLinkId externalTestLinkId) {
        return baseUri
                .resolve(String.format("lib/testcases/archiveData.php?targetTestCase=%s&edit=testcase&allowedit=0",
                        externalTestLinkId.getId()));
    }

    /**
     * Returns a link to the last version of the testcase description.
     *
     * http://testlink.sourceforge.net/demo/lib/testcases/tcPrint.php?testcase_id=2750
     * http://testlink.sourceforge.net/demo/lib/testcases/archiveData.php?targetTestCase=SM-1&edit=testcase&allowedit=0
     *
     * @param testLinkId
     *            of the test case.
     * @return an URI pointing to the last version of the testcase description.
     */
    URI fromTestLinkId(final TestLinkId<?> testLinkId) {
        // As this class is package protected we may safely assume there only two kinds of TestLinkIds.
        if (testLinkId instanceof TestLinkId.InternalTestLinkId) {
            return fromTestLinkId((TestLinkId.InternalTestLinkId) testLinkId);
        } else {
            return fromTestLinkId((TestLinkId.ExternalTestLinkId) testLinkId);
        }
    }
}
        