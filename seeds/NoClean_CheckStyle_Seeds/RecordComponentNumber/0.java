

public record MyRecord1(int x, int y) { // ok, 2 components
   ...
}

record MyRecord2(int x, int y, String str,
                         Node node, Order order, Data data
                         String location, Date date, Image image) { // violation, 9 components
   ...
}

        