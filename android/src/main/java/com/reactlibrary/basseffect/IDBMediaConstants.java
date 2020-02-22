package com.reactlibrary.basseffect;

/**
 * DBMedia constants
 * @author dobao
 * 12/7/2012
 */
public interface IDBMediaConstants {
	
	public static final String TYPE_MP3="mp3";
	public static final String TYPE_OGG="ogg";
	public static final String TYPE_WAV="wav";
	public static final String TYPE_FLAC="flac";
	public static final String TYPE_AAC="aac";
	public static final String TYPE_MIDI="mid";
	public static final String TYPE_WMA="wma";
	
	public static final int MAX_PITCH_SEMITONE=12;
	public static final int MIN_PITCH_SEMITONE=-12;
	
	public static final int ERROR_CAN_NOT_LOAD_FILE=-1;
	public static final int NO_ERROR=0;
	
	public static final int IDLE = 1;
	public static final int PREPARED = 2;
	public static final int PLAYING = 3;
	public static final int PAUSED = 4;
	
}
