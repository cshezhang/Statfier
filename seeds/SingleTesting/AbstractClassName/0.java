

abstract class AbstractFirstClass {} // OK
abstract class SecondClass {} // violation, it should match pattern "^Abstract.+$"
class AbstractThirdClass {} // violation, must be declared 'abstract'
class FourthClass {} // OK
        