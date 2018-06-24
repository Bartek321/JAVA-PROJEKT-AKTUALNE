package Pack;

public class User {
	private String param;
	private String type;
	private String adress;
	private Double value;
	
	public User(String param, String type, String adress, Double value) {
		this.param = param;
		this.type = type;
		this.adress = adress;
		this.value = value;
	}
	
	public void setParam(String param) {
		this.param = param;
	}
	public void setType(String type) {
		this.type = type;
	}
	public void setAdress(String adress) {
		this.adress = adress;
	}
	public void setValue(Double value) {
		this.value = value;
	}
	
	public String getParam() {
		return param;
	}
	public String getType() {
		return type;
	}
	public String getAdress() {
		return adress;
	}
	public Double getValue() {
		return value;
	}
}
