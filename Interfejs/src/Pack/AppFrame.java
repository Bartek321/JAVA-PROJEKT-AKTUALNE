package Pack;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Date;
import java.sql.SQLException;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.log4j.Logger;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.CategoryPlot;

public class AppFrame extends JFrame implements ActionListener {
	Logger logger = Logger.getLogger(AppFrame.class);
	List<Component> components;
	List<ArrayList<String>> paramList;
	List<ArrayList<Integer>> sensorIdList;
	List<Integer> idList;
	List<String> stationsNamesList;
	JPanel cards;
	HashMap<String, Integer> map;
	HashMap<Integer, String> idMap;
	String mail;
	
    public AppFrame(String mail) {	
    	super("Program");
    	this.mail = mail;
    	setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000, 720);
		setLocation(200, 100);
		
		this.getContentPane().setLayout(new BorderLayout());
		
		JPanel sidePanel = new JPanel();
		JPanel buttonPanel = new JPanel();
		JPanel checkPanel = new JPanel();
		JPanel comboPanel = new JPanel();
		JPanel optionsPanel = new JPanel();
		JPanel mainPanel = new JPanel();
		JPanel upPanel = new JPanel();
		JPanel downPanel = new JPanel();
		
		buttonPanel.setMaximumSize(new Dimension(300, 40));
		
		checkPanel.setMaximumSize(new Dimension(300, 80));
		checkPanel.setLayout(new GridLayout(3,2));
		
		comboPanel.setLayout(new GridLayout(0, 1));
		comboPanel.setMaximumSize(new Dimension(300, 44));
		
		optionsPanel.setPreferredSize(new Dimension(300, 500));
		optionsPanel.setLayout(new BoxLayout(optionsPanel, BoxLayout.PAGE_AXIS));

		idList = new ArrayList<Integer>();
		paramList = new ArrayList<ArrayList<String>>();
		sensorIdList = new ArrayList<ArrayList<Integer>>();
		
		stationsNamesList = new ArrayList<String>();
		
		map = new HashMap<String, Integer>();
		idMap = new HashMap<Integer, String>();
		Downloader dl = new Downloader();
		idList = dl.getStationsIds();
		stationsNamesList = dl.getStationsNames();
		for(int i = 0; i < idList.size(); i++) {
			paramList.add(dl.getSensorsParams(idList.get(i)));
			sensorIdList.add(dl.getSensorsIds(idList.get(i)));
			map.put(stationsNamesList.get(i), i);
		}
		
		
		for(int i = 0; i < idList.size(); i++) {
			for(int j = 0; j < paramList.get(i).size(); j++) {
				idMap.put(sensorIdList.get(i).get(j), paramList.get(i).get(j));
			}
		}
		Object[] stringsA = stationsNamesList.toArray();		
		JComboBox list = new JComboBox(stringsA);
		list.setName("list");
		list.setPreferredSize(new Dimension(200, 30));
		
		String[] strings1 = { "Przekroczenie góra", "Przekroczenie dół", "Wzrost", "Spadek" };
		JComboBox list1 = new JComboBox(strings1);
		list1.setName("list1");
		list1.setPreferredSize(new Dimension(200, 30));
			
		ArrayList<String> listB = new ArrayList<>();

		list.addActionListener(this);	

		cards = new JPanel(new CardLayout());		
		
		for(int i = 0; i < idList.size(); i++) {
			JPanel ck = new JPanel();
			JPanel fieldPane = new JPanel();
			JPanel sidePane = new JPanel();
			
			sidePane.setLayout(new GridLayout(0, 2));
			sidePane.add(new JLabel("Typ:"));
			JLabel jl1 = new JLabel("Brak");
			jl1.setName("Typy");
			sidePane.add(jl1);	
			
			ck.setMaximumSize(new Dimension(300, 80));
			ck.setMinimumSize(new Dimension(300, 80));
			ck.setLayout(new GridLayout(3,2));
			
			fieldPane.setMinimumSize(new Dimension(300, 80));
	
			for(int j = 0; j < paramList.get(i).size(); j++) {
				JCheckBox cb = new JCheckBox(paramList.get(i).get(j).toString());
				cb.setName(paramList.get(i).get(j).toString() + stationsNamesList.get(i).toString() + "cb");
				ck.add(cb);
				
				JTextField ct = new JTextField();
				ct.setName(paramList.get(i).get(j).toString() + stationsNamesList.get(i).toString());
				ct.setPreferredSize(new Dimension(70, 20));
				
				fieldPane.add(new JLabel(paramList.get(i).get(j).toString()));
				fieldPane.add(ct);
				
				JLabel jl = new JLabel();
				jl.setName(paramList.get(i).get(j).toString() + stationsNamesList.get(i).toString() + "cl");
				sidePane.add(new JLabel(paramList.get(i).get(j).toString()));
				sidePane.add(jl);
				
			}
			JPanel p = new JPanel();
			JPanel XPane = new JPanel();
			JPanel f = new JPanel();
			
			GridLayout gg = new GridLayout(0, 4);
			gg.setHgap(5);
			fieldPane.setLayout(gg);
			fieldPane.setMaximumSize(new Dimension(300, listB.size()*15));
			
			f.add(fieldPane);
			f.setPreferredSize(new Dimension(300, 70));
			
			XPane.add(f);
			XPane.setMaximumSize(new Dimension(300,100));
			XPane.setMinimumSize(new Dimension(300,100));
			
			p.add(ck);
			p.add(XPane);
			p.add(sidePane);
			p.setPreferredSize(new Dimension(500, 700));
			
			cards.add(p, stationsNamesList.get(i).toString());
		}

