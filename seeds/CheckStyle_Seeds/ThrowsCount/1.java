

class Test {
    public void myFunction() throws IllegalStateException,
                                ArrayIndexOutOfBoundsException,
                                NullPointerException { // violation, max allowed is 2
        // body
    }

    public void myFunc() throws ArithmeticException,
                                NumberFormatException { // ok
        // body
    }

    private void privateFunc() throws CloneNotSupportedException,
                                ClassNotFoundException,
                                IllegalAccessException,
                                ArithmeticException,
                                ClassCastException { // ok, private methods are ignored
        // body
    }

}
        