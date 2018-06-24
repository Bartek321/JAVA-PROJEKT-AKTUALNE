package Pack;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.stream.Collectors;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class Downloader {
	Logger logger = Logger.getLogger(Downloader.class);
	
	public String getData(String url) {
		URL u;
		String data = new String();
		try {
			u = new URL(url);
    	URLConnection conn;
			conn = u.openConnection();

    	BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
    	data = reader.lines().collect(Collectors.joining("\n")); 	
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		logger.info(data);
		return data;
	}
	
	public ArrayList<Integer> getStationsIds() {
		ArrayList<Integer> idList = new ArrayList<Integer>();
		try {
        	JSONArray Jarray = new JSONArray(getData("http://api.gios.gov.pl/pjp-api/rest/station/findAll"));

	    	JSONObject Jobject = new JSONObject();
	    	JSONObject Jobject1 = new JSONObject();
	    	JSONObject Jobject2 = new JSONObject();
	    	
	    	for(int i = 0; i < Jarray.length(); i++) {
	    		Jobject = (JSONObject) Jarray.get(i);
	    		Jobject1 = (JSONObject) Jobject.get("city");
	    		Jobject2 = (JSONObject) Jobject1.get("commune");
	    		if(Jobject2.get("communeName").toString().equals("Kraków"))
	    			idList.add(Integer.parseInt(Jobject.get("id").toString()));
	    	}    	

    	} catch(JSONException e) { 
    		e.printStackTrace();
    	}
		logger.info(idList);
		return idList;
	}
	
	public ArrayList<String> getStationsNames() {
		ArrayList<String> nameList = new ArrayList<String>();
		try {
        	JSONArray Jarray = new JSONArray(getData("http://api.gios.gov.pl/pjp-api/rest/station/findAll"));

	    	JSONObject Jobject = new JSONObject();
	    	JSONObject Jobject1 = new JSONObject();
	    	JSONObject Jobject2 = new JSONObject();
	    	
	    	for(int i = 0; i < Jarray.length(); i++) {
	    		Jobject = (JSONObject) Jarray.get(i);
	    		Jobject1 = (JSONObject) Jobject.get("city");
	    		Jobject2 = (JSONObject) Jobject1.get("commune");
	    		if(Jobject2.get("communeName").toString().equals("Kraków"))
	    			nameList.add(Jobject.get("addressStreet").toString());
	    	}    	

    	} catch(JSONException e) {
    		e.printStackTrace();
    	}
		logger.info(nameList);
		return nameList;
	}
	
	public ArrayList<Integer> getSensorsIds(Integer index) {
		ArrayList<Integer> idList = new ArrayList<Integer>();
		try {
        	JSONArray Jarray = new JSONArray(getData("http://api.gios.gov.pl/pjp-api/rest/station/sensors/" + index.toString()));
        	   	
	    	JSONObject Jobject = new JSONObject();
	    	
	    	for(int i = 0; i < Jarray.length(); i++) {
	    		Jobject = (JSONObject) Jarray.get(i);
	    		idList.add(Integer.parseInt(Jobject.get("id").toString()));    		
	    	} 
	    	
	     	
    	} catch(JSONException e) { 	
    		e.printStackTrace();
    	}
		logger.info(idList);
		return idList;
	}
	
	public ArrayList<String> getSensorsParams(Integer index) {
		ArrayList<String> paramList = new ArrayList<String>();
		try {
        	JSONArray Jarray = new JSONArray(getData("http://api.gios.gov.pl/pjp-api/rest/station/sensors/" + index.toString()));
	    	
	    	JSONObject Jobject = new JSONObject();
	    	JSONObject Jobject1 = new JSONObject();
	    	
	    	for(int i = 0; i < Jarray.length(); i++) {
	    		Jobject = (JSONObject) Jarray.get(i);
	    		Jobject1 = (JSONObject) Jobject.get("param");
	    		paramList.add(Jobject1.get("paramCode").toString());	    		
	    	} 
	     	
    	} catch(JSONException e) { 		
    		e.printStackTrace();
    	}
		logger.info(paramList);
		return paramList;
	}
	
	public ArrayList<String> getValues(Integer index) {
		ArrayList<String> valueList = new ArrayList<String>();
		try {
        	JSONObject Jobject = new JSONObject(getData("http://api.gios.gov.pl/pjp-api/rest/data/getData/" + index.toString()));
        	JSONArray Jarray = (JSONArray) Jobject.get("values");
        	
	    	JSONObject Jobject1 = new JSONObject();
	    	
	    	for(int i = 0; i < Jarray.length(); i++) {
	    		Jobject1 = (JSONObject) Jarray.get(i);
	    		valueList.add(Jobject1.get("date").toString());   
	    		valueList.add((Jobject1.get("value")).toString());
	    	} 
	    	
    	} catch(JSONException e) { 	
    		e.printStackTrace();
    	}
		logger.info(valueList);
		return valueList;
	}
	
	public ArrayList<ArrayList<String>> getAllStationData(Integer Id) {
		ArrayList<Integer> sensorsIds = new ArrayList<>();
		ArrayList<ArrayList<String>> values = new ArrayList<ArrayList<String>>();
		
		sensorsIds = getSensorsIds(Id);
		for(int j = 0; j < sensorsIds.size(); j++) {
			values.add(getValues(sensorsIds.get(j)));
		}
		logger.info(values);
		return values;			
	}
	
	public void getAllData() {
		ArrayList<Integer> stationsIds = getStationsIds();
		ArrayList<Integer> sensorsIds = new ArrayList<>();
		
		for(int i = 0; i < stationsIds.size(); i++) {
			sensorsIds = getSensorsIds(stationsIds.get(i));
			for(int j = 0; j < sensorsIds.size(); j++) {
			}
			break;		
		}
	}
	
	public ArrayList<Double> getWeather() {
		ArrayList<Double> list = new ArrayList<>();
		String s = new String();
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		    DocumentBuilder builder = factory.newDocumentBuilder();
		    InputSource is = new InputSource(new StringReader(getData("http://meteo.ftj.agh.edu.pl/meteo/meteo.xml")));
		    org.w3c.dom.Document d = builder.parse(is);

		    d.getDocumentElement().normalize();
		    
		    NodeList nList = d.getElementsByTagName("dane_aktualne");
		    NodeList nList1 = (NodeList) nList.item(0);
    
            for (int i = 0; i < nList1.getLength(); i++) {
            	if(i == 1 || i == 3 || i == 13 || i == 17) {
            		s = nList1.item(i).getTextContent().replaceAll("[^0-9\\.]", "");
            		list.add(Double.valueOf(s));
            	}
            }
		    
	    } catch (FileNotFoundException e) {
	    	e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		}
		logger.info(list);
		return list;
	}

}