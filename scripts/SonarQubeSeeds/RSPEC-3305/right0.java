
@Configuration
public class ​FooConfiguration {

 @Bean
  public ​MyService myService(DataSource dataSource) {
    return new ​MyService(dataSource);
  }
}
