
@Controller
@SessionAttributes("hello")  // Noncompliant; this doesn't get cleaned up
public class HelloWorld {

  @RequestMapping("/greet", method = GET)
  public String greet(String greetee) {

    return "Hello " + greetee;
  }
}
