public class Foo {
  private static org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Foo.class);

  public void bar() {
    StringBuffer sb = new StringBuffer(15);
    sb.append("Hello");
    StringBuffer sb2 = new StringBuffer(15);
    sb2.append("World");
  }

  public void bar2() {
    StringBuilder sb = new StringBuilder(15);
    sb.append("Hello");
    StringBuilder sb2 = new StringBuilder(15);
    sb2.append("World");
  }
}

