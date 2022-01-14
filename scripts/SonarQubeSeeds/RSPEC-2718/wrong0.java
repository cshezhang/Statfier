
public Date trunc(Date date) {
  return DateUtils.truncate(date, Calendar.SECOND);  // Noncompliant
}
