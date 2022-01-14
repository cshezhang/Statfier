
@RequestMapping(path = "/greeting", method = RequestMethod.GET) // Noncompliant
public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
...
}
