package iter0;

public interface IDomainObject<ID extends Serializable & Comparable<? super ID>> extends MutablePrimaryIdentifier<ID>, Serializable {
}
        