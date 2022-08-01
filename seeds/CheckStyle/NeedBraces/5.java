

allowedFuture.addCallback(result -> assertEquals("Invalid response",
  EnumSet.of(HttpMethod.GET, HttpMethod.OPTIONS), result), // violation, lambda spans 2 lines
  ex -> fail(ex.getMessage())); // OK

allowedFuture.addCallback(result -> {
  return assertEquals("Invalid response",
    EnumSet.of(HttpMethod.GET, HttpMethod.OPTIONS), result);
  }, // OK
  ex -> fail(ex.getMessage()));
        