// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// set all My Location  
package com.nanostuffs.cuba.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.TextView;

import com.nanostuffs.cuba.Class.latlngArray;
import com.nanostuffs.cuba.database.DatabaseHandler;
import com.nanostuffs.cuba.notification.LocationListActivity;
import com.nanostuffs.cuba.notification.R;


public class locationAdapter extends ArrayAdapter<latlngArray>  {

	private Context mContext;
	private ArrayList<latlngArray> mValues;
	public boolean listflag;
	DatabaseHandler db;
	public locationAdapter(Context context, ArrayList<latlngArray> list)
	{
		super(context,R.layout.text_location);
		db = new DatabaseHandler(context);
		this.mContext = context;
		this.mValues = list;
	}
	
	@Override
	public int getCount() {
		LocationListActivity.isChecked = new boolean[mValues.size()];

		for (int i = 0; i < LocationListActivity.isChecked.length; i++) {
			LocationListActivity.isChecked[i] = false;
		}
		return mValues.size();
	}
	// set all My Location  
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		Activity activity = (Activity) this.mContext;
		latlngArray hm=this.mValues.get(position);
		if(convertView == null)
		{
			LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.text_location, null);
			view = convertView;
		}
		else
		{
			view = convertView;
		}
		TextView title=(TextView) view.findViewById(R.id.textView_title);
		title.setText(hm.getName());
		TextView name=(TextView) view.findViewById(R.id.textView_Name);
		name.setText(hm.getHomeName());
		CheckBox chk=(CheckBox) view.findViewById(R.id.checkBox_delete);
		chk.setChecked(false);
		if(LocationListActivity.flag)
		{
			chk.setVisibility(View.VISIBLE);
		}else
		{
			chk.setVisibility(View.INVISIBLE);
		}
		chk.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked)
				{
					LocationListActivity.isChecked[position]=true;
				}else
				{
					LocationListActivity.isChecked[position]=false;
				}

			}
		});
		return view;
	}
}