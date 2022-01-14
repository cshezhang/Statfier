
@Configuration
public class ​FooConfiguration {

  @Autowired private ​DataSource dataSource​;  // Noncompliant

  @Bean
  public ​MyService myService() {
    return new ​MyService(this​.dataSource​);
  }
}
