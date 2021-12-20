package iter0;

public class Demo implements org.hibernate.usertype.UserType {
    @Override
    public boolean equals(java.lang.Object x, java.lang.Object y) throws HibernateException {
        return x == null ? y == null : x.equals(y);
    }
}
        