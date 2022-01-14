
Date d = new Date();
d.setDate(25);
d.setYear(2014);
d.setMonth(11);

Calendar c = new Gregorian Calendar(2014, 11, 25);
if (c.get(Calendar.MONTH) == 11) {
  // ...
}
