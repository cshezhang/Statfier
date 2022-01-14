
public class TextUtils {
  public static String stripHtml(String source) {
    return source.replaceAll("<[^>]+>", "");
  }
}

public class TextManipulator {

  // ...

  public void cleanText(String source) {
    TextUtils textUtils = new TextUtils(); // Noncompliant

    String stripped = textUtils.stripHtml(source);

    //...
  }
}
