
File f = new File("ZipBomb.zip");
ZipFile zipFile = new ZipFile(f);
Enumeration<? extends ZipEntry> entries = zipFile.entries();

int THRESHOLD_ENTRIES = 10000;
int THRESHOLD_SIZE = 1000000000; // 1 GB
double THRESHOLD_RATIO = 10;
int totalSizeArchive = 0;
int totalEntryArchive = 0;

while(entries.hasMoreElements()) {
  ZipEntry ze = entries.nextElement();
  InputStream in = new BufferedInputStream(zipFile.getInputStream(ze));
  OutputStream out = new BufferedOutputStream(new FileOutputStream("./output_onlyfortesting.txt"));

  totalEntryArchive ++;

  int nBytes = -1;
  byte[] buffer = new byte[2048];
  int totalSizeEntry = 0;

  while((nBytes = in.read(buffer)) > 0) { // Compliant
      out.write(buffer, 0, nBytes);
      totalSizeEntry += nBytes;
      totalSizeArchive += nBytes;

      double compressionRatio = totalSizeEntry / ze.getCompressedSize();
      if(compressionRatio > THRESHOLD_RATIO) {
        // ratio between compressed and uncompressed data is highly suspicious, looks like a Zip Bomb Attack
        break;
      }
  }

  if(totalSizeArchive > THRESHOLD_SIZE) {
      // the uncompressed data size is too much for the application resource capacity
      break;
  }

  if(totalEntryArchive > THRESHOLD_ENTRIES) {
      // too much entries in this archive, can lead to inodes exhaustion of the system
      break;
  }
}
