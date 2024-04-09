import { Header } from "@/components/header";
import { StatusBar, Text, View } from "react-native";
import { Credential } from "@/components/credential"
import { FontAwesome } from "@expo/vector-icons"
import { colors } from "@/styles/colors";

export default function Ticket(){
  return (
    <View className="flex-1 bg-green-500">
      <StatusBar barStyle="light-content" />

      <Header title="Minha Credencial" />
      <Credential />

      <FontAwesome name="angle-double-down" size={24} color={colors.gray[300]} className="self-center my-6"/>

      <Text className="text-white font-bold text-2xl mt-4">
        Compartilhar credencial
      </Text>

      <Text className="text-white font-regular text-base mt-1 mb-6">
        Mostre ao mundo que você vai participar do Unite Summit!
      </Text>
    </View>
  )
}