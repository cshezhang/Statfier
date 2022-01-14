
// Recommended for block ciphers
Cipher c1 = Cipher.getInstance("AES/GCM/NoPadding"); // Compliant

// Recommended for RSA
Cipher c3 = Cipher.getInstance("RSA/None/OAEPWITHSHA-256ANDMGF1PADDING"); // Compliant
// or the ECB mode can be used for RSA when "None" is not available with the security provider used - in that case, ECB will be treated as "None" for RSA.
Cipher c3 = Cipher.getInstance("RSA/ECB/OAEPWITHSHA-256ANDMGF1PADDING"); // Compliant
