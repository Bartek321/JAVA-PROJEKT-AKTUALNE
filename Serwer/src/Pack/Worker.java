package Pack;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

public class Worker implements Runnable {
	static Logger logger = Logger.getLogger(Worker.class);
    private Integer Id;
    
    public Worker(Integer Id){
        this.Id=Id;
    }

    @Override
    public void run() {
    	Mail mail = new Mail();
    	Downloader downloader = new Downloader();						//download
    	ArrayList<Integer> idList = new ArrayList<Integer>();
    	ArrayList<String> paramList = new ArrayList<String>();
    	ArrayList<String> namesList = new ArrayList<String>();
    	ArrayList<Integer> idsList = new ArrayList<Integer>();
    	ArrayList<ArrayList<String>> valuesList = new ArrayList<ArrayList<String>>();
    	ArrayList<ArrayList<Double>> valuesList1 = new ArrayList<ArrayList<Double>>();
    	HashMap<String, Double> map = new HashMap<String, Double>();
    	HashMap<Integer, String> map1 = new HashMap<Integer, String>();
    	HashMap<Integer, String> map2 = new HashMap<Integer, String>();
    	
    	idList = downloader.getSensorsIds(Id);
    	paramList = downloader.getSensorsParams(Id);
    	namesList = downloader.getStationsNames();
    	idsList = downloader.getStationsIds();
    	
    	for(int i = 0; i < idsList.size(); i++) {
    		map2.put(idsList.get(i), namesList.get(i));
    	}
    	
    	for(int i = 0; i < idList.size(); i++) {
    		map1.put(idList.get(i), paramList.get(i));
    	}
    	
    	for(int i = 0; i < idList.size(); i++) {
    		valuesList.add(downloader.getValues(idList.get(i)));
    		valuesList1.add(test(valuesList.get(i)));
    		map.put(paramList.get(i) + "1", valuesList1.get(i).get(0));
    		map.put(paramList.get(i) + "2", valuesList1.get(i).get(1));
    		downloader.saveToDataBase(valuesList.get(i), idList.get(i));
    	}
    	
    	ConnectionDataBase cdb = new ConnectionDataBase();
    	
    	ArrayList <User> user = new ArrayList<User>();
    	ArrayList<ArrayList<User>> userList = new ArrayList<ArrayList<User>>();
    	ArrayList <String> mailL = new ArrayList<String>();
    	ArrayList <String> typeL = new ArrayList<String>();
    	ArrayList <Double> valueL = new ArrayList<Double>();
    	for(int i = 0; i < idList.size(); i++) {
    		try {
				cdb.GetNotification(idList.get(i));
				mailL = cdb.getMails();
				typeL = cdb.getTypes();
				valueL = cdb.getValues();
			} catch (SQLException e) {
				e.printStackTrace();
			}
    		
    		for(int j = 0; j < cdb.getValues().size(); j++) {
    			user.add(new User(map1.get(idList.get(i)), typeL.get(j), mailL.get(j), valueL.get(j)));
    		}
    	}

    	ArrayList<String> ad = new ArrayList<String>();
    	ad = getAd(user);
    	for(int i = 0; i < ad.size(); i++) {
    		userList.add(getSort(user, ad.get(i)));
    	}
    	
    	String msg = new String();
    	Verify verify = new Verify();
    	for(int i = 0; i < userList.size(); i++) {
    		msg += "Stacja " + map2.get(Id) + ":\n";
    		for(int j = 0; j < userList.get(i).size(); j++) {
    			msg += verify.getMessage(userList.get(i).get(j).getType(), userList.get(i).get(j).getParam(), userList.get(i).get(j).getValue(), map.get(userList.get(i).get(j).getParam() + "1"), map.get(userList.get(i).get(j).getParam() + "2"));
    		}
    		if(paramList.contains(userList.get(i).get(0).getParam()))
    			mail.send(userList.get(i).get(0).getAdress(), msg);  //send mail
    		msg = "";	
    	}
    }
    
    public ArrayList<User> getSort(ArrayList<User> user, String adress) {
    	ArrayList<User> u = new ArrayList<User>();
    	for(int i = 0; i < user.size(); i++) {
    		if(user.get(i).getAdress().equals(adress))
    			u.add(user.get(i));
    	}
    	return u;
    }
    public ArrayList<String> getAd(ArrayList<User> user) {
    	ArrayList<String> ad = new ArrayList<String>();
    	for(int i = 0; i < user.size(); i++) {
    		if(!ad.contains(user.get(i).getAdress()))
    			ad.add(user.get(i).getAdress());
    	}
    	return ad;	
    }
    
    static public ArrayList<Double> test(ArrayList<String> values) {
    	ArrayList<Double> valueList = new ArrayList<Double>();
    	int j = 13;
    	HashMap<Integer, String> list = new HashMap<Integer, String>();
    	for(int i = -3; i < 24; i++) {
    		if(i < 10 && i > -1)
    			list.put(i, "0" + i);
    		else if(i < 0)
    			list.put(i, Integer.toString(i + 24));
    		else
    			list.put(i, Integer.toString(i));
    	}
    	for(int i = 0; i < j; i += 2) {
    		String[] value = values.get(i).split("\\s+");
        	if((value[1].substring(0, 2).equals(String.valueOf(list.get(LocalDateTime.now().getHour()) )) && !values.get(i + 1).equals("null")) || (value[1].substring(0, 2).equals(String.valueOf(list.get(LocalDateTime.now().getHour() - 1))) && !values.get(i + 1).equals("null")) || (value[1].substring(0, 2).equals(String.valueOf(list.get(LocalDateTime.now().getHour() - 2))) && !values.get(i + 1).equals("null")) || (value[1].substring(0, 2).equals(String.valueOf(list.get(LocalDateTime.now().getHour() - 3) )) && !values.get(i + 1).equals("null"))) {
        		if(valueList.size() == 2)
        			break;
        		valueList.add(Double.valueOf(values.get(i + 1)));
        	} else if ((value[1].substring(0, 2).equals(String.valueOf(list.get(LocalDateTime.now().getHour() - 2))) && values.get(i + 1).equals("null")) || (value[1].substring(0, 2).equals(String.valueOf(list.get(LocalDateTime.now().getHour() - 3))) && values.get(i + 1).equals("null"))) {
        		valueList.add(0.0);
        	} 			
    	}
    	logger.info(valueList);
    	return valueList;
    }

}