package app.googlemaptestapp;

public class Place {
	
	private int id;
	private double latitude;
	private double longitude;
	private String date;
	private String title;
	private String description;
	private String photoPath;
	

	public Place(){
	
	}
	
	public Place(double latitude, double longitude, String date, String title, String description, String photoPath){
		this.latitude = latitude;
		this.longitude = longitude;
		this.date = date;
		this.title = title;
		this.description = description;
		this.photoPath = photoPath;
	}
	
	/*public Place(int id, double latitude, double longitude, String date, String title, String description, String photoPath ){
		this.id = id;
		this.latitude = latitude;
		this.longitude = longitude;
		this.title = title;
		this.description = description;
		this.photoPath = photoPath;
	}*/
	
	public int getId(){
		return this.id;
	}
	
	public void setId(int id){
		this.id = id;
	}
	
	public double getLatitude(){
		return this.latitude;
		
	}
	
	public void setLatitude(double latitude){
		this.latitude = latitude;
	}
	
	public double getLongitude(){
		return this.longitude;
	}
	
	public void setLongitude(double longitude){
		this.longitude = longitude;
		
	}
	
	public String getDate(){
		return this.date;
	}
	
	public void setDate(String date){
		this.date = date;
	}
	
	public String getTitle(){
		return this.title;
	}
	
	public void setTitle(String title){
		this.title = title;
	}
	
	public String getDescription(){
		return this.description;
	}
	
	public void setDescription(String description){
		this.description = description;
	}
	
	public String getPhotoPath(){
		return this.photoPath;
	}
	
	public void setPhotoPath(String photoPath){
		this.photoPath = photoPath;
	}
	
}
