
public class UnnecessaryLocalBeforeReturnCase {
    private <T extends UserVisibleEventTypeValue<?>> T findEventTypeValueByName(Class<T> entityClass, String eventTypeName, String eventTypeValueName) {
        Criteria criteria = this.session.createCriteria(entityClass)
            .add(Restrictions.eq(UserVisibleEventTypeValue_.internalName, eventTypeValueName))
            .createCriteria(UserVisibleEventTypeValue_.type)
                .add(Restrictions.eq(UserVisibleEventType_.name, eventTypeName))
                .setCacheable(true);

        @SuppressWarnings("unchecked")
        T result = (T) criteria.uniqueResult();
        return result;
    }
}
        