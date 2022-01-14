
public class TextUtils {
  public static String stripHtml(String source) {
    return source.replaceAll("<[^>]+>", "");
  }
}

public class TextManipulator {

  // ...

  public void cleanText(String source) {
    String stripped = TextUtils.stripHtml(source);

    //...
  }
}
