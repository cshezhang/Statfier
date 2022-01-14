
public class MyClass {

  public enum COLOR {
    RED, GREEN, BLUE, ORANGE;
  }

  public void mapMood() {
    EnumMap<COLOR, String> moodMap = new EnumMap<> (COLOR.class);
  }
}
