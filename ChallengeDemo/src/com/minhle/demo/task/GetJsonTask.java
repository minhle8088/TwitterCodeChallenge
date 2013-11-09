package com.minhle.demo.task;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.concurrent.Callable;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;

import android.os.Bundle;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

import com.minhle.demo.activity.R;
import com.minhle.demo.common.Utils;

public class GetJsonTask implements Callable<String> {
	
	private final Message msg;

	public GetJsonTask(Message msg) {
		this.msg = Message.obtain(msg);
	}

	@Override
	public String call() {
		
		String result = null;
		
		String inputURL = msg.getData().getString(Utils.INPUT_PARAM_URL);
		String token = msg.getData().getString(Utils.INPUT_PARAM_TOKEN);
		
		Log.i("GetJsonTask.inputURL", inputURL);
		
		Log.i("GetJsonTask.token", token != null ? token : "Null Token");
		
		Log.i("Service", "Start getJsonFromServer in GetJsonTask");
		
		if (inputURL != null & token != null) {
			result = getJsonFromServer(inputURL, token);
		}
		
		Log.i("Result", result != null ? result : "Null result");

		Bundle b = new Bundle();
		b.putString(Utils.OUTPUT_VALUE_JSON_TEXT, result);
		
		//trends
		if (result.contains("trends")) {
			b.putInt(Utils.JSON_TYPE, Utils.JSON_TRENDS);
		
		//tweets
		} else if (result.contains("statuses")) {
			b.putInt(Utils.JSON_TYPE, Utils.JSON_TWEETS);
		
		}
		
		setResponseData(b);
		sendReply();
		
		return result;
	}
	
	private String getJsonFromServer(String url, String token) {
		
		try {
			
			DefaultHttpClient httpclient = new DefaultHttpClient(new BasicHttpParams());
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Authorization", "Bearer " + token);
			httpget.setHeader("Content-type", "application/json");

			InputStream inputStream = null;
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null)
			{
				sb.append(line + "\n");
			}
			
			return sb.toString();			
			
		} catch (Exception e){
			return null;
		}
	}
	
	private void setResponseData(Bundle data){
		
		Log.i("Task", "setResponseData() called.");
		
		msg.setData(data);
	}

	private void sendReply() {
		
		Messenger replyTo = msg.replyTo;
		
		if (replyTo != null) {
			
			try {
				replyTo.send(msg);
				
			} catch (Exception e) {
		
				try {
					
					msg.what = R.id.error_occurs; 
					replyTo.send(msg);
					
				} catch (RemoteException e1) { }
				
			}
		}
	}

}
