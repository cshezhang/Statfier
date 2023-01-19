import codetoanalyze.java.InferBuiltins;

public class Builtins {

  void blockErrorOk() {
    Object x = null;
    InferBuiltins.assume(x != null);
    x.toString();
  }

  void doNotBlockErrorBad(Object x) {
    Object y = null;
    InferBuiltins.assume(x != null);
    y.toString();
  }

  void blockErrorIntAssumeOk(Object x) {
    Object y = null;
    int i = 0;
    InferBuiltins.assume(i != 0);
    y.toString();
  }

  void causeErrorBad(Object x) {
    InferBuiltins.assume(x == null);
    x.toString();
  }
}

