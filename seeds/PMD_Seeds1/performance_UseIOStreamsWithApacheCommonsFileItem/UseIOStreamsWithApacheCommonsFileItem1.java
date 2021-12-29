
public class Foo {

    private org.apache.commons.fileupload.FileItem globalFileItem;

    private byte[] get(final org.apache.commons.fileupload.FileItem fileItem) {
        return fileItem.get(); // bad
    }

    private byte[] get(final org.apache.commons.fileupload.FileItem fileItem) {
        org.apache.commons.fileupload.FileItem sameFileItem = fileItem;
        return sameFileItem.get(); // bad
    }

    private byte[] get() {
        globalFileItem.get(); // bad
    }
}
        