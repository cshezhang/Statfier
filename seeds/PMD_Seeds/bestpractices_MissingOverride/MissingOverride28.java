
package net.sourceforge.pmd.lang.java.rule.bestpractices.missingoverride;

import java.util.Collection;

public abstract class AbstractBuilderMixedTypeVarOverride<B extends AbstractBuilderMixedTypeVarOverride<B, T>, T> {
    @SuppressWarnings("unchecked")
    public B defaultValue(T val) {
        return (B) this;
    }

    @SuppressWarnings("unchecked")
    public B defaultValue2(T val) {
        return (B) this;
    }

    public static final class ConcreteBuilder<V, C extends Collection<V>> extends AbstractBuilderMixedTypeVarOverride<ConcreteBuilder<V, C>, C> {
        //@Override is wrong here: method does not override or implement a method from a supertype
        public ConcreteBuilder<V, C> defaultValue(Collection<? extends V> val) {
            return this;
        }

        //@Override is indeed missing here
        public ConcreteBuilder<V, C> defaultValue2(C val) {
            return this;
        }
    }
}
        