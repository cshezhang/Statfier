
@Override
protected void configure(HttpSecurity http) throws Exception {
  http.sessionManagement()
     .sessionFixation().newSession(); // Compliant: a new session is created without any of the attributes from the old session being copied over

  // or

  http.sessionManagement()
     .sessionFixation().migrateSession(); // Compliant: a new session is created, the old one is invalidated and the attributes from the old session are copied over.
}
