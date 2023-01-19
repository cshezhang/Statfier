abstract class AbstractFirstClass {} // OK

abstract class SecondClass {} // OK, name validation is ignored

class AbstractThirdClass {} // violation, must be declared as 'abstract'

class FourthClass {} // OK, no "abstract" modifier

