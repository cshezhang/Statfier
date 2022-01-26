


class OuterTest {

  ArrayTest[] arrays;

  void mutate_via_field_bad() {
    ArrayTest atest = arrays[0];
    int[] array = atest.get_testArray();
    atest.mutate_param_ok(array); // ERROR!
  }
}
