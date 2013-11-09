package com.minhle.demo.service;

import java.lang.ref.WeakReference;
import java.util.LinkedList;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public abstract class ServiceManager implements ServiceConnection {

	private Context context;
	private boolean bound;
	private Messenger service;
	private final LinkedList<Message> msgQueue;
	private final ResponseHandler response;

	public ServiceManager(Context context, ResponseListener listener) {
		
		this.context = context;
		response = new ResponseHandler(listener);
		msgQueue = new LinkedList<Message>();
		
		context.bindService(new Intent(context, ChallengeDemoService.class), this, Context.BIND_AUTO_CREATE);
	}

	@Override
	public void onServiceConnected(ComponentName name, IBinder service) {
		this.service = new Messenger(service);
        bound = true;

        sendServiceMessage();
	}

	protected Message obtainMessage(int taskId){
		
		Message msg = Message.obtain();
		msg.replyTo = new Messenger(response);
		msg.what = taskId;
		
		return msg;
	}

	protected void addToQueue(Message msg) {
		
		Log.i("Queue Size Before Sending", msgQueue.size() + "");
		
		msgQueue.add(msg);
		
		if(bound) {
			
			Log.i("Service", "Send service message");
			
			sendServiceMessage();
			
			Log.i("Queue Size After Sending", msgQueue.size() + "");			
			
		} else {
        	
			Log.i("Service", "Do nothing at ServiceManager.addToQueue");
		}
	}
	
	private void sendServiceMessage() {
		
		try {
			
			while(!msgQueue.isEmpty()){
				
				Message msg = msgQueue.removeFirst();
				service.send(msg);
				
				Log.i("Service", "Message sent.");
				
			}
			
		} catch (RemoteException e) {
			
		}
	}

	private static class ResponseHandler extends Handler {
		private final WeakReference<ResponseListener> mListener;

		public ResponseHandler(ResponseListener listener) {
			mListener = new WeakReference<ResponseListener>(listener);
		}

		@Override
		public void handleMessage(Message msg) {

			if(mListener.get() != null)
				mListener.get().handleServiceResponse(msg);
		}
	}

	@Override
	public void onServiceDisconnected(ComponentName name) {
		service = null;
        bound = false;
	}

	public void stop(){
		
		msgQueue.clear();
		context.unbindService(this);
		
		bound = false;
		context = null;
	}
}