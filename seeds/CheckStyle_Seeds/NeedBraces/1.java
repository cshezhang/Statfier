

if (obj.isValid()) return true; // violation, single-line statements not allowed without braces
if (true) {                     // OK
    return true;
} else                          // violation, single-line statements not allowed without braces
    return false;
for (int i = 0; i < 5; i++) {   // OK
     ++count;
}
do                              // OK
    ++count;
while (false);
for (int j = 0; j < 10; j++);   // OK
for(int i = 0; i < 10; value.incrementValue()); // OK
while (counter < 10)                            // OK
    ++count;
while (value.incrementValue() < 5); // OK
switch (num) {
  case 1: counter++; break;         // OK
}
        