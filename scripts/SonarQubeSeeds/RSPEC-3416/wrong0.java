
public class MyClass {
  private final static Logger LOG = LoggerFactory.getLogger(WrongClass.class);  // Noncompliant; multiple classes using same logger
}
