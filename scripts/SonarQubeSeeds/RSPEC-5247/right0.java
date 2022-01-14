
Mustache.compiler().compile(template).execute(context); // Compliant, auto-escaping is enabled by default
Mustache.compiler().escapeHTML(true).compile(template).execute(context); // Compliant
