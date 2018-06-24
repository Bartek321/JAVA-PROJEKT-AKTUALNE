package Pack;

import java.awt.EventQueue;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import org.apache.log4j.Logger;

public class Main {
	Logger logger = Logger.getLogger(Main.class);
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
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new LoginFrame();
            }
        });
    }
}