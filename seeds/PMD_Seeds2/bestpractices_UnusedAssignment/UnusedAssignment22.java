
class Test{
    public static void main(String[] args){
        int a = 0; // used by compound below
        int i = 0;
        while (true) {
            if (a >= 30) {
                i += a + 1; // unused by below
                // break;  // no break here
            }
            a = a + 3;
            i = a + 2; // used by above
        }
    }
}
        