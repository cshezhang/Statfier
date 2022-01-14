
synchronized (obj) {
  if (!suitableCondition()){
    obj.wait(timeout);   //the thread can wake up even if the condition is still false
  }
   ... // Perform action appropriate to condition
}
