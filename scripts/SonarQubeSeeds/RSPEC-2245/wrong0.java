
Random random = new Random(); // Sensitive use of Random
byte bytes[] = new byte[20];
random.nextBytes(bytes); // Check if bytes is used for hashing, encryption, etc...
