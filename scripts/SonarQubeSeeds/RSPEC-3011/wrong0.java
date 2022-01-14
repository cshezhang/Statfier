
public void makeItPublic(String methodName) throws NoSuchMethodException {

  this.getClass().getMethod(methodName).setAccessible(true); // Noncompliant
}

public void setItAnyway(String fieldName, int value) {
  this.getClass().getDeclaredField(fieldName).setInt(this, value); // Noncompliant; bypasses controls in setter
}
