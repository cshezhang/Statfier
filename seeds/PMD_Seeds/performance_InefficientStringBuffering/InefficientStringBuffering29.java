
package a;

import java.io.InputStream;

import com.google.common.io.ByteStreams;

public record A(long from, long to) {
    public InputStream a() {
        return ByteStreams.limit(null, to - from);
    }
}
        