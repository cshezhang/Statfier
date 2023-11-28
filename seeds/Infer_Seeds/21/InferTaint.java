/**
 * WARNING! These methods are for testing the taint analysis only! Don't use them in models or in
 * real code.
 */
public class InferTaint {

  // these are to test whether we can add a taint spec to methods that have an implementation
  public static Object inferSecretSource() {
    Object o = InferUndefined.object_undefined();
    InferBuiltins.assume_allocated(o);
    return o;
  }

  public static void inferSensitiveSink(Object iMightBeTainted) {}

  // these are to test whether we can add a taint spec to undefined methods
  public static native Object inferSecretSourceUndefined();

  public static native void inferSensitiveSinkUndefined(Object iMightBeTainted);
}

