package com.reactlibrary;

import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.Toast;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.modules.core.DeviceEventManagerModule;
import com.reactlibrary.dataMng.JsonParsingUtils;
import com.reactlibrary.object.EffectObject;
import com.reactlibrary.utils.DBLog;
import com.reactlibrary.utils.StringUtils;
import com.un4seen.bass.BASS;
import com.reactlibrary.basseffect.DBMediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.un4seen.bass.BASS.BASS_CONFIG_FLOAT;

public class VoiceChangerModule extends ReactContextBaseJavaModule {

    public static final String TAG = VoiceChangerModule.class.getSimpleName();
    private final ReactApplicationContext reactContext;
    private ArrayList<EffectObject> effectObjects;
    private String mPathAudio;
    private DBMediaPlayer mDBMedia;
    private boolean isInit;
    private Integer playingIndex;

    public VoiceChangerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        this.effectObjects = new ArrayList<>();
        this.mPathAudio = null;
        this.playingIndex = null;
    }

    @Override
    public String getName() {
        return "VoiceChanger";
    }

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

    @ReactMethod
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
    }

    private void showToast(String message) {
        Toast toast = Toast.makeText(reactContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    @Override
    public Map<String, Object> getConstants() {
        final Map<String, Object> constants = new HashMap<>();
        constants.put(DURATION_SHORT_KEY, Toast.LENGTH_SHORT);
        constants.put(DURATION_LONG_KEY, Toast.LENGTH_LONG);
        return constants;
    }

    @ReactMethod
    public void insertEffect(String effect) {
        this.effectObjects.add(JsonParsingUtils.jsonToEffectObject(effect));
    }

    @ReactMethod
    public void setPath(String path) {
        this.mPathAudio = path;

    }

    @ReactMethod
    public void setPlayingIndex(Integer idx) {
        if (idx != null) {
            this.playingIndex = idx;
            WritableMap params = Arguments.createMap();
            params.putInt("index", idx);
            sendEvent(reactContext, "idxMediaPlaying", params);
        }
    }

    @ReactMethod
    public void playEffect(int effectIndex, final Promise promise) {
        Log.d(TAG, "audioPath: " + this.mPathAudio);

        if (!StringUtils.isEmptyString(mPathAudio)) {
            File mFile = new File(mPathAudio);
            if (!(mFile.exists() && mFile.isFile())) {
                //todo File not found exception
                showToast("File not found exception");
            }
        }

        try {
            this.setPlayingIndex(effectIndex);
            onPlayEffect(this.effectObjects.get(effectIndex));
            promise.resolve(true);
        } catch (Exception ex) {
            promise.reject("ERR_UNEXPECTED_EXCEPTION", ex);
        }
    }

    private void onPlayEffect(EffectObject mEffectObject) {
        boolean isPlaying = mEffectObject.isPlaying();
        if (isPlaying) {
            mEffectObject.setPlaying(false);
            if (mDBMedia != null) {
                mDBMedia.pauseAudio();
            }
            sendEvent(reactContext, "onMediaCompletion", null);
        } else {
            onResetState();
            mEffectObject.setPlaying(true);
            if (mDBMedia != null) {
                mDBMedia.setPathMix(mEffectObject.getPathMix());
                mDBMedia.setNeedMix(mEffectObject.isMix());
                mDBMedia.prepareAudio();

                mDBMedia.setReverse(mEffectObject.isReverse());
                mDBMedia.setAudioPitch(mEffectObject.getPitch());
                mDBMedia.setCompressor(mEffectObject.getCompressor());
                mDBMedia.setAudioRate(mEffectObject.getRate());
                mDBMedia.setAudioEQ1(mEffectObject.getEq1());
                mDBMedia.setAudioEQ2(mEffectObject.getEq2());
                mDBMedia.setAudioEQ3(mEffectObject.getEq3());
                mDBMedia.setPhaser(mEffectObject.getPhaser());
                mDBMedia.setAutoWah(mEffectObject.getAutoWah());
                mDBMedia.setAudioReverb(mEffectObject.getReverb());
                mDBMedia.setEcho4Effect(mEffectObject.getEcho4());
                mDBMedia.setAudioEcho(mEffectObject.getEcho());
                mDBMedia.setBiQuadFilter(mEffectObject.getFilter());
                mDBMedia.setFlangeEffect(mEffectObject.getFlange());
                mDBMedia.setChorus(mEffectObject.getChorus());
                mDBMedia.setAmplify(mEffectObject.getAmplify());
                mDBMedia.setDistort(mEffectObject.getDistort());
                mDBMedia.setRotate(mEffectObject.getRotate());

                mDBMedia.startAudio();
            }
        }
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
    }

    private void onInitAudioDevice() {
        if (!isInit) {
            isInit = true;
            if (!BASS.BASS_Init(-1, 44100, 0)) {
                new Exception(TAG + " Can't initialize device").printStackTrace();
                this.isInit = false;
                return;
            }
            String libpath = this.reactContext.getApplicationInfo().nativeLibraryDir;
            try {
                BASS.BASS_PluginLoad(libpath + "/libbass_fx.so", 0);
                BASS.BASS_PluginLoad(libpath + "/libbassenc.so", 0);
                BASS.BASS_PluginLoad(libpath + "/libbassmix.so", 0);
                BASS.BASS_PluginLoad(libpath + "/libbasswv.so", 0);
                int floatsupport = BASS.BASS_GetConfig(BASS_CONFIG_FLOAT);
                DBLog.d(TAG, "=======>floatsupport=" + floatsupport);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void onResetState() {
        if (effectObjects != null && effectObjects.size() > 0) {
            for (int i = 0; i < effectObjects.size(); i++) {
                if (effectObjects.get(i).isPlaying()) {
                    effectObjects.get(i).setPlaying(false);
                }
            }
        }
    }
}
