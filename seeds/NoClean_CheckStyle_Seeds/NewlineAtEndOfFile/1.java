

// File ending with a new line
public class Test {⤶
⤶
}⤶ // ok
Note: The comment // ok is a virtual, not actually present in the file

// File ending without a new line
public class Test1 {⤶
⤶
}␍⤶ // violation, expected line ending for file is LF(\n), but CRLF(\r\n) is detected
        