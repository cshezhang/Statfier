
// Signing:
Jwts.builder() // Compliant
  .setSubject(USER_LOGIN)
  .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
  .compact();
// Verifying:
Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody(); // Compliant
