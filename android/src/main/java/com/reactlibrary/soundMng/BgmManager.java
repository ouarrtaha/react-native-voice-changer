package com.reactlibrary.soundMng;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;

import java.io.IOException;


public class BgmManager {
	
	private static MediaPlayer mMediaPlayer=null;
	private static float mVolume = 1;
	private static BgmManager mBgmManager=null; 

	protected BgmManager() {
		if(mMediaPlayer==null){
			mMediaPlayer = new MediaPlayer();
			mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		}
	}
	
	public static BgmManager getInstance(){
		if(mBgmManager==null){
			mBgmManager = new BgmManager();
		}
		return mBgmManager;
	}
	
	public void addAndPrepareBgm(Context mContext, int rawId){
		if(mMediaPlayer==null) return;
		try {
			AssetFileDescriptor descriptor = mContext.getResources().openRawResourceFd(rawId);
			mMediaPlayer.reset();
			mMediaPlayer.setDataSource(descriptor.getFileDescriptor(),descriptor.getStartOffset(), descriptor.getLength());
			mMediaPlayer.prepare();
			mMediaPlayer.setLooping(true);
		} 
		catch (IllegalArgumentException e) {
			e.printStackTrace();
		} 
		catch (IllegalStateException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * start background sound
	 */
	public void start(Context mContext){
		if (mMediaPlayer==null) return;
		if(!mMediaPlayer.isPlaying()){
			mMediaPlayer.start();
			lound();
		}
	}
	
	/**
	 * stop background sound
	 */
	public void stop(){
		if (mMediaPlayer==null) return;
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.stop();
		}
	}
	/**
	 * pause sound
	 */
	public void pause(){
		if (mMediaPlayer==null) return;
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.pause();
		}
	}
	
	/**
	 * set volume for background sound
	 * @param v
	 */
	public void setVolume(float v){
		if (mMediaPlayer==null) return;
		mMediaPlayer.setVolume(v, v);
		mVolume=v;
	}
	/**
	 * mute sound
	 */
	public void mute(){
		setVolume(0);
	}
	
	public void lound(){
		setVolume(1);
	}
	
	/**
	 * release sound media
	 */
	public void onDestroy() {
		if (mMediaPlayer==null) return;
		if(mMediaPlayer.isPlaying()){
			mMediaPlayer.stop();
		}
		mMediaPlayer.release();
		mMediaPlayer = null;
		if(mBgmManager!=null){
			mBgmManager=null;
		}
	}
	/**
	 * get volume of sound
	 * @return
	 */
	public float getVolume(){
		return mVolume;
	}
}
