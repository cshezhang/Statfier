
public abstract class ShouldBeAbstract
{
    public native int isUseful();

    int i;

    public int anotherUseful() {
      i = i + 1;
      return i;
    }

    public String anotherUseful(int j) {
      i = j;
      return null;
   }
}
        