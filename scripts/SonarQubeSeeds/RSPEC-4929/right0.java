
public class MyInputStream extends java.io.InputStream {
  private FileInputStream fin;

  public MyInputStream(File file) throws IOException {
    fin = new FileInputStream(file);
  }

  @Override
  public int read() throws IOException {
    return fin.read();
  }

  @Override
  public int read(byte[] b, int off, int len) throws IOException {
    return fin.read(b, off, len);
  }
}