		Chart chart = new Chart();
        ChartPanel chartPanel1 = chart.createChart(new Color(255, 0, 0));
        chartPanel1.setName("paramChart");
        ChartPanel chartPanel11 = chart.createChart(new Color(0, 0, 255));
        chartPanel11.setName("weatherChart");
             
        upPanel.setPreferredSize(new Dimension(2000, 34));
        	
		JComboBox idList = new JComboBox(stringsA);
		idList.setName("idList");
		idList.setPreferredSize(new Dimension(200, 24));
		
		JComboBox idList1 = new JComboBox();
		idList1.setName("idList1");
		idList1.setPreferredSize(new Dimension(100, 24));
		
		for(String i: paramList.get(0)) {
    		idList1.addItem(i);
    	}
		
		idList.addActionListener(this);	
		
		int year = Calendar.getInstance().get(Calendar.YEAR);
		
		JComboBox listb1 = new JComboBox();
		listb1.setName("day");
		listb1.setPreferredSize(new Dimension(100, 24));
		for(int i = 1; i < 32; i++) {
			listb1.addItem(i);
		}
		
		String[] s = {"Styczeń", "Luty", "Marzec", "Kwiecień", "Maj", "Czerwiec", "Lipiec", "Sierpień", "Wrzesień", "Październik", "Listopad", "Grudzień"};
		JComboBox listb2 = new JComboBox(s);
		listb2.setName("month");
		listb2.setPreferredSize(new Dimension(100, 24));
		
		listb2.addActionListener(this);	
		
		JComboBox listb3 = new JComboBox();
		listb3.setName("year");
		listb3.setPreferredSize(new Dimension(100, 24));
		System.out.println(year + "SDfdd");
		for(int i = 0; i < year - 2017; i++) {
			listb3.addItem(Integer.toString(year + i));
			System.out.println(year + i);
		}
		
		JButton buttonD = new JButton("Wyświetl");
		buttonD.addActionListener(this);	
		
		upPanel.add(idList);
		upPanel.add(idList1);  
		upPanel.add(listb1);
		upPanel.add(listb2);
		upPanel.add(listb3);
		upPanel.add(buttonD);
		
		String[] ss = {"Temperatura", "Wilgotność", "Siła wiatru", "Ciśnienie"};
		JComboBox paramList1 = new JComboBox(ss);
		paramList1.setName("type");
		paramList1.setPreferredSize(new Dimension(100, 24));
		
		JComboBox paramList2 = new JComboBox();
		paramList2.setName("day1");
		paramList2.setPreferredSize(new Dimension(100, 24));
		for(int i = 1; i < 32; i++) {
			paramList2.addItem(i);
		}
		
		JComboBox paramList3 = new JComboBox(s);
		paramList3.setName("month1");
		paramList3.setPreferredSize(new Dimension(100, 24));
		
		paramList3.addActionListener(this);	
		
		JComboBox paramList4 = new JComboBox();
		paramList4.setName("year1");
		paramList4.setPreferredSize(new Dimension(100, 24));
		for(int i = 0; i < year - 2017; i++) {
			paramList4.addItem(Integer.toString(year + i));
		}
		
		JButton buttonE = new JButton("Wyświetl ");
		buttonE.addActionListener(this);
		
		downPanel.add(paramList1);
		downPanel.add(paramList2);  
		downPanel.add(paramList3);
		downPanel.add(paramList4);
		downPanel.add(buttonE);
             
		downPanel.setPreferredSize(new Dimension(2000, 38));
		
		JButton button = new JButton("Dodaj");
		button.addActionListener(this);	
		
		JButton button1 = new JButton("Reset");
		button1.addActionListener(this);		
		
		comboPanel.add(list);
		comboPanel.add(list1);

		buttonPanel.add(button);
		buttonPanel.add(button1);
		
		optionsPanel.add(comboPanel);
		optionsPanel.add(cards);

