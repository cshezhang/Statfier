
float a = 16777216.0f;
float b = 1.0f;
float c = a + b; // Noncompliant; yields 1.6777216E7 not 1.6777217E7

double d = a + b; // Noncompliant; addition is still between 2 floats
