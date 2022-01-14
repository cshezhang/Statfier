
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping(value = "/")
public void index(HttpServletResponse res, String value) {
    res.setHeader("Set-Cookie", value);  // Noncompliant
    Cookie cookie = new Cookie("jsessionid", value);  // Noncompliant
    res.addCookie(cookie);
}
