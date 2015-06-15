// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;

import static com.nanostuffs.cuba.notification.CommonUtilities.SENDER_ID;
import static com.nanostuffs.cuba.notification.CommonUtilities.displayMessage;

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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Vibrator;
import android.support.v4.app.NotificationCompat;
import android.text.Html;
import android.util.Log;

import com.google.android.gcm.GCMBaseIntentService;
import com.google.android.gcm.GCMRegistrar;
import com.nanostuffs.cuba.Class.Save_location;
import com.nanostuffs.cuba.Class.Alert;
import com.nanostuffs.cuba.Class.ListDataClass;
import com.nanostuffs.cuba.Class.latlngArray;
import com.nanostuffs.cuba.adapter.GPSTracker;
import com.nanostuffs.cuba.database.DatabaseHandler;


/**
 * IntentService responsible for handling GCM messages.
 */
public class GCMIntentService extends GCMBaseIntentService {
	String alertid="";
	String alertRefNo = "";
	static String alertMessageHeader="";
	String alertMessage="";
	static int notificationID = 132456;
	static Context alert_contenx;
	Intent alert_intent;
	@SuppressWarnings("hiding")
	static String jsonData;
	@SuppressWarnings("hiding")
	static String coords;
	static String ringtone;
	 SharedPreferences  preferences;
		DatabaseHandler db;
	static int get_msg=0;
	private static final String TAG = "GCMIntentService";

	public GCMIntentService() {
		super(SENDER_ID);
	}

	@Override
	protected void onRegistered(Context context, String registrationId) {
		Log.i(TAG, "Device registered: regId = " + registrationId);
		RegisterActivity.regId = registrationId;
		displayMessage(context, getString(R.string.gcm_registered));
		ServerUtilities.register(context, registrationId);
	}

