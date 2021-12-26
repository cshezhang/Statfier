
public class Foo {
    @Before("@ResetEsSetup")
    public void setUp() {
        esSetup.execute(EsSetup.deleteAll());
    }
}
        