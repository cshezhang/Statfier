

interface FirstInterface<T> {} // OK
interface SecondInterface<t> {} // OK
interface ThirdInterface<type> {} // violation, name 'type' must
                                        // match pattern '^[a-zA-Z]$'
        