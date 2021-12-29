
class PersistentObject {
    public Long getId() { return 1L; }
}

public class Example extends PersistentObject {
    @Override
    public Long getId() { return super.getId(); }
}
        