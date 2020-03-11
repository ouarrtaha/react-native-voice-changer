# react-native-voice-changer

**react-native-voice-changer** is a wrapper of [un4seen Bass](https://www.un4seen.com/bass.html) a native voice processing library 

### Demo

[Playstore](https://play.google.com/store/apps/details?id=com.pappillon.voice)

## Getting started

`$ npm install react-native-voice-changer --save`

### Mostly automatic installation

For `RN >=0.60` linking is automatic, else: <br>
`$ react-native link react-native-voice-changer`

### Android permissions

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```
You may use [react-native-permissions](https://github.com/react-native-community/react-native-permissions) to grant permissions 

## Usage

### Example

[Full example for Android](https://github.com/ouarrtaha/react-native-voice-changer/tree/master/example)

```javascript
import voiceChanger from 'react-native-voice-changer';

const effects = [
          {
              id: 1,
              icon: require('./icons/default_fx.png'),
              name: "helium",
              pitch: 10,
              rate: 0,
              reverb: []
          },
          {
              id: "3",
              icon: require('./icons/default_fx.png'),
              name: "Robot",
              pitch: 3,
              rate: 5,
              amplify: 10,
              echo: [100, 100, 40]
          },
          {
              id: "4",
              icon: require('./icons/default_fx.png'),
              name: "Cave",
              pitch: 0,
              rate: 0,
              reverb: [-3.25, 2000, 0.8]
          },
      ];

class VoiceChangerExample extends Component {
    componentDidMount() {
        this.initMedia();
    }
        
    initMedia = () => {
      voiceChanger.setPath(AudioUtils.DocumentDirectoryPath + '/record.wav');
      voiceChanger.createDBMedia();
      effects.forEach(e => voiceChanger.insertEffect(JSON.stringify(e)));
    };
    
    onPlay = (idx) => {
        voiceChanger.playEffect(idx)
    };
    
    onSave = (idx) => {
        voiceChanger
        .saveEffect(idx)
        .then(path => {
            console.log('Saved to: ', path);
            /* Do something with your saved file */
        });
    };
  ...
}

```

### API

| Method | Description |
| ------ | ------ |
| VoiceChanger.setPath(path) | Set current path of media to be processed |
| VoiceChanger.createOutputDir | Create folder to save processed media |
| VoiceChanger.createDBMedia | Prepare media player  |
| VoiceChanger.insertEffect(effect) | Add effect to effects list |
| VoiceChanger.playEffect(effectIndex) | Play specified effect by index |
| VoiceChanger.saveEffect(effectIdx) | Save processed media with a specific effect |


### EFFECTS

Effect object has the following properties

| Property | Type | Decription |
| ------ | ------ | ------ |
| pitch: | Int | The pitch of a channel, [-60...0...+60] semitones. |
| rate: | float | The tempo of a channel, [-95...0...+5000] percents. |
| reverb: | [fReverbMix,<br> fReverbTime,<br> fHighFreqRTRatio] | <strong>fReverbMix</strong>: Reverb mix, in dB, in the range from -96 through 0. The default value is 0 dB. <br> <strong>fReverbTime:</strong> Reverb time, in milliseconds, in the range from 0.001 through 3000. The default value is 1000. <br> <strong>fHighFreqRTRatio:</strong> High-frequency reverb time ratio, in the range from 0.001 through 0.999. The default value is 0.001. |
| filter: | [lFilter,<br> fCenter,<br> fBandwidth] | <strong>lFilter:</strong> Defines which BiQuad filter should be used. [Filters list](http://www.bass.radio42.com/help/html/6b76fdbd-d202-45cb-78b3-d171e09bfa83.htm) <br> <strong>fCenter:</strong> Cut-off frequency (Center in PEAKINGEQ and Shelving filters) in Hz (1...info.freq/2). Default = 200Hz. <br> <strong>fBandwidth:</strong> Bandwidth in octaves (0.1...4...n), Q is not in use (fBandwidth has priority over fQ). Default = 1 (0=not in use).   |
| flanger: | [fWetDryMix,<br> fDepth,<br> fFeedback,<br> fDelay,<br> lPhase,<br> fFrequency] | <strong>fWetDryMix:</strong> Ratio of wet (processed) signal to dry (unprocessed) signal. Must be in the range from 0 through 100 (all wet). The default value is 50. <br> <strong>fDepth:</strong> Percentage by which the delay time is modulated by the low-frequency oscillator (LFO). Must be in the range from 0 through 100. The default value is 100. <br> <strong>fFeedback:</strong> Percentage of output signal to feed back into the effect's input, in the range from -99 to 99. The default value is -50. <br> <strong>fDelay:</strong> Number of milliseconds the input is delayed before it is played back, in the range from 0 to 4. The default value is 2 ms. <br> <strong>lPhase:</strong> Phase differential between left and right LFOs, one of BASS_DX8_PHASE_NEG_180, BASS_DX8_PHASE_NEG_90, BASS_DX8_PHASE_ZERO, BASS_DX8_PHASE_90 and BASS_DX8_PHASE_180. The default value is BASS_DX8_PHASE_ZERO. <br> <strong>fFrequency:</strong> Frequency of the LFO, in the range from 0 to 10. The default value is 0.25. |
| chorus: | [fDryMix,<br> fWetMix,<br> fFeedback,<br> fMinSweep,<br> fMaxSweep,<br> fRate] | <strong>fDryMix:</strong> Dry (unaffected) signal mix (-2...+2). Default = 0. <br> <strong>fWetMix:</strong> Wet (affected) signal mix (-2...+2). Default = 0. <br> <strong>fFeedback:</strong> Feedback (-1...+1). Default = 0. <br> <strong>fMinSweep:</strong> Minimum delay in ms (0<...6000). Default = 0. <br> <strong>fMaxSweep:</strong> Maximum delay in ms (0<...6000). Default = 0. <br> <strong>fRate:</strong> Rate in ms/s (0<...1000). Default = 0.| 
| distort: | [fEdge,<br> fGain,<br> fPostEQBandwidth,<br> fPostEQCenterFrequency,<br> fPreLowpassCutoff] |  <strong>fEdge:</strong> Percentage of distortion intensity, in the range in the range from 0 through 100. The default value is 15 percent. <br> <strong>fGain:</strong> Amount of signal change after distortion, in the range from -60 through 0. The default value is -18 dB. <br> <strong>fPostEQBandwidth:</strong> Width of frequency band that determines range of harmonic content addition, in the range from 100 through 8000. The default value is 2400 Hz. <br> <strong>fPostEQCenterFrequency:</strong> Center frequency of harmonic content addition, in the range from 100 through 8000. The default value is 2400 Hz. <br> <strong>fPreLowpassCutoff:</strong> Filter cutoff for high-frequency harmonics attenuation, in the range from 100 through 8000. The default value is 8000 Hz. |
| reverse: | boolean | Reverse playback direction|
| echo: | [fLeftDelay,<br> fRightDelay,<br> fFeedback,<br> fWetDryMix] | <strong>fLeftDelay:</strong> Delay for left channel, in milliseconds, in the range from 1 through 2000. The default value is 500 ms. <br> <strong>fRightDelay:</strong> Delay for right channel, in milliseconds, in the range from 1 through 2000. The default value is 500 ms. <br> <strong>fFeedback:</strong> Percentage of output fed back into input, in the range from 0 through 100. The default value is 50. <br> <strong>fWetDryMix:</strong> Ratio of wet (processed) signal to dry (unprocessed) signal. Must be in the range from 0 through 100 (all wet). The default value is 50. |
| echo4: | [fDryMix,<br> fWetMix,<br> fFeedback,<br> fDelay] | <strong>fDryMix:</strong> Dry (unaffected) signal mix (-2...+2). Default = 0. <br> <strong>fWetMix:</strong> Wet (affected) signal mix (-2...+2). Default = 0. <br> <strong>fFeedback:</strong> Percentage of output signal to feed back into the effect's input, in the range from -99 to 99. The default value is -50. <br> <strong>fDelay:</strong> Delay in seconds (0<...6). Default = 0. |
| eq1: | [fCenter,<br> fBandwidth,<br> fGain] | <strong>fCenter:</strong> Center frequency, in hertz. <br> <strong>fBandwidth:</strong> Bandwidth, in semitones, in the range from 1 to 36. The default value is 12. <br> <strong>fGain:</strong> Gain, in the range from -15 to 15. The default value is 0 dB. |
| eq2: | [fCenter,<br> fBandwidth,<br> fGain] | *Same as eq1* |
| eq3: | [fCenter,<br> fBandwidth,<br> fGain] | *Same as eq1* |
| amplify: | float | Amplification level (0...1...n, linear). Default = 0. |
| rotate: | float | Rotation rate/speed in Hz (A negative rate can be used for reverse direction). |
| phaser: | [fDryMix,<br> fWetMix,<br> fFeedback,<br> fRate,<br> fRange,<br> fFreq] | <strong>fDryMix:</strong> Dry (unaffected) signal mix (-2...+2). Default = 0. <br> <strong>fWetMix:</strong> Wet (affected) signal mix (-2...+2). Default = 0. <br> <strong>fFeedback:</strong> Feedback (-1...+1). Default = 0. <br> <strong>fRate:</strong> Rate of sweep in cycles per second (0<...<10). Default = 0. <br> <strong>fRange:</strong> Sweep range in octaves (0<...<10). Default = 0. <br> <strong>fFreq:</strong> Base frequency of sweep range (0<...1000). Default = 0. |
| autoWah: | [fDryMix,<br> fWetMix,<br> fFeedback,<br> fRate,<br> fRange,<br> fFreq] | *Same as phaser*|
| compressor: | [fGain,<br> fThreshold,<br> fRatio,<br> fAttack,<br> fRelease] | <strong>fGain:</strong> Output gain in dB of signal after compression, in the range from -60 to 60. The default value is 5 dB. <br> <strong>fThreshold:</strong> Point in dB at which compression begins, in decibels, in the range from -60 to 0. The default value is -15 dB. <br> <strong>fRatio:</strong> Compression ratio, in the range from 1 to 100. The default value is 3, which means 3:1 compression. <br> <strong>fAttack:</strong> Time in ms before compression reaches its full value, in the range from 0.01 to 500. The default value is 20 ms. <br> <strong>fRelease:</strong> Time (speed) in ms at which compression is stopped after input drops below fThreshold, in the range from 50 to 3000. The default value is 200 ms. |


### Resources

- [Un4seen](https://www.un4seen.com/)
- [BassFX Effects](http://www.bass.radio42.com/help/html/7dca7f83-81b6-6232-ded4-8d68e46fb8f1.htm)
