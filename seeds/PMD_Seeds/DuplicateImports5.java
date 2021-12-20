package iter0;

import iter0.foo.*;
import iter0.foo.System;  //False positive

class Foo {
    System system;  //No, I do not mean java.lang.System
}
        