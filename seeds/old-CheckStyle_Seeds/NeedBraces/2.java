

if (obj.isValid()) return true;  // OK
if (true) {                      // OK
    return true;
} else                           // OK
    return false;
for (int i = 0; i < 5; i++) {    // OK
    ++count;
}
do                               // OK
   ++count;
while (false);
for (int j = 0; j < 10; j++);                   // violation, empty loop body not allowed
for(int i = 0; i < 10; value.incrementValue()); // violation, empty loop body not allowed
while (counter < 10)                 // OK
   ++count;
while (value.incrementValue() < 5);  // violation, empty loop body not allowed
switch (num) {
  case 1: counter++; break;          // OK
}
while (obj.isValid()) return true;   // OK
do this.notify(); while (o != null); // OK
for (int i = 0; ; ) this.notify();   // OK
        