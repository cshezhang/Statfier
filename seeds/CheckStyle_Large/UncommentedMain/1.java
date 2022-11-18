

public class Game {
   public static void main(String... args){}   // violation
}

public class Main {
   public static void main(String[] args){}   // OK
}

public class Launch {
   //public static void main(String[] args){} // OK
}

public class Start {
   public void main(){}                       // OK
}

public record MyRecord1 {
    public void main(){}                      // OK
}

        