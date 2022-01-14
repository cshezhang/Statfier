
public void evaluate(int operator) {
  // Do some computation...
  evaluateAdd();
}

private void evaluateAdd() {
  int a = stack.pop();
  int b = stack.pop();
  int result = a + b;
  stack.push(result);
}
