

public interface firstName {} // OK
public class SecondName {} // violation, name 'SecondName'
                          // must match pattern '^[a-z](_?[a-zA-Z0-9]+)*$'
protected class ThirdName {} // OK
private class FourthName {} // OK
        