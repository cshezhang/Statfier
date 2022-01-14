
List<String> bookNames = books.stream().filter(book -> book.getIsbn().startsWith("0"))
                .map(Book::getTitle)
                .collect(Collectors.toList());
