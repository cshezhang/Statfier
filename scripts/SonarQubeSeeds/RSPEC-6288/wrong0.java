
KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

KeyGenParameterSpec builder = new KeyGenParameterSpec.Builder("test_secret_key_noncompliant", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT) // Noncompliant
    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
    .build();

keyGenerator.init(builder);
