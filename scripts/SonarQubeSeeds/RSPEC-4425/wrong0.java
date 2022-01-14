
MessageDigest md = MessageDigest.getInstance("SHA-256");
byte[] bytes = md.digest(password.getBytes("UTF-8"));

StringBuilder sb = new StringBuilder();
for (byte b : bytes) {
    sb.append(Integer.toHexString( b & 0xFF )); // Noncompliant
}
