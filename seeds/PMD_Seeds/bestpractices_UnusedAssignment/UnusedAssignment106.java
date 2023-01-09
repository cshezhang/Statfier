
            import static org.apache.commons.lang3.StringUtils.trimToNull;

            public class DummyService {

                public boolean dummyMethod(final String stringValue, final Set<String> dummySet) {
                    final String trimmedValue = trimToNull(stringValue);
                    return dummySet.stream()
                                   .noneMatch(value -> value.equalsIgnoreCase(trimmedValue));
                }

            }
        