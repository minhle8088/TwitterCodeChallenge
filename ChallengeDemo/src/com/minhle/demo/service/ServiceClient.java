package com.minhle.demo.service;

import android.content.Context;
import android.os.Message;
import android.util.Log;

import com.minhle.demo.activity.R;
import com.minhle.demo.common.Utils;

public class ServiceClient extends ServiceManager {

	public ServiceClient(Context context, ResponseListener listener) {
		super(context, listener);
	}

	public void getJsonFromServer(String inputUrl, String inputToken){
		Message msg = obtainMessage(R.id.task_get_json);
		
		msg.getData().putString(Utils.INPUT_PARAM_URL, inputUrl);
		msg.getData().putString(Utils.INPUT_PARAM_TOKEN, inputToken);
		
		Log.i("Service", "Add getJsonFromServer message to queue");
		
		addToQueue(msg);
	}

}