

int[] numbers = {1, 2, 3}; // violation
boolean[] bools = {
true,
true,
false // violation
};

String[][] text = {{},{},}; // OK

double[][] decimals = {
{0.5, 2.3, 1.1,}, // OK
{1.7, 1.9, 0.6}, // violation
{0.8, 7.4, 6.5} // violation
}; // violation, previous line misses a comma

char[] chars = {'a', 'b', 'c'  // violation
  };

String[] letters = {
  "a", "b", "c"}; // violation

int[] a1 = new int[]{
  1,
  2
  ,
}; // OK

int[] a2 = new int[]{
  1,
  2
  ,}; // OK
        