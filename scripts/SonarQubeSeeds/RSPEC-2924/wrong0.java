
public class ProjectDefinitionTest {

  @Rule
  public TemporaryFolder temp = new TemporaryFolder();  // Noncompliant

  @Test
  public void shouldSetKey() {
    ProjectDefinition def = ProjectDefinition.create();
    def.setKey("mykey");
    assertThat(def.getKey(), is("mykey"));
  }
}
