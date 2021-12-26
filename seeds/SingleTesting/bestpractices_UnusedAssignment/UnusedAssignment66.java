
class Foo {

  public Flux<Object> decode() {
    Flux<List<XMLEvent>> splitEvents = splitEvts();

    return map(events -> {
      events = events.normalize();
      return dontUseEvents();
    });
  }

}
        