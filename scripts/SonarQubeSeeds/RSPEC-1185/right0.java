
@Override
public boolean isLegal(Action action) {         // Compliant - not simply forwarding the call
  return super.isLegal(new Action(/* ... */));
}

@Id
@Override
public int getId() {                            // Compliant - there is annotation different from @Override
  return super.getId();
}
