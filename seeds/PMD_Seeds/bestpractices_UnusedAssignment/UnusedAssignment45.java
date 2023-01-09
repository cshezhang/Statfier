
class Test{
    public static void main(String[] args){
        int t1 = 0 ;
        int t2 = 0 ;
        // the left assignment reaches the right of the ==
        if (   (t1 = t1 + t2)
            == (t1 = t2 * t1) ); // only this assignment is unused
    }
}
        