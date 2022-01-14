
DirContext ctx = new InitialDirContext();
// ...
ctx.search(query, filter,
        new SearchControls(scope, countLimit, timeLimit, attributes,
            false, // Compliant
            deref));
