
new DateTimeFormatterBuilder()
      .appendValue(WeekFields.ISO.weekBasedYear(), 4)
      .appendLiteral('-')
      .appendValue(WeekFields.ISO.weekOfWeekBasedYear(), 2)
      .toFormatter();

new DateTimeFormatterBuilder()
      .appendValue(ChronoField.YEAR, 4)
      .appendLiteral('-')
      .appendValue(ChronoField.ALIGNED_WEEK_OF_YEAR, 2)
      .toFormatter();

new DateTimeFormatterBuilder()
      .appendValue(ChronoField.YEAR_OF_ERA, 4)
      .appendLiteral('-')
      .appendValue(ChronoField.ALIGNED_WEEK_OF_YEAR, 2)
      .toFormatter();
