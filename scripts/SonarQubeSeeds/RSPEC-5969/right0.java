
@Test
void test_requiring_MyClass() {
  MyClass myClass = new MyClassForTest();
  //...
}

class MyClassForTest extends MyClass {

  @Override
  int f() {
    return 1;
  }

  @Override
  int g() {
    return 2;
  }
}
