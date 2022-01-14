
  public static List<String> zipSlipCompliant(ZipFile zipFile, String targetDirectory) throws IOException {
    Enumeration<? extends ZipEntry> entries = zipFile.entries();
    List<String> filesContent = new ArrayList<>();

    while (entries.hasMoreElements()) {
      ZipEntry entry = entries.nextElement();
      File file = new File(entry.getName());
      String canonicalDestinationPath = file.getCanonicalPath();

      if (!canonicalDestinationPath.startsWith(targetDirectory)) {
        throw new IOException("Entry is outside of the target directory");
      }

      String content = FileUtils.readFileToString(file, StandardCharsets.UTF_8); // OK
      filesContent.add(content);
    }

    return filesContent;
  }
