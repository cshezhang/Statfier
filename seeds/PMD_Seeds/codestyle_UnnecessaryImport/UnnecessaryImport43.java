
            import foo.*;
            import foo.System;  //False positive

            class Foo {
                System system;  //No, I do not mean java.lang.System
            }
            