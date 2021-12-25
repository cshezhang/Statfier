
            public class Test {
                public void test() {
                    Formatter formatter = null;
                    if (formatterClassName != null) {
                        try {
                            Class<? extends Formatter> formatterClass = findClass(formatterClassName);
                            formatter = formatterClass.getDeclaredConstructor().newInstance();
                        } catch (ClassNotFoundException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException ignored) {
                        }
                    }
                    setFormatter((formatter == null) ? new SimpleFormatter() : formatter);
                }
            }
        