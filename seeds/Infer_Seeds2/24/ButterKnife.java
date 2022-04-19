import javax.annotation.Nullable;

@interface InjectView {}

/** Support assignments of null to @InjectView fields, generated by butterknife. */
public class ButterKnife {
  @InjectView String injected;
  String normal = ""; // assign to suppress not initialized warning
  @Nullable String nullable = ""; // assign to suppress not initialized warning

  void f(String nonNullable) {}

  // When dereferencing, injected should behave as not nullable

  void dereferencingInjectedIsOK() {
    int n = injected.length();
  }

  void dereferencingNormalIsOK() {
    int n = normal.length();
  }

  void dereferencingNullableIsBAD() {
    int n = nullable.length();
  }

  // When returning a value, injected should be treated as non nullable

  String convertingToNotNullableForInjectedIsOK() {
    return injected;
  }

  String convertingToNotNullableForNormalIsOK() {
    return normal;
  }

  String convertingToNotNullableForNullableIsBAD() {
    return nullable;
  }

  // When passing to a non nullable param, injected should be treated as non nullable

  void passingToNullableForInjectedIsOK() {
    f(injected);
  }

  void passingToNullableForNormalIsOK() {
    f(normal);
  }

  void passingToNullableForNullableIsBAD() {
    f(nullable);
  }

  // Assigning null to Injected should be allowed (as if it was nullable)
  // (Those assignments are generated by Butterknife framework.)

  void assignNullToInjectedIsOK() {
    injected = null;
  }

  void assignNullToNullableIsOK() {
    nullable = null;
  }

  void assignNullToNormalIsBAD() {
    normal = null;
  }

  class TestNotInitialized {
    @InjectView String notInitializedInjectedIsOK;
    @Nullable String notInitializedNullableIsOK;
    String notInitializedNormalIsBAD;
  }
}
