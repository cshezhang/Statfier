
ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory("tcp://localhost:61616");
factory.setTrustedPackages(Arrays.asList("org.mypackage1", "org.mypackage2"));
