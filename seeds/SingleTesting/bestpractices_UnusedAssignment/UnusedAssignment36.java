
class Test{
    public static void main(String[] args){
        int a = 0 ;
        int i = 0 ;
        switch(i){
            case 1 : a = 1; // unused
            // break; // no break
            case 2 : a = 2;
            break;
            case 3 : a = 3;
            break;
            default : a = a + 1;
        }

        System.out.println(a);
    }
}
        