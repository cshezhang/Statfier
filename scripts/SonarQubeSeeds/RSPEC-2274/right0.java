
synchronized (obj) {
  while (!suitableCondition()){
    obj.wait(timeout);
  }
   ... // Perform action appropriate to condition
}
