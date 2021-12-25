
            class Test {
                private final List<CallableProcessingInterceptor> interceptors;
                private int preProcessIndex = -1;
                public void applyPreProcess(NativeWebRequest request, Callable<?> task) throws Exception {
                    for (CallableProcessingInterceptor interceptor : this.interceptors) {
                        interceptor.preProcess(request, task);
                        this.preProcessIndex++;
                    }
                }
            }
            