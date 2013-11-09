package com.minhle.demo.common;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public class Utils {
	
	public static final String AUTH = "Oauth";
	
	public static final String BEARER_TOKEN = "bearer_token";
	
	/* JSON types */
	public static final int JSON_TRENDS = 1;
	
	public static final int JSON_TWEETS = 2;
	
	public static final String JSON_TYPE = "json_type";
	
	/* API key and and secret*/
	public static final String CONSUMER_KEY = "9p1RkWpdMoKYmpZJY7b2EA";
	
	public static final String CONSUMER_SECRET = "dGTQzVPlM38BASE0QbfHn12voqU5yiHhEgdEZUc5A";	
	
	/* Input params */
	public static final String INPUT_PARAM_URL = "input_url";
	
	public static final String INPUT_PARAM_TOKEN = "input_token";
	
	public static final String INPUT_PARAM_JSON_TEXT = "input_json_text";
	
	/* Output values */
	public static final String OUTPUT_VALUE_JSON_TEXT = "output_json_text";
	
	public static final String RATE_LIMIT_EXCEED = "Rate limit exceeded";
	
	public static void popup(Context ctx, String msg) {
		Toast.makeText(ctx, msg, Toast.LENGTH_LONG).show();
	}	
	
	public static List<Trend> parseJsonToTrends(String inputJsonText) {
			
			if (inputJsonText == null || inputJsonText.length() == 0 || inputJsonText.contains(Utils.RATE_LIMIT_EXCEED))
				return null;
			
			List<Trend> trends = new ArrayList<Trend>();
			
			try {
			    JSONArray jsonArray = new JSONArray(inputJsonText);
			    JSONObject trendsObject = jsonArray.getJSONObject(0);
			    JSONArray trendsArray = trendsObject.getJSONArray("trends");	
		
				for (int t = 0; t < trendsArray.length(); t++) {
					JSONObject trendObject = trendsArray.getJSONObject(t);					
					Trend topic = new Trend(trendObject.getString("name"), trendObject.getString("query"), trendObject.getString("url"));					
					trends.add(topic);					
				}			
			}
			catch (Exception e) {
				
				Log.e("parseJsonToTrends", "Error:" + e.getMessage());				
				e.printStackTrace();
			}			
			
			return trends;
	}
	
	public static List<Tweet> parseJsonToTweets(String inputJsonText) {
		
		if (inputJsonText == null || inputJsonText.length() == 0 || inputJsonText.contains(Utils.RATE_LIMIT_EXCEED))
			return null;
		
		List<Tweet> tweets = new ArrayList<Tweet>();
		
		try {
			JSONObject obj = new JSONObject(inputJsonText);
			JSONArray arr = obj.getJSONArray("statuses");

			for (int t = 0; t < arr.length(); t++) {
				
				JSONObject tweetObject = arr.getJSONObject(t);
				
				Tweet tweet = new Tweet(tweetObject.getJSONObject("user").getLong("id"), 
										tweetObject.getJSONObject("user").getString("profile_image_url"), 
										tweetObject.getJSONObject("user").getString("name"),												
										tweetObject.getString("text"));
				
				tweets.add(tweet);		
			}
		}
		catch (Exception e) {			
			Log.e("parseJsonToTweets", "Error:" + e.getMessage());				
			e.printStackTrace();
		}			
		
		return tweets;
	}
	
}
	
	

