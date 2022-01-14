
Optional<Foo> fOpt = doSomething();
synchronized (fOpt) {  // Noncompliant
  // ...
}
