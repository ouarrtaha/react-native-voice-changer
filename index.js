import { NativeModules } from 'react-native';

const { VoiceChanger } = NativeModules;

const RnVoiceChanger = {
    insertEffect: effect => VoiceChanger.insertEffect(effect),
    setPath: path => VoiceChanger.setPath(path),
    setPlayingIndex: idx => VoiceChanger.setPlayingIndex(idx),
    saveEffect: effectIdx => VoiceChanger.saveEffect(effectIdx),
    createOutputDir: () => VoiceChanger.createOutputDir(),
    createDBMedia: () => VoiceChanger.createDBMedia(),
    playEffect: effectIndex => VoiceChanger.playEffect(effectIndex),
};

export default RnVoiceChanger;
