package Model;

public class Commission {

		 	private int id;
		 	private String precintName;
		 	private String cityName;
		 	private String provinceName;
		 	
		 	

			  	
		 public Commission(int id, String precintName, String cityName, String provinceName) {
				  super();
				  this.id = id;
				  this.precintName = precintName;
				  this.provinceName = provinceName;
				  this.cityName=cityName;
		 }
		 
		 
@Override
		 public String toString() {
//		 System.out.println(String.format(" %-25s  %-25s  %-25s  %-25s", "ID", "precint_name", "city_name ", "province_name" ));
		return(String.format(" %-20.25s  %-25.25s  %-30.25s  %-30.25s  ","UB_"+id, precintName,
						   cityName, provinceName));
//				 
		 }
		 //		 

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
		
		 public String getCityName() {
		 		 return cityName;
		 }
		 public void setCityName(String cityName) {
		 		 this.cityName = cityName;
		 }
		 
		 	
}
