
Cookie c = new Cookie(COOKIENAME, sensitivedata);
c.setHttpOnly(true); // Compliant: this sensitive cookie is protected against theft (HttpOnly=true)
