
Pattern.compile("[ab]*"); // Character classes don't cause recursion the way that '|' does
Pattern.compile("(?s).*"); // Enabling the (?s) flag makes '.' match line breaks, so '|\n' isn't necessary
Pattern.compile("(ab?)*+"); // Possessive quantifiers don't cause recursion because they disable backtracking
