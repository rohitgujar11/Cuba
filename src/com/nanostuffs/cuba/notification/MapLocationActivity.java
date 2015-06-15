// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Activity shows map to save location
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.LocationSource.OnLocationChangedListener;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.nanostuffs.cuba.Class.Save_location;
import com.nanostuffs.cuba.Class.MyHttp;
import com.nanostuffs.cuba.Class.RefernaceKeyClass;
import com.nanostuffs.cuba.Class.latlngArray;
import com.nanostuffs.cuba.adapter.GPSTracker;
import com.nanostuffs.cuba.adapter.PlaceAdapter;
import com.nanostuffs.cuba.database.DatabaseHandler;

public class MapLocationActivity  extends FragmentActivity implements OnLocationChangedListener {
	GoogleMap  myMap;
	private LocationManager locationManager;
	private String provider;
	
	AutoCompleteTextView search;
	PlaceAdapter adapterwhere;
	String latString,lngString,address;
	int i=0;
	String from_acivity;
	DatabaseHandler db;
	double lat,lng;
	EditText locationName;
	private ArrayList<RefernaceKeyClass> COUNTRIES= new ArrayList<RefernaceKeyClass>();
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		lat=0.0;
		lng=0.0;
		if(!RegisterActivity.isNetworkAvailable(this))
			finish();
		setContentView(R.layout.activity_map_location);
		Bundle bundle = getIntent().getExtras();
		from_acivity = bundle.getString("activity");
		actionBarDetails();
		FragmentManager myFM =getSupportFragmentManager();
		SupportMapFragment myMAPF = (SupportMapFragment) myFM
				.findFragmentById(R.id.map_all);
		myMap = myMAPF.getMap();
		search=(AutoCompleteTextView) findViewById(R.id.editText_where);
		locationName=(EditText) findViewById(R.id.editText_type);
		for(int i=0;i<RegisterActivity.location.size();i++)
		{
			latlngArray m=RegisterActivity.location.get(i);
			double lat=m.getLat();
			double lng=m.getLng();
			Log.e("lat", ""+lat+"lng:"+lng);
			String name=m.getName();
			LatLng sydney = new LatLng(lat, lng);
			Marker marker=myMap.addMarker(new MarkerOptions()
			.title(name)
			.snippet("")
			.position(sydney));
			CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 10);
			myMap.animateCamera(cameraUpdate);
			marker.showInfoWindow();
		}
		db = new DatabaseHandler(this);
		search.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// TODO Auto-generated method stub
				String abc=s.toString();
				adapterwhere = new PlaceAdapter(MapLocationActivity.this,android.R.layout.simple_dropdown_item_1line,COUNTRIES);
				search.setAdapter(adapterwhere);
				new asynLoginwhere(abc).execute();
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
			
			}
			@Override
			public void afterTextChanged(Editable s) {
				// TODO Auto-generated method stub

			}
		});
		search.setOnEditorActionListener(new OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId,
					KeyEvent event) {
				if (event != null&& (event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
					InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					in.hideSoftInputFromWindow(search.getApplicationWindowToken(),InputMethodManager.HIDE_NOT_ALWAYS);
				}
				// TODO Auto-generated method stub
				return false;
			}
		});
		search.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int postion,
					long arg3) {
				RefernaceKeyClass ref=COUNTRIES.get(postion);
				address=ref.getName();
				String key=ref.getKey();
				InputMethodManager imm = (InputMethodManager)getSystemService(
						Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(search.getWindowToken(), 0);
				Log.e("msg", key);
				ProgressDialog progress =new ProgressDialog(MapLocationActivity.this);
				new asynLoginlatlng(key,ref.getName(),progress).execute();
				Log.e("aaaaaaaaaaaa","bbbbbbbbbbb    "+RegisterActivity.location.size());
			}
		});
		LocationManager service = (LocationManager) getSystemService(LOCATION_SERVICE);
		boolean enabledGPS = service
				.isProviderEnabled(LocationManager.GPS_PROVIDER);
		boolean enabledWiFi = service
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
		locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		Criteria criteria = new Criteria();
		provider = locationManager.getBestProvider(criteria, false);
		LocationManager lm = (LocationManager) this
				.getSystemService(this.LOCATION_SERVICE);

		Location location = lm
				.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
		GPSTracker tracker=new GPSTracker(this);
		Location location1=tracker.getLocation();
		if (location != null) {

			onLocationChanged(location1);
		} else {


		}
	}
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}
	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub

		double lat =  location.getLatitude();
		double lng = location.getLongitude();
	
		LatLng coordinate = new LatLng(lat, lng);

	
		CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(coordinate, 20);
		myMap.animateCamera(cameraUpdate);

	}
	// get the name of cities depending upon user choice
	class asynLoginwhere extends AsyncTask<String, Void, String> {
		String key;
		public asynLoginwhere(String key) {
			this.key = key;
		}
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			COUNTRIES.clear();
			JSONObject jsonObject;
			try {
				if(result.length()>0)
				{
					jsonObject = new JSONObject(result);
					JSONArray array=jsonObject.getJSONArray("predictions");
					for (int i = 0; i < array.length(); i++) {
						RefernaceKeyClass ref=new RefernaceKeyClass();
						JSONObject obj=array.getJSONObject(i);
						String place=obj.getString("description");
						String referncs=obj.getString("reference");
						ref.setName(place);
						ref.setKey(referncs);
						COUNTRIES.add(ref);
					}
					adapterwhere.notifyDataSetChanged();
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String Response = null;
			HttpClient	client = MyHttp.getNewHttpClient();
			String key1=key.replaceAll(" ", "%20");
			HttpPost httppost = new HttpPost("https://maps.googleapis.com/maps/api/place/autocomplete/json?input="+key1+"&sensor=false&key=AIzaSyB3I331jH4KbR1ybyDIzeV1r0CwyzZjt-A");
			HttpResponse response;
			try {
				response = client.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				Response=EntityUtils.toString(resEntity);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response;

		}
	}
	// get the lat lng of cities depending upon user choice
	class asynLoginlatlng extends AsyncTask<String, Void, String> {
		String key;
		String name1;
		ProgressDialog p;

		public asynLoginlatlng(String key,String name,ProgressDialog p) {
			this.key = key;
			this.name1=name;
			this.p=p;
		}
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			p.setMessage("Wait..");
			p.show();
			p.setCancelable(false);
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);

			JSONObject jsonObject;
			try {
				p.dismiss();
				jsonObject = new JSONObject(result);
				JSONObject resultobj=jsonObject.getJSONObject("result");
				JSONObject geometry=resultobj.getJSONObject("geometry");
				String name=resultobj.getString("name");
				JSONObject location=geometry.getJSONObject("location");
				lat=Double.parseDouble(location.getString("lat"));
				lng=Double.parseDouble(location.getString("lng"));
				LatLng sydney = new LatLng(lat, lng);
				
				Marker marker=myMap.addMarker(new MarkerOptions()
				.title(name)
				.snippet("")
				.position(sydney));
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(sydney, 10);
				myMap.animateCamera(cameraUpdate);
				//search.setText("");
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			String Response = null;
			HttpClient	client = MyHttp.getNewHttpClient();
			String key1=key.replaceAll(" ", "%20");
			HttpPost httppost = new HttpPost("https://maps.googleapis.com/maps/api/place/details/json?key=AIzaSyBciR-U00DJwwJSLSUg3nJ3H7Zir0XsUg4&reference="+key+"&sensor=false");
			HttpResponse response;
			try {
				response = client.execute(httppost);
				HttpEntity resEntity = response.getEntity();
				Response=EntityUtils.toString(resEntity);
				Log.e("loc", ""+Response);
				//Log.e("resp", Response);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return Response;
		}
	}
	private void actionBarDetails() {
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		final View addView = getLayoutInflater().inflate(R.layout.action_bar,
				null);
		ImageView right = (ImageView) addView.findViewById(R.id.right_image);
		TextView back = (TextView) addView.findViewById(R.id.temp); 
		back.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent;
				if(from_acivity.equalsIgnoreCase("Add Addresses")){
					intent = new Intent(MapLocationActivity.this,HomeActivity.class);
					startActivity(intent);
				}
				else{
					intent = new Intent(MapLocationActivity.this,LocationListActivity.class);
					startActivity(intent);
				}
			}
		});
		right.setImageResource(R.drawable.done);
		right.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(address==null)
				{
					address=search.getText().toString();
				}

				if((lat==0.0)&&(lng==0.0) && locationName.getText().toString().length()==0)
				{
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							MapLocationActivity.this);
					alertDialogBuilder
					.setMessage(
							"Provide Name and Address details")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {

								}
							});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();


				}
				else if((lat==0.0)&&(lng==0.0)){
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							MapLocationActivity.this);
					alertDialogBuilder
					.setMessage(
							"Enter a valid Location")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {

								}
							});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
				else if(locationName.getText().toString().length()==0){
					AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
							MapLocationActivity.this);
					alertDialogBuilder
					.setMessage(
							"Please enter the Name")
							.setCancelable(false)
							.setPositiveButton("OK",
									new DialogInterface.OnClickListener() {
								public void onClick(
										DialogInterface dialog, int id) {

								}
							});
					AlertDialog alertDialog = alertDialogBuilder.create();
					alertDialog.show();
				}
				else
				{
					db.addContact(new Save_location(address, lat+"aa"+lng, locationName.getText().toString()));
					latlngArray latobj=new latlngArray();
					latobj.setName(address);
					latobj.setHomeName(locationName.getText().toString());
					latobj.setLat(lat);
					latobj.setLng(lng);
					RegisterActivity.location.add(latobj);
					finish();
					//}
				}
				// TODO Auto-generated method stub
			}
		});
		TextView actionBarText = (TextView) addView
				.findViewById(R.id.action_bar_header);
		actionBarText.setText("Add Addresses");
		getActionBar().setCustomView(addView);
	}
}
