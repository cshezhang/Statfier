
Mustache.compiler().escapeHTML(false).compile(template).execute(context); // Sensitive
Mustache.compiler().withEscaper(Escapers.NONE).compile(template).execute(context); // Sensitive
