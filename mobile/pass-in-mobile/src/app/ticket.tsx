import { Button } from "@/components/button"
import { Credential } from "@/components/credential"
import { Header } from "@/components/header"
import { QRCode } from "@/components/qrcode"
import { useBadgeStore } from "@/store/badge-store"
import { colors } from "@/styles/colors"
import { FontAwesome } from "@expo/vector-icons"
import * as ImagePicker from "expo-image-picker"
import { Redirect } from "expo-router"
import { MotiView } from "moti"
import { useState } from "react"
import { Alert, Modal, ScrollView, StatusBar, Text, TouchableOpacity, View, Share } from "react-native"

export default function Ticket() {
  const [expandQRCode, setExpandQRCode] = useState(false)

  const badgeStore = useBadgeStore()

  async function onSelectImage() {
    try {
      const result = await ImagePicker.launchImageLibraryAsync({
        mediaTypes: ImagePicker.MediaTypeOptions.Images,
        allowsEditing: true, // possibilita o usuário recortar a imagem
        aspect: [4, 4] // imagem 4 por 4
      })

      if (result.assets) {
        badgeStore.updateAvatar(result.assets[0].uri)
      }
    } catch (error) {
      console.error(error)
      Alert.alert("Foto", "Não foi possível selecionar a imagem.")
    }
  }

  async function onShare(){
    try {
      if(badgeStore.data?.checkInUrl){
        await Share.share({
          message: badgeStore.data.checkInUrl,
        })
      }

    } catch(error){
      console.log("Ticket onshare() => ", error)
      Alert.alert("Compartilhar", "Não foi possivel compartilhar")
    }
  }

  if (!badgeStore.data?.checkInUrl){
    return <Redirect href={"/"} />
  }

  return (
    <View className="flex-1 bg-green-500">
      <StatusBar barStyle="light-content" />
      <Header title="Minha Credencial" />

      {/* -z-10 joga para tras */}
      <ScrollView
        className="-mt-28 -z-10"
        contentContainerClassName="px-8 pb-8"
        showsVerticalScrollIndicator={false}>
        <Credential
          onChangeAvatar={onSelectImage}
          onExpandQRCode={() => setExpandQRCode(true)}
          data={badgeStore.data}
        />

        <MotiView
          from={{ 
            translateY: 0
           }}
          animate={{
            translateY: 10
          }}
          transition={{
            // repeat: 5
            loop: true,
            type: "timing",
            duration: 700
          }}
        >
          <FontAwesome
            name="angle-double-down"
            size={24}
            color={colors.gray[300]}
            className="self-center my-6"
          />
        </MotiView>

        <Text className="text-white font-bold text-2xl mt-4">Compartilhar Credencial</Text>

        <Text className="text-white font-regular text-base mt-1 mb-6">
          Mostre ao mundo que você vai participar do {badgeStore.data.eventTitle}!
        </Text>

        <Button title="Compartilhar" onPress={onShare}/>

        {/* *** BOTÃO REMOVER INGRESSO *** */}
        <TouchableOpacity
          activeOpacity={0.7}
          className="mt-10"
          onPress={() => badgeStore.remove()}>
          <Text className="text-base text-white font-bold text-center">Remover Ingresso</Text>
        </TouchableOpacity>
      </ScrollView>

      {/* *** MODAL *** */}
      <Modal
        visible={expandQRCode}
        statusBarTranslucent
        animationType="slide">
        <View className="flex-1 bg-green-500 items-center justify-center">
          <TouchableOpacity
            activeOpacity={0.7}
            onPress={() => setExpandQRCode(false)}>
            <QRCode
              value={"teste"}
              size={300}
            />
            <Text className="font-body text-center text-orange-500 text-base mt-10">
              Pressione para fechar
            </Text>
          </TouchableOpacity>
        </View>
      </Modal>
    </View>
  )
}
