

@interface anno {
  int value() default 10; // violation
  int[] value2() default {10}; // violation
}
        