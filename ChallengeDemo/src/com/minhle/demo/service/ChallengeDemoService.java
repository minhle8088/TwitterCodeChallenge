package com.minhle.demo.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Messenger;
import android.util.Log;

import com.minhle.demo.task.GetJsonTask;

public class ChallengeDemoService extends Service {
	
	private static final int CORE_POOL_SIZE = 3;
	private static final int MAX_POOL_SIZE = 3;
	private static final int KEEP_ALIVE = 10;
	private static final BlockingQueue<Runnable> workQueue = new LinkedBlockingQueue<Runnable>(10);
	
	private static List<Future<String>> submittedTasks;

	private Messenger messenger;

	private static ThreadPoolExecutor executor;	

	@Override
	public void onCreate() {
		super.onCreate();
		
		messenger = new Messenger(messageHandler);		
		executor = new ThreadPoolExecutor(CORE_POOL_SIZE, MAX_POOL_SIZE, KEEP_ALIVE, TimeUnit.SECONDS, workQueue);
		submittedTasks = new ArrayList<Future<String>>();
		
	}

	@Override
	public IBinder onBind(Intent intent) {		
		
		Log.i("Service", "Service bound");
		
		return messenger.getBinder();
	}

	protected static Handler messageHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			GetJsonTask task = new GetJsonTask(msg);
				
			Log.i("Service", "Execute Task");
			
			executor.submit(task);
			
			Log.i("Active Tasks", executor.getActiveCount() + "");				
			
		};
	};

	@Override
	public void onDestroy() {
		waitForTasksToFinishThenStop();
		
		messenger = null;
		
		super.onDestroy();
	}

	private static void waitForTasksToFinishThenStop() {
		executor.shutdown();
		
		try {
			executor.awaitTermination(60L, TimeUnit.SECONDS);
			
		} catch (InterruptedException e) { 
			cancelAllTasks();
		}
		
		executor = null;
	}
	
	private static void cancelTask(Future<String> ft) {
		
		if (!ft.isCancelled()) {
			ft.cancel(true);
		}
		
	}
	
	private static void cancelAllTasks() {
		for (Future<String> ft : submittedTasks) {
			cancelTask(ft);
		}
	}
	
}