
class Test{
    public static void main(String[] args){
        int a = 0;
        int i = 0;
        while (true) {
            if (a >= 30) {
                i = a + 1; // unused
                break;
            }
            a = a + 3;
            i = i + 1; // used by itself
        }
    }
}
        