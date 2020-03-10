import React from 'react';
import {Provider as PaperProvider} from 'react-native-paper';
import {NavigationContainer} from '@react-navigation/native';
import {createStackNavigator} from '@react-navigation/stack';
import 'react-native-gesture-handler';

import theme from './utils/Theme';
import Recording from './screens/Recording';
import Effects from './screens/Effects';

const Stack = createStackNavigator();

export default function App() {
    return (
        <NavigationContainer>
            <PaperProvider theme={theme}>
                <Stack.Navigator initialRouteName={'record'}>
                    <Stack.Screen name="record" component={Recording} options={{title: 'Record'}}/>
                    <Stack.Screen name="effects" component={Effects} options={{title: 'Effects'}}/>
                </Stack.Navigator>
            </PaperProvider>
        </NavigationContainer>
    );
}
