// vite.config.js
import {resolve} from 'path'
import {defineConfig} from 'vite'

export default defineConfig({
    build: {
        copyPublicDir: false,
        sourcemap: 'inline',
        lib: {
            // Could also be a dictionary or array of multiple entry points
            entry: resolve(__dirname, 'lib/main.ts'),
            name: 'EumowyLib',
            // the proper extensions will be added
            fileName: 'eumowy-lib',
        }
    }
})

