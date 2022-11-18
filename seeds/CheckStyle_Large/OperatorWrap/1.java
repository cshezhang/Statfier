

class Test {
    public static void main(String[] args) {
            int b
                    = 10; // violation, '=' should be on previous line
            int c =
                    10; // ok
            b
                    += 10; // violation, '+=' should be on previous line
            b +=
                    10; // ok
            c
                    *= 10; // violation, '*=' should be on previous line
            c *=
                    10; // ok
            c
                    -= 5; // violation, '-=' should be on previous line
            c -=
                    5; // ok
            c
                    /= 2; // violation, '/=' should be on previous line
            c
                    %= 1; // violation, '%=' should be on previous line
            c
                    >>= 1; // violation, '>>=' should be on previous line
            c
                >>>= 1; // violation, '>>>=' should be on previous line
        }
        public void myFunction() {
            c
                    ^= 1; // violation, '^=' should be on previous line
            c
                    |= 1; // violation, '|=' should be on previous line
            c
                    &=1 ; // violation, '&=' should be on previous line
            c
                    <<= 1; // violation, '<<=' should be on previous line
    }
}
        