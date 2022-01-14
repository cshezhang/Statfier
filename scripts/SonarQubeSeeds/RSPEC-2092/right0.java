
Cookie c = new Cookie(COOKIENAME, sensitivedata);
c.setSecure(true); // Compliant: the sensitive cookie will not be send during an unencrypted HTTP request thanks to the secure flag set to true
