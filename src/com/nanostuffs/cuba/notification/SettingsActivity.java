// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Activity shows Setting screen
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;

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

import com.nanostuffs.cuba.Class.ListDataClass;
import com.nanostuffs.cuba.adapter.GPSTracker;



import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


public class SettingsActivity extends Activity implements OnClickListener {
	private static final String PREFS_URI = null;
	RelativeLayout layout_vibrate,layout_alter,layout_location,layout_automatic,layout_deregister;
	CheckBox vibrate_check,notification_check,current_check;
	Uri uri;
	AlertDialogManager alert = new AlertDialogManager();
	SharedPreferences sharedPrefs;
	SharedPreferences.Editor editor;
	SharedPreferences  preferences;
	String chosenRingtone;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_settings);
		actionBarDetails();
		layout_automatic=(RelativeLayout) findViewById(R.id.layout_five);
		layout_automatic.setOnClickListener(this);
		layout_deregister=(RelativeLayout) findViewById(R.id.layout_six);
		layout_deregister.setOnClickListener(this);
		layout_vibrate=(RelativeLayout) findViewById(R.id.layout_three);
		layout_vibrate.setOnClickListener(this);
		layout_location=(RelativeLayout) findViewById(R.id.layout_one);
		layout_location.setOnClickListener(this);
		layout_alter=(RelativeLayout) findViewById(R.id.layout_four);
		vibrate_check=(CheckBox) findViewById(R.id.vibrate_chk);
		current_check=(CheckBox) findViewById(R.id.automatic_chk);
		notification_check=(CheckBox) findViewById(R.id.notification_chk);
		layout_alter.setOnClickListener(this);
		preferences = getSharedPreferences("cuba",
				android.content.Context.MODE_PRIVATE);
		sharedPrefs = getSharedPreferences("sharedprefs", 0);
		editor = preferences.edit();
		vibrate_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					vibrate_check.setButtonDrawable(R.drawable.on);
					editor.putBoolean("vibrate", true);
					editor.commit();
				} else
				{
					vibrate_check.setButtonDrawable(R.drawable.off);
					editor.putBoolean("vibrate", false);
					editor.commit();
				}
			}
			});
		current_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					current_check.setButtonDrawable(R.drawable.on);
					editor.putBoolean("current", true);
					editor.commit();
				} else
				{
					current_check.setButtonDrawable(R.drawable.off);
					editor.putBoolean("current", false);
					editor.commit();
				}
			}
		});
		notification_check.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				// TODO Auto-generated method stub
				if(isChecked)
				{
					notification_check.setButtonDrawable(R.drawable.on);  
					editor.putBoolean("notification", true);
					editor.commit();
				} else
				{
					AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
				      alertDialog.setMessage("You will not be able to receive any alert after turning off the notification?");
				      alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
				          public void onClick(DialogInterface dialog,int which) {
				        		notification_check.setButtonDrawable(R.drawable.off);	
								editor.putBoolean("notification", false);
								editor.commit();
				          }
				      });
				      alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
				          public void onClick(DialogInterface dialog,int which) {
				          }
				      });
				      alertDialog.show();
					//You will not be able to receive any alert after turning off the notification
				
				}
			}
		});
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.layout_four:
			SharedPreferences preferences = getSharedPreferences("cuba",
					android.content.Context.MODE_PRIVATE);
			uri = Uri.parse(preferences.getString("ringtone", ""));
			Log.e("iff", ""+uri);
			Intent intent = new Intent(
					RingtoneManager.ACTION_RINGTONE_PICKER);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
					RingtoneManager.TYPE_NOTIFICATION);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
					"Select Notification Tone");
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_DEFAULT, true);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_SHOW_SILENT, false);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_DEFAULT_URI, uri);
			intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
					uri);
			startActivityForResult(intent, 5);
			break;
		case R.id.layout_one:
			Intent intent1 =new Intent(SettingsActivity.this,LocationListActivity.class);
			startActivity(intent1);
			break;
		case R.id.layout_five:
			break;
		case R.id.layout_six:
			deregister();
			break;
		default:
			break;
		}
	}
	
	//when user want to deregister
	public void deregister(){
		if(RegisterActivity.isNetworkAvailable(SettingsActivity.this)){
			AlertDialog.Builder alertDialog = new AlertDialog.Builder(SettingsActivity.this);
		      alertDialog.setTitle("Deregister?");
		      alertDialog.setMessage("You will not able to receive any new alerts on this device");
		      alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
		          public void onClick(DialogInterface dialog,int which) {
					 ProgressDialog progress = new ProgressDialog(SettingsActivity.this);
						new deregister(progress).execute();
		          }
		      });
		      alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
		          public void onClick(DialogInterface dialog,int which) {
		          }
		      });
		      alertDialog.show();	
		}
		else{
			alert.showAlertDialog(SettingsActivity.this,
                 "Unable to Deregister",
                 "No Active internet connectivity to perform De-registration", false);
		}
		
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		if (resultCode == Activity.RESULT_OK && requestCode == 5) {
			Uri uri = intent
					.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
			if (uri != null) {
				Ringtone r = RingtoneManager.getRingtone(this, uri);
				String ringToneName = r.getTitle(this);
				String name = "Notification Tone : " + ringToneName;
				this.chosenRingtone = uri.toString();
				editor.putString("ringtone",this.chosenRingtone);
				editor.commit();
				Log.e("", "ringtonetame"+ringToneName+"chosenRingtone  " + chosenRingtone);
			} else {
				this.chosenRingtone = null;
			}
		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		SharedPreferences preferences = getSharedPreferences("cuba",
				android.content.Context.MODE_PRIVATE);
		Boolean vibrateflag=preferences.getBoolean("vibrate", true);
		Boolean notificationflag=preferences.getBoolean("notification", true);
		Boolean alert_on_current=preferences.getBoolean("current", true);
		if(vibrateflag)
		{
			vibrate_check.setButtonDrawable(R.drawable.on);
			vibrate_check.setChecked(true);

		}else
		{
			vibrate_check.setButtonDrawable(R.drawable.off);
			vibrate_check.setChecked(false);
		}
		if(alert_on_current)
		{
			current_check.setButtonDrawable(R.drawable.on);
			current_check.setChecked(true);

		}else
		{
			current_check.setButtonDrawable(R.drawable.off);
			current_check.setChecked(false);
		}
		if(notificationflag)
		{
			notification_check.setButtonDrawable(R.drawable.on);
			notification_check.setChecked(true);	
		}else
		{
			notification_check.setButtonDrawable(R.drawable.off);
			notification_check.setChecked(false);
		}
	}

	private void actionBarDetails() {
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		final View addView = getLayoutInflater().inflate(R.layout.action_bar,
				null);
		ImageView right = (ImageView) addView.findViewById(R.id.right_image);
		right.setVisibility(View.INVISIBLE);
		TextView back = (TextView) addView.findViewById(R.id.temp); 
		back.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(SettingsActivity.this,HomeActivity.class);
				startActivity(intent);
			}
		});
		TextView actionBarText = (TextView) addView
				.findViewById(R.id.action_bar_header);
		actionBarText.setText("SETTINGS");
		getActionBar().setCustomView(addView);
	}
	public String deregister_user(){
		String Response="";
		HttpClient httpClient = new DefaultHttpClient();
		String hdId = sharedPrefs.getString("hlid", "xyz");
	     if(hdId.equalsIgnoreCase("xyz"))
	    	hdId = "0";
		try {
		    HttpPost request = new HttpPost(RegisterActivity.url+"deregisterid");
		    JSONObject jsonObject = new JSONObject();
            jsonObject.accumulate("hdId", hdId);
            jsonObject.accumulate("deviceRegId", RegisterActivity.regId);
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
	  public class deregister extends AsyncTask<String, Void, String> {
		    private ProgressDialog progress;
		    String string;
		public deregister(ProgressDialog progress) {
			
				this.progress = progress;
				this.string = string;
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
				
				
				String resultdata = deregister_user();
		    	
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
					AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
			        alertDialog.setTitle("Please try again.");
			        alertDialog.setMessage("Unable to Deregister your device.");
				    alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
				    public void onClick(DialogInterface dialog, int which) {
				            
				    	}
				    });
				    alertDialog.show();
				}
				else{
					responseObj = new JSONObject(result);
					if(responseObj.getString("success").equalsIgnoreCase("true")){
						Toast.makeText(getApplicationContext(),"Deregister Sucessfully...", Toast.LENGTH_SHORT).show();
						SharedPreferences.Editor editors = sharedPrefs.edit();
						editors.putBoolean("111", false);
						editors.commit();
						Intent intent = new Intent(SettingsActivity.this,RegisterActivity.class);
						startActivity(intent);
					}
					else if(responseObj.getString("success").equalsIgnoreCase("false")){
						 AlertDialog alertDialog = new AlertDialog.Builder(SettingsActivity.this).create();
						 
					        // Setting Dialog Title
					        alertDialog.setTitle("Please try again.");
					        
					        // Setting Dialog Message
					        alertDialog.setMessage("Unable to Deregister your device.");
					 
					        // Setting OK Button
					        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
					            public void onClick(DialogInterface dialog, int which) {
					            
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
}
