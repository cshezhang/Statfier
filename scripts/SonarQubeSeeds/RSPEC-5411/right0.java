
Boolean b = getBoolean();
if (Boolean.TRUE.equals(b)) {
  foo();
} else {
  bar();  // will be invoked for both b == false and b == null
}
