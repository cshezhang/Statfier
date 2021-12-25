
import org.apache.commons.fileupload.FileItem;
public class Foo {

    private FileItem globalFileItem;

    private byte[] get(final FileItem fileItem) {
        return fileItem.get(); // bad
    }

    private byte[] get(final FileItem fileItem) {
        FileItem sameFileItem = fileItem;
        return sameFileItem.get(); // bad
    }

    private byte[] get() {
        globalFileItem.get(); // bad
    }
}
        