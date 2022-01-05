package SpotBugsCases;

/*
 * @Description: 
 * @Author: Austin ZHANG
 * @Date: 2021-10-18 21:36:50
 */
public class Case1 {
    
    public Boolean always_null() {
        return null;
    }

    public Boolean sometimes_null(int n) {
        if (n > 3) {
            return new Boolean(true);
        } else if (n < 1) {
            return new Boolean(false);
        } else {
            return null;
        }

    }
    
}