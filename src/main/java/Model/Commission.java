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
				  return "id:UB_" + id + "\t Phuong/xa:" + precintName + "\t\t Thanh Pho:" + cityName
				                        + "\t\t Tinh:" + provinceName + "\t\t id:CB_" + officerId ;
		 }
		 //		 
//@Override
//		 public String toString() {
//				  return "UB_" + id + "\t\t precintName=" + precintName + "\t\t cityName=" + cityName
//				                        + "\t\t provinceName=" + provinceName + "\t\t officerId=" + officerId ;
//		 }
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
