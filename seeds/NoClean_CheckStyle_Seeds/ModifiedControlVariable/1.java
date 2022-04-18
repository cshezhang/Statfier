

for(int i=0;i < 8;i++) {
  i++; // violation, control variable modified
}
String args1[]={"Coding", "block"};
for (String arg: args1) {
  arg = arg.trim(); // ok
}
        