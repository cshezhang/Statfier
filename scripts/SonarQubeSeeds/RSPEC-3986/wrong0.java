
Date date = new SimpleDateFormat("yyyy/MM/dd").parse("2015/12/31");
String result = new SimpleDateFormat("YYYY/MM/dd").format(date);   //Noncompliant; yields '2016/12/31'
result = DateTimeFormatter.ofPattern("YYYY/MM/dd").format(date); //Noncompliant; yields '2016/12/31'
