// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs// 
// Activity shows all alert
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.internal.fo;
import com.nanostuffs.cuba.Class.Alert;
import com.nanostuffs.cuba.Class.ListDataClass;
import com.nanostuffs.cuba.adapter.ListAdapter;
import com.nanostuffs.cuba.database.DatabaseHandler;
import com.nanostuffs.cuba.notification.SettingsActivity.deregister;

public class ListActivity extends Activity implements OnItemClickListener{
	ListView alert_list;
	public static boolean[] isChecked;
	int cnt_num=-1;
	ArrayList<ListDataClass> temparray;
	public static ListAdapter adapter;
	public static boolean flag;
	int lenght;
	DatabaseHandler db;
	private Handler mHandler1 = new Handler();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		flag=false;
		actionBarDetails();
		db = new DatabaseHandler(this);
		alert_list=(ListView) findViewById(R.id.listView_main);
		alert_list.setOnItemClickListener(this);
		alert_list.setOnItemLongClickListener(listPairedClickItem);
		DatabaseHandler db = new DatabaseHandler(this);
		List<Alert> contacts = db.getAllContacts_alert(); 
		for (Alert cn : contacts) {
			String log = "Id: "+cn.getID()+" ,Name: " + cn.get_title_string() + " ,Phone: " + cn.get_Data_String() + " ,status" + cn.getStatus();
	    }
		getalert();
		mHandler1.postDelayed(mRunnable, 1000);
	  
	}
	// check weather new alert is there
	public void getalert(){
		if(cnt_num != RegisterActivity.array.size())
		{
			cnt_num = RegisterActivity.array.size();
			
			adapter=new com.nanostuffs.cuba.adapter.ListAdapter(ListActivity.this, RegisterActivity.array);
			alert_list.setAdapter(adapter);
			adapter.notifyDataSetChanged();
		}
	}	

	// handler to check weather new alert is there
	private final Runnable mRunnable = new Runnable() {
		public void run() {
			 getalert();
			mHandler1.postDelayed(mRunnable, 1000);
		}
	};
	private OnItemLongClickListener listPairedClickItem = new OnItemLongClickListener() {
		@Override
		public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
				int arg2, long arg3) {
			// TODO Auto-generated method stub
			flag=true;
			adapter.notifyDataSetChanged();
			return false;
		}
    };
    // action Bar Details
    private void actionBarDetails() {
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		final View addView = getLayoutInflater().inflate(R.layout.action_bar,
				null);
		ImageView right = (ImageView) addView.findViewById(R.id.right_image);
		right.setImageResource(R.drawable.delete);
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int i = 0;
				for (; i < RegisterActivity.array.size(); i++) {
					if(isChecked[i]==true)
						break;
				}
				if(i==RegisterActivity.array.size()){
					if(flag==true){
						flag=false;
						adapter.notifyDataSetChanged();
					}
					else if(flag==false){
						flag=true;
						adapter.notifyDataSetChanged();
					}
				}
				else{
					if(flag==true)
					{
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(ListActivity.this);
					      alertDialog.setMessage("Do you really want to delete alert?");
					      alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					          public void onClick(DialogInterface dialog,int which) {
					        		temparray=new ArrayList<ListDataClass>();
									lenght=RegisterActivity.array.size();
									delete();
					          }
					      });
					      alertDialog.setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
					          public void onClick(DialogInterface dialog,int which) {
					          }
					      });
					      alertDialog.show();
					}
				
					
				}
				
			}
		});
		ImageButton back = (ImageButton) addView.findViewById(R.id.imageButton1); 
		back.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ListActivity.this,HomeActivity.class);
				startActivity(intent);
			}
		});
		TextView actionBarText = (TextView) addView
				.findViewById(R.id.action_bar_header);
		actionBarText.setText("ALERT SUMMARY");
		getActionBar().setCustomView(addView);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		adapter.notifyDataSetChanged();
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int pos, long arg3) {
		RegisterActivity.array.get(pos).setStatus(1);
		ArrayList<ListDataClass> temp_array = new ArrayList<ListDataClass>();
		List<Alert> contacts = db.getAllContacts_alert();
		Collections.reverse(contacts);
		int j=0;
		for (Alert cn : contacts) {
		
			if(j==pos){
	   			db.updateContact_alert(cn);
	   			break;
			}
			j++;
	   	 }
	   	 
	    
		if(pos==0){
			NotificationManager cancel = (NotificationManager) getApplicationContext()
					.getSystemService(Context.NOTIFICATION_SERVICE);
			cancel.cancelAll();
		}
	Intent intent=new Intent(ListActivity.this,DetailListActivity.class);
	Bundle  b=new Bundle();
	b.putInt("pos", pos);
	b.putInt("id", RegisterActivity.array.get(pos).getId());
	b.putString("title", RegisterActivity.array.get(pos).getTitleString());
	b.putString("desc",  RegisterActivity.array.get(pos).getDataString());
	intent.putExtras(b);
	startActivity(intent);
	}
	//delete the entry from list
	public void delete()
	{
		temparray.clear();
		for (int i = 0; i < lenght; i++) {
			if(isChecked[i] == true)
			{
				Log.e("valuecheck",""+i);
				temparray.add(RegisterActivity.array.get(i));
				Log.e("id",""+ RegisterActivity.array.get(i).getId());
				db.deleteContact_alert(RegisterActivity.array.get(i).getId());
			}
		}
		Log.e("arraysize",""+temparray.size());
		flag=false;
		RegisterActivity.array.removeAll(temparray);
		adapter.notifyDataSetChanged();
	}
	//delete the entry from stored data
	public void deletetable()
	{	
	    List<Alert> contacts = db.getAllContacts_alert();
	    for (Alert cn : contacts) {
	    	db.deleteContact_alert(cn.getID());
	    }
	    for(int i=0;i<RegisterActivity.array.size();i++){
	    	db.addContact_alert(new Alert(RegisterActivity.array.get(i).getTitleString(),RegisterActivity.array.get(i).getDataString(),String.valueOf(RegisterActivity.array.get(i).getStatus())));
		}
	}
}
