
List <Object> objs = new ArrayList<Object>();
objs.add("Hello");

objs.add(objs); // Noncompliant; StackOverflowException if objs.hashCode() called
objs.addAll(objs); // Noncompliant; behavior undefined
objs.containsAll(objs); // Noncompliant; always true
objs.removeAll(objs); // Noncompliant; confusing. Use clear() instead
objs.retainAll(objs); // Noncompliant; NOOP
