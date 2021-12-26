
public class MyClass {
    private static final String MY_STRING = "STRING";

    @Autowired
    private MyPrivate myPrivate;

    @Bean
    public void myPublicBean() {}

    private static void myPrivateStatic() {}
}
        