package a;

import com.google.common.io.ByteStreams;
import java.io.InputStream;

public record A(long from, long to) {
  public InputStream a() {
    return ByteStreams.limit(null, to - from);
  }
}

