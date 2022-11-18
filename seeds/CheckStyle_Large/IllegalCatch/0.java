

try {
    // some code here
} catch (Exception e) { // violation

}

try {
    // some code here
} catch (ArithmeticException e) { // OK

} catch (Exception e) { // violation, catching Exception is illegal
                          and order of catch blocks doesn't matter
}

try {
    // some code here
} catch (ArithmeticException | Exception e) { // violation, catching Exception is illegal

}

try {
    // some code here
} catch (ArithmeticException e) { // OK

}
        