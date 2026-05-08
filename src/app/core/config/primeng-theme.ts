import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

export const SmartAgentPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '#e6f8fd',
      100: '#b3eaf8',
      200: '#80dcf3',
      300: '#4dcdee',
      400: '#26bdea',
      500: '#009dde',
      600: '#0088c4',
      700: '#0078ae',
      800: '#005a92',
      900: '#004875',
      950: '#003a60'
    },
    colorScheme: {
      light: {
        primary: {
          color: '#009dde',
          contrastColor: '#ffffff',
          hoverColor: '#0088c4',
          activeColor: '#0078ae'
        },
        highlight: {
          background: '#e6f6ff',
          focusBackground: '#cceefb',
          color: '#005a92',
          focusColor: '#003a60'
        },
        formField: {
          background: '#ffffff',
          borderColor: '#cfd7e6',
          hoverBorderColor: '#9daabd',
          focusBorderColor: '#009dde',
          color: '#234678',
          placeholderColor: '#9daabd'
        }
      }
    }
  }
});
