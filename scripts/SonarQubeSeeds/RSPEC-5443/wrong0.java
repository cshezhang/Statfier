
new File("/tmp/myfile.txt"); // Sensitive
Paths.get("/tmp/myfile.txt"); // Sensitive

java.io.File.createTempFile("prefix", "suffix"); // Sensitive, will be in the default temporary-file directory.
java.nio.file.Files.createTempDirectory("prefix"); // Sensitive, will be in the default temporary-file directory.
