package com.reactlibrary.basseffect;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import com.reactlibrary.dataMng.TotalDataManager;
import com.reactlibrary.utils.DBLog;
import com.reactlibrary.utils.StringUtils;
import com.un4seen.bass.BASS;
import com.un4seen.bass.BASS_FX;
import com.un4seen.bass.BASSenc;
import com.un4seen.bass.BASSmix;

import java.io.File;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Locale;

import static com.un4seen.bass.BASS.BASS_FXSetParameters;

public class DBMediaPlayer implements IDBMediaConstants {

    private static final String TAG = DBMediaPlayer.class.getSimpleName();

    private String mMediaPath;
    private String mPathMix;

    private IDBMediaListener mDBMediaListener;

    private int currrentPostion = 0;
    private int duration = 0;

    private int mChanPlay;
    private int mChanTemp;
    private boolean isNeedMix;

    private boolean isPlaying = false;
    private boolean isPausing = false;
    private ArrayList<Integer> listChannelAds;

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            currrentPostion = getChannelPosition();
            duration = getChannelLength();
            if (!isReverse) {
                if (currrentPostion >= duration) {
                    removeMessages(0);
                    if (mDBMediaListener != null) {
                        mDBMediaListener.onMediaCompletion();
                    }
                } else {
                    sendEmptyMessageDelayed(0, 50);
                }
            } else {
                if (currrentPostion <= 0) {
                    removeMessages(0);
                    if (mDBMediaListener != null) {
                        mDBMediaListener.onMediaCompletion();
                    }
                } else {
                    sendEmptyMessageDelayed(0, 50);
                }
            }
        }
    };

    private int mFxReverbEffect;
    private int mFxFlangerEffect;
    private int mFxEchoEffect;
    private int mFxBiQuadEffect;
    private int mFxDistortEffect;
    private int mFxChorusEffect;
    private int mFxEcho4Effect;
    private int mFxAmplify;
    private int mFxEQ1Effect;
    private int mFxEQ2Effect;
    private int mFxEQ3Effect;
    private int mFxRotateEffect;
    private int mFxPhaserEffect;
    private int mFxCompressorEffect;
    private int mFxAutoWahEffect;
    private boolean isReverse;

    public DBMediaPlayer(String mBeatPath) {
        this.mMediaPath = mBeatPath;
    }


    public void setNeedMix(boolean needMix) {
        isNeedMix = needMix;
    }

    public void setPathMix(String mPathMix) {
        this.mPathMix = mPathMix;
    }

    public boolean prepareAudio() {
        if (!StringUtils.isEmptyString(mMediaPath)) {
            if (mMediaPath.toLowerCase(Locale.getDefault()).endsWith(TYPE_MP3)
                    || mMediaPath.toLowerCase(Locale.getDefault()).endsWith(TYPE_WAV)
                    || mMediaPath.toLowerCase(Locale.getDefault()).endsWith(TYPE_OGG)
                    || mMediaPath.toLowerCase(Locale.getDefault()).endsWith(TYPE_FLAC)) {
                this.initMedia();
                return true;
            } else {
                new Exception("DBMidiPlayer:can not support file format").printStackTrace();
            }
        }
        return false;
    }

    public void startAudio() {
        this.isPlaying = true;
        if (mChanPlay != 0) {
            BASS.BASS_ChannelPlay(mChanPlay, false);
        }
        this.mHandler.sendEmptyMessage(0);
    }

    /**
     * -12 to 12
     *
     * @param semitone
     */
    public void setAudioPitch(int semitone) {
        if (mChanPlay != 0) {
            BASS.BASS_ChannelSetAttribute(mChanPlay, BASS_FX.BASS_ATTRIB_TEMPO_PITCH, semitone);
        }
    }

    /**
     * -30 to 30
     *
     * @param value
     */

    public void setAudioRate(float value) {
        if (mChanPlay != 0) {
            BASS.BASS_ChannelSetAttribute(mChanPlay, BASS_FX.BASS_ATTRIB_TEMPO, value);
        }
    }

    /**
     * ReverbMix- ReverbTime-HighFreg
     *
     * @param value
     */
    public void setAudioReverb(float[] value) {
        if (mChanPlay != 0) {
            if (value != null) {
                if (mFxReverbEffect == 0) {
                    mFxReverbEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_REVERB, 0);
                }
                if (mFxReverbEffect != 0) {
                    BASS.BASS_DX8_REVERB p = new BASS.BASS_DX8_REVERB();
                    BASS.BASS_FXGetParameters(mFxReverbEffect, p);

                    p.fReverbMix = value[0];
                    p.fReverbTime = value[1];
                    p.fHighFreqRTRatio = value[2];
                    boolean b = BASS_FXSetParameters(mFxReverbEffect, p);
                    DBLog.d(TAG, "=================>setAudioReverb=" + b);
                }
            } else {
                if (mFxReverbEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxReverbEffect);
                    mFxReverbEffect = 0;
                }
            }
        }
    }


    /**
     * ReverbMix- ReverbTime-HighFreg
     */
    public void setAudioEcho(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxEchoEffect == 0) {
                    mFxEchoEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_ECHO, 0);
                }
                if (mFxEchoEffect != 0) {
                    BASS.BASS_DX8_ECHO p1 = new BASS.BASS_DX8_ECHO();
                    BASS.BASS_FXGetParameters(mFxEchoEffect, p1);
                    p1.fLeftDelay = values[0];
                    p1.fRightDelay = values[1];
                    p1.fFeedback = values[2];
                    if (values.length == 4) {
                        p1.fWetDryMix = values[3];
                        DBLog.d(TAG, "==========>fLeftDelay=" + values[0] + "===>fRightDelay=" + values[1] + "==>fFeedback=" + values[2] + "==>fWetDryMix=" + p1.fWetDryMix);
                    } else {
                        DBLog.d(TAG, "==========>fLeftDelay=" + values[0] + "===>fRightDelay=" + values[1] + "==>fFeedback=" + values[2]);
                    }
                    boolean echo = BASS_FXSetParameters(mFxEchoEffect, p1);
                    DBLog.d(TAG, "=========>echoEffect=" + echo);
                }
            } else {
                if (mFxEchoEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEchoEffect);
                    mFxEchoEffect = 0;
                }
            }
        }
    }

    public void setAmplify(float amplify) {
        if (mChanPlay != 0) {
            if (amplify != 0) {
                if (mFxAmplify == 0) {
                    mFxAmplify = BASS.BASS_ChannelSetFX(mChanPlay, BASS_FX.BASS_FX_BFX_DAMP, 0);
                }
                if (mFxAmplify != 0) {
                    BASS_FX.BASS_BFX_DAMP p1 = new BASS_FX.BASS_BFX_DAMP();
                    BASS.BASS_FXGetParameters(mFxAmplify, p1);

                    p1.fGain = amplify;
                    BASS_FXSetParameters(mFxAmplify, p1);
                }
            } else {
                if (mFxAmplify != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxAmplify);
                    mFxAmplify = 0;
                }
            }
        }
    }

    //hard distort [1,0,1,0,1] very hard distort [5,0,1,0.1,1], medium [0.2,1,1,0.1,1], soft[0,-2.95,-0.05,-0.18,0.25]
    public void setDistort(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxDistortEffect == 0) {
                    mFxDistortEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_DISTORTION, 0);
                }
                if (mFxDistortEffect != 0) {
                    BASS.BASS_DX8_DISTORTION p1 = new BASS.BASS_DX8_DISTORTION();
                    BASS.BASS_FXGetParameters(mFxDistortEffect, p1);

                    p1.fEdge = values[0];
                    p1.fGain = values[1];
                    p1.fPostEQBandwidth = values[2];
                    p1.fPostEQCenterFrequency = values[3];
                    p1.fPreLowpassCutoff = values[4];
                    BASS_FXSetParameters(mFxDistortEffect, p1);
                }
            } else {
                if (mFxDistortEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxDistortEffect);
                    mFxDistortEffect = 0;
                }
            }
        }
    }

    public void setChorus(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxChorusEffect == 0) {
                    mFxChorusEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS_FX.BASS_FX_BFX_CHORUS, 0);
                }
                if (mFxChorusEffect != 0) {
                    BASS_FX.BASS_BFX_CHORUS p1 = new BASS_FX.BASS_BFX_CHORUS();
                    BASS.BASS_FXGetParameters(mFxChorusEffect, p1);

                    p1.fDryMix = values[0];
                    p1.fWetMix = values[1];
                    p1.fFeedback = values[2];
                    p1.fMinSweep = values[3];
                    p1.fMaxSweep = values[4];
                    p1.fRate = values[5];
                    BASS_FXSetParameters(mFxChorusEffect, p1);
                }
            } else {
                if (mFxChorusEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxChorusEffect);
                    mFxChorusEffect = 0;
                }
            }
        }
    }

    public void setBiQuadFilter(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxBiQuadEffect == 0) {
                    mFxBiQuadEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS_FX.BASS_FX_BFX_BQF, 0);
                }
                if (mFxBiQuadEffect != 0) {
                    BASS_FX.BASS_BFX_BQF p1 = new BASS_FX.BASS_BFX_BQF();
                    BASS.BASS_FXGetParameters(mFxBiQuadEffect, p1);

                    p1.lFilter = (int) values[0];
                    p1.fCenter = values[1];
                    p1.fBandwidth = values[2];

                    BASS_FXSetParameters(mFxBiQuadEffect, p1);
                }
            } else {
                if (mFxBiQuadEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxBiQuadEffect);
                    mFxBiQuadEffect = 0;
                }
            }
        }
    }

    public void setEcho4Effect(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxEcho4Effect == 0) {
                    mFxEcho4Effect = BASS.BASS_ChannelSetFX(mChanPlay, BASS_FX.BASS_FX_BFX_ECHO4, 0);
                }
                if (mFxEcho4Effect != 0) {
                    BASS_FX.BASS_BFX_ECHO4 p1 = new BASS_FX.BASS_BFX_ECHO4();
                    p1.fDryMix = (int) values[0];
                    p1.fWetMix = values[1];
                    p1.fFeedback = values[2];
                    p1.fDelay = values[3];
                    p1.bStereo = false;
                    BASS_FXSetParameters(mFxEcho4Effect, p1);
                }
            } else {
                if (mFxEcho4Effect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEcho4Effect);
                    mFxEcho4Effect = 0;
                }
            }
        }
    }

    /**
     * fCenter- fBandwidth-fGain
     *
     * @param values
     */
    public void setAudioEQ1(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxEQ1Effect == 0) {
                    mFxEQ1Effect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_PARAMEQ, 0);
                }
                if (mFxEQ1Effect != 0) {
                    BASS.BASS_DX8_PARAMEQ p = new BASS.BASS_DX8_PARAMEQ();
                    p.fCenter = values[0];
                    p.fBandwidth = values[1];
                    p.fGain = values[2];
                    BASS_FXSetParameters(mFxEQ1Effect, p);
                }
            } else {
                if (mFxEQ1Effect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEQ1Effect);
                    mFxEQ1Effect = 0;
                }
            }
        }
    }

    public void setAudioEQ2(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxEQ2Effect == 0) {
                    mFxEQ2Effect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_PARAMEQ, 0);
                }
                if (mFxEQ2Effect != 0) {
                    BASS.BASS_DX8_PARAMEQ p = new BASS.BASS_DX8_PARAMEQ();
                    p.fCenter = values[0];
                    p.fBandwidth = values[1];
                    p.fGain = values[2];
                    BASS_FXSetParameters(mFxEQ2Effect, p);

                }
            } else {
                if (mFxEQ2Effect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEQ2Effect);
                    mFxEQ2Effect = 0;
                }
            }
        }
    }

    public void setAudioEQ3(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxEQ3Effect == 0) {
                    mFxEQ3Effect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_PARAMEQ, 0);
                }
                if (mFxEQ3Effect != 0) {
                    BASS.BASS_DX8_PARAMEQ p = new BASS.BASS_DX8_PARAMEQ();
                    p.fCenter = values[0];
                    p.fBandwidth = values[1];
                    p.fGain = values[2];
                    BASS_FXSetParameters(mFxEQ3Effect, p);
                }
            } else {
                if (mFxEQ3Effect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEQ3Effect);
                    mFxEQ3Effect = 0;
                }
            }
        }
    }

    public void setRotate(float values) {
        if (mChanPlay != 0) {
            if (values != 0) {
                if (mFxRotateEffect == 0) {
                    mFxRotateEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS_FX.BASS_FX_BFX_ROTATE, 0);
                }
                if (mFxRotateEffect != 0) {
                    BASS_FX.BASS_BFX_ROTATE p = new BASS_FX.BASS_BFX_ROTATE();
                    BASS.BASS_FXGetParameters(mFxRotateEffect, p);
                    p.fRate = values;
                    BASS_FXSetParameters(mFxRotateEffect, p);
                }
            } else {
                if (mFxRotateEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxRotateEffect);
                    mFxRotateEffect = 0;
                }
            }
        }
    }

    public void setPhaser(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxPhaserEffect == 0) {
                    mFxPhaserEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS_FX.BASS_FX_BFX_PHASER, 0);
                }
                if (mFxPhaserEffect != 0) {
                    BASS_FX.BASS_BFX_PHASER p = new BASS_FX.BASS_BFX_PHASER();
                    BASS.BASS_FXGetParameters(mFxPhaserEffect, p);
                    p.fDryMix = values[0];
                    p.fWetMix = values[1];
                    p.fFeedback = values[2];
                    p.fRate = values[3];
                    p.fRange = values[4];
                    p.fFreq = values[5];
                    boolean b = BASS.BASS_FXSetParameters(mFxPhaserEffect, p);
                    DBLog.d(TAG, "==============>setPhaser=" + b);
                }
            } else {
                if (mFxPhaserEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxPhaserEffect);
                    mFxPhaserEffect = 0;
                }
            }
        }
    }

    public void setCompressor(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxCompressorEffect == 0) {
                    mFxCompressorEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS_FX.BASS_FX_BFX_COMPRESSOR2, 0);
                }
                if (mFxPhaserEffect != 0) {
                    BASS_FX.BASS_BFX_COMPRESSOR2 p = new BASS_FX.BASS_BFX_COMPRESSOR2();
                    BASS.BASS_FXGetParameters(mFxCompressorEffect, p);
                    p.fGain = values[0];
                    p.fThreshold = values[1];
                    p.fRatio = values[2];
                    p.fAttack = values[3];
                    p.fRelease = values[4];
                    BASS_FXSetParameters(mFxCompressorEffect, p);
                }
            } else {
                if (mFxCompressorEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxCompressorEffect);
                    mFxCompressorEffect = 0;
                }
            }
        }
    }

    public void setAutoWah(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxAutoWahEffect == 0) {
                    mFxAutoWahEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS_FX.BASS_FX_BFX_PHASER, 0);
                }
                if (mFxAutoWahEffect != 0) {
                    BASS_FX.BASS_BFX_PHASER p = new BASS_FX.BASS_BFX_PHASER();
                    BASS.BASS_FXGetParameters(mFxAutoWahEffect, p);
                    p.fDryMix = values[0];
                    p.fWetMix = values[1];
                    p.fFeedback = values[2];
                    p.fRate = values[3];
                    p.fRange = values[4];
                    p.fFreq = values[5];
                    BASS_FXSetParameters(mFxAutoWahEffect, p);
                }
            } else {
                if (mFxAutoWahEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxAutoWahEffect);
                    mFxAutoWahEffect = 0;
                }
            }
        }
    }

    public void setFlangeEffect(float[] values) {
        if (mChanPlay != 0) {
            if (values != null) {
                if (mFxFlangerEffect == 0) {
                    mFxFlangerEffect = BASS.BASS_ChannelSetFX(mChanPlay, BASS.BASS_FX_DX8_FLANGER, 0);
                }
                if (mFxFlangerEffect != 0) {
                    try {
                        BASS.BASS_DX8_FLANGER p = new BASS.BASS_DX8_FLANGER();
                        BASS.BASS_FXGetParameters(mFxFlangerEffect, p);
                        p.fWetDryMix = values[0];
                        p.fDepth = values[1];
                        p.fFeedback = values[2];
                        p.fDelay = values[3];
                        p.lPhase = (int) values[4];
                        if (values.length == 6) {
                            p.fFrequency = values[5];//funny effect "reverb":[-5.25,300,0.3],
                        }
                        BASS_FXSetParameters(mFxFlangerEffect, p);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                if (mFxFlangerEffect != 0) {
                    BASS.BASS_ChannelRemoveFX(mChanPlay, mFxFlangerEffect);
                    mFxFlangerEffect = 0;
                }
            }
        }
    }

    public void pauseAudio() {
        if (!isPlaying) {
            new Exception(TAG + " pauseAudio:HanetMediaPlayer not init").printStackTrace();
            return;
        }
        this.isPausing = true;
        if (mChanPlay != 0) {
            BASS.BASS_ChannelPause(mChanPlay);
        }

    }

    public void releaseAudio() {
        mHandler.removeMessages(0);
        if (mFxReverbEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxReverbEffect);
            mFxReverbEffect = 0;
        }
        if (mFxFlangerEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxFlangerEffect);
            mFxReverbEffect = 0;
        }
        if (mFxEchoEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEchoEffect);
            mFxEchoEffect = 0;
        }
        if (mFxBiQuadEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxBiQuadEffect);
            mFxBiQuadEffect = 0;
        }
        if (mFxAmplify != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxAmplify);
            mFxAmplify = 0;
        }
        if (mFxDistortEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxDistortEffect);
            mFxDistortEffect = 0;
        }
        if (mFxChorusEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxChorusEffect);
            mFxChorusEffect = 0;
        }
        if (mFxEcho4Effect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEcho4Effect);
            mFxEcho4Effect = 0;
        }
        if (mFxEQ1Effect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEQ1Effect);
            mFxEQ1Effect = 0;
        }
        if (mFxEQ2Effect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEQ2Effect);
            mFxEQ2Effect = 0;
        }
        if (mFxEQ3Effect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxEQ3Effect);
            mFxEQ3Effect = 0;
        }
        if (mFxRotateEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxRotateEffect);
            mFxRotateEffect = 0;
        }
        if (mFxPhaserEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxPhaserEffect);
            mFxPhaserEffect = 0;
        }
        if (mFxAutoWahEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxAutoWahEffect);
            mFxAutoWahEffect = 0;
        }
        if (mFxCompressorEffect != 0) {
            BASS.BASS_ChannelRemoveFX(mChanPlay, mFxCompressorEffect);
            mFxCompressorEffect = 0;
        }
        isPlaying = false;
        isPausing = false;

        BASS.BASS_StreamFree(mChanPlay);
        BASS.BASS_StreamFree(mChanTemp);
        if (listChannelAds != null && listChannelAds.size() > 0) {
            for (Integer mInteger : listChannelAds) {
                BASS.BASS_StreamFree(mInteger);
            }
            listChannelAds.clear();
            listChannelAds = null;
        }
        mChanTemp = 0;
        mChanPlay = 0;
        DBLog.d(TAG, "=======>release ALl");

    }

    public void setOnDBMediaListener(IDBMediaListener mDBMediaListener) {
        this.mDBMediaListener = mDBMediaListener;
    }

    public int getDuration() {
        if (mChanPlay != 0) {
            duration = getChannelLength();
        }
        return duration;
    }

    public int getCurrrentPostion() {
        return currrentPostion;
    }

    public boolean isPlaying() {
        return isPlaying;
    }

    public void seekTo(int postion) {
        if (!isPlaying) {
            new Exception(TAG + " seekTo:HanetMediaPlayer is not playing").printStackTrace();
            return;
        }
        currrentPostion = postion;
        this.seekChannelTo(postion);
    }

    private void initMedia() {
        releaseAudio();
        if (!StringUtils.isEmptyString(mMediaPath)) {
            if (!isNeedMix) {
                mChanPlay = BASS.BASS_StreamCreateFile(mMediaPath, 0, 0, BASS.BASS_STREAM_DECODE);
            } else {
                Log.d(TAG, "========>BASS_Error=" + BASS.BASS_ErrorGetCode());
                initMediaToMix(false);
            }
        }
        DBLog.d(TAG, "========>mChanPlay=" + mChanPlay);

        if (mChanPlay != 0) {
            if (!isNeedMix) {
                mChanPlay = BASS_FX.BASS_FX_ReverseCreate(mChanPlay, 2, BASS.BASS_STREAM_DECODE | BASS_FX.BASS_FX_FREESOURCE);
            }
            if (mChanPlay != 0) {
                BASS.BASS_CHANNELINFO infoPlay = new BASS.BASS_CHANNELINFO();
                BASS.BASS_ChannelGetInfo(mChanPlay, infoPlay);
                mChanPlay = BASS_FX.BASS_FX_TempoCreate(mChanPlay, BASS_FX.BASS_FX_FREESOURCE);
                if (mChanPlay == 0) {
                    Log.d(TAG, "========>BASS_Error=" + BASS.BASS_ErrorGetCode());
                    new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
                    BASS.BASS_StreamFree(mChanPlay);
                    return;
                }
            } else {
                Log.d(TAG, "========>BASS_Error=" + BASS.BASS_ErrorGetCode());
                new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
                BASS.BASS_StreamFree(mChanPlay);
            }
        } else {
            Log.d(TAG, "========>BASS_Error=" + BASS.BASS_ErrorGetCode());
            new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
            BASS.BASS_StreamFree(mChanPlay);
        }
    }

    private void initMediaToMix(boolean isNeedSave) {
        if (isNeedMix) {
            File mFile = TotalDataManager.getInstance().getTempDirectory();
            if (mFile != null && mFile.isDirectory() && !StringUtils.isEmptyString(mPathMix)) {
                File pathMix = new File(mFile, mPathMix);
                if (pathMix.exists() && pathMix.isFile()) {
                    int mChanMix = BASSmix.BASS_Mixer_StreamCreate(44100, 2, BASS.BASS_STREAM_DECODE);
                    if (mChanMix != 0) {
                        int mTempChanPlay = BASS.BASS_StreamCreateFile(mMediaPath, 0, 0, BASS.BASS_STREAM_DECODE);
                        if (mTempChanPlay != 0) {
                            boolean b = BASSmix.BASS_Mixer_StreamAddChannel(mChanMix, mTempChanPlay, BASSmix.BASS_MIXER_NORAMPIN);
                            DBLog.d(TAG, "=========>add channel play=" + b + "===>mChanPlay=" + mTempChanPlay + "==>mChanTemp=" + mChanMix);
                            if (!b) {
                                BASS.BASS_StreamFree(mChanMix);
                                mChanPlay = mTempChanPlay;
                                return;
                            }
                            this.mChanPlay = mChanMix;
                            this.mChanTemp = mTempChanPlay;

                            long lenght = BASS.BASS_ChannelSeconds2Bytes(mChanMix, 3);
                            long oneSecond = BASS.BASS_ChannelSeconds2Bytes(mChanMix, 1);
                            int totalDuration = getChannelLength();
                            DBLog.d(TAG, "===============>totalDuration=" + totalDuration);
                            if (totalDuration > 0) {
                                listChannelAds = new ArrayList<>();
                                long durationInBytes = BASS.BASS_ChannelSeconds2Bytes(mChanMix, totalDuration);
                                for (int i = 0; i < totalDuration; i++) {
                                    if (i % 3 == 0) {
                                        long start = i * oneSecond;
                                        DBLog.d(TAG, "=====>start=" + start + "==>totalDuration=" + durationInBytes + "==>start+lenght=" + (start + lenght));
                                        int mChannelAdd = BASS.BASS_StreamCreateFile(pathMix.getAbsolutePath(), 0, 0, BASS.BASS_STREAM_DECODE);
                                        if (mChannelAdd != 0) {
                                            if ((start + lenght) < durationInBytes) {
                                                b = BASSmix.BASS_Mixer_StreamAddChannelEx(mChanMix, mChannelAdd, BASSmix.BASS_MIXER_NORAMPIN, start, lenght);
                                            } else {
                                                long duration = durationInBytes - start;
                                                DBLog.d(TAG, "=======>duration still=" + duration);
                                                if (duration > 0) {
                                                    b = BASSmix.BASS_Mixer_StreamAddChannelEx(mChanMix, mChannelAdd, BASSmix.BASS_MIXER_NORAMPIN, start, duration);
                                                }
                                            }
                                        }
                                        if (!b) {
                                            BASS.BASS_StreamFree(mChannelAdd);
                                        } else {
                                            listChannelAds.add(mChannelAdd);
                                        }


                                    }
                                }
                            }


                        } else {
                            BASS.BASS_StreamFree(mChanMix);
                        }
                    }
                }
            }

        }
    }


    public boolean initMediaToSave() {
        BASS.BASS_StreamFree(mChanPlay);
        mChanPlay = BASS.BASS_StreamCreateFile(mMediaPath, 0, 0, BASS.BASS_STREAM_DECODE);
        if (mChanPlay != 0) {
            if (!isNeedMix) {
                mChanPlay = BASS_FX.BASS_FX_ReverseCreate(mChanPlay, 2, BASS.BASS_STREAM_DECODE);
            } else {
                initMediaToMix(true);
            }
            DBLog.d(TAG, "========>mChanPlay=" + mChanPlay);
            if (mChanPlay != 0) {
                mChanPlay = BASS_FX.BASS_FX_TempoCreate(mChanPlay, BASS.BASS_STREAM_DECODE);
                if (mChanPlay == 0) {
                    new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
                    BASS.BASS_StreamFree(mChanPlay);
                    return false;
                }
                return true;
            } else {
                new Exception(TAG + " Couldnt create a resampled stream!").printStackTrace();
                BASS.BASS_StreamFree(mChanPlay);
            }
        }
        return false;
    }

    public void seekChannelTo(int position) {
        if (mChanPlay != 0) {
            BASS.BASS_ChannelSetPosition(mChanPlay, BASS.BASS_ChannelSeconds2Bytes(mChanPlay, position), BASS.BASS_POS_BYTE);
        }
    }

    private int getChannelPosition() {
        if (mChanTemp != 0) {
            int p = (int) BASS.BASS_ChannelBytes2Seconds(mChanTemp, BASS.BASS_ChannelGetPosition(mChanTemp, BASS.BASS_POS_BYTE));
            return p;
        }
        if (mChanPlay != 0) {
            int p = (int) BASS.BASS_ChannelBytes2Seconds(mChanPlay, BASS.BASS_ChannelGetPosition(mChanPlay, BASS.BASS_POS_BYTE));
            return p;
        }
        return -1;
    }

    private int getChannelLength() {
        if (mChanTemp != 0) {
            int p = (int) BASS.BASS_ChannelBytes2Seconds(mChanTemp, BASS.BASS_ChannelGetLength(mChanTemp, BASS.BASS_POS_BYTE));
            return p;
        }
        if (mChanPlay != 0) {
            int p = (int) BASS.BASS_ChannelBytes2Seconds(mChanPlay, BASS.BASS_ChannelGetLength(mChanPlay, BASS.BASS_POS_BYTE));
            return p;
        }
        return 0;
    }


    public void setReverse(boolean b) {
        isReverse = b;
        if (mChanPlay != 0) {
            int srcChan = BASS_FX.BASS_FX_TempoGetSource(mChanPlay);
            float dir = 0.0f;
            BASS.FloatValue floatValue = new BASS.FloatValue();
            floatValue.value = dir;
            BASS.BASS_ChannelGetAttribute(srcChan, BASS_FX.BASS_ATTRIB_REVERSE_DIR, floatValue);
            if (b) {
                BASS.BASS_ChannelSetAttribute(srcChan, BASS_FX.BASS_ATTRIB_REVERSE_DIR, BASS_FX.BASS_FX_RVS_REVERSE);
            } else {
                BASS.BASS_ChannelSetAttribute(srcChan, BASS_FX.BASS_ATTRIB_REVERSE_DIR, BASS_FX.BASS_FX_RVS_FORWARD);
            }
        }
    }


    public void saveToFile(String filePath) {
        if (!StringUtils.isEmptyString(filePath) && mChanPlay != 0) {
            int encoder = BASSenc.BASS_Encode_Start(mChanPlay, filePath, BASSenc.BASS_ENCODE_PCM | BASSenc.BASS_ENCODE_AUTOFREE, null, 0);
            if (encoder != 0) {
                try {
                    ByteBuffer buf = ByteBuffer.allocateDirect(20000); // allocate buffer for decoding
                    while (true) {
                        int r = BASS.BASS_ChannelGetData(mChanPlay, buf, buf.capacity()); // decode some data (and send it to WAV writer)
                        if (r == -1 || r == 0) {
                            break;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
