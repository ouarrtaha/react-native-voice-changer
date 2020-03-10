import React, {Component} from 'react';
import {DeviceEventEmitter, ScrollView, StyleSheet, View} from 'react-native';
import voiceChanger from 'react-native-voice-changer';
import {AudioUtils} from 'react-native-audio';

import effects from '../assets/effects';
import EffectItem from '../components/ListItem';

class Effects extends Component<Props> {
    constructor(props) {
        super(props);
        this.state = {
            playingIndex: null
        };
    }

    componentDidMount() {
        this.initMedia();
        this.mediaPlayerListner = DeviceEventEmitter.addListener('onMediaCompletion', () => this.setState({playingIndex: null}));
    }

    componentWillUnmount() {
        this.mediaPlayerListner.remove();
    }

    initMedia = () => {
        voiceChanger.setPath(AudioUtils.DocumentDirectoryPath + '/record.wav');
        voiceChanger.createDBMedia();
        effects.forEach(e => voiceChanger.insertEffect(JSON.stringify(e)));
    };

    render() {
        const {playingIndex} = this.state;

        return (
            <View style={styles.container}>
                <ScrollView style={styles.scrollContainer}>
                    {effects.map((effect, idx) => {
                        return (
                            <EffectItem
                                key={idx}
                                onPlay={() => {
                                    voiceChanger.playEffect(idx);
                                    this.setState({playingIndex: idx});
                                }}
                                onSave={() => {
                                    voiceChanger.saveEffect(idx)
                                        .then(path => {
                                            console.log('Saved to: ', path);
                                            /* Do something with your saved file */
                                        });
                                }}
                                playingIndex={playingIndex}
                                idx={idx}
                                effect={effect}
                            />
                        );
                    })}
                </ScrollView>
            </View>
        );
    }

}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        alignItems: 'center',
        width: '100%',
    },
    scrollContainer: {
        marginTop: 4,
        height: '80%',
        width: '95%',
    },
});


export default Effects;


