// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Activity to register user 
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;

import static com.nanostuffs.cuba.notification.CommonUtilities.DISPLAY_MESSAGE_ACTION;
import static com.nanostuffs.cuba.notification.CommonUtilities.EXTRA_MESSAGE;
import static com.nanostuffs.cuba.notification.CommonUtilities.SENDER_ID;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gcm.GCMRegistrar;
import com.nanostuffs.cuba.Class.Alert;
import com.nanostuffs.cuba.Class.ListDataClass;
import com.nanostuffs.cuba.Class.Save_location;
import com.nanostuffs.cuba.Class.latlngArray;

import com.nanostuffs.cuba.database.DatabaseHandler;


public class RegisterActivity extends Activity {

	public static ArrayList <latlngArray> location=new ArrayList<latlngArray>();
	public static ArrayList<ListDataClass> array =new ArrayList<ListDataClass>();


	public static final String PREFS_LOGIN = "LOGIN";
	public static final String PREFS_NAME = "NAME";
	AsyncTask<Void, Void, Void> mRegisterTask;
	Button Btn_register;
	static String notificationID = "";
	boolean completed;
	SharedPreferences sharedPrefs;
	String deviceId,jsonData_demo="",coords="";
	public static String regId;
	//public static String url = "http://qa.donald.ums2.no/api/channel/smartphone/";
	public static String url = "http://dev.pluto.ums2.no/api/channel/smartphone/";
	boolean flag1;
	DatabaseHandler db;
	int flag=0;
	ConnectionDetector cd;
	AlertDialogManager alert = new AlertDialogManager();
	private Thread thread;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		db = new DatabaseHandler(this);
		String text = "";
		flag1=true;
		cd = new ConnectionDetector(getApplicationContext());
	        // Check if Internet present
	    if (!cd.isConnectingToInternet()) {
	       // Internet Connection is not present
	       alert.showAlertDialog(RegisterActivity.this,
	       "Internet Connection Error",
	       "Please connect to working Internet connection", false);
	       // stop executing code by return
	    }
	    // Make sure the device has the proper dependencies.
	    GCMRegistrar.checkDevice(this);
	    // Make sure the manifest was properly set - comment out this line
	    // while developing the app, then uncomment it when it's ready.
	    GCMRegistrar.checkManifest(this);
	    registerReceiver(mHandleMessageReceiver, new IntentFilter(
	             DISPLAY_MESSAGE_ACTION));
	    // Get GCM registration id
	    regId = GCMRegistrar.getRegistrationId(this);
	    // Check if regid already presents
	    if (regId.equals("")) {
	        // Registration is not present, register now with GCM           
	        GCMRegistrar.register(this, SENDER_ID);
	    } else {
	        // Device is already registered on GCM
	        if (GCMRegistrar.isRegisteredOnServer(this)) {
	            // Skips registration.              
	            Toast.makeText(getApplicationContext(), "Already registered with GCM", Toast.LENGTH_LONG).show();
	        } else {
	        // Try to register again, but not in the UI thread.
	        // It's also necessary to cancel the thread onDestroy(),
	        // hence the use of AsyncTask instead of a raw thread.
	        final Context context = this;
	        mRegisterTask = new AsyncTask<Void, Void, Void>() {
	        @Override
	               protected Void doInBackground(Void... params) {
	            // Register on our server
	            // On server creates a new user
	               ServerUtilities.register(context,  regId);
	                        return null;
		               }
		               @Override
		               protected void onPostExecute(Void result) {
		                      mRegisterTask = null;
		               }
	                };
	                mRegisterTask.execute(null, null, null);
	         }
	     }
	    // get all stored My Location     
		RegisterActivity.location.clear();
		List<Save_location> contacts = db.getAllContacts();
		int i=0;
		for (Save_location cn : contacts) {
			latlngArray aa= new latlngArray();
			aa.setName(cn.getName());
			if(cn.get_lat_lng()!= null){
				String[] parts = cn.get_lat_lng().split("aa");
				if(parts.length>0)
				{
					String part1 = parts[0];
					String part2 = parts[1];
					aa.setLat(Double.parseDouble(part1));
					aa.setLng(Double.parseDouble(part2));
				}
				aa.setHomeName(cn.getNameLocation());
			}
			RegisterActivity.location.add(i, aa);
			i++;
		}
		// get all alert
		RegisterActivity.array.clear();
		List<Alert> contacts_alert = db.getAllContacts_alert();
		int j=0;
		for (Alert cn : contacts_alert) {
			ListDataClass abc= new ListDataClass();
			abc.setTitleString(cn.get_title_string());
			abc.setDataString(cn.get_Data_String());
			abc.setStatus(Integer.parseInt(cn.getStatus()));
			abc.setId(cn.getID());
			Log.e("ID", ""+abc.getId());
			RegisterActivity.array.add(j, abc);
			j++;
		}
		Collections.reverse(RegisterActivity.array);
		sharedPrefs = getSharedPreferences("sharedprefs", 0);
		if(!RegisterActivity.isNetworkAvailable(this))
			flag=1;
		Btn_register=(Button) findViewById(R.id.register_btn);
		Intent intent = getIntent();
		/*if(null != intent)*/
	

