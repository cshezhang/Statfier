
Cookie c = new Cookie(COOKIENAME, sensitivedata);
c.setHttpOnly(false);  // Sensitive: this sensitive cookie is created with the httponly flag set to false and so it can be stolen easily in case of XSS vulnerability
