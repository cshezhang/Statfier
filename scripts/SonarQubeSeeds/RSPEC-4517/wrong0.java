
@Override
public int read() throws IOException {
  if (pos == buffer.length()) {
    return -1;
  }
  return buffer.getByte(pos++); // Noncompliant, a signed byte value is returned
}
