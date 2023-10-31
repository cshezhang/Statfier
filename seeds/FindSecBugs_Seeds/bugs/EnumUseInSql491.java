import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.Entity;
import javax.persistence.Id;

public class EnumUseInSql491 {
    EntityManager entityManager;

    private String prepareQuery(SomeEnum someEnum) {
        String sql = " ... some SQL... " + someEnum.name(); // This line causes the problem.
        return sql;
    }

    public void doSQL(SomeEnum value){
        Query q = entityManager.createNativeQuery(prepareQuery(value), UserEntity.class);
    }

    private String prepareQuery2(Enum someEnum) {
        String sql = " ... some SQL... " + someEnum.toString(); // This line causes the problem.
        return sql;
    }

    public void doSQL2(SomeEnum value){
        Query q = entityManager.createNativeQuery(prepareQuery2(value), UserEntity.class);
    }
}


@Entity
class UserEntity {
    @Id
    private Long id;
    private String test;

    public String getTest() {
        return test;
    }

    public void setTest(String test) {
        this.test = test;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}

enum SomeEnum {

    A("a"),

    B("b"),

    C("c"),

    D("d");

    private final String someName;

    SomeEnum(String someName) {
        this.someName = someName;
    }

    public static SomeEnum fromString(String someName) {
        for (SomeEnum v : SomeEnum.values()) {
            if (v.someName.equals(someName)) {
                return v;
            }
        }
        throw new IllegalArgumentException("No enum constant with value " + someName + " found!");
    }

    public String getSomeName() {
        return someName;
    }


    public String toString() {
        return someName;
    }
}

