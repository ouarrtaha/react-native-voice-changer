package com.reactlibrary.soundMng;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.reactlibrary.utils.DBLog;

import java.io.IOException;
import java.util.ArrayList;

public class SoundManager {

	private static final int MAX_STREAM = 100;
	private static final int QUALITY = 0;
	private static final int NORMAL_PIORITY = 1;
	
	private static final String TAG = SoundManager.class.getSimpleName();
	
	private static SoundPool mSoundPool=null;
	private static SoundManager mSingletonSoundMng = null;
	private static ArrayList<SoundObject> mListSoundObjects=null;
	private static float mVolume =1;
	
	/**
	 * A private Constructor prevents any other class from instantiating.
	 */
	private SoundManager() {
		if(mSoundPool==null){
			mSoundPool  = new SoundPool(MAX_STREAM, AudioManager.STREAM_MUSIC, QUALITY);
			mListSoundObjects = new ArrayList<SoundObject>();
		}
	}
	
	/** 
	 * Static 'instance' method 
	 * @return instance of class
	 */
	public static SoundManager getInstance() {
		if(mSingletonSoundMng==null){
			mSingletonSoundMng = new SoundManager();
		}
		return mSingletonSoundMng;
	}
	
	public void addSound(Context mContext, int resId){
		if(mSoundPool!=null && mListSoundObjects!=null){
			SoundObject mCheckSoundObject = getSound(resId);
			if(mCheckSoundObject==null){
				int mSoundId = mSoundPool.load(mContext, resId, NORMAL_PIORITY);
				SoundObject mSoundObject = new SoundObject(resId, mSoundId);
				mListSoundObjects.add(mSoundObject);
			}
		}
	}
	
	public void addSound(Context mContext, String mNameResouce){
		if(mSoundPool!=null && mListSoundObjects!=null){
			SoundObject mCheckSoundObject = getSound(mNameResouce);
			if(mCheckSoundObject==null){
				int mSoundId;
				try {
					mSoundId = mSoundPool.load(mContext.getResources().getAssets().openFd(mNameResouce), NORMAL_PIORITY);
					SoundObject mSoundObject = new SoundObject(mNameResouce, mSoundId);
					mListSoundObjects.add(mSoundObject);
				} 
				catch (IOException e) {
					DBLog.e(TAG, "ErrorWhenLoadSoundFromAsset");
					e.printStackTrace();
				}
			}
		}
	}
	
	private SoundObject getSound(int resId){
		if(mSoundPool==null || mListSoundObjects==null){
			return null;
		}
		int size = mListSoundObjects.size();
		if(size==0){
			return null;
		}
		for(int i=0;i<size;i++){
			SoundObject mSoundObject = mListSoundObjects.get(i);
			if(mSoundObject.getResId()!= SoundObject.RES_ID_ERROR){
				if(resId==mSoundObject.getResId()){
					return mSoundObject;
				}
			}
		}
		return null;
	}
	
	private SoundObject getSound(String resId){
		if(mSoundPool==null || mListSoundObjects==null){
			return null;
		}
		int size = mListSoundObjects.size();
		if(size==0){
			return null;
		}
		for(int i=0;i<size;i++){
			SoundObject mSoundObject = mListSoundObjects.get(i);
			if(mSoundObject.getNameResource()!=null){
				if(mSoundObject.getNameResource().equals(resId)){
					return mSoundObject;
				}
			}
		}
		return null;
	}
	
	public void play(Context mContext, String soundname){
		if (mSoundPool==null) return ;
		SoundObject mSoundObject = getSound(soundname);
		if(mSoundObject!=null){
			mSoundPool.play(mSoundObject.getSoundId(), mVolume, mVolume, 1, 0, 1f);
		}
	}
	
	public void play(Context mContext, String soundname, float rate){
		if (mSoundPool==null) return ;
		SoundObject mSoundObject = getSound(soundname);
		if(mSoundObject!=null){
			mSoundPool.play(mSoundObject.getSoundId(), mVolume, mVolume, 1, 0, rate);
		}
	}
	
	public void play(Context mContext, int resId, float rate){
		if (mSoundPool==null) return ;
		SoundObject mSoundObject = getSound(resId);
		if(mSoundObject!=null){
			mSoundPool.play(mSoundObject.getSoundId(), mVolume, mVolume, 1, 0, rate);
		}
	}
	
	public void play(final Context mContext, final int resId, final int loop){
		if (mSoundPool==null) return ;
		SoundObject mSoundObject = getSound(resId);
		if(mSoundObject!=null){
			mSoundPool.play(mSoundObject.getSoundId(), mVolume, mVolume, 1, loop, 1f);
		}
	}
	
	public void play(Context mContext, int resId){
		if (mSoundPool==null) return ;
		SoundObject mSoundObject = getSound(resId);
		if(mSoundObject!=null){
			mSoundPool.play(mSoundObject.getSoundId(), mVolume, mVolume, 1, 0, 1f);
		}
	}
	public void pauseSounds(){
		if(mSoundPool==null) return ;
		mSoundPool.autoPause();
	}
	
	public void resumeSounds(){
		if(mSoundPool==null) return ;
		mSoundPool.autoResume();
	}
	
	public void setVolumne(float mVolumne){
		mVolume =mVolumne;
	}
	
	public void releaseSound() {
		if(mSoundPool!=null){
			DBLog.d(TAG, "------>DadestroySOund");
			mSoundPool.release();
			mSoundPool = null;
		}
		if(mListSoundObjects!=null){
			DBLog.d(TAG, "------>DadestroySOundObject="+mListSoundObjects.size());
			mListSoundObjects.clear();
			mListSoundObjects=null;
		}
		mSingletonSoundMng=null;
	}
	
	
}
