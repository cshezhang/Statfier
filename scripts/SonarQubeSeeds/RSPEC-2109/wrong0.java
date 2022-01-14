
Method m = String.class.getMethod("getBytes", new Class[] {int.class,
int.class, byte[].class, int.class});
if (m.isAnnotationPresent(Override.class)) {  // Noncompliant; test will always return false, even when @Override is present in the code
