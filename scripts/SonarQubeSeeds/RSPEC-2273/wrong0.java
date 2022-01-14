
private void removeElement() {
  while (!suitableCondition()){
    obj.wait();
  }
  ... // Perform removal
}
