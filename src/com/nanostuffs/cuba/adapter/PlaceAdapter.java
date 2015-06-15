// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
package com.nanostuffs.cuba.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import com.nanostuffs.cuba.Class.RefernaceKeyClass;
import com.nanostuffs.cuba.notification.R;

public class PlaceAdapter extends ArrayAdapter<RefernaceKeyClass>  {

	private Context mContext;
	private ArrayList<RefernaceKeyClass> items;
	private ArrayList<RefernaceKeyClass> itemsAll;
	private ArrayList<RefernaceKeyClass> suggestions;
	private int viewResourceId;
	public PlaceAdapter(Context context, int viewResourceId, ArrayList<RefernaceKeyClass> items) {
		super(context, viewResourceId, items);
		this.mContext=context;
		this.items = items;
		this.itemsAll = (ArrayList<RefernaceKeyClass>) items.clone();
		this.suggestions = new ArrayList<RefernaceKeyClass>();
		this.viewResourceId = viewResourceId;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		View view;
		Activity activity = (Activity) this.mContext;
		RefernaceKeyClass hm=this.items.get(position);
		if(convertView == null)
		{
			LayoutInflater inflater = activity.getLayoutInflater();
			convertView = inflater.inflate(R.layout.textviewlistview, null);
			view = convertView;
		}
		else
		{
			view = convertView;
		}
		TextView title=(TextView) view.findViewById(R.id.textView_name);
		title.setText(hm.getName());
		return view;
	}
	@Override
	public Filter getFilter() {
		// TODO Auto-generated method stub
		return nameFilter;
	}
	Filter nameFilter = new Filter() {
		@Override
		public String convertResultToString(Object resultValue) {
			String str = ((RefernaceKeyClass)(resultValue)).getName(); 
			return str;
		}
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if(constraint != null) {
				suggestions.clear();
				for (RefernaceKeyClass customer : itemsAll) {
					if(customer.getName().toLowerCase().startsWith(constraint.toString().toLowerCase())){
						suggestions.add(customer);
					}
				}
				FilterResults filterResults = new FilterResults();
				filterResults.values = suggestions;
				filterResults.count = suggestions.size();
				return filterResults;
			} else {
				return new FilterResults();
			}
		}
		@Override
		protected void publishResults(CharSequence constraint, FilterResults results) {
			ArrayList<RefernaceKeyClass> filteredList = (ArrayList<RefernaceKeyClass>) results.values;
			if(results != null && results.count > 0) {
				clear();
				for (RefernaceKeyClass c : filteredList) {
					add(c);
				}
				notifyDataSetChanged();
			}
		}
	};

}
