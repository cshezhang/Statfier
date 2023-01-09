

public record MyRecord1(int x, int y, String str) { // ok, public record definition allowed 10
   ...
}

private record MyRecord2(int x, int y, String str, Node node) { // violation
   ...                                // private record definition allowed 3 components
}

        