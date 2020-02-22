package com.reactlibrary;

import android.widget.Toast;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.bridge.Callback;
import com.reactlibrary.dataMng.JsonParsingUtils;
import com.reactlibrary.object.EffectObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VoiceChangerModule extends ReactContextBaseJavaModule {

    private final ReactApplicationContext reactContext;
    private ArrayList<EffectObject> effectObjects;

    public VoiceChangerModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.reactContext = reactContext;

        this.effectObjects = new ArrayList<>();
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
}
