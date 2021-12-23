
package net.sourceforge.pmd.lang.rule;

import net.sourceforge.pmd.Rule;
import net.sourceforge.pmd.properties.MultiValuePropertyDescriptor;

/**
 * Base class for Rule implementations which delegate to another Rule instance.
 */
public abstract class AbstractDelegateRule implements Rule {

    // missing
    public <V> void setProperty(MultiValuePropertyDescriptor<V> propertyDescriptor, V... values) {
        rule.setProperty(propertyDescriptor, values);
    }

}
        