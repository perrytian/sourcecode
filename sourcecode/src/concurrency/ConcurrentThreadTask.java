package concurrency;

public class ConcurrentThreadTask implements Runnable{

	@Override
	public void run() {
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Execute Thread.");		
	}

}
