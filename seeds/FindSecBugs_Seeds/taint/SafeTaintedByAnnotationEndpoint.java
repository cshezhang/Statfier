import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/testme")
public abstract class SafeTaintedByAnnotationEndpoint {

  @Autowired private SessionFactory sessionFactory;

  public abstract String getUnknownValue();

  // No tainted annotation therefore, the level of confidence should not be rise

  public void noTaintAnnotation(String user) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + user + "'"); // Medium
  }

  public void safeAnnotation1(@ModelAttribute("comment") CommentDto comment) {
    sessionFactory
        .openSession()
        .createQuery("FROM comment WHERE user='" + getUnknownValue() + "'"); // Medium
  }

  // Some tainted annotation are present but they are not place to sink location

  public void safeAnnotation2(@RequestParam("comment") String comment, String input) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + input + "'"); // Medium
  }

  public void safeAnnotation3(String input, @RequestParam("unsafe") String unsafe) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + input + "'"); // Medium
  }

  public void safeAnnotation4(
      String unusedParameter, String input, @RequestParam("unsafe") String unsafe) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + input + "'"); // Medium
  }

  public void safeAnnotation5(
      int unusedParameter1,
      String unusedParameter2,
      String input,
      @RequestParam("unsafe") String unsafe) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + input + "'"); // Medium
  }

  // Makes sure that primitive types that takes two slots are properly consider

  public void safeAnnotation6(
      double unusedParameter1, String input, @RequestParam("unsafe") String unsafe) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + input + "'"); // Medium
  }

  public void safeAnnotation7(
      String input, @RequestParam("unsafe") String unsafe, double unusedParameter1) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + input + "'"); // Medium
  }

  public void safeAnnotation8(
      long unusedParameter1, String input, @RequestParam("unsafe") String unsafe) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + input + "'"); // Medium
  }

  public void safeAnnotation9(
      String input, @RequestParam("unsafe") String unsafe, long unusedParameter1) {
    sessionFactory.openSession().createQuery("FROM comment WHERE user='" + input + "'"); // Medium
  }
}

class CommentDto {
  private String user;
  private String email;
  private String avatarUrl;
  private String content;

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAvatarUrl() {
    return avatarUrl;
  }

  public void setAvatarUrl(String avatarUrl) {
    this.avatarUrl = avatarUrl;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }
}
