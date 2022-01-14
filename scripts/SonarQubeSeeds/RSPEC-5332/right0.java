
JSch jsch = new JSch(); // Compliant

if(implicit) {
  // implicit mode is considered deprecated but offer the same security than explicit mode
  FTPSClient ftpsClient = new FTPSClient(true); // Compliant
}
else {
  FTPSClient ftpsClient = new FTPSClient(); // Compliant
}

if(implicit) {
  // implicit mode is considered deprecated but offer the same security than explicit mode
  SMTPSClient smtpsClient = new SMTPSClient(true); // Compliant
}
else {
  SMTPSClient smtpsClient = new SMTPSClient(); // Compliant
  smtpsClient.connect("127.0.0.1", 25);
  if (smtpsClient.execTLS()) {
    // commands
  }
}
