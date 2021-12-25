
public class Foo {
    public void foo() {
        Collections.sort(files, new Comparator<DataSource>() {
            @Override
            public int compare(DataSource left, DataSource right) {
                String leftString = left.getNiceFileName(useShortNames, inputPaths);
                String rightString = right.getNiceFileName(useShortNames, inputPaths);
                return leftString.compareTo(rightString);
            }
        });
        final TreeSet<Map.Entry<String, TimedResult>> sortedKeySet = new TreeSet<>(
            new Comparator<Map.Entry<String, TimedResult>>() {
                @Override
                public int compare(final Entry<String, TimedResult> o1, final Entry<String, TimedResult> o2) {
                    return Long.compare(o1.getValue().selfTimeNanos.get(), o2.getValue().selfTimeNanos.get());
                }
            });
        new ThreadLocal<Queue<TimerEntry>>() {
            @Override
            protected Queue<TimerEntry> initialValue() {
                return Collections.asLifoQueue(new LinkedList<TimerEntry>());
            }
        };
        Iterator<Node> EMPTY_ITERATOR = new ArrayList<Node>().iterator();
        ((ListNode<E>) rev).reverseCache = new SoftReference<ImmutableList<E>>(this);
    }
    public Map<PropertyDescriptor<?>, Object> getOverriddenPropertiesByPropertyDescriptor() {
        return propertyValues == null ? new HashMap<PropertyDescriptor<?>, Object>() : new HashMap<>(propertyValues);
    }
}
        