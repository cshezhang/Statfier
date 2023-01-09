

for (Iterator it = map.entrySet().iterator();  it.hasNext();); // violation as there is no
                                                               // whitespace after semicolon

for (Iterator it = map.entrySet().iterator();  it.hasNext(); ); // ok

for (Iterator foo = very.long.line.iterator();
     foo.hasNext();
     ); // violation as there  is no whitespace after semicolon
        