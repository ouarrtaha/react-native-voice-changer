package com.reactlibrary.task;

import android.os.AsyncTask;

/**
 * DoBaoTask to excute something in background
 * @author Dobao
 * 20/3/2012
 */

public class DBTask extends AsyncTask<Void, Void, Void> {
	
	private IDBTaskListener mDownloadListener;
	
	public DBTask(IDBTaskListener mDownloadListener) {
		this.mDownloadListener = mDownloadListener;
	}
	
	@Override
	protected void onPreExecute() {
		if(mDownloadListener!=null){
			mDownloadListener.onPreExcute();
		}
	}
	
	@Override
	protected Void doInBackground(Void... params) {
		if(mDownloadListener!=null){
			mDownloadListener.onDoInBackground();
		}
		return null;
	}
	@Override
	protected void onPostExecute(Void result) {
		if(mDownloadListener!=null){
			mDownloadListener.onPostExcute();
		}
	}

}
