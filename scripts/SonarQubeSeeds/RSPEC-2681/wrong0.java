
if (condition)
  firstActionInBlock();
  secondAction();  // Noncompliant; executed unconditionally
thirdAction();

if (condition) firstActionInBlock(); secondAction();  // Noncompliant; secondAction executed unconditionally

if (condition) firstActionInBlock();  // Noncompliant
  secondAction();  // Executed unconditionally

if (condition); secondAction();  // Noncompliant; secondAction executed unconditionally

String str = null;
for (int i = 0; i < array.length; i++)
  str = array[i];
  doTheThing(str);  // Noncompliant; executed only on last array element
