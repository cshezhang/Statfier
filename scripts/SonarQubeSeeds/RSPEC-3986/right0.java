
Date date = new SimpleDateFormat("yyyy/MM/dd").parse("2015/12/31");
String result = new SimpleDateFormat("yyyy/MM/dd").format(date);   //Yields '2015/12/31' as expected
result = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(date); //Yields '2015/12/31' as expected
