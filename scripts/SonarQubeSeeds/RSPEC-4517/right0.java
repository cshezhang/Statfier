
@Override
public int read() throws IOException {
  if (pos == buffer.length()) {
    return -1;
  }
  return buffer.getByte(pos++) & 0xFF; // The 0xFF bitmask is applied
}
