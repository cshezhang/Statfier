
package computer;
class Pear extends Laptop { ... }

package food;
class Pear extends Fruit { ... }

class Store {

  public boolean hasSellByDate(Object item) {
    if ("Pear".equals(item.getClass().getSimpleName())) {  // Noncompliant
      return true;  // Results in throwing away week-old computers
    }
    return false;
  }

  public boolean isList(Class<T> valueClass) {
    if (List.class.getName().equals(valueClass.getName())) {  // Noncompliant
      return true;
    }
    return false;
  }
}
