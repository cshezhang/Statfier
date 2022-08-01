

int foo()   { // violation, 3 whitespaces
  return  1; // violation, 2 whitespaces
}
int fun1() { // OK, 1 whitespace
  return 3; // OK, 1 whitespace
}
void  fun2() {} // violation, 2 whitespaces
        