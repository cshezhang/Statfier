
ThreadFactory threadFactory = Executors.defaultThreadFactory();
ThreadPoolExecutor executorPool = new ThreadPoolExecutor(3, 10, 5, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2), threadFactory);

for (int i = 0; i < 10; i++) {
  executorPool.execute(new JobThread("Job: " + i));
}

System.out.println(executorPool.getActiveCount()); // Compliant
executorPool.shutdown();