	@Override
	protected void onUnregistered(Context context, String registrationId) {
		Log.i(TAG, "Device unregistered");
		displayMessage(context, getString(R.string.gcm_unregistered));
		if (GCMRegistrar.isRegisteredOnServer(context)) {
			ServerUtilities.unregister(context, registrationId);
		} else {
			// This callback results from the call to unregister made on
			// ServerUtilities when the registration to the server failed.
			Log.i(TAG, "Ignoring unregister callback");
		}
	}
	// when user received push notification
	@Override
	protected void onMessage(Context context, Intent intent) {
		
		try {
			GPSTracker gps;
	        gps = new GPSTracker(context);
	        System.out.println("message is :- " +intent.getExtras().toString() );
				// check if GPS enabled		
		        if(gps.canGetLocation()){
		        }else{
		        }
			} catch (Exception e) {
				String text = "";
				// TODO: handle exception
			}
			preferences = getSharedPreferences("cuba",
				android.content.Context.MODE_PRIVATE);
			alert_contenx = context;
			alert_intent = intent;
			ArrayList <latlngArray> temp_location=new ArrayList<latlngArray>();
			db = new DatabaseHandler(this);
			List<Save_location> contacts = db.getAllContacts();
			String message = "get msg";
			ArrayList<Double> lat=new ArrayList<Double>();
			ArrayList<Double> lng=new ArrayList<Double>();
			int cnt_lat=0;
			int cnt_lng=0;
			System.out.println("alert id :- "+intent.getExtras().toString());
			String respose = intent.getExtras().getString("data");
			try {
				JSONObject responseObj = new JSONObject(respose);
				alertRefNo = responseObj.getString("id");
				JSONArray ja = responseObj.getJSONArray("s");
				for (int i = 0; i < ja.length(); i++) {
					if(i%2==0){
						lat.add(cnt_lat, Double.parseDouble(ja.getString(i)));
						cnt_lat++;
					}
					else{
						lng.add(cnt_lng, Double.parseDouble(ja.getString(i)));
						cnt_lng++;
					}
				}
			} catch (JSONException e) {
			// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Boolean alert_on_current=preferences.getBoolean("current", true);
			if(alert_on_current == true){
				try {
					GPSTracker gps;
			        gps = new GPSTracker(context);
					// check if GPS enabled		
				    if(gps.canGetLocation()){
				    	double latitude = gps.getLatitude();
				        double longitude = gps.getLongitude();
				        for (int i = 0; i < 10; i++) {
				        	if(String.valueOf(latitude).length()==3){
				       			GPSTracker gps1 = new GPSTracker(context);;
					       		Thread.sleep(1000);
					       		gps1.canGetLocation();
					       		latitude = gps1.getLatitude();
				        		longitude = gps1.getLongitude();
				        	}
				        	if(String.valueOf(latitude).length()>3){
				        		latlngArray abc= new latlngArray();
				        		abc.setLat(latitude);
				        		abc.setLng(longitude);
				        		temp_location.add(abc);
				        		break;
				        	}
						}
				     }
				} catch (Exception e) {
				// TODO: handle exception
			}
		}
		int i1=0;
		for (Save_location cn : contacts) {
			latlngArray aa= new latlngArray();
			aa.setName(cn.getName());
			aa.setHomeName(cn.getNameLocation());
			if(cn.get_lat_lng()!= null)
			{
				String[] parts = cn.get_lat_lng().split("aa");
				String part1 = parts[0];
				String part2 = parts[1];
				aa.setLat(Double.parseDouble(part1));
				aa.setLng(Double.parseDouble(part2));
			}
			temp_location.add(aa);
			i1++;
		}
		for (int k = 0; k <temp_location.size(); k++) {
			Boolean  oddNodes=false;
			Double x =temp_location.get(k).getLat();
			Double y = temp_location.get(k).getLng();
			int i=0,j=lat.size()-1 ;
			
			for (; i<lat.size(); i++) 
			{	
				if (lng.get(i)<y && lng.get(j)>=y ||  lng.get(j)<y && lng.get(i)>=y)
				{
					if (lat.get(i)+(y-lng.get(i))/(lng.get(j)-lng.get(i))*(lat.get(j)-lat.get(i))<x)
					{
						oddNodes=!oddNodes; 
					}
				}
				j=i;
			}
			System.out.println("inside is true/false "+oddNodes);
			if(oddNodes==true){
				Boolean temp_notification = preferences.getBoolean("notification",true);
				if(temp_notification){
					new ireceive().execute();
					break;
				}
			}
		}
	}
	// perform background operation to get the alert header 
	public  class ireceive extends AsyncTask<String, Void, String> {
		
		public ireceive() {
		}
		public void onPreExecute() {
			//progress.show();
			Log.v("", "onPreExecute()");
		}
		@Override
		protected String doInBackground(String... params) {
			Log.v("", "doInBackground(Void... params)");	
			String resultdata =ireceive();
			return resultdata;
		}
		public void onPostExecute(String result){ 
			Log.v("","onPostExecute()");
			
			try
			{
				JSONObject responseObj;
				responseObj = new JSONObject(result);
				if(responseObj.getString("success").equalsIgnoreCase("true")){
					alertMessage = responseObj.getString("alertMessage");
					alertMessageHeader = responseObj.getString("alertMessageHeader");
					Boolean vibr=preferences.getBoolean("vibrate", true);
					db.addContact_alert(new Alert(alertMessageHeader,alertMessage,"0"));
					if(vibr==true){
						Vibrator v = (Vibrator)  alert_contenx.getSystemService(alert_contenx.VIBRATOR_SERVICE);
					 // Vibrate for 500 milliseconds
						v.vibrate(500);
					}
					ringtone = preferences.getString("ringtone","");
					RegisterActivity.array.clear();
					List<Alert> contacts_alert = db.getAllContacts_alert();
					int j=0;
					for (Alert cn : contacts_alert) {
						ListDataClass abc= new ListDataClass();
						abc.setTitleString(cn.get_title_string());
						abc.setDataString(cn.get_Data_String());
						abc.setStatus(Integer.parseInt(cn.getStatus()));
						abc.setId(cn.getID());
						RegisterActivity.array.add(j, abc);
						j++;
					}
					Collections.reverse(RegisterActivity.array);
					generateNotification(alert_contenx, alertMessageHeader,alert_intent);
				}
			}catch(Exception e){
			}
		}
	}
	public String ireceive() {
	String Response="";
	HttpClient httpClient = new DefaultHttpClient();
	try {
	    HttpPost request = new HttpPost(RegisterActivity.url+"ireceive");
	    JSONObject jsonObject = new JSONObject();
        jsonObject.accumulate("alertRefNo",alertRefNo);
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
	@Override
	protected void onDeletedMessages(Context context, int total) {
		Log.i(TAG, "Received deleted messages notification");
		String message = getString(R.string.gcm_deleted, total);
		displayMessage(context, message);
		// notifies user
	}

	@Override
	public void onError(Context context, String errorId) {
		Log.i(TAG, "Received error: " + errorId);
		displayMessage(context, getString(R.string.gcm_error, errorId));
	}

	@Override
	protected boolean onRecoverableError(Context context, String errorId) {
		// log message
		Log.i(TAG, "Received recoverable error: " + errorId);
		displayMessage(context,
				getString(R.string.gcm_recoverable_error, errorId));
		return super.onRecoverableError(context, errorId);
	}

	/**
	 * Issues a notification to inform the user that server has sent a message.
	 */
	private static void generateNotification(Context context, String message,Intent src) {
		

			
		try {
			
				int icon = R.drawable.ic_launcher;
				long when = System.currentTimeMillis();
				NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
				NotificationManager notificationManager = (NotificationManager) context
						.getSystemService(Context.NOTIFICATION_SERVICE);
				Notification notification = new Notification(icon, Html.fromHtml(message), when);
				notificationManager.notify(notificationID, mBuilder.build());
				String title = context.getString(R.string.app_name);
				Intent notificationIntent = new Intent(context, RegisterActivity.class);
				notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
				notificationIntent.putExtra("GCM", "getnotification");
				notificationIntent.putExtra("notificationID",notificationID);
				notificationIntent.putExtra("coords", coords);
				notificationIntent.putExtras(src);
				if(ringtone == ""){
					 notification.defaults |= Notification.DEFAULT_SOUND;
				}
				else{
					notification.sound = Uri.parse(ringtone);
				}
				PendingIntent contentIntent = contentIntent = PendingIntent.getActivity(context,
	                    (int) (Math.random() * 100), notificationIntent,
	                    PendingIntent.FLAG_UPDATE_CURRENT);
				notification.setLatestEventInfo(context, title, message, contentIntent);
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notificationManager.notify(0, notification);
		} catch (Exception e) {
			Log.i(TAG, "Received deleted messages notification");
	
			// TODO: handle exception
		}
	}

}
