
@Autowired
public void configureGlobal(AuthenticationManagerBuilder auth, DataSource dataSource) throws Exception {
  auth.jdbcAuthentication()
    .dataSource(dataSource)
    .usersByUsernameQuery("Select * from users where username=?")
    .passwordEncoder(new BCryptPasswordEncoder());

  // or
  auth.userDetailsService(null).passwordEncoder(new BCryptPasswordEncoder());
}
