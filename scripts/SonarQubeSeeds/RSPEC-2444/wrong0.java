
private static Properties fPreferences = null;

private static Properties getPreferences() {
        if (fPreferences == null) {
            fPreferences = new Properties(); // Noncompliant
            fPreferences.put("loading", "true");
            fPreferences.put("filterstack", "true");
            readPreferences();
        }
        return fPreferences;
    }
}
