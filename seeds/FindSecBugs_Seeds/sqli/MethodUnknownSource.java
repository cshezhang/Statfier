import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import javax.persistence.Id;
import javax.persistence.Entity;

public abstract class MethodUnknownSource {

    EntityManager em;

    public void getUserByUsername1() {
        String username = unknownSource();

        TypedQuery<UserEntity> q = em.createQuery(
                String.format("select * from Users where name = %s", username),
                UserEntity.class);

        UserEntity res = q.getSingleResult();
    }

    public void getUserByUsername2() {
        String username = unknownEncoder(unknownSource());

        TypedQuery<UserEntity> q = em.createQuery(
                "select * from Users where name = '" + username + "'",
                UserEntity.class);

        UserEntity res = q.getSingleResult();
    }

    public abstract String unknownSource();
    public abstract String unknownEncoder(String value);
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
