
public class Foo {
    public void bar() {
        KeyMap keymap = maps.getKeyMap();
        if (obj.getParamTypes().stream().allMatch(keymap::booleanFunc))
        {
            // do something
        }
    }
}
        