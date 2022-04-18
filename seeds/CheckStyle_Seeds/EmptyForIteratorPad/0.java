

for (Iterator it = map.entrySet().iterator();  it.hasNext(););  // ok
for (Iterator it = map.entrySet().iterator();  it.hasNext(); ); // violation since whitespace
                                                                //after semicolon

for (Iterator foo = very.long.line.iterator();
     foo.hasNext();
     ); // ok
        