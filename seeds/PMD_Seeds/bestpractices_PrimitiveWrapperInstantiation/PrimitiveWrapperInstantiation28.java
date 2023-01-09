
public class SomeClass {
      private Boolean bar;

      public void method(String s) {
            this.bar = new Boolean(s);      //violation for the BooleanInstantiation
            this.bar = new Boolean("some arbitrary string is just false"); //violation
            this.bar = Boolean.valueOf(s);  //use this instead of Boolean#new
      }
}
        