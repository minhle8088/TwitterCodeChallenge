package com.minhle.demo.activity;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.minhle.demo.adapter.TrendsAdapter;
import com.minhle.demo.auth.AuthManager;
import com.minhle.demo.common.Trend;
import com.minhle.demo.common.Utils;

/**
 * An activity to show the trending topics
 * Press button to get the latest trending topics
 * */
public class TrendsActivity extends FactoryListActivity {
	
	private static final String TRENDS_URL = "https://api.twitter.com/1.1/trends/place.json?id=1";
	private static String token;
	private AuthManager authMgr;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trends);
        
        authMgr = new AuthManager(this);
		
        token = authMgr.getAuthToken();
        
        getTrends();
    }
    
    public void getTrendsButtonClickHandler(View button) {
    	getTrends();
    }
    
    private void getTrends() {
    	if (token != null) {
    		
    		Log.i("Service", "Run task from TrendsActivity");
    		
    		getServiceClient().getJsonFromServer(TRENDS_URL, token);
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
    		
			case R.id.error_occurs:			
				//process error
				break;
				
			default:
				break;
    	}
	}
		
	private void processResponse(Bundle data) {
		
		List<Trend> trends = null;
		
		String result = data.getString(Utils.OUTPUT_VALUE_JSON_TEXT);
		
		Log.i("Result at TrendsActivity.processResponse", result);
		
		int jsonType = data.getInt(Utils.JSON_TYPE);

		if (jsonType == Utils.JSON_TRENDS) {
			trends = Utils.parseJsonToTrends(result);
			
			if (trends != null && trends.size() > 0) {
				
				try {				
					
					final TrendsAdapter adapter = new TrendsAdapter(this, trends);
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
		
		Intent tweetsIntent = new Intent(this, TweetsActivity.class);
		
		TextView urlTextView = (TextView) findViewById(R.id.trend_url);
		String url = urlTextView.getText().toString();
		tweetsIntent.putExtra("TREND_URL", url);
	    startActivity(tweetsIntent);
	}
}