
void foo() {
  outer: //label is not used.
  for(int i = 0; i<10; i++) {
    break;
  }
}
