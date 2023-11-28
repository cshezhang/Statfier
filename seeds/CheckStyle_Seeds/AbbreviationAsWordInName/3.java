public class MyClass { // OK, ignore checking the class name
  int firstNum; // OK, abbreviation "N" is of allowed length 1
  int secondNUm; // OK
  int secondMYNum; // violation, found "MYN" but only
  // 2 consecutive capital letters are allowed
  int thirdNUM; // violation, found "NUM" but it is allowed
  // only 2 consecutive capital letters
  static int fourthNUM; // OK, variables with static modifier
  // would be ignored
  String firstCSV; // OK, CSV abbreviation is allowed
  String firstXML; // violation, XML abbreviation is not allowed
  final int TOTAL = 5; // OK, final is ignored
  static final int LIMIT = 10; // OK, static final is ignored
}

