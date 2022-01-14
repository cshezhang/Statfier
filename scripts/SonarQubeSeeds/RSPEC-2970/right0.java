
// Fest
boolean result = performAction();
// let's now check that result value is true
assertThat(result).isTrue();

// Mockito
List mockedList = Mockito.mock(List.class);
mockedList.add("one");
mockedList.clear();
// let's check that "add" and "clear" methods are actually called
Mockito.verify(mockedList).add("one");
Mockito.verify(mockedList).clear();
