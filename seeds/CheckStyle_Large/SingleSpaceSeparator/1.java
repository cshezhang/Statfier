

void fun1() {}  // violation, 2 whitespaces before the comment starts
void fun2() { return; }  /* violation here, 2 whitespaces before the comment starts */

/* violation, 2 whitespaces after the comment ends */  int a;

String s; /* OK, 1 whitespace */

/**
 * This is a Javadoc comment
 */  int b; // violation, 2 whitespaces after the javadoc comment ends

float f1; // OK, 1 whitespace

/**
 * OK, 1 white space after the doc comment ends
 */ float f2;
        