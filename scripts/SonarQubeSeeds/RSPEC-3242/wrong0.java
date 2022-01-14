
public void printSize(ArrayList<Object> list) {  // Collection can be used instead
    System.out.println(list.size());
}

public static void loop(List<Object> list) { // java.lang.Iterable can be used instead
   for (Object o : list) {
     o.toString();
  }
}
