package com.minhle.demo.activity;

import android.app.ListActivity;
import android.os.Bundle;
import android.os.Message;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.minhle.demo.service.ResponseListener;
import com.minhle.demo.service.ServiceClient;

/**
 * An activity with common methods that can be extended
 * */
public abstract class FactoryListActivity extends ListActivity implements ResponseListener {

	private ServiceClient serviceClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		initService();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initService();
	}

    protected ServiceClient getServiceClient() {
    	initService();

        return serviceClient;
    }

	private void initService() {
		
		if(serviceClient == null) {			
			serviceClient = new ServiceClient(this, this);
		}
		
	}

	public void popToast(String msg){
		Toast.makeText(this, msg, Toast.LENGTH_LONG).show();
	}
	
	@Override
	public void handleServiceResponse(Message msg){
	}

	@Override
	protected void onStop() {
		
		stopService();
		
		super.onStop();
	}

	private void stopService() {
		
		if(serviceClient != null){	
			serviceClient.stop();
			serviceClient = null;
		}
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {	
		super.onListItemClick(l, v, position, id);
	}
}