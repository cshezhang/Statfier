
public class Foo {

    private org.apache.commons.fileupload.FileItem globalFileItem;

    private String getString(final org.apache.commons.fileupload.FileItem fileItem) {
        return fileItem.getString(); // bad
    }

    private String getString(final org.apache.commons.fileupload.FileItem fileItem) {
        org.apache.commons.fileupload.FileItem sameFileItem = fileItem;
        return fileItem.getString() + sameFileItem.getString(); // bad (2x)
    }

    private String getString() {
        globalFileItem.getString(); // bad
    }
}
        