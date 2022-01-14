
"start123endstart456".replaceAll("start\\w*?(end)?", "x"); // Noncompliant. In contrast to what one would expect, the result is not "xx".
str.matches("\\d*?"); // Noncompliant. Matches the same as "\d*", but will backtrack in every position.
