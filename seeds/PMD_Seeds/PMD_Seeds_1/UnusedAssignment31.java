
class Test{
    public static void main(String[] args){
        int a = 0;
        int i = 0;
        do {
            if (a >= 20) {
                i = 4;
                a *= 5;
                break;
            }

            a = i + 3;
            i += 3;
        } while (i < 30);
   }
}
        