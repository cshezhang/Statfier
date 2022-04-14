

interface FirstInterface<T> {} // OK
interface SecondInterface<t> {} // violation, name 't' must match pattern '^[A-Z]$'
        