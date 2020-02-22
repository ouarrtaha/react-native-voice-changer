
package com.reactlibrary.basseffect;

import android.content.Context;

import com.un4seen.bass.BASS;

public class DBAudioManager implements IDBMediaConstants {
	
	private static final String TAG = DBAudioManager.class.getSimpleName();

	private static DBAudioManager instance;
	
	private boolean isInit;

	public static DBAudioManager getInstance() {
		if (null == instance) {
			instance = new DBAudioManager();
		}
		return instance;
	}
	
	private DBAudioManager() {
		
	}
	
	public void onDestroy() {
		BASS.BASS_Free();
		BASS.BASS_PluginFree(0);
		instance=null;
	}
	
	public void onInitAudioDevice(Context mContext){
		if(!isInit){
			isInit=true;
			if (!BASS.BASS_Init(-1, 44100, BASS.BASS_DEVICE_LATENCY)) {
				new Exception(TAG+ " Can't initialize device").printStackTrace();
				this.isInit =false;
				return;
			}
			BASS.BASS_SetConfig(BASS.BASS_CONFIG_FLOATDSP, 1);
			String libpath=mContext.getApplicationInfo().nativeLibraryDir;
			BASS.BASS_PluginLoad(libpath+"/libbassenc.so", 0);
			BASS.BASS_PluginLoad(libpath+"/libbasswv.so", 0);
			BASS.BASS_PluginLoad(libpath+"/libbass_fx.so", 0);
		}
	}
	
	
}
