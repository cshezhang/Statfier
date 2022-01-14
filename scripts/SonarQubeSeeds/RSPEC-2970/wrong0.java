
// Fest
boolean result = performAction();
// let's now check that result value is true
assertThat(result); // Noncompliant; nothing is actually checked, the test passes whether "result" is true or false

// Mockito
List mockedList = Mockito.mock(List.class);
mockedList.add("one");
mockedList.clear();
// let's check that "add" and "clear" methods are actually called
Mockito.verify(mockedList); // Noncompliant; nothing is checked here, oups no call is chained to verify()
