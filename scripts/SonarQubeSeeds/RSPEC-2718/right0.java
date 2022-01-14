
public Date trunc(Date date) {
  Instant instant = date.toInstant();
  ZonedDateTime zonedDateTime = instant.atZone(ZoneId.systemDefault());
  ZonedDateTime truncatedZonedDateTime = zonedDateTime.truncatedTo(ChronoUnit.SECONDS);
  Instant truncatedInstant = truncatedZonedDateTime.toInstant();
  return Date.from(truncatedInstant);
}
