
@SpringBootApplication(exclude = {
  MultipartAutoConfiguration.class,
  JmxAutoConfiguration.class,
})
public class MyApplication {
...
}
