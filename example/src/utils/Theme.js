import {DefaultTheme} from "react-native-paper";
import Colors from "./Colors";

export default {
  ...DefaultTheme,
  roundness: 2,
  dark: true,
  colors: {
    ...DefaultTheme.colors,
    primary: Colors.primary,
    accent: Colors.secondary,
    text: Colors.text
  },
};
