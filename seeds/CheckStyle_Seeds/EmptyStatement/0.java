

public void foo() {
  int i = 5;
  if (i > 3); // violation, ";" right after if statement
    i++;
  for (i = 0; i < 5; i++); // violation
    i++;
  while (i > 10) // OK
    i++;
}
        