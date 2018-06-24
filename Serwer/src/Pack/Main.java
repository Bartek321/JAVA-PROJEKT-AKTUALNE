package Pack;

import java.awt.EventQueue;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.Properties;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

public class Main {
	static Properties props = new Properties();
    public static void main(String[] args) {
    	String resourceName = "configuration.properties";
    	ClassLoader loader = Thread.currentThread().getContextClassLoader();
    	try(InputStream resourceStream = loader.getResourceAsStream(resourceName)) {
    	    props.load(resourceStream);
    	} catch (IOException e) {
			e.printStackTrace();
		}
    	System.setProperty("file.encoding", "UTF-8");
    	ThreatPoll tp = new ThreatPoll();
    	//tp.go();
    	WeatherWorker ww = new WeatherWorker();
    	ww.run();
    }
}
