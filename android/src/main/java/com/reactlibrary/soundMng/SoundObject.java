package com.reactlibrary.soundMng;


public class SoundObject {
	
	public static final int RES_ID_ERROR = -1;
	
	private int resId=RES_ID_ERROR;
	private String nameResource;
	private int soundId;
	
	
	public SoundObject(int resId, int soundId) {
		this.resId = resId;
		this.soundId = soundId;
	}
	
	public SoundObject(String nameResource, int soundId) {
		super();
		this.nameResource = nameResource;
		this.soundId = soundId;
	}

	public int getResId() {
		return resId;
	}
	public void setResId(int resId) {
		this.resId = resId;
	}
	public String getNameResource() {
		return nameResource;
	}
	public void setNameResource(String nameResource) {
		this.nameResource = nameResource;
	}
	public int getSoundId() {
		return soundId;
	}
	public void setSoundId(int soundId) {
		this.soundId = soundId;
	}
	
	
}
