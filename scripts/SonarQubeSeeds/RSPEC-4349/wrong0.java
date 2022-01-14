
public class MyStream extends OutputStream { // Noncompliant
    private FileOutputStream fout;

    public MyStream(File file) throws IOException {
        fout = new FileOutputStream(file);
    }

    @Override
    public void write(int b) throws IOException {
        fout.write(b);
    }

    @Override
    public void close() throws IOException {
        fout.write("\n\n".getBytes());
        fout.close();
        super.close();
    }
}
