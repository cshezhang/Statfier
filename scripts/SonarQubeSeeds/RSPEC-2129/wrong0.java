
String empty = new String(); // Noncompliant; yields essentially "", so just use that.
String nonempty = new String("Hello world"); // Noncompliant
Double myDouble = new Double(1.1); // Noncompliant; use valueOf
Integer integer = new Integer(1); // Noncompliant
Boolean bool = new Boolean(true); // Noncompliant
BigInteger bigInteger1 = new BigInteger("3"); // Noncompliant
BigInteger bigInteger2 = new BigInteger("9223372036854775807"); // Noncompliant
BigInteger bigInteger3 = new BigInteger("111222333444555666777888999"); // Compliant, greater than Long.MAX_VALUE
