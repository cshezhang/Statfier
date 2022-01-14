void makeAllZero_impure(ArrayList<Foo> list) {
  Iterator<Foo> listIterator = list.iterator();
  while (listIterator.hasNext()) {
    Foo foo = listIterator.next();
    foo.x = 0;
  }
}