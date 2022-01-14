
@Test
public void testDoTheThing(){

  MyClass myClass = new MyClass();
  myClass.doTheThing();

  Thread.sleep(500);  // Noncompliant
  // assertions...
}
