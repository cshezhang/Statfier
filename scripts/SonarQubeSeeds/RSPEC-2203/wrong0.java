
List<String> bookNames = new ArrayList<>();
books.stream().filter(book -> book.getIsbn().startsWith("0"))
                .map(Book::getTitle)
                .forEach(bookNames::add);  // Noncompliant
