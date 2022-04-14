

@NotNull private boolean field1; //ok
@Override public int hashCode() { return 1; } //ok
@NotNull //ok
private boolean field2;
@Override //ok
public boolean equals(Object obj) { return true; }
@Mock DataLoader loader; //ok
@SuppressWarnings("deprecation") DataLoader loader; //ok
@SuppressWarnings("deprecation") public int foo() { return 1; } //ok
@NotNull @Mock DataLoader loader; //ok
        