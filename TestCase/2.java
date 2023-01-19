import javax.crypto.spec.SecretKeySpec;

public class Foo {
  void encrypt() {
    String str;
    str = "hard coded key here";
    SecretKeySpec keySpec = new SecretKeySpec(str.getBytes("UTF-8"), "AES");
  }
}

