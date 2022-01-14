
new DateTimeFormatterBuilder()
      .appendValue(ChronoField.YEAR, 4) // Noncompliant: using week of week-based year with regular year
      .appendLiteral('-')
      .appendValue(WeekFields.ISO.weekOfWeekBasedYear(), 2)
      .toFormatter();

new DateTimeFormatterBuilder()
      .appendValue(ChronoField.YEAR_OF_ERA, 4) // Noncompliant: using week of week-based year with regular year
      .appendLiteral('-')
      .appendValue(WeekFields.ISO.weekOfWeekBasedYear(), 2)
      .toFormatter();

new DateTimeFormatterBuilder()
      .appendValue(WeekFields.ISO.weekBasedYear(), 4) // Noncompliant: using aligned week of year with week-based year
      .appendLiteral('-')
      .appendValue(ChronoField.ALIGNED_WEEK_OF_YEAR, 2)
      .toFormatter();
