
SecureRandom random = new SecureRandom(); // Compliant for security-sensitive use cases
byte bytes[] = new byte[20];
random.nextBytes(bytes);
