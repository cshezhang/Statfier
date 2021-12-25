
class PersistentObject {
    public Long getId() { return 1L; }
}

public class Example extends PersistentObject {
    @Override
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public Long getId() { return super.getId(); }
}
        