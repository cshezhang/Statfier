
public long computeDurationInMilliseconds() {
  long duration = (((hours * 60) + minutes) * 60 + seconds ) * 1000 ;
  return duration;
}

public void doSomething() {
  RuntimeException myException = new RuntimeException();
  throw myException;
}
