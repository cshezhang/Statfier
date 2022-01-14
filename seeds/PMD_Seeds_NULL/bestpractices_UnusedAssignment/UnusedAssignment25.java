
class Test{
    public static void main(String[] args){
        int a = 0;
        int i = 0; // unused now

        outer:
        while (true) {
            a += 2;

            while (true) {
                if (a >= 30) {
                    i += a + 1; // used by print
                    break outer;
                }
                a = a + 3;
                i = a + 2;  // used by i += a + 1
            }

            i = 2; // used by print
        }

        System.out.println(i); // uses i = i + 1
    }
}
        