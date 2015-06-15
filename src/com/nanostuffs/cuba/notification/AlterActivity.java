// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Actvity to get music
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;

import java.io.File;
import java.io.FilenameFilter;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;



public class AlterActivity extends Activity {
	ListView list;
	static Uri[] mUris;
	static String[] mFiles = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_alter);
		list=(ListView) findViewById(R.id.listView_alter);
		getMusic();
	}
	// function to get music 
	public static String[] getMusic(){
	    File music = Environment.getExternalStorageDirectory();
	    File[] musicList = music.listFiles(new FilenameFilter() {

	        @Override
	        public boolean accept(File dir, String name) {
	            // TODO Auto-generated method stub
	        	 Log.e("music", name);
	            return ((name.endsWith(".mp3")||(name.endsWith(".m4b")||(name.endsWith(".flac")))));
	        }
	    });
	    mFiles = new String[musicList.length];
	    for(int i=0; i<musicList.length;i++) {
	        mFiles[i]=musicList[i].getAbsolutePath();
	    }
	    return mFiles;
	}
}
