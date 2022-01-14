
public void evaluate(int operator) {
  // Do some computation...
  {
    int a = stack.pop();
    int b = stack.pop();
    int result = a + b;
    stack.push(result);
  }
}
