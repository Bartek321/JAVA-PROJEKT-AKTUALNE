package Pack;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;

public class ThreatPoll {
	static Logger logger = Logger.getLogger(ThreatPoll.class);
	public static void go() {
		logger.info("seleep for: " + getSleepTime() / 60000);
		try {
			Thread.sleep(getSleepTime());
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(;;) {
			run(0);
			try {
				logger.info("sleep for" + getSleepTime());
				Thread.sleep(getSleepTime());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private static int getSleepTime() {
		int mins = 0;
		if(LocalDateTime.now().getMinute() > 30)
			mins = 60 - LocalDateTime.now().getMinute() + 30;	
		else 
			mins = 30 - LocalDateTime.now().getMinute();
		return mins * Integer.valueOf(Main.props.getProperty("time"));
	}

    public static void run(int index) {
        ExecutorService executor = Executors.newFixedThreadPool(Integer.valueOf(Main.props.getProperty("threat")));
        ArrayList<Integer> list = new ArrayList<Integer>();  
        Downloader downloader = new Downloader(); 
        Runnable weatherWorker = new WeatherWorker();
        
        list = downloader.getStationsIds();
        executor.execute(weatherWorker);
        
        for (int i = 0; i < list.size(); i++) {
        	Runnable worker = new Worker(list.get(i));
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        logger.info("all done!");
    }
}