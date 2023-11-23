import java.io.*;
import javax.portlet.GenericPortlet;
import javax.portlet.PortletConfig;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

public class XssPortlet extends GenericPortlet {

  public void init(PortletConfig portletConfig) throws PortletException {
    super.init(portletConfig);
  }

  public void doView(RenderRequest request, RenderResponse response)
      throws PortletException, IOException {
    response.setContentType("text/html");
    PrintWriter writer = response.getWriter();
    String user = request.getParameter("user");
    writer.println("<h1>Hello " + user + "!</h1>");
  }
}
