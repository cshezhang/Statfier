
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/")
public void index(HttpServletResponse res, String value) {
    res.setHeader("X-Data", value);
    Cookie cookie = new Cookie("data", value);
    res.addCookie(cookie);
}
