// Developed By :- Rohit Gujar
// Company Name :- Nanostuffs
// Class to check internet connection
// Last Modified date :- 13/3/2015
package com.nanostuffs.cuba.notification;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
  
public class ConnectionDetector {
  
    private Context _context;
  
    public ConnectionDetector(Context context){
        this._context = context;
    }
  
    /**
     * Checking for all possible internet providers
     * **/
    public boolean isConnectingToInternet(){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null)
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                          return true;
                      }
          }
          return false;
    }
}
