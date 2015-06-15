// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
//Set all alerts
package com.nanostuffs.cuba.adapter;

import java.util.ArrayList;
import java.util.Collections;

import android.app.Activity;
import android.content.Context;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanostuffs.cuba.Class.ListDataClass;
import com.nanostuffs.cuba.database.DatabaseHandler;
import com.nanostuffs.cuba.notification.ListActivity;
import com.nanostuffs.cuba.notification.R;
import com.nanostuffs.cuba.notification.RegisterActivity;

public class ListAdapter extends ArrayAdapter<ListDataClass>  {

	private Context mContext;
	private ArrayList<ListDataClass> mValues;
	public boolean listflag;
	DatabaseHandler db;
	
	public ListAdapter(Context context, ArrayList<ListDataClass> list)
	{
		super(context,R.layout.listview);
		db = new DatabaseHandler(context);
		this.mContext = context;
	
		this.mValues = list;
	
		listflag=true;
		 
		
		
	}
	@Override
	public int getCount() {
		if(listflag)
		{
			ListActivity.isChecked = new boolean[mValues.size()];
			listflag=false;
		}
		// TODO Auto-generated method stub
		return mValues.size();
	}
	//Set all alerts
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		Activity activity = (Activity) this.mContext;
		ListDataClass hm=this.mValues.get(position);
		Log.e("list", ""+hm.getId());
		if(convertView == null)
		{
			LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.listview, null);
			view = convertView;
		}
		else
		{
			view = convertView;
		}
		CheckBox chk=(CheckBox) view.findViewById(R.id.checkBox_delete);
		chk.setChecked(false);
		if(ListActivity.flag)
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
					ListActivity.isChecked[position]=true;
					
				}else
				{
					ListActivity.isChecked[position]=false;
				}
				
			}
		});
		TextView title=(TextView) view.findViewById(R.id.textView_title);
		TextView data=(TextView) view.findViewById(R.id.textView_desc);
		ImageView imgae_check=(ImageView) view.findViewById(R.id.imageView1);
		if(hm.getStatus()==0)
		{
			imgae_check.setImageResource(R.drawable.unopened);
		} else
		{
			imgae_check.setImageResource(R.drawable.opened);
		}
		title.setText( Html.fromHtml(hm.getTitleString()));
		data.setText( Html.fromHtml(hm.getDataString()));
		return view;
	}
}
