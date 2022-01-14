
str.matches("[0-99]") // Noncompliant, this won't actually match strings with two digits
str.matches("[0-9.-_]") // Noncompliant, .-_ is a range that already contains 0-9 (as well as various other characters such as capital letters)
