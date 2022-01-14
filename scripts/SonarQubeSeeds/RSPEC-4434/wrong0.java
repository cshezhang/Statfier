
DirContext ctx = new InitialDirContext();
// ...
ctx.search(query, filter,
        new SearchControls(scope, countLimit, timeLimit, attributes,
            true, // Noncompliant; allows deserialization
            deref));
