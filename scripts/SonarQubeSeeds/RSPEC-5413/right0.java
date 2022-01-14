
static void removeFrom(List<String> list) {
    // expected: iterate over all the elements of the list
    for (int i = 0; i < list.size(); i++) {
      if (list.get(i).isEmpty()) {
        // actual: remaining elements are shifted, so the one immediately following will be skipped
        list.remove(i);
        i--;
      }
    }
  }
