package com.reactlibrary.dataMng;

import android.content.Context;
import android.os.Environment;

import com.reactlibrary.constants.IVoiceChangerConstants;
import com.reactlibrary.object.EffectObject;
import com.reactlibrary.soundMng.SoundManager;
import com.reactlibrary.utils.ApplicationUtils;
import com.reactlibrary.utils.DBLog;
import com.reactlibrary.utils.IOUtils;

import java.io.File;
import java.util.ArrayList;


public class TotalDataManager implements IVoiceChangerConstants {

    public static final String TAG = TotalDataManager.class.getSimpleName();

    private static TotalDataManager totalDataManager;
    private ArrayList<EffectObject> listEffectObjects;

    public static TotalDataManager getInstance() {
        if (totalDataManager == null) {
            totalDataManager = new TotalDataManager();
        }
        return totalDataManager;
    }

    private TotalDataManager() {

    }

    public void onDestroy() {
        if (listEffectObjects != null) {
            listEffectObjects.clear();
            listEffectObjects = null;
        }
        try {
            SoundManager.getInstance().releaseSound();
        } catch (Exception e) {
            e.printStackTrace();
        }
        totalDataManager = null;
    }

    public ArrayList<EffectObject> getListEffectObjects() {
        return listEffectObjects;
    }

    public void setListEffectObjects(ArrayList<EffectObject> listEffectObjects) {
        this.listEffectObjects = listEffectObjects;
    }

    public void onResetState() {
        if (listEffectObjects != null && listEffectObjects.size() > 0) {
            for (EffectObject mEffectObject : listEffectObjects) {
                mEffectObject.setPlaying(false);
            }
        }
    }
    public File getTempDirectory() {
        try {
            File mFileRoot = null;
            if (ApplicationUtils.hasSDcard()) {
                mFileRoot = new File(Environment.getExternalStorageDirectory(), NAME_FOLDER_RECORD);
                if (!mFileRoot.exists()) {
                    mFileRoot.mkdirs();
                }
                File mFileCache = new File(mFileRoot, TEMP_FOLDER);
                if (!mFileCache.exists()) {
                    mFileCache.mkdirs();
                }
                return mFileCache;
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }

    public void startCheckCopyMix(Context mContext){
        File mTemp = getTempDirectory();
        if(mTemp!=null){
            try {
                String[] mListFIles = mContext.getAssets().list("mix");
                if(mListFIles!=null && mListFIles.length>0){
                    for(String mName:mListFIles){
                        File mFile = new File(mTemp,mName);
                        if(!mFile.exists() || !mFile.isFile()){
                            boolean b= IOUtils.copyFileAssetsToSdCard(mContext,"mix/"+mName,mName,mTemp.getAbsolutePath());
                            DBLog.d(TAG,"========>start copy name="+mName+"===>b="+b);
                        }

                    }
                }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
