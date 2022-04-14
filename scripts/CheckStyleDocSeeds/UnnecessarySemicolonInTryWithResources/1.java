

class A {
    void method() throws IOException {
        try(Reader r1 = new PipedReader();){} // violation
        try(Reader r6 = new PipedReader();
            Reader r7 = new PipedReader(); // violation
        ){}
    }
}
        