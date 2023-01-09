
package sample;

import java.lang.annotation.*;

@interface CustomAnnotation {
    Object object = new Object() {
        @Override
        public String toString() {
            return new String(new StringBuilder("Hello").append(",World"));
        }
    }.toString();
}
        