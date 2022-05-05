

abstract class AbstractFirstClass {} // OK
abstract class SecondClass {} // violation, it should match pattern "^Abstract.+$"
class AbstractThirdClass {} // OK, no "abstract" modifier
class FourthClass {} // OK
        