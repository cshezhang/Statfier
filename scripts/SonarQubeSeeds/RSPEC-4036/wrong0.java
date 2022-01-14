
Runtime.getRuntime().exec("make");  // Sensitive
Runtime.getRuntime().exec(new String[]{"make"});  // Sensitive

ProcessBuilder builder = new ProcessBuilder("make");  // Sensitive
builder.command("make");  // Sensitive
