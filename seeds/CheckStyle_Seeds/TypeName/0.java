

public interface FirstName {} // OK
protected class SecondName {} // OK
enum Third_Name {} // violation, name 'Third_Name' must match pattern '^[A-Z][a-zA-Z0-9]*$'
private class FourthName_ {} // violation, name 'FourthName_'
                            // must match pattern '^[A-Z][a-zA-Z0-9]*$'
        