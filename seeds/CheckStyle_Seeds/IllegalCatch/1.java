

try {
    // some code here
} catch (OutOfMemoryError e) { // violation

}

try {
    // some code here
} catch (ArithmeticException e) { // violation

}

try {
    // some code here
} catch (NullPointerException e) { // OK

} catch (OutOfMemoryError e) { // violation

}

try {
    // some code here
} catch (ArithmeticException | Exception e) {  // violation

}

try {
    // some code here
} catch (Exception e) { // OK

}
        