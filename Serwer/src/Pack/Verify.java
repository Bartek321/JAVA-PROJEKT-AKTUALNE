package Pack;

import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

import org.apache.log4j.Logger;

public class Verify {
	int[] z;
	static Logger logger = Logger.getLogger(Verify.class);
    public String getMessage(String type, String param, double value, double nowValue, double oldValue) {
		String message = "";
		switch(type) {
		case "Przekroczenie góra":
			message = checkMax(param, nowValue, value);
			break;
		case "Przekroczenie dó³":
			message = checkMin(param, nowValue, value);
			break;
		case "Wzrost":
			message = checkRise(param, nowValue, oldValue, value);
			break;
		case "Spadek":
			message = checkFall(param, nowValue, oldValue, value);
			break;		
		}
		logger.info(message);
		return message;
	}
    
    public static DecimalFormat getFormat() {
    	DecimalFormat df = new DecimalFormat("#.##");
		df.setRoundingMode(RoundingMode.CEILING);
		DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
	    sym.setDecimalSeparator('.');
	    df.setDecimalFormatSymbols(sym);
	    
	    return df;
    }
	
	static public String checkMax(String param, double values, double value1) {
		
		DecimalFormat df = getFormat();
		
		if(values > value1)
			return "Przekroczona górna wartoœæ graniczna " + param + " " + value1 + " o " + df.format(values - value1) + ", wartoœæ aktualna: " + values + ",\n";
		else
			return "";
	}
	
	static public String checkMin(String param, double values, double value1) {		
		DecimalFormat df = getFormat();
		
		if(values < value1)
			return "Przekroczona dolna wartoœæ graniczna " + param + " " + value1 + " o " + df.format(value1 - values) + ", wartoœæ aktualna: " + values + ",\n";
		else
			return "";	
	}
	
	static public String checkRise(String param, double values, double values1,  double value1) {		
		DecimalFormat df = getFormat();
		
		if(values1 * value1 < values - values1)
			return "Przekroczona wartoœæ graniczna wzrostu " + param + " " +  value1 * 100.0 + "% o " + df.format((((values - values1 - values * value1) / values) * 100.0)) + "%(" + df.format((values - values1 - values * value1)) + ")" + ", wartoœæ poprzednia: " + values1 + ", wartoœæ aktualna: " + values + ",\n";
		else
			return "";	
	}
	
	static public String checkFall(String param, double values, double values1,  double value1) {	
		DecimalFormat df = getFormat();

		if(values1 * value1 < values1 - values)
			return "Przekroczona wartoœæ graniczna spadku " + param + " " + value1 * 100.0 + "% o " + df.format((((values1 - values - values1 * value1) / values1) * 100.0)) + "%(" + df.format((values1 - values - values1 * value1)) + ")" + ", wartoœæ poprzednia: " + values1 + ", wartoœæ aktualna: " + values + ",\n";	
		else
			return "";		
	}
	

}
