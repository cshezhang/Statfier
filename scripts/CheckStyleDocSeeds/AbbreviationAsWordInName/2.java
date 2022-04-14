

public class MyClass { // OK
  int firstNum; // OK
  int secondNUM; // violation, it allowed only 1 consecutive capital letter
  static int thirdNum; // OK, the static modifier would be checked
  static int fourthNUm; // violation, the static modifier would be checked,
                        // and only 1 consecutive capital letter is allowed
  String firstXML; // OK, XML abbreviation is allowed
  String firstURL; // OK, URL abbreviation is allowed
  final int TOTAL = 5; // OK, final is ignored
  static final int LIMIT = 10; // OK, static final is ignored
}
        