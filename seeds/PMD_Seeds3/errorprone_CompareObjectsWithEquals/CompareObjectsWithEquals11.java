
public class SonOfSomeClass extends SomeClass {
    protected javax.swing.JCheckBox checkBox;
    public class SomeEmbeddedClass {
        public boolean someNotWorkingMethod(boolean valid){
    // This line presents a CompareObjectsWithEquals violation
            valid |= SonOfSomeClass.this.object.isConfigurationEnabled() != SonOfSomeClass.this.checkBox.isSelected();
            return valid;
        }
        public boolean someWorkingMethod(boolean valid){
    // This line does not present any violation
            valid |= (SonOfSomeClass.this.object.isConfigurationEnabled()) != SonOfSomeClass.this.checkBox.isSelected();
            return valid;
        }
    }
}
/*
// just for reference
class SomeClass {
    protected SomeObject object;
}
class SomeObject {
    private boolean configuration;
    public SomeObject() {
        super();
    }
    public boolean isConfigurationEnabled() {
        return configuration;
    }
    public void setConfiguration(boolean configuration) {
        this.configuration = configuration;
    }
}
*/
        