package com.minhle.demo.activity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import com.minhle.demo.auth.AuthManager;
import com.minhle.demo.common.Utils;

/**
 * Splash Activity
 * */
public class Splash extends Activity {	
	
	private AuthManager authMgr = null;
	private static String bearerToken;
	private static Context context;
	private static ProgressDialog dialog;

	@Override
	public void onCreate(final Bundle savedInstanceState) {
		try {
			super.onCreate(savedInstanceState);
			
			setContentView(R.layout.splash);
			context = this;
			authMgr = new AuthManager(context);		
			
			getAuth();
			
		} catch (Exception ex) {			
			Log.e(Splash.class.getSimpleName(), "Error occurs at Splash.onCreate() " + ex.getMessage());
		}
	}	
	
	private void getAuth() {
		
		 ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		 NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
		 
		 if (networkInfo != null && networkInfo.isConnected()) {
			 new BearerTokenTask().execute();		
		 } else {
			 Utils.popup(context, "No network connection found.");
		 }
		 
	}

	private void gotoTrends() {		
		
		if (bearerToken != null) {
			new Handler().postDelayed(new Runnable() {
	            @Override
	            public void run() {
	                Intent i = new Intent(Splash.this, TrendsActivity.class);
	                startActivity(i);
	                finish();
	            }
	        }, 2000);
		} else {
			Utils.popup(context, "Error occurs. Please go back later.");
		}
	}
	
	private class BearerTokenTask extends AsyncTask<Void, Void, String> {
	
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
		
			dialog = ProgressDialog.show(context, "Loading", "Please wait");
		}
    	
        @Override
		protected String doInBackground(Void... params) {
        	
			try {
				DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());				
				
				String apiString = Utils.CONSUMER_KEY + ":" + Utils.CONSUMER_SECRET;
				String authorization = "Basic " + Base64.encodeToString(apiString.getBytes(), Base64.NO_WRAP);
		
				HttpPost httppost = new HttpPost("https://api.twitter.com/oauth2/token");
				httppost.setHeader("Authorization", authorization);
				httppost.setHeader("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");
				httppost.setEntity(new StringEntity("grant_type=client_credentials"));
		
				InputStream inputStream = null;
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
		
				inputStream = entity.getContent();
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
				StringBuilder sb = new StringBuilder();
		
				String line = null;
				while ((line = reader.readLine()) != null)
				{
				    sb.append(line + "\n");
				}
				
				return sb.toString();
				
			} catch (Exception e){
				Log.e("GetBearerTokenTask", "Error:" + e.getMessage());
				return null;
			}
        	
		}
		
		@Override
		protected void onPostExecute(String jsonText){
			dialog.dismiss();
			
			try {
				JSONObject root = new JSONObject(jsonText);
				bearerToken = root.getString("access_token");	
				authMgr.saveAuthTokens(bearerToken);
				
				gotoTrends();
				
			} catch (Exception e){
				Log.e("GetBearerTokenTask", "Error:" + e.getMessage());
			}
			
		}
    }
	
	@Override
	protected void onDestroy() {
		
		if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
            dialog = null;
        }
		
		super.onDestroy();
	}

}
