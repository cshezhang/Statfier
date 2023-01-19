import javax.annotation.concurrent.ThreadSafe;

// TODO fix FN T38248006
@ThreadSafe
public class NonSourceVar {
  private long field;

  public void FN_conditionalOperatorBad(long v) {
    field = field < v ? field : v;
  }
}

