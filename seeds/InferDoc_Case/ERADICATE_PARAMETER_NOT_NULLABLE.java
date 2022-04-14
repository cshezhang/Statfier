import android.support.annotation.Nullable;

class ERADICATE_PARAMETER_NOT_NULLABLE {
  void m(C x) {
    String s = x.toString();
  }

  void test(@Nullable C x) {
    m(x);
  }
}