
            record HelloMsg(String msg) {

                    HelloMsg toTheWorld() {
                       return new HelloMsg("World");
                    }

                    String hello() {
                    return "Hello " + msg;
                    }
            }
            