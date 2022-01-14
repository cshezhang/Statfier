
public static List<Result> getAllResults() {
  return Collections.emptyList();          // Compliant
}

public static Result[] getResults() {
  return new Result[0];                    // Compliant
}

public static Map<String, Object> getValues() {
  return Collections.emptyMap();           // Compliant
}

public static void main(String[] args) {
  for (Result result: getAllResults()) {
    /* ... */
  }

  for (Result result: getResults()) {
    /* ... */
  }

  getValues().forEach((k, v) -> doSomething(k, v));
}
