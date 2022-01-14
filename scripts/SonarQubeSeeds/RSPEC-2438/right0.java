
	public static void main(String[] args) {
		Runnable r =new Runnable() {
			int p;
			@Override
			public void run() {
				while(true)
					System.out.println("a");
			}
		};
		new Thread(r).start();
