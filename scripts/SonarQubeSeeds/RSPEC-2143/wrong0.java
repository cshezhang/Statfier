
Date now = new Date();  // Noncompliant
DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
Calendar christmas  = Calendar.getInstance();  // Noncompliant
christmas.setTime(df.parse("25.12.2020"));
