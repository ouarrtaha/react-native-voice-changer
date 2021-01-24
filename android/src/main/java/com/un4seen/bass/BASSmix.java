/*
	BASSmix 2.4 Java class
	Copyright (c) 2005-2020 Un4seen Developments Ltd.

	See the BASSMIX.CHM file for more detailed documentation
*/

package com.un4seen.bass;

import java.nio.ByteBuffer;

@SuppressWarnings({"all"})
public class BASSmix
{
	// Additional BASS_SetConfig options
	public static final int BASS_CONFIG_MIXER_BUFFER = 0x10601;
	public static final int BASS_CONFIG_MIXER_POSEX = 0x10602;
	public static final int BASS_CONFIG_SPLIT_BUFFER = 0x10610;

	// BASS_Mixer_StreamCreate flags
	public static final int BASS_MIXER_END = 0x10000;	// end the stream when there are no sources
	public static final int BASS_MIXER_NONSTOP = 0x20000;	// don't stall when there are no sources
	public static final int BASS_MIXER_RESUME = 0x1000;	// resume stalled immediately upon new/unpaused source
	public static final int BASS_MIXER_POSEX = 0x2000;	// enable BASS_Mixer_ChannelGetPositionEx support

	// BASS_Mixer_StreamAddChannel/Ex flags
	public static final int BASS_MIXER_CHAN_ABSOLUTE = 0x1000;	// start is an absolute position
	public static final int BASS_MIXER_CHAN_BUFFER = 0x2000;	// buffer data for BASS_Mixer_ChannelGetData/Level
	public static final int BASS_MIXER_CHAN_LIMIT = 0x4000;	// limit mixer processing to the amount available from this source
	public static final int BASS_MIXER_CHAN_MATRIX = 0x10000;	// matrix mixing
	public static final int BASS_MIXER_CHAN_PAUSE = 0x20000;	// don't process the source
	public static final int BASS_MIXER_CHAN_DOWNMIX = 0x400000; // downmix to stereo/mono
	public static final int BASS_MIXER_CHAN_NORAMPIN = 0x800000; // don't ramp-in the start
	public static final int BASS_MIXER_BUFFER = BASS_MIXER_CHAN_BUFFER;
	public static final int BASS_MIXER_LIMIT = BASS_MIXER_CHAN_LIMIT;
	public static final int BASS_MIXER_MATRIX = BASS_MIXER_CHAN_MATRIX;
	public static final int BASS_MIXER_PAUSE = BASS_MIXER_CHAN_PAUSE;
	public static final int BASS_MIXER_DOWNMIX = BASS_MIXER_CHAN_DOWNMIX;
	public static final int BASS_MIXER_NORAMPIN = BASS_MIXER_CHAN_NORAMPIN;

	// Mixer attributes
	public static final int BASS_ATTRIB_MIXER_LATENCY = 0x15000;

	// BASS_Split_StreamCreate flags
	public static final int BASS_SPLIT_SLAVE = 0x1000;	// only read buffered data
	public static final int BASS_SPLIT_POS = 0x2000;

	// Splitter attributes
	public static final int BASS_ATTRIB_SPLIT_ASYNCBUFFER = 0x15010;
	public static final int BASS_ATTRIB_SPLIT_ASYNCPERIOD = 0x15011;

	// Envelope node
	public static class BASS_MIXER_NODE {
		public BASS_MIXER_NODE() {}
		public BASS_MIXER_NODE(long _pos, float _value) { pos=_pos; value=_value; }
		public long pos;
		public float value;
	}

	// Envelope types
	public static final int BASS_MIXER_ENV_FREQ = 1;
	public static final int BASS_MIXER_ENV_VOL = 2;
	public static final int BASS_MIXER_ENV_PAN = 3;
	public static final int BASS_MIXER_ENV_LOOP = 0x10000; // flag: loop
	public static final int BASS_MIXER_ENV_REMOVE = 0x20000; // flag: remove at end

	// Additional sync types
	public static final int BASS_SYNC_MIXER_ENVELOPE = 0x10200;
	public static final int BASS_SYNC_MIXER_ENVELOPE_NODE = 0x10201;

	// Additional BASS_Mixer_ChannelSetPosition flag
	public static final int BASS_POS_MIXER_RESET = 0x10000; // flag: clear mixer's playback buffer

	// BASS_CHANNELINFO types
	public static final int BASS_CTYPE_STREAM_MIXER = 0x10800;
	public static final int BASS_CTYPE_STREAM_SPLIT = 0x10801;

	public static native int BASS_Mixer_GetVersion();

	public static native int BASS_Mixer_StreamCreate(int freq, int chans, int flags);
	public static native boolean BASS_Mixer_StreamAddChannel(int handle, int channel, int flags);
	public static native boolean BASS_Mixer_StreamAddChannelEx(int handle, int channel, int flags, long start, long length);
	public static native int BASS_Mixer_StreamGetChannels(int handle, int[] channels, int count);

	public static native int BASS_Mixer_ChannelGetMixer(int handle);
	public static native int BASS_Mixer_ChannelFlags(int handle, int flags, int mask);
	public static native boolean BASS_Mixer_ChannelRemove(int handle);
	public static native boolean BASS_Mixer_ChannelSetPosition(int handle, long pos, int mode);
	public static native long BASS_Mixer_ChannelGetPosition(int handle, int mode);
	public static native long BASS_Mixer_ChannelGetPositionEx(int channel, int mode, int delay);
	public static native int BASS_Mixer_ChannelGetLevel(int handle);
	public static native boolean BASS_Mixer_ChannelGetLevelEx(int handle, float[] levels, float length, int flags);
	public static native int BASS_Mixer_ChannelGetData(int handle, ByteBuffer buffer, int length);
	public static native int BASS_Mixer_ChannelSetSync(int handle, int type, long param, BASS.SYNCPROC proc, Object user);
	public static native boolean BASS_Mixer_ChannelRemoveSync(int channel, int sync);
	public static native boolean BASS_Mixer_ChannelSetMatrix(int handle, float[][] matrix);
	public static native boolean BASS_Mixer_ChannelSetMatrixEx(int handle, float[][] matrix, float time);
	public static native boolean BASS_Mixer_ChannelGetMatrix(int handle, float[][] matrix);
	public static native boolean BASS_Mixer_ChannelSetEnvelope(int handle, int type, BASS_MIXER_NODE[] nodes, int count);
	public static native boolean BASS_Mixer_ChannelSetEnvelopePos(int handle, int type, long pos);
	public static native long BASS_Mixer_ChannelGetEnvelopePos(int handle, int type, Float value);

	public static native int BASS_Split_StreamCreate(int channel, int flags, int[] chanmap);
	public static native int BASS_Split_StreamGetSource(int handle);
	public static native int BASS_Split_StreamGetSplits(int handle, int[] splits, int count);
	public static native boolean BASS_Split_StreamReset(int handle);
	public static native boolean BASS_Split_StreamResetEx(int handle, int offset);
	public static native int BASS_Split_StreamGetAvailable(int handle);

    static {
        System.loadLibrary("bassmix");
    }
}
