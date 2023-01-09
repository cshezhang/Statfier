
class Test{
    public static void main(String[] args){
        int a = 0;
        int i = 0; // unused now

        outer:
        while (true) {
            a += 2;

            while (true) {
                if (a >= 30) {
                    i += a + 1; // unused because of i = a + 2
                    // break outer;
                }
                a = a + 3;
                i = a + 2;  // killed by below
            }

            i = 2; // used by print
        }

        System.out.println(i); // uses i = i + 1
    }
}
        