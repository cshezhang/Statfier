

public record MyRecord1(int x, int y, String str) { // ok, 3 components
   ...
}

public record MyRecord2(int x, int y, String str,
                         Node node, Order order, Data data) { // violation, 6 components
   ...
}

        