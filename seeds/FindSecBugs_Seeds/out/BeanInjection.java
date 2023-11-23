import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.beanutils.BeanUtilsBean;

public class BeanInjection extends HttpServlet {

  public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
    User user = new User();
    User originUser = new User();
    originUser.setName("test");
    HashMap map = new HashMap();
    Enumeration names = request.getParameterNames();
    while (names.hasMoreElements()) {
      String name = (String) names.nextElement();
      map.put(name, request.getParameterValues(name));
    }
    try {
      BeanUtils.populate(user, map); // BAD

      BeanUtils.copyProperties(user, originUser); // BAD

      org.springframework.beans.BeanUtils.copyProperties(originUser, user); // BAD

      BeanUtilsBean beanUtl = BeanUtilsBean.getInstance();
      beanUtl.populate(user, map); // BAD
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class User implements java.io.Serializable {

    private String name;

    public String getName() {
      return this.name;
    }

    public void setName(String name) {
      this.name = name;
    }
  }
}
