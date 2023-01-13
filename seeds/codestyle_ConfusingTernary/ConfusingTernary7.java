
public class Foo {
    public void foo() {
        if (noSave) {
            //continue on
        } else {
            if (goBack == "Step") {
                //do logic
            } else if (currentStateID != -1 && selectedNextState != -2) {
                //do different logic
            } else {
                //more logic
            }
        }
    }
}
        