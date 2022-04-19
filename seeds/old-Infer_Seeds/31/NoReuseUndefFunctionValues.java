



import javax.annotation.Nullable;

public class NoReuseUndefFunctionValues {

  Object mObject1;
  Object mObject2;

  native Object create();

  public NoReuseUndefFunctionValues(@Nullable Object object) {
    if (object != null) {
      this.mObject1 = object;
    } else {
      this.mObject1 = this.create();
    }
    if (object != null) {
      this.mObject2 = object;
    } else {
      this.mObject2 = this.create();
    }
  }
}
