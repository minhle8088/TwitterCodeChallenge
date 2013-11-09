package com.minhle.demo.activity;

import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import com.minhle.demo.adapter.TweetsAdapter;
import com.minhle.demo.auth.AuthManager;
import com.minhle.demo.common.Tweet;
import com.minhle.demo.common.Utils;

public class TweetsActivity extends FactoryListActivity {

	private static String URL;	
	
	private static String token;
	private AuthManager authMgr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tweets);
        
        authMgr = new AuthManager(this);
		
        token = authMgr.getAuthToken();
        
        Bundle extras = getIntent().getExtras();
		if (extras != null) {
		    URL = extras.getString("TREND_URL");
		}
		
		getTweets();
        
	}
	
	public void getTweetsButtonClickHandler(View button) {		
    	getTweets();
    }
	
	private void getTweets() {
		
		if (token != null && URL != null) {
    		
    		Log.i("Service", "Run task from TweetsActivity");
    		
    		getServiceClient().getJsonFromServer(URL, token);
    		
    	} else {
			popToast(getString(R.string.invalid_token));
		}
	}

    @Override
    public void handleServiceResponse(Message msg) {
    	analyzeResponse(msg.what, msg.getData());
    }

	private void analyzeResponse(int responseId, Bundle data) {
		
		switch(responseId){
    	
			case R.id.task_get_json:
				processResponse(data);
				break;
    		
			default:
				break;
    	}
	}

	private void processResponse(Bundle data) {
		
		List<Tweet> tweets = null;
		
		String result = data.getString(Utils.OUTPUT_VALUE_JSON_TEXT);
		
		Log.i("Result at TweetsActivity.processResponse", result);
		
		int jsonType = data.getInt(Utils.JSON_TYPE);

		if (jsonType == Utils.JSON_TWEETS) {
			tweets = Utils.parseJsonToTweets(result);
			
			if (tweets != null && tweets.size() > 0) {
				try {				
					
					final TweetsAdapter adapter = new TweetsAdapter(this, tweets);
					setListAdapter(adapter);
					
				} catch (Exception e) {
					Log.e("processResponse", "Error:" + e.getMessage());				
					e.printStackTrace();			
				}
			}
		}

	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {		
		super.onListItemClick(l, v, position, id);
		
		popToast("Tweet");
	}
	
}
