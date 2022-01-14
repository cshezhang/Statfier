
float myNumber = 3.146;
if ( myNumber == 3.146f ) { //Noncompliant. Because of floating point imprecision, this will be false
  // ...
}
if ( myNumber != 3.146f ) { //Noncompliant. Because of floating point imprecision, this will be true
  // ...
}

if (myNumber < 4 || myNumber > 4) { // Noncompliant; indirect inequality test
  // ...
}

float zeroFloat = 0.0f;
if (zeroFloat == 0) {  // Noncompliant. Computations may end up with a value close but not equal to zero.
}
