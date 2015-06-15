// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Activity shows all saved location
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;
import java.util.ArrayList;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nanostuffs.cuba.Class.ListDataClass;
import com.nanostuffs.cuba.Class.Save_location;
import com.nanostuffs.cuba.Class.latlngArray;
import com.nanostuffs.cuba.adapter.locationAdapter;
import com.nanostuffs.cuba.database.DatabaseHandler;

public class LocationListActivity extends Activity {

	SharedPreferences sPrefs;
	SharedPreferences.Editor sEdit;
	ListView list;
	LinearLayout lin;
	public static boolean[] isChecked;

	ArrayList<latlngArray> temparray;
	DatabaseHandler db;
	locationAdapter adapter;
	public static boolean flag;
	TextView  tview;
	int lenght;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		sPrefs=PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
		sEdit=sPrefs.edit();
		int size=sPrefs.getInt("size",0);
		db = new DatabaseHandler(this);

		setContentView(R.layout.activity_location_list);
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		actionBarDetails();
		Log.e("aaaaaaaaaaaa","ccccccccccccc    "+RegisterActivity.location.size());
		adapter= new locationAdapter(this, RegisterActivity.location);
		list=(ListView) findViewById(R.id.listView_alter);
		flag=false;
		list.setAdapter(adapter);
		list.setOnItemLongClickListener(listPairedClickItem);
		adapter.notifyDataSetChanged();
	}

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
				for (; i < RegisterActivity.location.size(); i++) {
					if(isChecked[i]==true)
						break;
				}
				if(i==RegisterActivity.location.size()){
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
						AlertDialog.Builder alertDialog = new AlertDialog.Builder(LocationListActivity.this);
					      alertDialog.setMessage("Do you really want to delete location?");
					      alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
					          public void onClick(DialogInterface dialog,int which) {
					        		temparray=new ArrayList<latlngArray>();
									lenght=RegisterActivity.location.size();
									delete();
									deletetable();
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
		TextView back = (TextView) addView.findViewById(R.id.temp); 
		back.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				intent = new Intent(LocationListActivity.this,SettingsActivity.class);
				startActivity(intent);

			}
		});
		ImageView share = (ImageView) addView.findViewById(R.id.shar_image);
		share.setBackgroundResource(R.drawable.add_btn_effect);
		share.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(LocationListActivity.this,MapLocationActivity.class);
				intent.putExtra("activity","locationlist");
				startActivity(intent);
			}
		});
		TextView actionBarText = (TextView) addView
				.findViewById(R.id.action_bar_header);
		actionBarText.setText("My Addresses");
		getActionBar().setCustomView(addView);
	}
	// delete data from list
	public void delete()
	{

		temparray.clear();
		for (int i = 0; i < lenght; i++) {
			if(isChecked[i] == true)
			{
				//Log.e("valuecheck",""+i);
				temparray.add(RegisterActivity.location.get(i));
			}
		}
		Log.e("arraysize",""+temparray.size());
		flag=false;
		RegisterActivity.location.removeAll(temparray);
		adapter.notifyDataSetChanged();
	}
	// delete data from stored data
	public void deletetable()
	{
		List<Save_location> contacts = db.getAllContacts();
		for (Save_location cn : contacts) {
			db.deleteContact(cn);
		}
		for(int i=0;i<RegisterActivity.location.size();i++){
			db.addContact(new Save_location(RegisterActivity.location.get(i).getName(),RegisterActivity.location.get(i).getLat()+"aa"+RegisterActivity.location.get(i).getLng(),RegisterActivity.location.get(i).getHomeName()));
		}
	}
}
