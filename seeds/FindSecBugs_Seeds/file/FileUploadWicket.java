import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.wicket.util.upload.FileItem;
import org.apache.wicket.util.upload.FileUploadException;
import org.apache.wicket.util.upload.ServletFileUpload;

public class FileUploadWicket {
  public void handleFile(HttpServletRequest req) throws FileUploadException {
    ServletFileUpload upload = new ServletFileUpload();
    List<FileItem> fileItems = upload.parseRequest(req);

    for (FileItem item : fileItems) {
      System.out.println("Saving " + item.getName() + "...");
    }
  }
}
