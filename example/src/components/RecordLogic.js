import React, {Component} from 'react';
import {StyleSheet, View} from 'react-native';
import Sound from 'react-native-sound';
import AudioRecord from 'react-native-audio-record';
import Pulse from 'react-native-pulse';
import { Button } from 'react-native-paper';

export default class App extends Component {
    sound = null;
    state = {
        audioFile: '',
        recording: false,
        loaded: false,
        paused: true
    };

    async componentDidMount() {
        const options = {
            sampleRate: 44100,
            channels: 1,
            bitsPerSample: 16,
            wavFile: 'record.wav'
        };

        AudioRecord.init(options);
    }

    componentWillUnmount() {
        this.stop();
    }

    start = () => {
        console.log('start record');
        this.setState({audioFile: '', recording: true, loaded: false});
        AudioRecord.start();
    };

    stop = async () => {
        if (!this.state.recording) return;
        let audioFile = await AudioRecord.stop();
        this.setState({audioFile, recording: false});
        console.log('audioFile', audioFile);
    };

    load = () => {
        return new Promise((resolve, reject) => {
            if (!this.state.audioFile) {
                return reject('file path is empty');
            }

            this.sound = new Sound(this.state.audioFile, '', error => {
                if (error) {
                    console.log('failed to load the file', error);
                    return reject(error);
                }
                this.setState({loaded: true});
                return resolve();
            });
        });
    };

    play = async () => {
        if (!this.state.loaded) {
            try {
                await this.load();
            } catch (error) {
                console.log(error);
            }
        }

        this.setState({paused: false});
        Sound.setCategory('Playback');

        this.sound.play(success => {
            if (success) {
                console.log('successfully finished playing');
            } else {
                console.log('playback failed due to audio decoding errors');
            }
            this.setState({paused: true});
        });
    };

    pause = () => {
        this.sound.pause();
        this.setState({paused: true});
    };

    render() {
        const {recording, paused, audioFile } = this.state;
        const {navigation} = this.props;

        return (
            <View style={styles.container}>
                <View style={styles.rowCenter}>
                    {recording && <Pulse color="#F53830"
                                         numPulses={5}
                                         diameter={250}
                                         initialDiameter={100}
                                         speed={15}
                                         duration={800}
                                         style={styles.pulse}/>}
                    <Button
                        style={styles.roundedBtn}
                        contentStyle={{padding: 15}}
                        icon="record-voice-over"
                        mode="contained"
                        onPress={() => !recording ? this.start() : this.stop()}
                    >
                        {!recording ? "Record" : "Stop"}
                    </Button>
                </View>
                <View style={styles.rowSecondary}>
                    <Button
                        style={styles.roundedBtn}
                        disabled={!audioFile}
                        icon={paused ? "play-arrow" : "pause"}
                        mode="contained"
                        onPress={() => paused ? this.play() : this.pause()}
                    >
                        {paused ? "Play" : "Pause"}
                    </Button>
                    <Button
                        style={styles.roundedBtn}
                        disabled={!audioFile}
                        icon={"brush"}
                        mode="contained"
                        onPress={() => {audioFile && navigation.navigate('effects')} }
                    >
                        {"Magic"}
                    </Button>
                </View>
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flex: 1,
        justifyContent: 'center',
        flexDirection: "column"
    },
    rowCenter: {
        width: "100%",
        flexDirection: 'row',
        justifyContent: 'center',
    },
    rowSecondary: {
        width: "100%",
        flexDirection: 'row',
        justifyContent: 'space-evenly',
        marginTop: 20,
    },
    secondaryBtnContainer: {
        justifyContent: "center",
        alignItems: "center",
    },
    secondaryBtn: {
        resizeMode: "stretch",
        aspectRatio: 1,
        width: "55%",
        height: undefined
    },
    roundedBtn: {
        borderRadius: 50,
        overflow: 'hidden'
    }
});
