
for (int i = 0; i < 10; i++) { // noncompliant, loop only executes once
  printf("i is %d", i);
  break;
}
...
for (int i = 0; i < 10; i++) { // noncompliant, loop only executes once
  if (i == x) {
    break;
  } else {
    printf("i is %d", i);
    return;
  }
}
