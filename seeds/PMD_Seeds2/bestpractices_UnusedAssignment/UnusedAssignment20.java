
class Test{
    public static void main(String[] args){
        int a = 0;
        int i = 0; // unused
        while (a < 30) {
            a = a + 3;
            i = 5; // unused (kills itself)
        }
    }
}
        