
import java.util.ArrayList;
import java.util.List;

public class Foo {
    private List<String> fileExtensions = new ArrayList();
    public void good(List<String> fileExtensions) {
        for (String fileExtension : fileExtensions) {
                if (fileExtension.charAt(0) != '.') {
                    fileExtension = "." + fileExtension;
                }
                this.fileExtensions.add(fileExtension);
            }
    }
}
        