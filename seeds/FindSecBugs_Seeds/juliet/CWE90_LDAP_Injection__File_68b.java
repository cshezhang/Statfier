import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.*;
import java.util.Hashtable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CWE90_LDAP_Injection__File_68b {

    public void badSink() throws Throwable {
        String data = CWE90_LDAP_Injection__File_68a.data;
        Hashtable<String, String> environmentHashTable = new Hashtable<String, String>();
        environmentHashTable.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        environmentHashTable.put(Context.PROVIDER_URL, "ldap://localhost:389");
        DirContext directoryContext = null;
        try {
            directoryContext = new InitialDirContext(environmentHashTable);
            /* POTENTIAL FLAW: data concatenated into LDAP search, which could result in LDAP Injection */
            String search = "(cn=" + data + ")";
            NamingEnumeration<SearchResult> answer = directoryContext.search("", search, null);
            while (answer.hasMore()) {
                SearchResult searchResult = answer.next();
                Attributes attributes = searchResult.getAttributes();
                NamingEnumeration<?> allAttributes = attributes.getAll();
                while (allAttributes.hasMore()) {
                    Attribute attribute = (Attribute) allAttributes.next();
                    NamingEnumeration<?> allValues = attribute.getAll();
                    while(allValues.hasMore()) {
                        IO.writeLine(" Value: " + allValues.next().toString());
                    }
                }
            }
        }
        catch (NamingException exceptNaming)
        {
            IO.logger.log(Level.WARNING, "The LDAP service was not found or login failed.", exceptNaming);
        }
        finally
        {
            if (directoryContext != null)
            {
                try
                {
                    directoryContext.close();
                }
                catch (NamingException exceptNaming)
                {
                    IO.logger.log(Level.WARNING, "Error closing DirContext", exceptNaming);
                }
            }
        }

    }

    /* goodG2B() - use goodsource and badsink */
    public void goodG2BSink() throws Throwable
    {
        String data = CWE90_LDAP_Injection__File_68a.data;

        Hashtable<String, String> environmentHashTable = new Hashtable<String, String>();
        environmentHashTable.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory");
        environmentHashTable.put(Context.PROVIDER_URL, "ldap://localhost:389");
        DirContext directoryContext = null;

        try
        {
            directoryContext = new InitialDirContext(environmentHashTable);
            /* POTENTIAL FLAW: data concatenated into LDAP search, which could result in LDAP Injection */
            String search = "(cn=" + data + ")";

            NamingEnumeration<SearchResult> answer = directoryContext.search("", search, null);
            while (answer.hasMore())
            {
                SearchResult searchResult = answer.next();
                Attributes attributes = searchResult.getAttributes();
                NamingEnumeration<?> allAttributes = attributes.getAll();
                while (allAttributes.hasMore())
                {
                    Attribute attribute = (Attribute) allAttributes.next();
                    NamingEnumeration<?> allValues = attribute.getAll();
                    while(allValues.hasMore())
                    {
                        IO.writeLine(" Value: " + allValues.next().toString());
                    }
                }
            }
        }
        catch (NamingException exceptNaming)
        {
            IO.logger.log(Level.WARNING, "The LDAP service was not found or login failed.", exceptNaming);
        }
        finally
        {
            if (directoryContext != null)
            {
                try
                {
                    directoryContext.close();
                }
                catch (NamingException exceptNaming)
                {
                    IO.logger.log(Level.WARNING, "Error closing DirContext", exceptNaming);
                }
            }
        }

    }
}

class CWE90_LDAP_Injection__File_68a {
    public static String data;
}

class IO {
    public static final Logger logger = Logger.getLogger("testcases");

    public static void writeString(String str)
    {
        System.out.print(str);
    }

    public static void writeLine(String line)
    {
        System.out.println(line);
    }

    public static void writeLine(int intNumber)
    {
        writeLine(String.format("%02d", intNumber));
    }

    public static void writeLine(long longNumber)
    {
        writeLine(String.format("%02d", longNumber));
    }

    public static void writeLine(double doubleNumber)
    {
        writeLine(String.format("%02f", doubleNumber));
    }

    public static void writeLine(float floatNumber)
    {
        writeLine(String.format("%02f", floatNumber));
    }

    public static void writeLine(short shortNumber)
    {
        writeLine(String.format("%02d", shortNumber));
    }

    public static void writeLine(byte byteHex)
    {
        writeLine(String.format("%02x", byteHex));
    }


    public static boolean staticReturnsTrueOrFalse()
    {
        return (new java.util.Random()).nextBoolean();
    }
}


