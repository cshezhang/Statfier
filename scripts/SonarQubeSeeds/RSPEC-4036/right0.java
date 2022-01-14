
Runtime.getRuntime().exec("/usr/bin/make");  // Compliant
Runtime.getRuntime().exec(new String[]{"~/bin/make"});  // Compliant

ProcessBuilder builder = new ProcessBuilder("./bin/make");  // Compliant
builder.command("../bin/make");  // Compliant
builder.command(Arrays.asList("..\bin\make", "-j8")); // Compliant

builder = new ProcessBuilder(Arrays.asList(".\make"));  // Compliant
builder.command(Arrays.asList("C:\bin\make", "-j8"));  // Compliant
builder.command(Arrays.asList("\\SERVER\bin\make"));  // Compliant
