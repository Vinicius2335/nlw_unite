# Bash

- ``npx create-expo-app --template``
  - navigation
- ``npm install nativewind@^4.0.1 react-native-reanimated tailwindcss`` 
  - NativeWind [Link](https://www.nativewind.dev/v4/getting-started/expo-router)
  - add styles > global.css
  - config seguindo a documentação para SDK 50+
    - tailwind.config.js
    - babel.config.js
    - metro.config.js
  - adicionar a tipagem para o tailwind
- ``npx expo start --clear``
- `` npx expo install expo-font @expo-google-fonts/roboto``
- ``npx expo install expo-image-picker`` - permite o usuário selecionar imagens do próprio dispositivo
- ``npx expo install react-native-svg react-native-qrcode-svg`` - para criar o qrcode

# Notas

- ajustar as importaçoes em tsconfig.json

```` json
"paths": {
      "@/*": [
        "./src/*"
      ]
````

- Separamos o Input do Field
  - É um pattern composição de componente, para criar um componente em partes
  - Dá mais flexibilidade ao componente
  - o Input pode ter um icone no inicio, pode nao ter, pode ter um botao no final para clear, etc...

- Icones que já existem dentro do expo [Link](https://icons.expo.fyi/Index)