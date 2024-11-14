import { defineConfig, createSystem, mergeConfigs, defaultConfig } from "@chakra-ui/react"

console.log(defaultConfig)

const config = mergeConfigs(
    defaultConfig,
    defineConfig({
        preflight: true,
        strictTokens: false,
        cssVarsPrefix: 'certeasy',
        theme: {
            tokens: {
                colors: {
                    primary: { value: '#03258C' },
                    secondary: { value: '#D4C6FB' },
                    red: { value: '#FF0000' },
                    black: { value: '#000000' },
                    blackAlpha60: { value: 'rgba(0, 0, 0, 0.6)'},
                    white: { value: '#FFFFFF' },
                },
                fonts: {
                    body: { value: "'Poppins', sans-serif" }
                },
                fontWeights: {
                    light: { value: '300' },
                    normal: { value: '400' },
                    semibold: { value: '500' }
                },
                fontSizes: {
                    xs: { value: '12px' },
                    sm: { value: '14px' },
                    md: { value: '16px' },
                    lg: { value: '18px' },
                    xl: { value: '20px' },
                    '2xl': { value: '24px' },
                    '3xl': { value: '32px' },
                    '4xl': { value: '40px' },
                    '5xl': { value: '48px' },
                    '6xl': { value: '64px' },
                    '7xl': { value: '80px' },
                    '8xl': { value: '96px' },
                    '9xl': { value: '128px' },
                }
            },
        }
    })
)

export default createSystem(config)
