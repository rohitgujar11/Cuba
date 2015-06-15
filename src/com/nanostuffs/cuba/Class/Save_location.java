// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
package com.nanostuffs.cuba.Class;
public class Save_location {
    
    //private variables
    int _id;
    String _name;
    String _lat_lng;
    String NameLocation;
     
    public String getNameLocation() {
		return NameLocation;
	}
	public void setNameLocation(String nameLocation) {
		NameLocation = nameLocation;
	}
	// Empty constructor
    public Save_location(){
         
    }
    // constructor
    public Save_location(int id, String name, String _lat_lng){
        this._id = id;
        this._name = name;
        this._lat_lng = _lat_lng;
    }
     
    // constructor
    public Save_location(String name, String _lat_lng,String NameLocation){
        this._name = name;
        this._lat_lng = _lat_lng;
        this.NameLocation=NameLocation;
    }
    // getting ID
    public int getID(){
        return this._id;
    }
     
    // setting id
    public void setID(int id){
        this._id = id;
    }
     
    // getting name
    public String getName(){
        return this._name;
    }
     
    // setting name
    public void setName(String name){
        this._name = name;
    }
     
    // getting latitude and longitude
    public String get_lat_lng(){
        return this._lat_lng;
    }
     
    // setting latitude and longitude
    public void set_lat_lng(String phone_number){
        this._lat_lng = phone_number;
    }
}
