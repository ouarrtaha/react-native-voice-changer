import React from 'react';
import {StyleSheet, View} from "react-native";
import Colors from '../utils/Colors'
import { Surface, Text, IconButton, Avatar } from 'react-native-paper';

export default (props) => {
    const {effect, playingIndex, idx} = props;

    return (
        <Surface style={styles.container}>
            <View style={styles.iconContainer}>
                <Avatar.Image
                    source={effect.icon}
                    size={35}
                />
            </View>
            <Text style={[styles.text, styles.title]}>
                {effect.name}
            </Text>
            <IconButton
                icon={playingIndex === idx ? "stop" : "play-arrow"}
                size={24}
                color={Colors.primary}
                onPress={() => props.onPlay()}
            />
            <IconButton
                icon={"save"}
                size={24}
                color={Colors.primary}
                onPress={() => props.onSave()}
            />
        </Surface>
    )
}

const styles = StyleSheet.create({
    container: {
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: "center",
        paddingHorizontal: 15,
        marginVertical: 4,
        elevation: 4,
        borderRadius: 3,
        backgroundColor: '#eee'
    },
    text: {
        fontSize: 18,
        flex: 1
    },
    title: {
        textAlignVertical: "center",
        flex: 3
    },
    iconContainer: {
        marginTop: 2,
        marginRight: 8,
        marginLeft: -8,
        borderWidth: 2,
        borderColor: Colors.secondary,
        borderRadius: 100,
    }
});
