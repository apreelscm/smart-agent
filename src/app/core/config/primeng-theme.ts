import { definePreset } from '@primeuix/themes';
import Aura from '@primeuix/themes/aura';

export const SmartAgentPreset = definePreset(Aura, {
  semantic: {
    primary: {
      50: '{blue.50}',
      100: '{blue.100}',
      200: '{blue.200}',
      300: '{blue.300}',
      400: '{blue.400}',
      500: '{blue.500}',
      600: '{blue.600}',
      700: '{blue.700}',
      800: '{blue.800}',
      900: '{blue.900}',
      950: '{blue.950}'
    },
    colorScheme: {
      light: {
        primary: {
          color: '#1d4ed8',
          contrastColor: '#f8fbff',
          hoverColor: '#1e40af',
          activeColor: '#1e3a8a'
        },
        highlight: {
          background: '#eaf2ff',
          focusBackground: '#d8e7ff',
          color: '#163b8c',
          focusColor: '#102e6f'
        },
        formField: {
          background: '#fcfdff',
          borderColor: '#cfd7e6',
          hoverBorderColor: '#aeb9ce',
          focusBorderColor: '#1d4ed8',
          color: '#172033',
          placeholderColor: '#66728a'
        }
      }
    }
  }
});
