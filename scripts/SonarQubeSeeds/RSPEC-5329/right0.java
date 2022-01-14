
Arrays.asList(1, 2, 54000).stream().collect(Collectors.toMap(Function.identity(), id -> new ArrayList<>())); // Compliant, explicitly show the usage of "id -> new ArrayList<>()" or "id -> new ArrayList<>(id)"
