

class MyClass {
    MyClass(Object o1){
        if (o1 instanceof String s) { // violation, name 's' must
        // match pattern '^[a-z][_a-zA-Z0-9]{2,}$'
        }
        if (o1 instanceof Integer num) { // OK
        }
    }
}
        