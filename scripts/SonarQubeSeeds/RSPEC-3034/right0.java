
  int intFromBuffer() {
    int result = 0;
    for (int i = 0; i < 4; i++) {
      result = (result << 8) | (readByte() & 0xff);
    }
    return result;
  }
