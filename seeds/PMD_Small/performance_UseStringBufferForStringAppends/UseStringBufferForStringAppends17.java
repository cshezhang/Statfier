
import java.util.*;

public class ConcatInLoop {
    private static final String ROLE_PREFIX = "role-";

    public void good1() {
        List<String> functionNames = Arrays.asList(new String[]{"a", "b"});
        for (final String functionName : functionNames) {
            if (true) {
                functionNames.add(ROLE_PREFIX + functionName);
            }
        }
    }

    public static void good2(String propertyFile) {
        String[] properyFilenames = propertyFile.split(",");
        for (String propertyFilename : properyFilenames) {
            if (propertyFilename != null) {
                try {
                    //getResourceAsStream(propertyFilename);
                } catch (Exception e) {
                    logError("Failed to load propertyFile with name " + propertyFilename + ": ", e);
                }
            }
        }
    }

    public static void good3() {
        List<String> linkNames = new ArrayList<String>();
        Map<String, String> messages = new HashMap<String, String>();
        for (String linkName : linkNames) {
            messages.put(linkName + ".url", "url");
            messages.put(linkName + ".description", "desc");
        }
    }

    public static void good4() {
        List<String> linkNames = new ArrayList<String>();
        Map<String, String> messages = new HashMap<String, String>();
        String URL = "", DESCRIPTION = "";
        for (String linkName : linkNames) {
            if (!messages.containsKey(linkName + URL)) {
                messages.put(linkName + URL, "some");
            }
            if (!messages.containsKey(linkName + DESCRIPTION)) {
                messages.put(linkName + DESCRIPTION, "some");
            }
        }
    }

    private static void logError(String text, Exception e) {
    }
}
        