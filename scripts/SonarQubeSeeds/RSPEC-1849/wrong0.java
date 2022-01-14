
public class FibonacciIterator implements Iterator<Integer>{
...
@Override
public boolean hasNext() {
  if(next() != null) {
    return true;
  }
  return false;
}
...
}