		// check if user is already register or not
	
		text= intent.getStringExtra("GCM");
		if(text == null)
			text = "";
		if(text.equalsIgnoreCase("getnotification")){
			Intent intent1 = new Intent();
			intent1.setClass(RegisterActivity.this, ListActivity.class);
			startActivity(intent1);
			finish();
		}
		else{
			completed = sharedPrefs.getBoolean("111", false);
			if (completed == true) {
				if(flag1)
				{
					Intent intent1 = new Intent();
					intent1.setClass(RegisterActivity.this, HomeActivity.class);
					startActivity(intent1);
					finish();
				}
			}
		}
		try {
			Btn_register.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if(regId.length()==0){
						Toast.makeText(RegisterActivity.this, "Please try again...", Toast.LENGTH_SHORT).show();
					}
					else{
						Log.e("aaaaaaaaaaaaaa","bbbbbbbbbbbbbbbb    "+regId);
						if(RegisterActivity.isNetworkAvailable(RegisterActivity.this)){
							ProgressDialog progress = new ProgressDialog(RegisterActivity.this);
							new Register(progress).execute();
						}
						else{
							 alert.showAlertDialog(RegisterActivity.this,
					                    "Unable to register",
					                    "Please connect to working Internet connection", false);
						}
					}
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	@Override
	protected void onDestroy() {
		if (mRegisterTask != null) {
			mRegisterTask.cancel(true);
		}
		try{
			unregisterReceiver(mHandleMessageReceiver);
			GCMRegistrar.onDestroy(this);
			super.onDestroy();
		}
		catch(Exception e){
		}
		finally{
		}
	}
	private void checkNotNull(Object reference, String name) {
		if (reference == null) {
			throw new NullPointerException(
					getString(R.string.error_config, name));
		}
	}

	private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String newMessage = intent.getExtras().getString(EXTRA_MESSAGE);
		}
	};
	// Http request when user register 
	public String register() {
		String Response="";
		HttpClient httpClient = new DefaultHttpClient();
		String hdId = sharedPrefs.getString("hlid", "xyz");
	     if(hdId.equalsIgnoreCase("xyz"))
	    	hdId = "0";
	     try {
		    HttpPost request = new HttpPost(RegisterActivity.url+"registerid");
		    JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("hdId", hdId);
            jsonObject.accumulate("deviceRegId", regId);
            jsonObject.accumulate("deviceType", "A");
		    StringEntity params =new StringEntity(jsonObject.toString());
		    Log.e("qqqqqqqq", "ggggg   "+jsonObject.toString());
		    request.addHeader("content-type", "application/json");
		    request.addHeader("Accept","application/json");
		    request.setEntity(params);
		    HttpResponse response = httpClient.execute(request);
		    HttpEntity resEntity = response.getEntity();
		    int cnt = response.getStatusLine().getStatusCode();
		    Log.e("aaaaaaaaaaaaaa","bbbbbbbbbbbbbbbb    "+cnt);
		    Response=EntityUtils.toString(resEntity);
		    Log.e("qqqqqqqq", "ggggg   "+Response);
		    // handle response here...
		}catch (Exception ex) {
		    // handle exception here
		} finally {
		    httpClient.getConnectionManager().shutdown();
		}
		return Response;
	} 
	// Perform background operation when user want to register
	public class Register extends AsyncTask<String, Void, String> {
		private ProgressDialog progress;
	
		public Register(ProgressDialog progress) {
			this.progress = progress;
			progress.setMessage("Please Wait...");
		}
		
		public void onPreExecute() {
			progress.show();
			progress.setCancelable(false);
			Log.v("", "onPreExecute()");	
		}
		
		@Override
		protected String doInBackground(String... params) {
			Log.v("", "doInBackground(Void... params)");	
			String resultdata =register();

			return resultdata;
		}

		public void onPostExecute(String result){ 
			Log.v("","onPostExecute()");
			JSONObject responseObj;
			if(progress.isShowing()){
				progress.dismiss();

			}
			try {
				if(result.length()==0){
					//Toast.makeText(MainActivity.this, "Incorrect Usre name or Password", Toast.LENGTH_LONG).show();

					 AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
					 
				        // Setting Dialog Title
				        alertDialog.setTitle("Please try again.");
				        
				        // Setting Dialog Message
				        alertDialog.setMessage("Unable to register your device.");
				 
				      
				            // Setting alert dialog icon
				 
				        // Setting OK Button
				        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				            public void onClick(DialogInterface dialog, int which) {
				            	Intent reg_done=new Intent(RegisterActivity.this, RegisterActivity.class);
								startActivity(reg_done);
				            }
				        });
				 
				        // Showing Alert Message
				        alertDialog.show();
				
				}
				else{
					responseObj = new JSONObject(result);
					if(responseObj.getString("success").equalsIgnoreCase("true")){
						String hlid = responseObj.getString("hdId");
						SharedPreferences.Editor editors = sharedPrefs.edit();
						editors.putBoolean("111", true);
						editors.putString("hlid", hlid);
						editors.commit();
						Intent reg_done=new Intent(RegisterActivity.this, HomeActivity.class);
						startActivity(reg_done);
					}
					else if(responseObj.getString("success").equalsIgnoreCase("false")){
						 AlertDialog alertDialog = new AlertDialog.Builder(RegisterActivity.this).create();
					        // Setting Dialog Title
					        alertDialog.setTitle("Please try again.");
					        // Setting Dialog Message
					        alertDialog.setMessage("Unable to register your device.");
					        // Setting alert dialog icon
					        // Setting OK Button
					        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int which) {
					            	Intent reg_done=new Intent(RegisterActivity.this, RegisterActivity.class);
									startActivity(reg_done);
					            }
					        });
					 
					        // Showing Alert Message
					        alertDialog.show();
					}
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
	}
	// when user press back button
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		onbackPressed();
	}
	public void onbackPressed() {
		Intent intent = new Intent(Intent.ACTION_MAIN);
		intent.addCategory(Intent.CATEGORY_HOME);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		startActivity(intent);
	}
	public static boolean isNetworkAvailable(Context context) 
	{
	    ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    if (connectivity != null) 
	    {
	        NetworkInfo[] info = connectivity.getAllNetworkInfo();
	        if (info != null) 
	        {
	            for (int i = 0; i < info.length; i++) 
	            {
	                Log.i("Class", info[i].getState().toString());
	                if (info[i].getState() == NetworkInfo.State.CONNECTED) 
	                {
	                    return true;
	                }
	            }
	        }
	    }
	  // Toast.makeText(context, "connection is not available", Toast.LENGTH_SHORT).show();
	    return false;
	}
}