		sidePanel.setLayout(new BorderLayout());
		sidePanel.add(optionsPanel, BorderLayout.NORTH);
		sidePanel.add(buttonPanel, BorderLayout.SOUTH);
		
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		JPanel chartsPanel = new JPanel();
		chartsPanel.add(chartPanel1);
		chartsPanel.add(chartPanel11);
		chartsPanel.setPreferredSize(new Dimension(860, (int) screenSize.getHeight() - 150));
		
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(upPanel, BorderLayout.NORTH);
		mainPanel.add(chartsPanel, BorderLayout.CENTER);
		mainPanel.add(downPanel, BorderLayout.SOUTH);
		
		chartsPanel.setBackground(new Color(249,249,249));
		downPanel.setBackground(new Color(220,220,220));
		upPanel.setBackground(new Color(220,220,220));
		
		this.getContentPane().add(sidePanel, BorderLayout.LINE_END);
		this.getContentPane().add(mainPanel, BorderLayout.CENTER);
		components = Util.getAllComponents(this.getContentPane());
		list.setSelectedIndex(0);
    }
    
    public void actionPerformed(ActionEvent e) {
    	Object selected = ((JComboBox) Util.getComponent("list", components)).getSelectedItem();
    	if(e.getActionCommand() == "Dodaj") {
	    	ConnectionDataBase cdb = new ConnectionDataBase();	    		    		
	    		Integer x = new Integer(0);
	    		for(int i = 0; i < idList.size(); i++) {
	    			if(stationsNamesList.get(i).toString().equals( ((JComboBox) Util.getComponent("list", components)).getSelectedItem().toString()) )
						x = i;
	    		}
	    		for(int i = 0; i < paramList.get(x).size(); i++) {
	        		if(((JCheckBox) Util.getComponent( paramList.get(x).get(i)  +  ((JComboBox) Util.getComponent("list", components)).getSelectedItem().toString() + "cb"   , components)).isSelected()) {
	        			cdb.SetNotification(sensorIdList.get(x).get(i), (((JComboBox)Util.getComponent("list1", components)).getSelectedItem().toString()), Double.valueOf(((JTextField) Util.getComponent(paramList.get(x).get(i)  +  ((JComboBox) Util.getComponent("list", components)).getSelectedItem().toString(), components)).getText()), mail);
	        			logger.info("added" + paramList.get(x).get(i));
	        		}
	        	}   	
	    	((JComboBox)Util.getComponent("list", components)).setSelectedIndex( ((JComboBox)Util.getComponent("list", components)).getSelectedIndex() );
    	} else if (e.getActionCommand() == "Reset") {
    		ConnectionDataBase cdb = new ConnectionDataBase();
    		ArrayList<Integer> sensorList = new ArrayList<Integer>();
    		sensorList = sensorIdList.get(((JComboBox)Util.getComponent("list", components)).getSelectedIndex());
    		cdb.reset(sensorList, mail);
    		((JComboBox)Util.getComponent("list", components)).setSelectedIndex( ((JComboBox)Util.getComponent("list", components)).getSelectedIndex() );
    		logger.info("reset");
    	} else if (e.getActionCommand() == "Wyświetl") { 
    		int year = Integer.valueOf(((JComboBox)Util.getComponent("year", components)).getSelectedItem().toString()) - 1900;
    		int month = ((JComboBox)Util.getComponent("month", components)).getSelectedIndex();
    		int day = ((JComboBox)Util.getComponent("day", components)).getSelectedIndex() + 1;
    		Date date1 = new Date(year, month, day);
    		
    		String date = ((JComboBox)Util.getComponent("year", components)).getSelectedItem().toString() + "-" + month + "-" + day;
    		JFreeChart chart = ((ChartPanel) Util.getComponent("paramChart", components)).getChart();
    		CategoryPlot plot = chart.getCategoryPlot();
    		plot.setDataset(Chart.createDataset(((JComboBox)Util.getComponent("idList1", components)).getSelectedItem().toString(), Integer.valueOf(sensorIdList.get(((JComboBox)Util.getComponent("idList", components)).getSelectedIndex()).get(((JComboBox)Util.getComponent("idList1", components)).getSelectedIndex())), date1));
    		logger.info(year + " " + month + " " + day);
    	} else if (e.getActionCommand() == "Wyświetl ") { 
    		int year = Integer.valueOf(((JComboBox)Util.getComponent("year1", components)).getSelectedItem().toString()) - 1900;
    		int month = ((JComboBox)Util.getComponent("month1", components)).getSelectedIndex();
    		int day = ((JComboBox)Util.getComponent("day1", components)).getSelectedIndex() + 1;
    		Date date1 = new Date(year, month, day);
    		logger.info(year + " " + month + " " + day);
    		HashMap<String, String> paramNameMap = new HashMap<String, String>();
    		paramNameMap.put("Temperatura", "Temperatura");
    		paramNameMap.put("Wilgotność", "Wilgotnosc");
    		paramNameMap.put("Siła wiatru", "Sila wiatru");
    		paramNameMap.put("Ciśnienie", "Cisnienie");
    		
    		String date = ((JComboBox)Util.getComponent("year1", components)).getSelectedItem().toString() + "-" + ((JComboBox)Util.getComponent("month1", components)).getSelectedItem().toString() + "-" + ((JComboBox)Util.getComponent("day1", components)).getSelectedItem().toString();
    		JFreeChart chart = ((ChartPanel) Util.getComponent("weatherChart", components)).getChart();
    		CategoryPlot plot = chart.getCategoryPlot(); 		
    		plot.setDataset(Chart.createDataset(paramNameMap.get(((JComboBox)Util.getComponent("type", components)).getSelectedItem().toString()), -1, date1));
    	} else if (((Component) e.getSource()).getName().equals("month")) {
    		YearMonth yearMonthObject = YearMonth.of(Integer.parseInt(((JComboBox)Util.getComponent("year", components)).getSelectedItem().toString()), ((JComboBox)Util.getComponent("month", components)).getSelectedIndex() + 1);
        	int daysInMonth = yearMonthObject.lengthOfMonth();
    		((JComboBox)Util.getComponent("day", components)).removeAllItems();
        	for(int i = 1; i < daysInMonth + 1; i++) {
        		((JComboBox)Util.getComponent("day", components)).addItem(i);
        	}
    	} else if (((Component) e.getSource()).getName().equals("month1")) {
    		YearMonth yearMonthObject1 = YearMonth.of(Integer.parseInt(((JComboBox)Util.getComponent("year1", components)).getSelectedItem().toString()), ((JComboBox)Util.getComponent("month1", components)).getSelectedIndex() + 1);
        	int daysInMonth1 = yearMonthObject1.lengthOfMonth();

        	((JComboBox)Util.getComponent("day1", components)).removeAllItems();
        	for(int i = 1; i < daysInMonth1 + 1; i++) {
        		((JComboBox)Util.getComponent("day1", components)).addItem(i);
        	}	
    	} else if (((Component) e.getSource()).getName().equals("idList")) {
    		((JComboBox)Util.getComponent("idList1", components)).removeAllItems();
        	for(int i = 0; i < (paramList.get( map.get( ( (JComboBox) Util.getComponent("idList", components) ).getSelectedItem().toString())).size()); i++) {
        		((JComboBox)Util.getComponent("idList1", components)).addItem((paramList.get( map.get( ( (JComboBox) Util.getComponent("idList", components) ).getSelectedItem().toString())).get(i)));
        	}
    		
    	} else if (((Component) e.getSource()).getName().equals("list")) {
    		CardLayout cardLayout = (CardLayout) cards.getLayout();
        	cardLayout.show(cards, (((JComboBox)Util.getComponent("list", components)).getSelectedItem()).toString());
        	ArrayList<Integer> l = sensorIdList.get(((JComboBox)Util.getComponent("list", components)).getSelectedIndex());
        	ConnectionDataBase cdb = new ConnectionDataBase();
        	try {
				cdb.GetNotification(mail);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
        	ArrayList<Integer> li = cdb.getIdSensors(); 
        	ArrayList<Double> li1 = cdb.getValues(); 
        	ArrayList<String> li2 = cdb.getTypes(); 
        	String type = new String();
        	HashMap <String, Double> paraMap = new HashMap<String, Double>();
        	for(int i = 0; i < li.size(); i++) {
        		
        		if(l.contains(li.get(i))) {
        			paraMap.put(idMap.get(li.get(i)), li1.get(i));
        			type = li2.get(i);
        		}
        	}
        	if(paraMap.size() > 0)
        		((JLabel)Util.getComponent("Typy", components)).setText(type);
        	else
        		((JLabel)Util.getComponent("Typy", components)).setText("Brak");
        	
        	int i = ((JComboBox)Util.getComponent("list", components)).getSelectedIndex();
        	for(int j = 0; j < paramList.get(i).size(); j++) {
        		if(li.contains(sensorIdList.get(i).get(j)))
        			((JLabel)Util.getComponent( paramList.get(i).get(j).toString() + stationsNamesList.get(i).toString() + "cl", components)).setText(paraMap.get(paramList.get(i).get(j)).toString());
        		else
        			((JLabel)Util.getComponent( paramList.get(i).get(j).toString() + stationsNamesList.get(i).toString() + "cl", components)).setText("Brak");
        	}     	
    	}
    	for(int i = 0; i < paramList.size(); i++) {
	    	if (selected.toString().equals(sensorIdList.get(i).toString())) {
	    	}
    	}
    }
}