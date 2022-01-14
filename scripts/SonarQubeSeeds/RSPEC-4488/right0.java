
@GetMapping(path = "/greeting") // Compliant
public Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
...
}
