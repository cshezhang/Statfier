
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.csrf().disable(); // Sensitive: csrf protection is entirely disabled
   // or
    http.csrf().ignoringAntMatchers("/route/"); // Sensitive: csrf protection is disabled for specific routes
  }
}
