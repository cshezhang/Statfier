
public interface IDomainObject<ID extends Serializable & Comparable<? super ID>> extends MutablePrimaryIdentifier<ID>, Serializable {
}
        