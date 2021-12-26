
import org.apache.commons.fileupload.FileItem;
public class Foo {

    private FileItem globalFileItem;

    private String getString(final FileItem fileItem) {
        return fileItem.getString(); // bad
    }

    private String getString(final FileItem fileItem) {
        FileItem sameFileItem = fileItem;
        return fileItem.getString() + sameFileItem.getString(); // bad (2x)
    }

    private String getString() {
        globalFileItem.getString(); // bad
    }
}
        