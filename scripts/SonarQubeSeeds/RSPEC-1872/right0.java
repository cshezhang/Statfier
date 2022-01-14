
class Store {

  public boolean hasSellByDate(Object item) {
    if (item instanceof food.Pear) {
      return true;
    }
    return false;
  }

  public boolean isList(Class<T> valueClass) {
    if (valueClass.isAssignableFrom(List.class)) {
      return true;
    }
    return false;
  }
}
