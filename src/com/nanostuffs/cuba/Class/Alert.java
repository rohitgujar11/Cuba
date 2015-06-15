// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
package com.nanostuffs.cuba.Class;

public class Alert {
    
    //private variables
    int _id;
    String _title_string;
    String _Data_String;
    String _status;
     
    // Empty constructor
    public Alert(){
         
    }
    // constructor
    public Alert(int id, String _title_string, String _Data_String,String _status){
        this._id = id;
        this._title_string = _title_string;
        this._Data_String = _Data_String;
        this._status = _status;
    }
     
    // constructor
    public Alert(String _title_string, String _Data_String, String _status){
        this._title_string = _title_string;
        this._Data_String = _Data_String;
        this._status = _status;
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
    public String get_title_string(){
        return this._title_string;
    }
     
    // setting name
    public void set_title_string(String name){
        this._title_string = name;
    }
     
    // getting latitude and longitude
    public String get_Data_String(){
        return this._Data_String;
    }
     
    // setting latitude and longitude
    public void set_Data_String(String phone_number){
        this._Data_String = phone_number;
    }
    
 // getting phone number
    public String getStatus(){
        return this._status;
    }
     
    // setting phone number
    public void setStatus(String _status){
        this._status = _status;
    }
}
