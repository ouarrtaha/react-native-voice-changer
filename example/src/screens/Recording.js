import React, {Component} from 'react';
import {Button, StyleSheet, View} from 'react-native';
import voiceChanger from 'react-native-voice-changer';
import {check, PERMISSIONS, request} from 'react-native-permissions';
import RecordLogic from '../components/RecordLogic';


class Recording extends Component<props> {
    constructor(props) {
        super(props);
        this.state = {
            permissions: [],
        };
    }

    componentDidMount() {
        this._checkPermissions();
    }

    _checkPermissions = () => {
        let permissions = [
            PERMISSIONS.ANDROID.RECORD_AUDIO,
            PERMISSIONS.ANDROID.WRITE_EXTERNAL_STORAGE,
        ];

        Promise.all(permissions.map(check)).then(async response => {
            console.log('permissions', response);
            this.setState({
                permissions: response,
            });
            for (let i = 0; i < permissions.length; i++) {
                if (response[i] !== 'granted') {
                    await this._requestPermission(permissions[i], i);
                }
            }
            voiceChanger.createOutputDir();
        });
    };

    _requestPermission = (type, idx) => {
        return new Promise((resolve, reject) => {
            request(type).then(response => {
                // Returns once the user has chosen to 'allow' or to 'not allow' access
                // Response is one of: 'granted', 'denied', 'blocked' {
                this.setState({
                    permissions: this.state.permissions.map((p, i) => i === idx ? response : p),
                });
                resolve(response);
            });
        });
    };


    render() {
        const {permissions} = this.state;

        return (
            <View style={styles.container}>
                {permissions.every(p => p === 'granted') ? (
                    <View style={styles.recordButtons}>
                        <RecordLogic navigation={this.props.navigation}/>
                    </View>
                ) : (
                    <View style={styles.AllowPermissions}>
                        <Button
                            title={'Allow permissions'}
                            onPress={this._checkPermissions}
                        />
                    </View>
                )}
            </View>
        );
    }
}

const styles = StyleSheet.create({
    container: {
        flexDirection: 'column',
        justifyContent: 'center',
        alignItems: 'center',
        width: '100%',
        alignSelf: 'center',
        backgroundColor: "#5a7aee",
    },
    recordButtons: {
        height: '100%',
        width: '100%',
    },
    AllowPermissions: {
        width: '100%',
    },
});


export default Recording;
