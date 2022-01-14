
@javax.annotation.ParametersAreNonnullByDefault
class A {

  void foo() {
    bar(getValue()); // Noncompliant - method 'bar' do not expect 'null' values as parameter
  }

  void bar(Object o) { // 'o' is by contract expected never to be null
    // ...
  }

  @javax.annotation.CheckForNull
  abstract Object getValue();
}
