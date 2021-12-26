
public class Foo {
   public void methodFlagged(Object[] arrayObj) {
       for(int i=0; i<arrayObj.length; i++) {
           methodFlagged(arrayObj[i]);
       }
   }
   private void methodFlagged(Object a) {
       a.toString();
   }
}
        