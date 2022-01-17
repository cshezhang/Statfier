

public void myTest() {
    int test1 = 0; // OK
    int test2 = 0x111; // OK
    int test3 = 0X111; // OK, case is ignored
    int test4 = 010; // violation
    long test5 = 0L; // OK
    long test6 = 010L; // violation
}
        