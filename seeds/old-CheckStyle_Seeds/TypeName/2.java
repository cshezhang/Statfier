

public interface I_firstName {} // OK
interface SecondName {} // violation, name 'SecondName'
                       // must match pattern '^I_[a-zA-Z0-9]*$'
        