
public class Demo implements org.hibernate.usertype.UserType {
    @Override
    public boolean equals(Object x, Object y) throws HibernateException {
        return x == null ? y == null : x.equals(y);
    }
}
        