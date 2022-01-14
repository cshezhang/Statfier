
public void printSize(Collection<?> list) {  // Collection can be used instead
    System.out.println(list.size());
}

public static void loop(Iterable<?> list) { // java.lang.Iterable can be used instead
   for (Object o : list) {
     o.toString();
  }
}
