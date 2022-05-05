

class Test {
    public static void main(String[] args) {
        int b=10; // violation
        int c = 10; // ok
        b+=10; // violation
        b += 10; // ok
        c*=10; // violation
        c *= 10; // ok
        c-=5; // violation
        c -= 5; // ok
        c/=2; // violation
        c /= 2; // ok
        c%=1; // violation
        c %= 1; // ok
        c>>=1; // violation
        c >>= 1; // ok
        c>>>=1; // violation
        c >>>= 1; // ok
    }
    public void myFunction() {
        c^=1; // violation
        c ^= 1; // ok
        c|=1; // violation
        c |= 1; // ok
        c&=1; // violation
        c &= 1; // ok
        c<<=1; // violation
        c <<= 1; // ok
    }
}
        