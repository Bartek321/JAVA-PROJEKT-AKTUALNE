package Pack;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.geom.Ellipse2D;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import org.apache.log4j.Logger;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.CategoryAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.LineAndShapeRenderer;
import org.jfree.data.category.DefaultCategoryDataset;

public class Chart {
	static Logger logger = Logger.getLogger(Chart.class);
	
	 public ChartPanel createChart(Color color) {
		 JFreeChart lineChart = ChartFactory.createLineChart("", "czas","wartoœæ", createDefaultDataset(), PlotOrientation.VERTICAL, true,true,false);
	     Font font = new Font("Dialog", Font.BOLD, 8); 
	     CategoryPlot plot = lineChart.getCategoryPlot(); 
	     ValueAxis valueAxis = plot.getRangeAxis();
	     CategoryAxis categoryAxis = plot.getDomainAxis();
	     
	     LineAndShapeRenderer renderer = (LineAndShapeRenderer) plot.getRenderer();
	     renderer.setBaseShapesVisible(true);
	     renderer.setSeriesShape(0, new Ellipse2D.Double(-3d, -3d, 6d, 6d));
	     renderer.setSeriesFillPaint(0, color);
	        
	     categoryAxis.setTickLabelFont(font);
	     categoryAxis.setLowerMargin(0);
         categoryAxis.setUpperMargin(0);
         
	     ChartPanel chartPanel = new ChartPanel(lineChart);
	     chartPanel.setPreferredSize(new Dimension(840, 310));
	     
	     return chartPanel;	             
	 }
	
	 public static DefaultCategoryDataset createDataset(String param, Integer id, Date date) {
		 ArrayList<Double> data = new ArrayList<>();
		 ArrayList<String> dateList = new ArrayList<>();
		 ArrayList<Integer> dateList1 = new ArrayList<>();
	     ConnectionDataBase cdb = new ConnectionDataBase();
	     try {
	    	 if(id == -1)
	    		 data = cdb.getData(param, date.toString());
	    	 else
	    		 data = cdb.getData(id, date.toString());
		} catch (SQLException e) {
			e.printStackTrace();
		}
	     DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	     dateList = cdb.getCzasy();

	     for(int i = 0; i < dateList.size(); i++) {
	    	 String[] value = dateList.get(i).split("\\s+");
	    	 dateList1.add(Integer.valueOf(value[1].substring(0, 2)));
	     }

	     for(int i = 0, j = 0; i < dateList1.size(); i++, j++) {	 
	    	 if(i == 0){
	    		 if(dateList1.get(0) != 0) {
	    			 for(int k = 0; k < dateList1.get(0); k++) {
			    		 dataset.addValue( null , param , k + ":" + "00" );
			    		 j++;
	    			 }
	    		 }  	 
	    	 } else {
	    		 if(dateList1.get(i) > dateList1.get(i - 1) + 1) {
	    			 for(int k = 0; k < dateList1.get(i) - (dateList1.get(i - 1) + 1); k++) {
			    		 dataset.addValue( null , param , dateList1.get(i - 1) + 1 + k + ":" + "00" );
			    		 j++;
	    			 }
		    	 }
	    	 }
	    	 
	         dataset.addValue( data.get(i) , param , j + ":" + "00" );         
	         if(dateList1.get(dateList1.size() - 1) < 23 && i == dateList1.size() - 1)  {
    			 for(int k = dateList1.get(dateList1.size() - 1) + 1; k < 24; k++) {
		    		 dataset.addValue( null , param , k + ":" + "00" );
		    		 j++;
    			 }
    		 }
	     }
	     if(data.isEmpty())
	    	 dataset.addValue( null , " " , " " );
	     logger.info(data);
	     logger.info("Number of elements: " + data.size());
	     return dataset;
	 }
	 
	 public DefaultCategoryDataset createDefaultDataset() {
	     DefaultCategoryDataset dataset = new DefaultCategoryDataset( );
	     dataset.addValue( null , " " , " " );
	     return dataset;
	 }

}
