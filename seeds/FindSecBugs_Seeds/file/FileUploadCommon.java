import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class FileUploadCommon {
  public void handleFile(HttpServletRequest req) throws FileUploadException {
    ServletFileUpload upload = new ServletFileUpload(new DiskFileItemFactory());
    List<FileItem> fileItems = upload.parseRequest(req);

    for (FileItem item : fileItems) {
      System.out.println("Saving " + item.getName() + "...");
    }
  }
}
