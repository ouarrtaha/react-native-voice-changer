package com.reactlibrary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.widget.EditText;
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
import com.reactlibrary.basseffect.IDBMediaListener;
import com.reactlibrary.dataMng.JsonParsingUtils;
import com.reactlibrary.object.EffectObject;
import com.reactlibrary.task.DBTask;
import com.reactlibrary.task.IDBCallback;
import com.reactlibrary.task.IDBTaskListener;
import com.reactlibrary.utils.ApplicationUtils;
import com.reactlibrary.utils.DBLog;
import com.reactlibrary.utils.StringUtils;
import com.un4seen.bass.BASS;
import com.reactlibrary.basseffect.DBMediaPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.reactlibrary.constants.IVoiceChangerConstants.FORMAT_NAME_VOICE;
import static com.reactlibrary.constants.IVoiceChangerConstants.NAME_FOLDER_RECORD;
import static com.un4seen.bass.BASS.BASS_CONFIG_FLOAT;

public class VoiceChangerModule extends ReactContextBaseJavaModule {

    public static final String TAG = VoiceChangerModule.class.getSimpleName();
    private final ReactApplicationContext reactContext;
    private ArrayList<EffectObject> effectObjects;
    private String mPathAudio;
    private DBMediaPlayer mDBMedia;
    private boolean isInit;
    private Integer playingIndex;
    private File outputDir;
    private String mNameExportVoice;

