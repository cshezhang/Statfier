abstract class GeneratorFirstClass {} // OK

abstract class SecondClass {} // violation, must match pattern '^Generator.+$'

class GeneratorThirdClass {} // violation, must be declared 'abstract'

class FourthClass {} // OK, no "abstract" modifier

