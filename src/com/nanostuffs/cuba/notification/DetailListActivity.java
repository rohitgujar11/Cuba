// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Activity shows Alert Description
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;
import android.app.ActionBar;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.Html.ImageGetter;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.nanostuffs.cuba.database.DatabaseHandler;

public class DetailListActivity extends Activity {
	String desc,Title;
	TextView textTitle;
	WebView textdesc;
	public static boolean flagmsg;
	public static int pos,id;
	DatabaseHandler db;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_detail_list);
		Bundle b = getIntent().getExtras();
		desc=b.getString("desc");
		Title=b.getString("title");
		pos=b.getInt("pos");
		id=b.getInt("id");
		actionBarDetails();//call to 
		db = new DatabaseHandler(this);
		textTitle=(TextView) findViewById(R.id.title);
		textdesc=(WebView) findViewById(R.id.desc);
		textTitle.setText( Html.fromHtml(Title));
		textdesc.loadData(desc, "text/html", "UTF-8");
	}
	// function set the action bar details
	private void actionBarDetails() {
		getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
		final View addView = getLayoutInflater().inflate(R.layout.action_bar,
				null);
		ImageView right = (ImageView) addView.findViewById(R.id.right_image);
		right.setImageResource(R.drawable.delete);
		right.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				flagmsg=true;
				db.deleteContact_alert(id);
				RegisterActivity.array.remove(pos);
				finish();
			}
		});
		ImageButton back = (ImageButton) addView.findViewById(R.id.imageButton1); 
		back.setVisibility(View.INVISIBLE);
		back.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finish();
			}
		});
		ImageView share = (ImageView) addView.findViewById(R.id.shar_image);
		share.setBackgroundResource(R.drawable.share2);
		share.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent shareIntent = new Intent(
						android.content.Intent.ACTION_SEND);
				shareIntent.setType("text/plain");
				shareIntent.putExtra(
						android.content.Intent.EXTRA_TEXT,
						Title+"\n"+desc);
				shareIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
				startActivity(Intent.createChooser(shareIntent, "Share using "));

			}
		});
		TextView actionBarText = (TextView) addView
				.findViewById(R.id.action_bar_header);
		actionBarText.setText("ALERT INFORMATION");
		getActionBar().setCustomView(addView);
	}
	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		flagmsg=false;
	}

}
