
import com.company.CompareToTester;

@Test
public void testDoSomething() {
  MyClass myClass = new MyClass();
  assertNull(myClass.doSomething());  // JUnit assertion
  assertThat(myClass.doSomething()).isNull();  // Fest assertion
}

@Test
public void testDoSomethingElse() {
  MyClass myClass = new MyClass();
  new CompareToTester().compareWith(myClass);  // Compliant - custom assertion method defined as rule parameter
  CompareToTester.compareStatic(myClass);  // Compliant
}
