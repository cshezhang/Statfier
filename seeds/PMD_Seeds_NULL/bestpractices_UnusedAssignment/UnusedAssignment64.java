
class Foo {

  public Flux<Object> decode() {
    Flux<List<XMLEvent>> splitEvents = splitEvts();

    return map(events -> {
      return unmarshal(events.append(splitEvents));
    });
  }

}
        