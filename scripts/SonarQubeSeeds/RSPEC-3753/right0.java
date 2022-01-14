
@Controller
@SessionAttributes("hello")
public class HelloWorld {

  @RequestMapping("/greet", method = GET)
  public String greet(String greetee) {

    return "Hello " + greetee;
  }

  @RequestMapping("/goodbye", method = POST)
  public String goodbye(SessionStatus status) {
    //...
    status.setComplete();
  }

}
