package Model;

public class Commission {

		 	private int id;
		 	private String precintName;
		 	private String cityName;
		 	private String provinceName;
		 	private int officerId;
		 	

		 	 public Commission( String precintName, String cityName, String provinceName, int officerId) {
					  super();
					  this.precintName = precintName;
					  this.provinceName = provinceName;
					  this.cityName=cityName;
					  this.officerId = officerId;
			 } 	
		 public Commission(int id, String precintName, String cityName, String provinceName, int officerId) {
				  super();
				  this.id = id;
				  this.precintName = precintName;
				  this.provinceName = provinceName;
				  this.cityName=cityName;
				  this.officerId = officerId;
		 }
		 @Override
		 public String toString() {
				  System.out.println("- ----------------------------------------------------------------------------------------------------------------------------------------------");
				  return "Commission [id=UB_" + id + ", precintName=" + precintName + ", cityName=" + cityName
				                        + ", provinceName=" + provinceName + ", officerId=CB_" + officerId + "]";
		 }
		 public Commission(String precintName, String cityName, String provinceName) {
				  super();
				  this.precintName = precintName;
				  this.provinceName = provinceName;
				  this.cityName=cityName;
		 }
		 public Commission() {
				  super();
		 }
		 public int getId() {
		 		 return id;
		 }
		 public void setId(int id) {
		 		 this.id = id;
		 }
		 public String getPrecintName() {
		 		 return precintName;
		 }
		 public void setPrecintName(String precintName) {
		 		 this.precintName = precintName;
		 }
		 public String getProvinceName() {
		 		 return provinceName;
		 }
		 public void setProvinceName(String provinceName) {
		 		 this.provinceName = provinceName;
		 }
		 public int getOfficerId() {
		 		 return officerId;
		 }
		 public void setOfficerId(int officerId) {
		 		 this.officerId = officerId;
		 }
		 public String getCityName() {
		 		 return cityName;
		 }
		 public void setCityName(String cityName) {
		 		 this.cityName = cityName;
		 }
		 
		 	
}
