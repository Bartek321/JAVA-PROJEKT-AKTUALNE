package Pack;

import java.time.LocalDateTime;
import java.util.ArrayList;

import org.apache.log4j.Logger;

public class WeatherWorker implements Runnable {
	static Logger logger = Logger.getLogger(WeatherWorker.class);
    public WeatherWorker(){
    }

    @Override
    public void run() {
    	Downloader downloader = new Downloader();
    	ConnectionDataBase cdb = new ConnectionDataBase();
    	ArrayList <Double> data = downloader.getWeather();
    	ArrayList <String> values = new ArrayList<String>();
    	ArrayList <String> params = new ArrayList<String>();
    	params.add("Temperatura");
    	params.add("Wilgotnosc");
    	params.add("Sila wiatru");
    	params.add("Cisnienie");

    	String date = new String();
    	date = LocalDateTime.now().toString().substring(0, 10);
    	if(LocalDateTime.now().getHour() < 10)
    		date += " " + LocalDateTime.now().getHour() + ":00:00";
    	else
    		date += " " + LocalDateTime.now().getHour() + ":00:00";
    	
    	for(int i = 0; i < data.size(); i++) {
    		values.clear();
    		values.add(date);
    		values.add(data.get(i).toString());
    		downloader.saveToDataBase(values, params.get(i));
    	}
    	if(data.size() < 4)
    		logger.warn("data not complete");
    }

}