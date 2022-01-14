
KeyGenerator keyGenerator = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore");

KeyGenParameterSpec builder = new KeyGenParameterSpec.Builder("test_secret_key", KeyProperties.PURPOSE_ENCRYPT | KeyProperties.PURPOSE_DECRYPT)
    .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
    .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
    .setUserAuthenticationRequired(true)
    .setUserAuthenticationParameters (60, KeyProperties.AUTH_DEVICE_CREDENTIAL)
    .build();

keyGenerator.init(builder)
