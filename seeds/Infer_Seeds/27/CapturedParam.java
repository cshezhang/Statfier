



import javax.annotation.Nullable;

/** Nullability checks for captured params */
public class CapturedParam {

  void dereferencingNullableIsBAD(@Nullable Object parameter) {
    parameter.toString();
  }

  void dereferencingCapturedNullableShouldBeBAD_FIXME(@Nullable Object parameter) {
    Object object =
        new Object() {
          void foo() {
            // Should be disallowed, but it is not the case
            // TODO(T53473076) fix the FN.
            parameter.toString();
          }
        };
  }
}
