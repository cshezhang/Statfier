
public class MyIterator implements Iterator<String>{
  ...
  public String next(){
    if(!hasNext()){
      throw new NoSuchElementException();
    }
    ...
  }
}
