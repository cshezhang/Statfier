
import java.io.IOException;

public class Bug {
    void test() throws IOException {
        try {
            // do something
        } catch (final IOException e) {
            throw uncheckedException(ErrorCodeCommon.DIRECTORY_NOT_FOUND)
                    .withField("dirname", dirname)
                    .causedBy(e)
                    .build();
        }
    }
}
        