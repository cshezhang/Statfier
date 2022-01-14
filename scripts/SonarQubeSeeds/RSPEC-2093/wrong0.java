
FileReader fr = null;
BufferedReader br = null;
try {
  fr = new FileReader(fileName);
  br = new BufferedReader(fr);
  return br.readLine();
} catch (...) {
} finally {
  if (br != null) {
    try {
      br.close();
    } catch(IOException e){...}
  }
  if (fr != null ) {
    try {
      br.close();
    } catch(IOException e){...}
  }
}
