

class Bar {

  double d1 = 3.14;

  int n = ( int ) d1; // OK

  int m = (int ) d1; // violation, no space after left parenthesis

  double d2 = 9.8;

  int x = (int) d2; // violation, no space after left parenthesis and before right parenthesis

  int y = ( int) d2; // violation, no space before right parenthesis

}
        