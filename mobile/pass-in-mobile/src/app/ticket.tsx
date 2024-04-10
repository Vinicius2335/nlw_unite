import { Header } from "@/components/header"
import { StatusBar, Text, View, ScrollView, TouchableOpacity, Alert, Modal } from "react-native"
import { Credential } from "@/components/credential"
import { FontAwesome } from "@expo/vector-icons"
import { colors } from "@/styles/colors"
import { Button } from "@/components/button"
import { useState } from "react"
import * as ImagePicker from "expo-image-picker"
import { QRCode } from "@/components/qrcode"
import { Link } from "expo-router"

export default function Ticket() {
  const [image, setImage] = useState("")
  const [expandQRCode, setExpandQRCode] = useState(false)

  async function onSelectImage() {
    try {
      const result = await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: true, // possibilita o usuário recortar a imagem
        aspect: [4, 4] // imagem 4 por 4
      })

      if (result.assets) {
        setImage(result.assets[0].uri)
      }
    } catch (error) {
      console.error(error)
      Alert.alert("Foto", "Não foi possível selecionar a imagem.")
    }
  }

  return (
    <View className="flex-1 bg-green-500">
      <StatusBar barStyle="light-content" />
      <Header title="Minha Credencial" />

      {/* -z-10 joga para tras */}
      <ScrollView
        className="-mt-28 -z-10"
        contentContainerClassName="px-8 pb-8"
        showsVerticalScrollIndicator={false}
      >
        <Credential image={image} onChangeAvatar={onSelectImage} onExpandQRCode={() => setExpandQRCode(true)} />

        <FontAwesome
          name="angle-double-down"
          size={24}
          color={colors.gray[300]}
          className="self-center my-6"
        />

        <Text className="text-white font-bold text-2xl mt-4">Compartilhar credencial</Text>

        <Text className="text-white font-regular text-base mt-1 mb-6">
          Mostre ao mundo que você vai participar do Unite Summit!
        </Text>

        <Button title="Compartilhar" />

        <Link href={"/"} className="text-gray-100 text-base fond-bold text-center mt-10">
          Remover Ingresso
        </Link>

        {/* NOTE : Pequena alteração, talvez remover depois */}
        {/* <TouchableOpacity activeOpacity={0.7} className="mt-10">
          <Text className="text-base text-white font-bold text-center">Remover Ingresso</Text>
        </TouchableOpacity> */}
      </ScrollView>

      {/* *** MODAL *** */}
      <Modal visible={expandQRCode} statusBarTranslucent animationType="slide">
        <View className="flex-1 bg-green-500 items-center justify-center">
          <TouchableOpacity activeOpacity={0.7} onPress={() => setExpandQRCode(false)}>
            <QRCode value={"teste"} size={300} />
            <Text className="font-body text-center text-orange-500 text-base mt-10">Pressione para fechar</Text>
          </TouchableOpacity>
        </View>
      </Modal>
    </View>
  )
}
