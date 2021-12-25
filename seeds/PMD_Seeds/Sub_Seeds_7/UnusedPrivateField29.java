
public class UnusedPrivateFieldClass
{
    private int m_number; // UnusedPrivateField incorrectly errors

    public void increment()
    {
        m_number++; // Write
    }

    public User getUser()
    {
        return new User(this);
    }

    public static class User
    {
        private UnusedPrivateFieldClass m_bugTest;
        private int m_number;

        private User(UnusedPrivateFieldClass bugTest)
        {
            m_bugTest = bugTest;
            m_number = bugTest.m_number; // Read
        }

        public boolean isValid()
        {
            return m_bugTest.m_number == m_number;
        }
    }
}
        