
public String readFile(File f) {
  StringBuilder sb = new StringBuilder();
  try {
    FileReader fileReader = new FileReader(fileName);
    BufferedReader bufferedReader = new BufferedReader(fileReader);

    while((line = bufferedReader.readLine()) != null) {
      //...
  }
  catch (IOException e) {
    logger.LogError(e);
    throw e;
  }
  return sb.toString();
}
