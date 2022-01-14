
public void myMethod() {
  if(something) {
    executeTask();
  } else if (somethingElse) {
    doSomethingElse();
  } else {
     generateError();
  }

  try {
    generateOrder();
  } catch (Exception e) {
    log(e);
  } finally {
    closeConnection();
  }
}
