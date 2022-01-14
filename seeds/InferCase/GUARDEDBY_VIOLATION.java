public class MODIFIES_IMMUTABLE {
@Immutable int[] testArray = new int[]{0, 1, 2, 4};
  
  int[] getTestArray() {
    return testArray;
  }                
          
  void mutateArray() {
    int[] array = getTestArray();
    array[2] = 7;
  }
}