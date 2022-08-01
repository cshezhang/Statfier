
public class Bug1843273 {
    public boolean isTrue(Boolean value) {
       boolean result = true;

       if (value.booleanValue()) {
           return result;
       } else {
           return ! result;
       }
   }
}
        