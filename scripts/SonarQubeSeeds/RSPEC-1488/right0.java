
public long computeDurationInMilliseconds() {
  return (((hours * 60) + minutes) * 60 + seconds ) * 1000 ;
}

public void doSomething() {
  throw new RuntimeException();
}
