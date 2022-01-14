
@Test
public void testDoTheThing(){

  MyClass myClass = new MyClass();
  myClass.doTheThing();

  await().atMost(2, Duration.SECONDS).until(didTheThing());  // Compliant
  // assertions...
}

private Callable<Boolean> didTheThing() {
  return new Callable<Boolean>() {
    public Boolean call() throws Exception {
      // check the condition that must be fulfilled...
    }
  };
}
