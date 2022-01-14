
@Override
public void doTheThing() {
  // ...
  actuallyDoTheThing();  // Noncompliant
}

@Override
@Transactional
public void actuallyDoTheThing() {
  // ...
}
