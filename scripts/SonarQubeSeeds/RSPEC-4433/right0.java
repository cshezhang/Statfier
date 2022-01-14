
// Set up the environment for creating the initial context
Hashtable<String, Object> env = new Hashtable<String, Object>();
env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
env.put(Context.PROVIDER_URL, "ldap://localhost:389/o=JNDITutorial");

// Use simple authentication
env.put(Context.SECURITY_AUTHENTICATION, "simple");
env.put(Context.SECURITY_PRINCIPAL, "cn=S. User, ou=NewHires, o=JNDITutorial");
env.put(Context.SECURITY_CREDENTIALS, getLDAPPassword());

// Create the initial context
DirContext ctx = new InitialDirContext(env);
