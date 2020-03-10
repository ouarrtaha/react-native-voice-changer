# react-native-voice-changer

## Getting started

`$ npm install react-native-voice-changer --save`

### Mostly automatic installation

`$ react-native link react-native-voice-changer`

### Android permissions

```
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
<uses-permission android:name="android.permission.RECORD_AUDIO" />
```
You may use [react-native-permissions](https://github.com/react-native-community/react-native-permissions) to grant permissions 

## Usage

### Example

[Full example for Android](https://github.com/ouarrtaha/react-native-voice-changer/tree/master/example) //todo example

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
| VoiceChanger.insertEffect(effect) | Set effects array to be played |
| VoiceChanger.playEffect(effectIndex) | Play specified effect by index |
| VoiceChanger.saveEffect(effectIdx) | Save processed media with a specific effect |



