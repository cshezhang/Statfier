

public int square(int a, int b){
  int square = (a * b); // violation
  return (square);      // violation
}
int sumOfSquares = 0;
for(int i=(0); i<10; i++){          // violation
  int x = (i + 1);                  // violation
  sumOfSquares += (square(x * x));  // violation
}
double num = (10.0); //violation
List<String> list = Arrays.asList("a1", "b1", "c1");
myList.stream()
  .filter((s) -> s.startsWith("c")) // violation
  .forEach(System.out::println);
int a = 10, b = 12, c = 15;
boolean x = true, y = false, z= true;
if ((a >= 0 && b <= 9)            // violation, unnecessary parenthesis
         || (c >= 5 && b <= 5)    // violation, unnecessary parenthesis
         || (c >= 3 && a <= 7)) { // violation, unnecessary parenthesis
    return;
}
if ((-a) != -27 // violation, unnecessary parenthesis
         && b > 5) {
    return;
}
if (x==(a <= 15)) { // ok
    return;
}
if (x==(y == z)) { // ok
    return;
}
        