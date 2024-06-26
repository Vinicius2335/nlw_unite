import { Input } from "@/components/input"
import { View, Image, StatusBar, Alert } from "react-native"
import { FontAwesome6, MaterialIcons } from "@expo/vector-icons"
import { colors } from "@/styles/colors"
import { Button } from "@/components/button"
import { Link, router } from "expo-router"
import { useState } from "react"
import { api } from "@/server/api"
import axios from "axios"
import { BadgeStore, useBadgeStore } from '../store/badge-store';

// Não seria o ideal deixar de forma fixa 
export const EVENT_ID = "058423d2-6a2f-4cf5-bf5d-d3dc040c23b4"

export default function Register() {
  const [name, setName] = useState("")
  const [email, setEmail] = useState("")
  const [isLoading, setIsLoading] = useState(false)

  const badgeStore = useBadgeStore()

  async function onRegister(){
    // Quando estamos usando dependências externas, utilizar try catch
    try {

    if(!name.trim() || !email.trim()){
      return Alert.alert("Inscrição", "Informe seu nome e e-mail!")
    }

    setIsLoading(true)

    const registerResponse = await api.post(`/events/${EVENT_ID}/attendees`, {name, email})

    if(registerResponse.data.attendeeId){
      const badgeResponse = await api.get(`/attendees/${registerResponse.data.attendeeId}/badge`)

      badgeStore.save(badgeResponse.data.badge)

      // Depois que a inscrição ocorrer com sucesso
      // O usuário pressior o botão de OK
      // O usuário será redirecionado para ticket
      Alert.alert("Inscrição", "Inscrição realizada com sucesso!", [
        { text: "OK", onPress: () => router.push("/ticket") },
      ])
    }

    } catch(error){
      console.log(error)
      setIsLoading(false)
      
      // Verifica se o erro é da requisição
      if(axios.isAxiosError(error)){
        // Ação para uma condição mais específica
        // OBS: Tratar do mais específico para o mais genérico
        if(String(error.response?.data.title).includes("already registered")){
          return Alert.alert("Inscrição", "Este e-mail já está cadastrado!")
        }
      }

      Alert.alert("Inscrição", "Não foi possível fazer a inscrição.")

    }
  }

  return (
    <View className="flex-1 bg-green-500 items-center justify-center p-8">
      <StatusBar barStyle="light-content" />
      
      <Image source={require("@/assets/logo.png")} className="h-16" resizeMode="contain" />

      <View className="w-full mt-12 gap-3">
        <Input>
          <FontAwesome6
            name="user-circle"
            size={20}
            color={colors.gray[200]}
          />
          <Input.Field placeholder="Nome Completo" onChangeText={setName} />
        </Input>

        <Input>
          <MaterialIcons
            name="alternate-email"
            size={20}
            color={colors.gray[200]}
          />
          <Input.Field placeholder="E-mail" keyboardType="email-address" onChangeText={setEmail}/>
        </Input>

        <Button title="Realizar Inscrição" onPress={onRegister} isLoading={isLoading}/>

        <Link href={"/"} className="text-gray-100 text-base fond-bold text-center mt-8">
          Já possui ingresso?
        </Link>
      </View>
    </View>
  )
}
