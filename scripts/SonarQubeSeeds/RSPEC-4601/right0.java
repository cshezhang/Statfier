
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests()
      .antMatchers("/resources/**", "/signup", "/about").permitAll() // Compliant
      .antMatchers("/admin/login").permitAll()
      .antMatchers("/admin/**").hasRole("ADMIN") // Compliant
      .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')")
      .antMatchers("/**", "/home").permitAll() // Compliant; "/**" is the last one
      .and().formLogin().loginPage("/login").permitAll().and().logout().permitAll();
  }
