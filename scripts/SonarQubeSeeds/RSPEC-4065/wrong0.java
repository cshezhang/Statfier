
ThreadLocal<List<String>> myThreadLocal =
    new ThreadLocal<List<String>>() { // Noncompliant
        @Override
        protected List<String> initialValue() {
            return new ArrayList<String>();
        }
    };
