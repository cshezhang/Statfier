public class Foo {
  public void bar() {
    StringBuffer sb = new StringBuffer();
    if (true) {
      sb.append("CCC");
    }
    if (true) {
      sb.append("CCC");
    }
    if (true) {
      sb.append("CCC");
    }
  }

  public void bar2() {
    StringBuilder sb = new StringBuilder();
    if (true) {
      sb.append("CCC");
    }
    if (true) {
      sb.append("CCC");
    }
    if (true) {
      sb.append("CCC");
    }
  }
}

