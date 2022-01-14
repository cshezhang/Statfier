
	public static void main(String[] args) {
		Thread r =new Thread() {
			int p;
			@Override
			public void run() {
				while(true)
					System.out.println("a");
			}
		};
		new Thread(r).start();  // Noncompliant
