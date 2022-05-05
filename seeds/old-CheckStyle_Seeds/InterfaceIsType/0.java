

public interface Test1 { // violation
    int a = 3;
}

public interface Test2 { // OK

}

public interface Test3 { // OK
    int a = 3;
    void test();
}
        