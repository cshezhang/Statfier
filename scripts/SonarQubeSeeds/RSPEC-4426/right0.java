
KeyPairGenerator keyPairGen6 = KeyPairGenerator.getInstance("RSA");
keyPairGen6.initialize(2048); // Compliant

KeyPairGenerator keyPairGen5 = KeyPairGenerator.getInstance("EC");
ECGenParameterSpec ecSpec10 = new ECGenParameterSpec("secp256r1"); // compliant
keyPairGen5.initialize(ecSpec10);

KeyGenerator keyGen2 = KeyGenerator.getInstance("AES");
keyGen2.init(128); // Compliant
