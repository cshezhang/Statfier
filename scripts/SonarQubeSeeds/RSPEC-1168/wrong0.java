
public static List<Result> getAllResults() {
  return null;                             // Noncompliant
}

public static Result[] getResults() {
  return null;                             // Noncompliant
}

public static Map<String, Object> getValues() {
  return null;                             // Noncompliant
}

public static void main(String[] args) {
  Result[] results = getResults();
  if (results != null) {                   // Nullity test required to prevent NPE
    for (Result result: results) {
      /* ... */
    }
  }

  List<Result> allResults = getAllResults();
  if (allResults != null) {                // Nullity test required to prevent NPE
    for (Result result: allResults) {
      /* ... */
    }
  }

  Map<String, Object> values = getValues();
  if (values != null) {                   // Nullity test required to prevent NPE
    values.forEach((k, v) -> doSomething(k, v));
  }
}
