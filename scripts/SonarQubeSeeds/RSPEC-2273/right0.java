
private void removeElement() {
  synchronized(obj) {
    while (!suitableCondition()){
      obj.wait();
    }
    ... // Perform removal
  }
}
