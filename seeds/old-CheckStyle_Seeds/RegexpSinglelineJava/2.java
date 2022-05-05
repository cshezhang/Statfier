

FileReader in = new FileReader("path/to/input");
int ch = in.read(); // violation
while(ch != -1) {
  System.out.print((char)ch);
  ch = in.read(); // violation
}

FileWriter out = new FileWriter("path/to/output");
out.write("something"); // violation
        