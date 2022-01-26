

import android.support.annotation.Nullable;

class ERADICATE_RETURN_OVER_ANNOTATED {
  @Nullable
  String m() {
    String s = new String("abc");
    return s;
  }
}