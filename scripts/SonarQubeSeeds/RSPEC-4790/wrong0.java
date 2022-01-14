
MessageDigest md1 = MessageDigest.getInstance("SHA");  // Sensitive:  SHA is not a standard name, for most security providers it's an alias of SHA-1
MessageDigest md2 = MessageDigest.getInstance("SHA1");  // Sensitive
