public class JsonArray implements JsonType {

  public StringBuilder array = new StringBuilder("[");

  public void addStringEntry(String value) {
    if (array.length() != 1) {
      array.append(",");
    }
    JsonUtils.serialize(array, value);
  }
}

