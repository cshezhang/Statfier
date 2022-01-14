
File f = new File("ZipBomb.zip");
ZipFile zipFile = new ZipFile(f);
Enumeration<? extends ZipEntry> entries = zipFile.entries(); // Sensitive

while(entries.hasMoreElements()) {
  ZipEntry ze = entries.nextElement();
  File out = new File("./output_onlyfortesting.txt");
  Files.copy(zipFile.getInputStream(ze), out.toPath(), StandardCopyOption.REPLACE_EXISTING);
}
