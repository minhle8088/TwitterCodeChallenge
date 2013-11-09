package com.minhle.demo.auth;


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

import com.minhle.demo.common.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

public class AuthManager {
	
	private static String bearerToken;	

	private Context context = null;

	public AuthManager(Context context) {
		this.context = context;
	}
	
	public String getAuthToken() {
		final SharedPreferences settings = context.getSharedPreferences(Utils.AUTH, 0);

		bearerToken = settings.getString(Utils.BEARER_TOKEN, null);
		
		if (bearerToken == null) {
			new BearerTokenTask().execute();			
		}
		
		return bearerToken;
	}
	
	public void saveAuthTokens(String bearerToken){
		final SharedPreferences settings = context.getSharedPreferences(Utils.AUTH, 0);
		
		Editor editor = settings.edit();
		editor.putString(Utils.BEARER_TOKEN, bearerToken);
		editor.commit();
	}
	
	protected class BearerTokenTask extends AsyncTask<Void, Void, String> {
    	
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
			try {
				JSONObject root = new JSONObject(jsonText);
				bearerToken = root.getString("access_token");	
				saveAuthTokens(bearerToken);
				
			} catch (Exception e){
				Log.e("GetBearerTokenTask", "Error:" + e.getMessage());
			}
		}
    }	
}