    public VoiceChangerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        this.effectObjects = new ArrayList<>();
        this.mPathAudio = null;
        this.playingIndex = null;
        this.outputDir = null;
        this.onInitAudioDevice();
    }

    @Override
    public String getName() {
        return "VoiceChanger";
    }

    private static final String DURATION_SHORT_KEY = "SHORT";
    private static final String DURATION_LONG_KEY = "LONG";

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
    public void show(String message, int duration) {
        Toast.makeText(getReactApplicationContext(), message, duration).show();
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
    public void saveEffect(int effectIndex, Promise promise) {
        onSaveEffect(this.effectObjects.get(effectIndex), promise);
    }

    @ReactMethod
    public void createOutputDir() {
        this.outputDir = this.getDir();
    }

    @ReactMethod
    public void createDBMedia() {
        this.onCreateDBMedia();
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

    public void onSaveEffect(final EffectObject mEffectObject, final Promise promise) {
        if (mDBMedia != null) {
            onResetState();
        }

        mNameExportVoice = String.format(FORMAT_NAME_VOICE, String.valueOf(System.currentTimeMillis() / 1000));
        showDialogEnterName(new IDBCallback() {
            @Override
            public void onAction() {
                if (mDBMedia != null) {
                    startSaveEffect(mEffectObject, new IDBCallback() {
                        @Override
                        public void onAction() {

                            final File mOutPutFile = new File(outputDir, mNameExportVoice);
                            if (mOutPutFile.exists() && mOutPutFile.isFile()) {
                                String mInfoSave = String.format("Your voice path is %1$s", mOutPutFile.getAbsolutePath());
                                showToast(mInfoSave);
                                promise.resolve(mOutPutFile.getAbsolutePath());
                            }
                        }
                    });

                }
            }
        });
    }

    private void startSaveEffect(final EffectObject mEffectObject, final IDBCallback mDBCallback) {
        final File mTempOutPutFile = new File(outputDir, mNameExportVoice);

        final DBMediaPlayer mDBExportMedia = new DBMediaPlayer(mPathAudio);
        mDBExportMedia.setPathMix(mEffectObject.getPathMix());
        mDBExportMedia.setNeedMix(mEffectObject.isMix());

        DBTask mDBTask = new DBTask(new IDBTaskListener() {

            @Override
            public void onPreExcute() {
                //todo progress dialog
            }

            @Override
            public void onDoInBackground() {
                boolean b = mDBExportMedia.initMediaToSave();
                if (b) {
                    mDBExportMedia.setReverse(mEffectObject.isReverse());
                    mDBExportMedia.setAudioPitch(mEffectObject.getPitch());
                    mDBExportMedia.setCompressor(mEffectObject.getCompressor());
                    mDBExportMedia.setAudioRate(mEffectObject.getRate());
                    mDBExportMedia.setAudioEQ1(mEffectObject.getEq1());
                    mDBExportMedia.setAudioEQ2(mEffectObject.getEq2());
                    mDBExportMedia.setAudioEQ3(mEffectObject.getEq3());
                    mDBExportMedia.setPhaser(mEffectObject.getPhaser());
                    mDBExportMedia.setAutoWah(mEffectObject.getAutoWah());
                    mDBExportMedia.setAudioReverb(mEffectObject.getReverb());
                    mDBExportMedia.setEcho4Effect(mEffectObject.getEcho4());
                    mDBExportMedia.setAudioEcho(mEffectObject.getEcho());

                    mDBExportMedia.setBiQuadFilter(mEffectObject.getFilter());
                    mDBExportMedia.setFlangeEffect(mEffectObject.getFlange());
                    mDBExportMedia.setChorus(mEffectObject.getChorus());
                    mDBExportMedia.setAmplify(mEffectObject.getAmplify());
                    mDBExportMedia.setDistort(mEffectObject.getDistort());
                    mDBExportMedia.setRotate(mEffectObject.getRotate());

                    mDBExportMedia.saveToFile(mTempOutPutFile.getAbsolutePath());
                    mDBExportMedia.releaseAudio();
                }
            }

            @Override
            public void onPostExcute() {
                //todo dissmiss progress dialog
                if (mDBCallback != null) {
                    mDBCallback.onAction();
                }
            }

        });
        mDBTask.execute();
    }

    private void showDialogEnterName(final IDBCallback mDCallback) {
        final EditText mEdName = new EditText(reactContext);
        mEdName.setSingleLine(true);
        AlertDialog.Builder builder = new AlertDialog.Builder(getCurrentActivity()).setTitle("Enter title").setView(mEdName)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ApplicationUtils.hiddenVirtualKeyboard(reactContext, mEdName);
                        String mNewName = mEdName.getText().toString();
                        if (!StringUtils.isEmptyString(mNewName)) {
                            if (StringUtils.isContainsSpecialCharacter(mNewName)) {
                                showToast("Your name can only contain the alphabet or number characters");
                                return;
                            }
                            mNameExportVoice = mNewName + ".wav";
                        }
                        if (mDCallback != null) {
                            mDCallback.onAction();
                        }
                    }
                }).setNegativeButton("Skip", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (mDCallback != null) {
                            mDCallback.onAction();
                        }
                    }
                });
        AlertDialog mDialogEnterPass = builder.create();
        mDialogEnterPass.show();
    }

    @ReactMethod
    private void onCreateDBMedia() {
        if (!StringUtils.isEmptyString(mPathAudio)) {
            mDBMedia = new DBMediaPlayer(mPathAudio);
            mDBMedia.prepareAudio();
            mDBMedia.setOnDBMediaListener(new IDBMediaListener() {
                @Override
                public void onMediaError() {

                }

                @Override
                public void onMediaCompletion() {
                    effectObjects.get(playingIndex).setPlaying(false);
                    setPlayingIndex(null);
                    WritableMap params = Arguments.createMap();
                    sendEvent(reactContext, "onMediaCompletion", params);
                }
            });
        } else {
            showToast("Media file not found!");
        }
    }


    private void showToast(String message) {
        Toast toast = Toast.makeText(reactContext, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM, 0, 0);
        toast.show();
    }

    private void sendEvent(ReactContext reactContext, String eventName, @Nullable WritableMap params) {
        reactContext
                .getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class)
                .emit(eventName, params);
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

    private File getDir() {
        String dirpath = Environment.getExternalStorageDirectory().getPath();
        File dir = new File(dirpath, NAME_FOLDER_RECORD);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir;
    }
}
