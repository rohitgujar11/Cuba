// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Activity shows main screen 
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nanostuffs.cuba.Class.Alert;
import com.nanostuffs.cuba.Class.ListDataClass;
import com.nanostuffs.cuba.database.DatabaseHandler;

public class HomeActivity extends Activity implements OnClickListener {
	private ImageButton mAlert;
	private ImageButton mSettings;
	private ImageButton mAbout;
	TextView count;
	ImageView location;
	RelativeLayout top;
	DatabaseHandler db;
	int cnt_num=-1;
	private Handler mHandler1 = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.home);
		db = new DatabaseHandler(this);
		location=(ImageView) findViewById(R.id.location);
		location.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(HomeActivity.this,MapLocationActivity.class);
				intent.putExtra("activity","home");
				startActivity(intent);	
			}
		});
		initializeComponents();
		top=(RelativeLayout) findViewById(R.id.alert_top);
		String activity = getCurrentTopActivity();
		System.out.println("activity name :- "+activity);
		mHandler1.postDelayed(mRunnable, 1000);
		
	}
	private final Runnable mRunnable = new Runnable() {
		public void run() {
			getcount();
			mHandler1.postDelayed(mRunnable, 1000);
		}
	};
	public void getcount_onresume() {
		
			cnt_num = RegisterActivity.array.size();
			int cnt=0;
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
			for (int i = 0; i < RegisterActivity.array.size(); i++) {
				if(RegisterActivity.array.get(i).getStatus()==0)
					cnt++;
			}
			if(cnt>0){
				count.setVisibility(View.VISIBLE);
				count.setText(String.valueOf(cnt));
			}
			else{
				count.setVisibility(View.INVISIBLE);
		}
		
	}
	public void getcount() {
		if(cnt_num != RegisterActivity.array.size())
		{
			cnt_num = RegisterActivity.array.size();
			int cnt=0;
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
			for (int i = 0; i < RegisterActivity.array.size(); i++) {
				if(RegisterActivity.array.get(i).getStatus()==0)
					cnt++;
			}
			if(cnt>0){
				count.setVisibility(View.VISIBLE);
				count.setText(String.valueOf(cnt));
			}
			else{
				count.setVisibility(View.INVISIBLE);
		}
		}
	}	
	private void initializeComponents() {
		count = (TextView) findViewById(R.id.count);
		mAlert = (ImageButton) findViewById(R.id.alert);
		mAlert.setOnClickListener(this);
		mSettings = (ImageButton) findViewById(R.id.settings);
		mSettings.setOnClickListener(this);
		mAbout = (ImageButton) findViewById(R.id.about);
		mAbout.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.alert:
			Log.e("", "alert");
			alert();
			break;
		case R.id.settings:
			settings();
			break;
		case R.id.about:
			break;
		default:
			break;
		}
	}
	
	// navigate user to Setting
	private void settings() {

		Intent intent =new Intent(HomeActivity.this,SettingsActivity.class);
		startActivity(intent);
		overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_right);
	}
	// get the actvity which is on top 
	private String getCurrentTopActivity() {

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);
        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);
        return ar.topActivity.getClassName().toString();
    }
	// navigate user to Alert
	private void alert() {
		  DatabaseHandler db;
	      db = new DatabaseHandler(this);
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
	    Intent intent =new Intent(HomeActivity.this,ListActivity.class);
	    startActivity(intent);
	    overridePendingTransition(R.anim.slide_in_left,
				R.anim.slide_out_right);
	}		
	// when back button is press by user
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			  Intent intent = new Intent(Intent.ACTION_MAIN);
	          intent.addCategory(Intent.CATEGORY_HOME);
	          intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	          startActivity(intent);
			overridePendingTransition(R.anim.slide_in_left,
					R.anim.slide_out_right);
		}
		return true;
	}
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		getcount_onresume();
		if(RegisterActivity.location.size()>0)
		{
			top.setVisibility(View.INVISIBLE);
		}
		else
		{
			top.setVisibility(View.VISIBLE);
		}
	}
}
