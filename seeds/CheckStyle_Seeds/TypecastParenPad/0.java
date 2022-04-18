

class Foo {

  float f1 = 3.14f;

  int n = ( int ) f1; // violation, space after left parenthesis and before right parenthesis

  double d = 1.234567;

  float f2 = (float ) d; // violation, space before right parenthesis

  float f3 = (float) d; // OK

  float f4 = ( float) d; // violation, space after left parenthesis

}
        