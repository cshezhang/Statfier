class MyComponentSpec {

  static void onCreate(ComponentContext c, @Prop(optional = true) String prop1, @Prop int prop2) {

  }

}

class Main {
    public void func() {
        MyComponent.create(c)
            .prop1("My prop 1")
            .prop2(256)
            .build();
        MyComponent.create(c)
            .prop1("My prop 1")
            .build();

        MyComponent.create(c)
            .prop2(8)
            .build();
    }
}