
public void myMethod() {
  if(something) {
    executeTask();
  } else if (somethingElse) {          // Noncompliant
    doSomethingElse();
  }
  else {                               // Compliant
     generateError();
  }

  try {
    generateOrder();
  } catch (Exception e) {
    log(e);
  }
  finally {
    closeConnection();
  }
}
