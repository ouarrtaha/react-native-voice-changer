package com.reactlibrary.task;


/**
 * Interface for DBTASK 
 * @author DoBao
 * Dec 26, 2012
 * @company Citigo
 * @email bao.do@citigovietnam.com
 */

public interface IDBTaskListener {
	
	void onPreExcute();
	void onPostExcute();
	void onDoInBackground();
}
