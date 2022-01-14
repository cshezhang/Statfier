
public void do(){

  ScheduledThreadPoolExecutor stpe1 = new ScheduledThreadPoolExecutor(0); // Noncompliant

  ScheduledThreadPoolExecutor stpe2 = new ScheduledThreadPoolExecutor(POOL_SIZE);
  stpe2.setCorePoolSize(0);  // Noncompliant
